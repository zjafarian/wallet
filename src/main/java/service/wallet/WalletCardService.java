package service.wallet;


import exception.BusinessException;
import lombok.extern.log4j.Log4j2;
import mapper.FinancierMapper;
import mapper.WalletCardMapper;
import model.WalletCard;
import model.WalletCardType;
import org.springframework.stereotype.Service;
import repository.FinancierRepository;
import repository.WalletCardRepository;
import service.financier.FinancierService;
import service.model.response.ApiResponse;
import service.wallet.model.WalletCardRequest;

import java.util.List;
import java.util.Optional;


@Service
@Log4j2
public class WalletCardService {

    private final WalletCardRepository walletCardRepository;
    private final FinancierRepository financierRepository;
    private final WalletCardMapper walletCardMapper;
    private final FinancierMapper financierMapper;
    private final FinancierService financierService;
    private final WalletService walletService;


    public WalletCardService(WalletCardRepository walletCardRepository,
                             FinancierRepository financierRepository,
                             WalletCardMapper walletCardMapper,
                             FinancierMapper financierMapper,
                             FinancierService financierService,
                             WalletService walletService) {
        this.walletCardRepository = walletCardRepository;
        this.walletCardMapper = walletCardMapper;
        this.financierRepository = financierRepository;
        this.financierMapper = financierMapper;
        this.financierService = financierService;
        this.walletService = walletService;
    }

    public ApiResponse createDebitCard(WalletCardRequest debitCardRequest) {
        return getDebitCardByWalletId(debitCardRequest.getWalletId()).map((debitCard) ->
                new ApiResponse(false,
                        debitCard,
                        "exists debitCard for this wallet: " +
                                debitCard.getWallet() +
                                " ,and profile: " +
                                debitCard.getWallet().getProfile(),
                        ""
                )
        ).orElseGet(() -> {
            debitCardRequest.setWalletCardType(WalletCardType.DebitCard);
            var debitCard = saveWalletCard(walletCardMapper.toWalletCard(debitCardRequest));
            updateWalletBalance(debitCard);

            return new ApiResponse(true, debitCard);
        });

    }

    public ApiResponse createCreditCard(WalletCardRequest creditCardRequest) {
        if (creditCardRequest.getFinancierRequest() == null) {
            new BusinessException("Financier doesn't define for this creditCard" + creditCardRequest.getTitle(), 100010);
        }
        creditCardRequest.setWalletCardType(WalletCardType.CreditCard);
        var creditCard = walletCardMapper.toWalletCard(creditCardRequest);
        var financier = financierMapper.toFinancier(creditCardRequest.getFinancierRequest());
        creditCard.setFinancier(financier);
        creditCard = saveWalletCard(creditCard);
        updateWalletBalance(creditCard);
        return new ApiResponse(true, creditCard);

    }

    public void updateWalletBalance(WalletCard creditCard) {
        var wallet = creditCard.getWallet();
        wallet.setTotalBalance(wallet.getTotalBalance() + creditCard.getBalance());
        wallet = walletService.saveWallet(wallet);
    }

    public Optional<WalletCard> getDebitCardByWalletId(Long walletId) {
        return walletCardRepository.findWalletCardByWalletIdAndWalletCardTypeEqualsAndIsDeletedIsFalse(walletId,
                WalletCardType.DebitCard);
    }

    public Optional<WalletCard> getCreditCardByCreditId(Long creditCardId) {
        return walletCardRepository.findWalletCardByIdAndWalletCardTypeEqualsAndIsDeletedIsFalse(creditCardId,
                WalletCardType.CreditCard);
    }

    public List<WalletCard> getCreditCardsByWalletId(Long walletId) {
        return walletCardRepository.findWalletCardsByWalletIdAndWalletCardTypeEqualsAndIsDeletedIsFalse(walletId,
                WalletCardType.CreditCard);
    }

    public WalletCard saveWalletCard(WalletCard walletCard) {
        return walletCardRepository.save(walletCard);
    }

    public WalletCard findActiveWalletCard(Long walletCardId) {
        return walletCardRepository.findWalletCardByIdAndIsDeletedIsFalseAndIsActiveIsTrue(walletCardId)
                .orElseThrow(() -> new BusinessException("walletCard isn't active by this id :" + walletCardId, 300010));
    }


    public Boolean withdrawWalletCard(Long amount, WalletCard walletCard) {
        if (checkBalance(amount, walletCard)) {
            var newBalance =   calculateWithdraw.calculateBalance(walletCard.getBalance(),amount);
            walletCard.setBalance(newBalance);
            walletCardRepository.save(walletCard);
            var wallet = walletService.getWallet(walletCard.getWallet().getId())
                    .orElseThrow(() -> new BusinessException("wallet isn't active by this id :" +
                            walletCard.getWallet().getId(), 700010));

            wallet.setTotalBalance(calculateWithdraw.calculateBalance(wallet.getTotalBalance(),amount));
            walletService.updateWallet(wallet);

        } else {
            return false;
        }
        return true;
    }

    private Boolean checkBalance(Long amount, WalletCard walletCard) {
        if (walletCard.getBalance() <= amount ||
                calculateWithdraw.calculateBalance(walletCard.getBalance(),amount) < 0) {
            return false;
        }
        return true;

    }



    public void depositWalletCard(Long amount, WalletCard walletCard) {
        walletCard.setBalance(calculateDeposit.calculateBalance(walletCard.getBalance(),amount));
        walletCardRepository.save(walletCard);
        var wallet = walletService.getWallet(walletCard.getWallet().getId())
                .orElseThrow(() -> new BusinessException("wallet isn't active by this id :" +
                walletCard.getWallet().getId(), 700010));;

        wallet.setTotalBalance(calculateDeposit.calculateBalance(wallet.getTotalBalance(),amount));
        walletService.updateWallet(wallet);
    }

    private final CalculateBalance<Long> calculateWithdraw = (balance, amount) -> balance - amount;

    private final CalculateBalance<Long> calculateDeposit = Long::sum;

}

@FunctionalInterface
interface CalculateBalance<T> {
    T calculateBalance(T a, T b);
}
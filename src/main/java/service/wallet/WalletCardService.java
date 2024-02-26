package service.wallet;


import exception.BusinessException;
import lombok.extern.log4j.Log4j2;
import mapper.FinancierMapper;
import mapper.WalletCardMapper;
import model.Wallet;
import model.WalletCard;
import model.WalletCardType;
import org.springframework.cache.annotation.Cacheable;
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
        log.info("WalletCardService , createDebitCard , debitCardRequest: {}", debitCardRequest);
        var result = getDebitCardByWalletId(debitCardRequest.getWalletId()).map((debitCard) ->
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

        log.info("WalletCardService , createDebitCard , result: {}", result);
        return result;

    }

    public ApiResponse createCreditCard(WalletCardRequest creditCardRequest) {
        log.info("WalletCardService , createDebitCard , createCreditCard: {}", creditCardRequest);
        if (creditCardRequest.getFinancierRequest() == null) {
            new BusinessException("Financier doesn't define for this creditCard" + creditCardRequest.getTitle(), 100010);
        }
        creditCardRequest.setWalletCardType(WalletCardType.CreditCard);
        var creditCard = walletCardMapper.toWalletCard(creditCardRequest);
        var financier = financierMapper.toFinancier(creditCardRequest.getFinancierRequest());
        creditCard.setFinancier(financier);
        creditCard = saveWalletCard(creditCard);
        var wallet = updateWalletBalance(creditCard);

        log.info("WalletCardService , createDebitCard , updateWalletBalance: {}", wallet.getTotalBalance());
        return new ApiResponse(true, creditCard);

    }

    public Wallet updateWalletBalance(WalletCard creditCard) {
        log.info("WalletCardService , updateWalletBalance , creditCard: {}", creditCard);
        var wallet = creditCard.getWallet();
        wallet.setTotalBalance(wallet.getTotalBalance() + creditCard.getBalance());
        wallet = walletService.saveWallet(wallet);

        log.info("WalletCardService , updateWalletBalance , result: {}", wallet);
        return wallet;
    }

    public Optional<WalletCard> getDebitCardByWalletId(Long walletId) {
        log.info("WalletCardService , getDebitCardByWalletId , walletId: {}", walletId);
        var result = walletCardRepository.findWalletCardByWalletIdAndWalletCardTypeEqualsAndIsDeletedIsFalse(walletId,
                WalletCardType.DebitCard);

        log.info("WalletCardService , getDebitCardByWalletId , result: {}", result);
        return result;
    }

    public Optional<WalletCard> getCreditCardByCreditId(Long creditCardId) {
        log.info("WalletCardService , getCreditCardByCreditId , creditCardId: {}", creditCardId);
        var result = walletCardRepository.findWalletCardByIdAndWalletCardTypeEqualsAndIsDeletedIsFalse(creditCardId,
                WalletCardType.CreditCard);

        log.info("WalletCardService , getCreditCardByCreditId , result: {}", result);
        return result;
    }

    public List<WalletCard> getCreditCardsByWalletId(Long walletId) {
        log.info("WalletCardService , getCreditCardsByWalletId , walletId: {}", walletId);
        var result = walletCardRepository.findWalletCardsByWalletIdAndWalletCardTypeEqualsAndIsDeletedIsFalse(walletId,
                WalletCardType.CreditCard);
        log.info("WalletCardService , getCreditCardsByWalletId , result: {}", result);
        return result;
    }

    public WalletCard getWalletCardId(Long walletCardId) {

        log.info("WalletCardService , getWalletCardId , walletCardId: {}", walletCardId);
        var result = walletCardRepository.findWalletCardByIdAndIsDeletedIsFalse(walletCardId)
                .orElseThrow(() -> new BusinessException("walletCard isn't find by this id :" + walletCardId, 300010));

        log.info("WalletCardService , getWalletCardId , result: {}", result);
        return result;
    }

    public WalletCard saveWalletCard(WalletCard walletCard) {
        log.info("WalletCardService , saveWalletCard , walletCard: {}", walletCard);
        var result = walletCardRepository.save(walletCard);

        log.info("WalletCardService , saveWalletCard , result: {}", result);
        return result;
    }


    @Cacheable(value = "get_wallet_card", key = "{#walletCardId}")
    public WalletCard findActiveWalletCard(Long walletCardId) {
        log.info("WalletCardService , findActiveWalletCard , walletCardId: {}", walletCardId);

        var result = walletCardRepository.findWalletCardByIdAndIsDeletedIsFalseAndIsActiveIsTrue(walletCardId)
                .orElseThrow(() -> new BusinessException("walletCard isn't active by this id :" + walletCardId, 300010));

        log.info("WalletCardService , findActiveWalletCard , result: {}", result);
        return result;
    }


    public Boolean withdrawWalletCard(Long amount, WalletCard walletCard) {
        log.info("WalletCardService , withdrawWalletCard , amount: {} , walletCard :{}", amount, walletCard);
        if (checkBalance(amount, walletCard)) {
            var newBalance = calculateWithdraw.calculateBalance(walletCard.getBalance(), amount);
            walletCard.setBalance(newBalance);
            walletCard = walletCardRepository.save(walletCard);

            log.info("WalletCardService , withdrawWalletCard , newBalance : {} of walletCard :{}", newBalance, walletCard.getId());
            var wallet = walletService.getWallet(walletCard.getWallet().getId());

            wallet.setTotalBalance(calculateWithdraw.calculateBalance(wallet.getTotalBalance(), amount));
            wallet = walletService.updateWallet(wallet);

            log.info("WalletCardService , withdrawWalletCard , newBalance: {} of wallet :{}", newBalance, wallet.getId());

        } else {
            return false;
        }
        return true;
    }

    private Boolean checkBalance(Long amount, WalletCard walletCard) {
        log.info("WalletCardService , checkBalance , amount: {} , walletCard :{}", amount, walletCard);
        if (walletCard.getBalance() <= amount ||
                calculateWithdraw.calculateBalance(walletCard.getBalance(), amount) < 0) {
            log.info("WalletCardService , checkBalance , amount: {} , walletCard :{} , result :{}", amount, walletCard, false);
            return false;
        }

        log.info("WalletCardService , checkBalance , amount: {} , walletCard :{} , result :{}", amount, walletCard, true);
        return true;

    }


    public void depositWalletCard(Long amount, WalletCard walletCard) {
        log.info("WalletCardService , depositWalletCard , amount: {} , walletCard :{}", amount, walletCard);
        walletCard.setBalance(calculateDeposit.calculateBalance(walletCard.getBalance(), amount));
        walletCard = walletCardRepository.save(walletCard);
        var wallet = walletService.getWallet(walletCard.getWallet().getId());

        log.info("WalletCardService , depositWalletCard , newBalance : {} of walletCard :{}", amount, walletCard.getBalance());
        wallet.setTotalBalance(calculateDeposit.calculateBalance(wallet.getTotalBalance(), amount));
        wallet = walletService.updateWallet(wallet);

        log.info("WalletCardService , depositWalletCard , newBalance : {} of wallet :{}", amount, wallet.getTotalBalance());
    }

    private final CalculateBalance<Long> calculateWithdraw = (balance, amount) -> balance - amount;

    private final CalculateBalance<Long> calculateDeposit = Long::sum;

}

@FunctionalInterface
interface CalculateBalance<T> {
    T calculateBalance(T a, T b);
}
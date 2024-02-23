package service.wallet;


import exception.BusinessException;
import lombok.extern.log4j.Log4j2;
import mapper.FinancierMapper;
import mapper.WalletCardMapper;
import model.Financier;
import model.Wallet;
import model.WalletCard;
import model.WalletCardType;
import org.springframework.stereotype.Service;
import repository.FinancierRepository;
import repository.WalletCardRepository;
import service.FinancierService;
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
            var wallet = debitCard.getWallet();
            wallet.setTotalBalance(wallet.getTotalBalance() + debitCard.getBalance());
            wallet = walletService.saveWallet(wallet);

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
        var wallet = creditCard.getWallet();
        creditCard = saveWalletCard(creditCard);
        wallet.setTotalBalance(wallet.getTotalBalance() + creditCard.getBalance());
        wallet = walletService.saveWallet(wallet);
        return new ApiResponse(true, creditCard);

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


}
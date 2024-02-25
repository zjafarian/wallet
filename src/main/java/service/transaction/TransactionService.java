package service.transaction;


import exception.BusinessException;
import lombok.extern.log4j.Log4j2;
import model.Transaction;
import model.TransactionLog;
import model.TransactionStatus;
import model.WalletCard;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.TransactionLogRepository;
import repository.TransactionRepository;
import service.merchant.MerchantService;
import service.profile.ProfileService;
import service.transaction.model.PurchaseRequest;
import service.transaction.model.PurchaseResponseModel;
import service.wallet.WalletCardService;
import service.wallet.WalletService;

import java.util.Date;


@Service
@Log4j2
public class TransactionService {


    private final WalletService walletService;
    private final WalletCardService walletCardService;
    private final ProfileService profileService;
    private final MerchantService merchantService;
    private final TransactionRepository transactionRepository;
    private final TransactionLogRepository transactionLogRepository;

    public TransactionService(WalletService walletService,
                              WalletCardService walletCardService,
                              ProfileService profileService,
                              MerchantService merchantService,
                              TransactionRepository transactionRepository,
                              TransactionLogRepository transactionLogRepository) {

        this.walletService = walletService;
        this.walletCardService = walletCardService;
        this.profileService = profileService;
        this.merchantService = merchantService;
        this.transactionRepository = transactionRepository;
        this.transactionLogRepository = transactionLogRepository;
    }

    @Transactional
    public ResponseEntity<PurchaseResponseModel> requestPurchase(PurchaseRequest purchaseRequest) {
        var walletCard = validation(purchaseRequest);
        var merchant = merchantService.findActiveMerchantById(purchaseRequest.getMerchantId());
        var transaction = Transaction.builder()
                .transactionStatus(TransactionStatus.CREATE)
                .description("transaction start")
                .merchant(merchant)
                .amount(purchaseRequest.getAmount())
                .walletCard(walletCard)
                .build();

        transaction = transactionRepository.save(transaction);

        transactionLogRepository.save(TransactionLog.builder()
                .transactionStatus(TransactionStatus.CREATE)
                .transaction(transaction).build());


        if (walletCardService.withdrawWalletCard(purchaseRequest.getAmount(), walletCard)){
            transaction.setTransactionStatus(TransactionStatus.PROCESSING);
            transaction = transactionRepository.save(transaction);

            transactionLogRepository.save(TransactionLog.builder()
                    .transactionStatus(TransactionStatus.PROCESSING)
                    .transaction(transaction).build());



            return new ResponseEntity(PurchaseResponseModel.builder()
                    .code(0)
                    .message("ثبت درخواست خرید باموفقیت انجام شد.")
                    .doTime(new Date().toString())
                    .referenceNumber(String.valueOf(transaction.getId()))
                    .success(true)
                    .build(), HttpStatus.OK);

        } else {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transaction = transactionRepository.save(transaction);

            transactionLogRepository.save(TransactionLog.builder()
                    .transactionStatus(TransactionStatus.FAILED)
                    .transaction(transaction).build());

          return new ResponseEntity(PurchaseResponseModel.builder()
                    .code(0)
                    .message("موجودی کافی نیست")
                    .doTime(new Date().toString())
                    .referenceNumber(String.valueOf(transaction.getId()))
                    .success(true)
                    .build(), HttpStatus.OK);
        }

    }

    public ResponseEntity<PurchaseResponseModel> verifyTransaction(Transaction transaction) {
        transaction.setTransactionStatus(TransactionStatus.SUCCEED);
        transaction = transactionRepository.save(transaction);

        transactionLogRepository.save(TransactionLog.builder()
                .transactionStatus(TransactionStatus.SUCCEED)
                .transaction(transaction).build());

        return new ResponseEntity(PurchaseResponseModel.builder()
                .code(0)
                .message("خرید نهایی شد")
                .doTime(new Date().toString())
                .referenceNumber(String.valueOf(transaction.getId()))
                .success(true)
                .build(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<PurchaseResponseModel> reverseTransaction(Transaction transaction) {
        walletCardService.depositWalletCard(transaction.getAmount(),transaction.getWalletCard());
        transaction.setTransactionStatus(TransactionStatus.REVERSE);
        transaction = transactionRepository.save(transaction);

        transactionLogRepository.save(TransactionLog.builder()
                .transactionStatus(TransactionStatus.REVERSE)
                .transaction(transaction).build());

        return new ResponseEntity(PurchaseResponseModel.builder()
                .code(0)
                .message("خرید لغو شد")
                .doTime(new Date().toString())
                .referenceNumber(String.valueOf(transaction.getId()))
                .success(true)
                .build(), HttpStatus.OK);


    }




    public WalletCard validation(PurchaseRequest purchaseRequest) {
        var walletCard = walletCardService.findActiveWalletCard(purchaseRequest.getWalletCardId());
        var wallet = walletService.findActiveWallet(walletCard.getWallet().getId());
        var profile = profileService.findProfileActiveById(wallet.getProfile().getId());
        return walletCard;

    }


}
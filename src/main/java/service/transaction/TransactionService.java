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
import service.transaction.model.TransactionRequest;
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
    public ResponseEntity<PurchaseResponseModel> requestTransaction(TransactionRequest transactionRequest) {
        log.info("TransactionService ,requestTransaction , transactionRequest :{}", transactionRequest);
        var walletCard = validation(transactionRequest);
        var merchant = merchantService.findActiveMerchantById(transactionRequest.getMerchantId());
        var transaction = Transaction.builder()
                .transactionStatus(TransactionStatus.CREATE)
                .description("transaction start")
                .merchant(merchant)
                .amount(transactionRequest.getAmount())
                .walletCard(walletCard)
                .build();

        transaction = transactionRepository.save(transaction);

        //save state of a transaction in transactionLog table
        transactionLogRepository.save(TransactionLog.builder()
                .transactionStatus(TransactionStatus.CREATE)
                .transaction(transaction).build());

        log.info("TransactionService ,requestTransaction , transaction :{} , state :{}", transaction,
                transaction.getTransactionStatus());


        if (walletCardService.withdrawWalletCard(transactionRequest.getAmount(), walletCard)){
            log.info("TransactionService ,requestTransaction , withdrawWalletCard is ok for this walletCard : {}",walletCard);

            transaction.setTransactionStatus(TransactionStatus.PROCESSING);
            transaction = transactionRepository.save(transaction);

            //save state of a transaction in transactionLog table
            transactionLogRepository.save(TransactionLog.builder()
                    .transactionStatus(TransactionStatus.PROCESSING)
                    .transaction(transaction).build());

            log.info("TransactionService ,requestTransaction , transaction :{} , state :{}", transaction,
                    transaction.getTransactionStatus());



            return new ResponseEntity(PurchaseResponseModel.builder()
                    .code(0)
                    .message("ثبت درخواست خرید باموفقیت انجام شد.")
                    .doTime(new Date().toString())
                    .referenceNumber(String.valueOf(transaction.getId()))
                    .success(true)
                    .build(), HttpStatus.OK);

        } else {

            log.info("TransactionService ,requestTransaction , withdrawWalletCard isn't ok for this walletCard : {}",walletCard);
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transaction = transactionRepository.save(transaction);

            //save state of a transaction in transactionLog table
            transactionLogRepository.save(TransactionLog.builder()
                    .transactionStatus(TransactionStatus.FAILED)
                    .transaction(transaction).build());

            log.info("TransactionService ,requestTransaction , transaction :{} , state :{}", transaction,
                    transaction.getTransactionStatus());

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

        log.info("TransactionService ,verifyTransaction , transaction: {}",transaction);
        transaction.setTransactionStatus(TransactionStatus.SUCCEED);
        transaction = transactionRepository.save(transaction);

        //save state of a transaction in transactionLog table
        transactionLogRepository.save(TransactionLog.builder()
                .transactionStatus(TransactionStatus.SUCCEED)
                .transaction(transaction).build());


        log.info("TransactionService ,verifyTransaction , transaction :{} , state :{}", transaction,
                transaction.getTransactionStatus());


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
        log.info("TransactionService , reverseTransaction , transaction: {}",transaction);
        walletCardService.depositWalletCard(transaction.getAmount(),transaction.getWalletCard());
        transaction.setTransactionStatus(TransactionStatus.REVERSE);
        transaction = transactionRepository.save(transaction);

        //save state of a transaction in transactionLog table
        transactionLogRepository.save(TransactionLog.builder()
                .transactionStatus(TransactionStatus.REVERSE)
                .transaction(transaction).build());

        log.info("TransactionService ,reverseTransaction , transaction :{} , state :{}", transaction,
                transaction.getTransactionStatus());


        return new ResponseEntity(PurchaseResponseModel.builder()
                .code(0)
                .message("خرید لغو شد")
                .doTime(new Date().toString())
                .referenceNumber(String.valueOf(transaction.getId()))
                .success(true)
                .build(), HttpStatus.OK);


    }




    public WalletCard validation(TransactionRequest transactionRequest) {
        log.info("TransactionService , validation , transactionRequest: {}",transactionRequest);
        var walletCard = walletCardService.findActiveWalletCard(transactionRequest.getWalletCardId());
        var wallet = walletService.findActiveWallet(walletCard.getWallet().getId());
        var profile = profileService.findProfileActiveById(wallet.getProfile().getId());
        return walletCard;

    }

    public Transaction getTransaction(Long transactionId){
        log.info("TransactionService , getTransaction , transactionId: {}",transactionId);
        var result = transactionRepository.findTransactionByIdAndIsDeletedIsFalse(transactionId)
                .orElseThrow(() -> new BusinessException("transaction isn't find by this id :" +
                        transactionId, 900010));

        log.info("TransactionService , getTransaction , result: {}",result);
       return result;
    }


}
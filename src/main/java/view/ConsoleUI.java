package view;

import service.profile.ProfileService;
import service.transaction.TransactionService;
import service.transaction.model.TransactionRequest;
import service.wallet.WalletCardService;
import service.wallet.WalletService;

import java.util.Scanner;

public class ConsoleUI {
    private final TransactionService transactionService;
    private final WalletCardService walletCardService;
    private final WalletService walletService;
    private final ProfileService profileService;


    public ConsoleUI(TransactionService transactionService,
                     WalletCardService walletCardService,
                     WalletService walletService,
                     ProfileService profileService) {
        this.transactionService = transactionService;
        this.walletCardService = walletCardService;
        this.walletService = walletService;
        this.profileService = profileService;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. View Wallet Details");
            System.out.println("2. Make a Purchase");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewWalletCardDetails();
                    break;
                case 2:
                    makePurchase();
                    break;
                case 3:
                    comfirmTransaction();
                    break;
                case 4:
                    reverseTransaction();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    private void viewWalletCardDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select walletCard : ");
        var walletCardId = scanner.nextLong();
        var walletCard = walletCardService.getWalletCardId(walletCardId);


        System.out.println("WalletCard Details:");
        System.out.println("Title: " + walletCard.getTitle());
        System.out.println("Balance this card: " + walletCard.getBalance());
    }

    private void makePurchase() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter amount for purchase: ");
        var amount = scanner.nextLong();
        System.out.print("Enter walletCard: ");
        var walletCardId = scanner.nextLong();
        System.out.print("Enter merchant: ");
        var merchantId = scanner.nextLong();


        transactionService.requestTransaction(TransactionRequest.builder().amount(amount)
                .walletCardId(walletCardId)
                .merchantId(merchantId)
                .build());

        var result = transactionService.requestTransaction(TransactionRequest.builder().amount(amount)
                .walletCardId(walletCardId)
                .merchantId(merchantId)
                .build());

        System.out.println("Do you comfirm?! :" + result);
    }

    private void comfirmTransaction() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter transaction ");
        var transactionId = scanner.nextLong();
        var transaction = transactionService.getTransaction(transactionId);

        var result = transactionService.verifyTransaction(transaction);

        System.out.println("Purchase successful! :" + result);
    }


    private void reverseTransaction() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter transaction ");
        var transactionId = scanner.nextLong();
        var transaction = transactionService.getTransaction(transactionId);

        var result = transactionService.reverseTransaction(transaction);

        System.out.println("Purchase successful! :" + result);
    }
}

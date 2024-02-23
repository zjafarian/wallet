package service.wallet;


import lombok.extern.log4j.Log4j2;
import model.Wallet;
import org.springframework.stereotype.Service;
import repository.WalletRepository;


@Service
@Log4j2
public class WalletService {

    private final WalletRepository walletRepository;


    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet saveWallet(Wallet wallet){
        return walletRepository.save(wallet);
    }
}
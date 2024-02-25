package service.wallet;


import exception.BusinessException;
import lombok.extern.log4j.Log4j2;
import model.Wallet;
import org.springframework.stereotype.Service;
import repository.WalletRepository;

import java.util.Optional;


@Service
@Log4j2
public class WalletService {

    private final WalletRepository walletRepository;


    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet saveWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    public Optional<Wallet> getWallet(Long walletId) {
        return walletRepository.findWalletByIdAndIsDeletedIsFalse(walletId);
    }

    public void deleteWallet(Long walletId) {
        var wallet = getWallet(walletId).get();
        wallet.setIsDeleted(true);
        walletRepository.save(wallet);
    }

    public void updateWallet(Wallet wallet){
        walletRepository.save(wallet);
    }

    public Wallet activeWallet(Long walletId) {
        var wallet = getWallet(walletId).get();
        if (!wallet.getIsActive())
            wallet.setIsActive(true);

       return walletRepository.save(wallet);
    }

    public Wallet deActiveWallet(Long walletId) {
        var wallet = getWallet(walletId).get();
        if (wallet.getIsActive())
            wallet.setIsActive(false);
       return walletRepository.save(wallet);
    }

    public Wallet findActiveWallet(Long walletId){
       return walletRepository.findWalletByIdAndIsDeletedIsFalseAndIsActiveIsTrue(walletId)
                .orElseThrow(() -> new BusinessException("wallet isn't active by this id :" +
                        walletId, 200010));
    }
}
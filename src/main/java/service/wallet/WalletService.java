package service.wallet;


import exception.BusinessException;
import lombok.extern.log4j.Log4j2;
import model.Wallet;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

    @CacheEvict(cacheNames = "get_wallet", allEntries = true)
    public Wallet saveWallet(Wallet wallet) {
        log.info("WalletService , saveWallet , wallet : {} ", wallet);

        var result = walletRepository.save(wallet);

        log.info("WalletService , saveWallet , result : {} ", result);
        return result;
    }

    @Cacheable(value = "get_wallet", key = "{#walletId}")
    public Wallet getWallet(Long walletId) {
        log.info("WalletService , getWallet , walletId : {} ", walletId);
        var result = walletRepository.findWalletByIdAndIsDeletedIsFalse(walletId)
                .orElseThrow(() -> new BusinessException("wallet isn't active by this id :" +
                        walletId, 700010));

        log.info("WalletService , getWallet , result : {} ", result);
        return result;

    }

    @CacheEvict(cacheNames = "get_wallet", allEntries = true)
    public void deleteWallet(Long walletId) {

        log.info("WalletService , deleteWallet , walletId : {} ", walletId);
        var wallet = getWallet(walletId);
        wallet.setIsDeleted(true);
        wallet = walletRepository.save(wallet);

        log.info("WalletService , deleteWallet , result : {} ", wallet);
    }

    @CacheEvict(cacheNames = "get_wallet", allEntries = true)
    public Wallet updateWallet(Wallet wallet) {
        log.info("WalletService , updateWallet , wallet : {} ", wallet);
        var result = walletRepository.save(wallet);

        log.info("WalletService , updateWallet , result : {} ", result);
        return result;
    }

    @CacheEvict(cacheNames = "get_wallet", allEntries = true)
    public Wallet activeWallet(Long walletId) {
        log.info("WalletService , activeWallet , walletId : {} ", walletId);
        var wallet = getWallet(walletId);
        if (!wallet.getIsActive())
            wallet.setIsActive(true);

        var result =  walletRepository.save(wallet);

        log.info("WalletService , activeWallet , result : {} ", result);
        return result;
    }

    @CacheEvict(cacheNames = "get_wallet", allEntries = true)
    public Wallet deActiveWallet(Long walletId) {
        log.info("WalletService , deActiveWallet , walletId : {} ", walletId);
        var wallet = getWallet(walletId);
        if (wallet.getIsActive())
            wallet.setIsActive(false);

        var result =  walletRepository.save(wallet);

        log.info("WalletService , deActiveWallet , result : {} ", result);
        return result;
    }

    @Cacheable(value = "get_wallet", key = "{#walletId}")
    public Wallet findActiveWallet(Long walletId) {
        log.info("WalletService , findActiveWallet , walletId : {} ", walletId);

        var result = walletRepository.findWalletByIdAndIsDeletedIsFalseAndIsActiveIsTrue(walletId)
                .orElseThrow(() -> new BusinessException("wallet isn't active by this id :" +
                        walletId, 200010));

        log.info("WalletService , findActiveWallet , result : {} ", result);
        return result;
    }
}
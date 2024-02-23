package repository;

import model.Wallet;
import model.WalletCard;
import model.WalletCardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletCardRepository extends JpaRepository<WalletCard, Long>, JpaSpecificationExecutor<WalletCard> {


    Optional<WalletCard> findWalletCardByWalletIdAndWalletCardTypeEqualsAndIsDeletedIsFalse(Long walletId,
                                                                                            WalletCardType walletCardType);

    Optional<WalletCard> findWalletCardByIdAndWalletCardTypeEqualsAndIsDeletedIsFalse(Long cardId,
                                                                                      WalletCardType walletCardType);

    List<WalletCard> findWalletCardsByWalletIdAndWalletCardTypeEqualsAndIsDeletedIsFalse(Long walletId,
                                                                                         WalletCardType walletCardType);
}

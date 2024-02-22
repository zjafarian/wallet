package repository;

import model.Wallet;
import model.WalletCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletCardRepository extends JpaRepository<WalletCard, Long>, JpaSpecificationExecutor<WalletCard> {
}

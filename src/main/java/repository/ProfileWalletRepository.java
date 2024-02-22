package repository;

import model.ProfileWallet;
import model.WalletCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileWalletRepository extends JpaRepository<ProfileWallet, Long>, JpaSpecificationExecutor<ProfileWallet> {
}

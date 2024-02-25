package repository;

import model.Profile;
import model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>, JpaSpecificationExecutor<Wallet> {


    Optional<Wallet> findWalletByIdAndIsDeletedIsFalse(Long id);

    Optional<Wallet> findWalletByIdAndIsDeletedIsFalseAndIsActiveIsTrue(Long id);
}

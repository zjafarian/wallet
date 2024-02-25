package repository;

import model.Merchant;
import model.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long>, JpaSpecificationExecutor<Merchant> {

    Optional<Merchant> findMerchantByIdAndIsDeletedIsFalseAndIsActiveIsTrue(Long merchantId);

}

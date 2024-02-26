package repository;

import model.Transaction;
import model.WalletCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {


    Optional<Transaction> findTransactionByIdAndIsDeletedIsFalse(Long transactionId);
}

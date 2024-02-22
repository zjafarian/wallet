package repository;

import model.Financier;
import model.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancierRepository extends JpaRepository<Financier, Long>, JpaSpecificationExecutor<Financier> {
}

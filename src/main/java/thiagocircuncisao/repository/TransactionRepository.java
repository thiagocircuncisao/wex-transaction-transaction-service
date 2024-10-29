package thiagocircuncisao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thiagocircuncisao.model.PurchaseTransaction;

@Repository
public interface TransactionRepository extends JpaRepository<PurchaseTransaction, String> {
}

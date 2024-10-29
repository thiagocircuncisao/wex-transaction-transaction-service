package thiagocircuncisao.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "purchase_transaction", schema = "public")
public class PurchaseTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @Column(name = "transaction_date")
    private String transactionDate;
    private Double amount;
}

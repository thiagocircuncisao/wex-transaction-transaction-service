package thiagocircuncisao.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseResponse {
    private String id;
    private String description;
    private String transactionDate;
    private Double amount;
}

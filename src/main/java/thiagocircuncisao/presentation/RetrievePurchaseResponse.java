package thiagocircuncisao.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetrievePurchaseResponse {
    String id;
    String description;
    String transactionDate;
    Double amount;
    String country;
    String currency;
    Double exchangeRate;
}

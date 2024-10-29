package thiagocircuncisao.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetrievePurchaseRequest {
    private String id;
    private String country;
    private String currency;
}

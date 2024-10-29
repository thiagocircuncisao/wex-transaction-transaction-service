package thiagocircuncisao.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import thiagocircuncisao.model.PurchaseTransaction;
import thiagocircuncisao.presentation.PurchaseRequest;
import thiagocircuncisao.presentation.PurchaseResponse;
import thiagocircuncisao.presentation.RetrievePurchaseResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {
    PurchaseTransaction toTransaction(PurchaseRequest purchaseRequest);
    PurchaseResponse toPurchaseResponse(PurchaseTransaction purchaseTransaction);
    RetrievePurchaseResponse toRetrievePurchaseResponse(PurchaseTransaction purchaseTransaction);
}

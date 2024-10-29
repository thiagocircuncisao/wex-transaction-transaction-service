package thiagocircuncisao.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thiagocircuncisao.exception.TransactionException;
import thiagocircuncisao.presentation.*;
import thiagocircuncisao.service.TransactionsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionsController {
    @Autowired
    private TransactionsService transactionsService;

    @RequestMapping(value = "/create-purchase", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<PurchaseResponse> purchase(@RequestBody PurchaseRequest purchaseRequest) {
        return ResponseEntity.ok(transactionsService.createTransaction(purchaseRequest));
    }

    @RequestMapping(value = "/retrieve-purchase", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<List<RetrievePurchaseResponse>> retrievePurchase(@RequestBody RetrievePurchaseRequest retrievePurchaseRequest) {
        if (retrievePurchaseRequest.getCountry() == null || retrievePurchaseRequest.getCountry().isEmpty()) {
            throw new TransactionException("Country must be provided");
        }

        if (retrievePurchaseRequest.getCurrency() == null || retrievePurchaseRequest.getCurrency().isEmpty()) {
            throw new TransactionException("Currency must be provided");
        }

        return ResponseEntity.ok(transactionsService.getTransactionById(retrievePurchaseRequest));
    }

    @RequestMapping(value = "/retrieve-currencies", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CurrencyResponse>> retrieveCurrencies() {
        return ResponseEntity.ok(transactionsService.retrieveCurrencies());
    }
}

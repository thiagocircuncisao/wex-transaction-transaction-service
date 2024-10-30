package thiagocircuncisao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thiagocircuncisao.exception.TransactionException;
import thiagocircuncisao.mapper.CurrencyMapper;
import thiagocircuncisao.mapper.CurrencyMapperImpl;
import thiagocircuncisao.mapper.TransactionMapper;
import thiagocircuncisao.mapper.TransactionMapperImpl;
import thiagocircuncisao.model.Currency;
import thiagocircuncisao.model.PurchaseTransaction;
import thiagocircuncisao.presentation.*;
import thiagocircuncisao.repository.TransactionRepository;
import thiagocircuncisao.repository.impl.CurrencyRepositoryImpl;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionsService {
    private final TransactionRepository transactionRepository;
    private final CurrencyRepositoryImpl currencyRepositoryImpl;

    public List<RetrievePurchaseResponse> getTransactionById(RetrievePurchaseRequest retrievePurchaseRequest) {
        PurchaseTransaction purchaseTransaction = transactionRepository.findById(retrievePurchaseRequest.getId()).orElseThrow(()
                -> new TransactionException("PurchaseTransaction not found with id: " + retrievePurchaseRequest.getId()));

        List<Currency> currencies = getCurrencies(currencyRepositoryImpl.getCurrency(retrievePurchaseRequest.getCountry(), retrievePurchaseRequest.getCurrency())).stream()
                .filter(c -> c.getCountry().equals(retrievePurchaseRequest.getCountry()) && c.getCurrency().equals(retrievePurchaseRequest.getCurrency())).toList();

        if (currencies.isEmpty())
            throw new TransactionException("Currency not found with country: " + retrievePurchaseRequest.getCountry() + " and currency: " + retrievePurchaseRequest.getCurrency()
                    + "or currency hasn't been updated in 6 months");

        TransactionMapper transactionMapper = new TransactionMapperImpl();
        return currencies.stream().map(currency -> {
            RetrievePurchaseResponse retrievePurchaseResponse = transactionMapper.toRetrievePurchaseResponse(purchaseTransaction);
            retrievePurchaseResponse.setCurrency(currency.getCurrency());
            retrievePurchaseResponse.setCountry(currency.getCountry());
            retrievePurchaseResponse.setExchangeRate(currency.getExchangeRate());
            retrievePurchaseResponse.setAmount(Math.round((retrievePurchaseResponse.getAmount() * currency.getExchangeRate()) * 100.0) / 100.0);
            return retrievePurchaseResponse;
        }).toList();
    }

    public PurchaseResponse createTransaction(PurchaseRequest purchaseRequest) {
        if (purchaseRequest.getDescription().length() > 50)
            throw new TransactionException("Description must be less than 50 characters");

        isDateValid(purchaseRequest.getTransactionDate());

        if (purchaseRequest.getAmount() < 0)
            throw new TransactionException("Total must be greater than 0");

        if (BigDecimal.valueOf(purchaseRequest.getAmount()).scale() > 2)
            throw new TransactionException("Total must have 2 decimal places");

        TransactionMapper transactionMapper = new TransactionMapperImpl();
        PurchaseTransaction purchaseTransaction = transactionMapper.toTransaction(purchaseRequest);
        transactionRepository.save(purchaseTransaction);
        return transactionMapper.toPurchaseResponse(purchaseTransaction);
    }

    public List<CurrencyResponse> retrieveCurrencies() {
        List<Currency> currencies = getCurrencies(currencyRepositoryImpl.getCurrencies());

        CurrencyMapper currencyMapper = new CurrencyMapperImpl();
        return currencies.stream().map(currencyMapper::toCurrencyResponse).toList();
    }

    private List<Currency> getCurrencies(Map result) {
        ArrayList<Map> currencies = (ArrayList<Map>) result.get("data");
        return currencies.stream().map(currency -> Currency.builder()
                .currency((String) currency.get("currency"))
                .country((String) currency.get("country"))
                .exchangeRate(Double.valueOf((String) currency.get("exchange_rate")))
                .sourceLineNumber((String) currency.get("src_line_nbr"))
                .recordDate(LocalDate.parse((String) currency.get("record_date")))
                .build()).toList();
    }

    private void isDateValid(String dateText) {
        try {
            LocalDate.parse(dateText);
        } catch (Exception e) {
            throw new TransactionException("Date invalid be sure it is in format yyyy-MM-dd,and the day and month are valid values");
        }

        if (LocalDate.parse(dateText).isAfter(LocalDate.now()))
            throw new TransactionException("Date must be before today");
    }
}
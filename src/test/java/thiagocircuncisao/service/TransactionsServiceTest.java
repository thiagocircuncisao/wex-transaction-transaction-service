package thiagocircuncisao.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import thiagocircuncisao.exception.TransactionException;
import thiagocircuncisao.model.PurchaseTransaction;
import thiagocircuncisao.presentation.*;
import thiagocircuncisao.repository.TransactionRepository;
import thiagocircuncisao.repository.impl.CurrencyRepositoryImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionsServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CurrencyRepositoryImpl currencyRepository;

    @InjectMocks
    private TransactionsService transactionsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionsService = new TransactionsService(transactionRepository, currencyRepository);
    }

    @Test
    void testGetTransactionByIdSuccess() {
        RetrievePurchaseRequest request = generateRetrievePurchaseRequest();

        PurchaseTransaction purchaseTransaction = new PurchaseTransaction();
        purchaseTransaction.setId(123L);
        purchaseTransaction.setAmount(100.0);

        when(transactionRepository.findById(request.getId())).thenReturn(Optional.of(purchaseTransaction));
        when(currencyRepository.getCurrency(anyString(), anyString())).thenReturn(Map.of("data",
                new ArrayList<>(List.of(generateMapOfCurrencies()))));

        List<RetrievePurchaseResponse> response = transactionsService.getTransactionById(request);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals("USD", response.get(0).getCurrency());
        assertEquals("US", response.get(0).getCountry());
        assertEquals(120.0, response.get(0).getAmount());
    }

    @Test
    void testGetTransactionByIdCurrencyNotFound() {
        RetrievePurchaseRequest request = generateRetrievePurchaseRequest();

        PurchaseTransaction purchaseTransaction = new PurchaseTransaction();

        when(transactionRepository.findById(anyString())).thenReturn(Optional.of(purchaseTransaction));
        when(currencyRepository.getCurrency(anyString(), anyString())).thenReturn(Map.of("data", new ArrayList<>()));

        TransactionException exception = assertThrows(TransactionException.class,
                () -> transactionsService.getTransactionById(request));
        assertTrue(exception.getMessage().contains("Currency not found"));
    }

    @Test
    void testCreateTransactionSuccess() {
        PurchaseRequest request = generatePurchaseRequest();
        request.setAmount(100.0);

        PurchaseTransaction purchaseTransaction = new PurchaseTransaction();

        when(transactionRepository.save(any())).thenReturn(purchaseTransaction);
        PurchaseResponse response = transactionsService.createTransaction(request);

        assertNotNull(response);
    }

    @Test
    void testCreateTransactionInvalidAmount() {
        PurchaseRequest request = generatePurchaseRequest();
        request.setAmount(-100.0);

        TransactionException exception = assertThrows(TransactionException.class,
                () -> transactionsService.createTransaction(request));
        assertTrue(exception.getMessage().contains("Total must be greater than 0"));
    }

    @Test
    void testRetrieveCurrenciesSuccess() {
        when(currencyRepository.getCurrencies()).thenReturn(Map.of("data",
                new ArrayList<>(List.of(generateMapOfCurrencies()))));

        List<CurrencyResponse> response = transactionsService.retrieveCurrencies();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals("USD", response.get(0).getCurrency());
        assertEquals("US", response.get(0).getCountry());
        assertEquals(1.2, response.get(0).getExchangeRate());
    }

    @Test
    void testIsDateValidInvalidDate() {
        String invalidDate = "2024-15-15";

        TransactionException exception = assertThrows(TransactionException.class,
                () -> transactionsService.createTransaction(PurchaseRequest.builder().description("Description test")
                                .amount(100.0).transactionDate(invalidDate).build()));
        assertTrue(exception.getMessage().contains("Date invalid"));
    }

    @Test
    void testIsDateValidFutureDate() {
        String futureDate = "2025-01-01";

        TransactionException exception = assertThrows(TransactionException.class,
                () -> transactionsService.createTransaction(PurchaseRequest.builder().description("Description test")
                        .amount(100.0).transactionDate(futureDate).build()));
        assertTrue(exception.getMessage().contains("Date must be before today"));
    }

    private RetrievePurchaseRequest generateRetrievePurchaseRequest() {
        return RetrievePurchaseRequest.builder()
                .id("123")
                .country("US")
                .currency("USD")
                .build();
    }

    private PurchaseRequest generatePurchaseRequest() {
        return PurchaseRequest.builder()
                .description("Test Transaction")
                .transactionDate("2024-01-01")
                .build();
    }

    private Map<String, String> generateMapOfCurrencies() {
        return Map.of("country", "US", "currency", "USD", "exchange_rate", "1.2",
                "record_date", "2024-01-01");
    }
}

package thiagocircuncisao.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CurrencyRepositoryImplTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyRepositoryImpl currencyRepository;

    private static final String mockUrl = "http://mock-url";

    @BeforeEach
    void setUp() {
        openMocks(this);
        ReflectionTestUtils.setField(currencyRepository, "currencyApiUrl", mockUrl);
    }

    @Test
    void getCurrenciesShouldReturnMapWhenApiCallIsSuccessful() {
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("key", "value");

        String url = mockUrl + "?filter=record_date:gte:" + LocalDate.now().minusMonths(6);

        when(restTemplate.exchange(
                eq(url),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))
        ).thenReturn(ResponseEntity.ok(expectedResponse));

        Map<String, Object> actualResponse = currencyRepository.getCurrencies();

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getCurrenciesShouldThrowRuntimeExceptionWhenApiCallFails() {
        String url = mockUrl + "?filter=record_date:gte:" + LocalDate.now().minusMonths(6);

        when(restTemplate.exchange(
                eq(url),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))
        ).thenThrow(new RuntimeException("API call failed"));

        Exception exception = assertThrows(RuntimeException.class, () -> currencyRepository.getCurrencies());
        assertEquals("API call failed", exception.getMessage());
    }

    @Test
    void getCurrencyShouldReturnMapWhenApiCallIsSuccessful() {
        String country = "US";
        String currencyName = "USD";
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("key", "value");

        String url = mockUrl + "?filter=country:eq:" + country
                + ",currency:eq:" + currencyName
                + ",record_date:gte:" + LocalDate.now().minusMonths(6);

        when(restTemplate.exchange(
                eq(url),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))
        ).thenReturn(ResponseEntity.ok(expectedResponse));

        Map<String, Object> actualResponse = currencyRepository.getCurrency(country, currencyName);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getCurrencyShouldThrowRuntimeExceptionWhenApiCallFails() {
        String country = "US";
        String currencyName = "USD";
        String url = mockUrl + "?filter=country:eq:" + country
                + ",currency:eq:" + currencyName
                + ",record_date:gte:" + LocalDate.now().minusMonths(6);

        when(restTemplate.exchange(
                eq(url),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))
        ).thenThrow(new RuntimeException("API call failed"));

        Exception exception = assertThrows(RuntimeException.class, () -> currencyRepository.getCurrency(country, currencyName));
        assertEquals("API call failed", exception.getMessage());
    }
}
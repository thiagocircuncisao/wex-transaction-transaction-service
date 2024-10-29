package thiagocircuncisao.repository.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class CurrencyRepositoryImpl {
    @Value("${currency-api.url}")
    private String currencyApiUrl = "";

    private final RestTemplate restTemplate;

    public CurrencyRepositoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public Map<String, Object> getCurrencies() {
        String url = currencyApiUrl + "?filter=record_date:gte:" + LocalDate.now().minusMonths(6);
        HttpEntity<String> entity = new HttpEntity<>(null, createHeaders());

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET,
                entity, new ParameterizedTypeReference<>() {});

        return response.getBody();
    }

    public Map<String, Object> getCurrency(String country, String currencyName) {
        String url = currencyApiUrl + "?filter=country:eq:" + country
                + ",currency:eq:" + currencyName
                + ",record_date:gte:" + LocalDate.now().minusMonths(6);

        HttpEntity<String> entity = new HttpEntity<>(null, createHeaders());

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET,
                entity, new ParameterizedTypeReference<>() {});

        return response.getBody();
    }
}

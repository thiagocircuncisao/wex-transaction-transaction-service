package thiagocircuncisao.mapper;

import org.mapstruct.Mapper;
import thiagocircuncisao.model.Currency;
import thiagocircuncisao.presentation.CurrencyResponse;

@Mapper
public interface CurrencyMapper {
    CurrencyResponse toCurrencyResponse(Currency currency);
    Currency toCurrency(CurrencyResponse currencyResponse);
}

package thiagocircuncisao.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import thiagocircuncisao.exception.TransactionException;

@ControllerAdvice
public class ResponseExceptionHandler {

	@ExceptionHandler(TransactionException.class)
	public ResponseEntity<ErrorResponse> handleTransactionException(TransactionException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(ex.getMessage()); // A mensagem da exceção
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@Setter
    @Getter
    public static class ErrorResponse {
        private String message;
    }
}
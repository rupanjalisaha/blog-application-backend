/*
 * import org.springframework.web.bind.annotation.ControllerAdvice; import
 * org.springframework.web.bind.annotation.ExceptionHandler; import
 * org.springframework.http.ResponseEntity;
 * 
 * import java.util.Map;
 * 
 * import org.springframework.http.HttpStatus;
 * 
 * @ControllerAdvice public class GlobalExceptionHandler {
 * 
 * @ExceptionHandler(TokenExpiredException.class) public ResponseEntity<?>
 * handleRuntime(TokenExpiredException ex) { return ResponseEntity
 * .status(HttpStatus.BAD_REQUEST) .body(Map.of("error", ex.getMessage())); } }
 */
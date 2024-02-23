package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends ResponsibleRuntimeException implements Responsible {


    public BusinessException(String message, int code) {
        super(message, code);
    }

}

package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends ResponsibleRuntimeException implements Responsible {



    public BusinessException(String message, int code) {
        super(message, code);
    }


    public BusinessException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    public BusinessException(String message, Throwable cause, int code, String title) {
        super(message, cause, code, title);
    }

    public BusinessException(String message, Throwable cause, int code, String title, boolean directMessage) {
        super(message, cause, code, title);
        this.directMessage = directMessage;
    }

    public static BusinessException build(String message, int code) {
        return new BusinessException(message, code);
    }

    public static BusinessException build(String message, int code, Throwable cause) {
        return new BusinessException(message, cause, code);
    }
}

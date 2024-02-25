package exception;

public class ResponsibleRuntimeException extends RuntimeException implements Responsible {

    public String title;
    public int code;
    public boolean directMessage = false;



    public ResponsibleRuntimeException(String message, int code) {
        super(message);
        this.code = code;
    }


    public ResponsibleRuntimeException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public ResponsibleRuntimeException(String message, Throwable cause, int code, String title) {
        super(message, cause);
        this.code = code;
        this.title = title;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }


    @Override
    public boolean getDirectMessage() {
        return directMessage;
    }

    public void setDirectMessage(Boolean direct) {
         directMessage=direct;
    }

    public void setTitle(String titlei) {

        title=titlei;
    }
}

package exception;

public class ResponsibleRuntimeException extends RuntimeException implements Responsible {

    public String title;
    public int code;
    public boolean directMessage = false;


    public ResponsibleRuntimeException(String message, int code) {
        super(message);
        this.code = code;
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

}

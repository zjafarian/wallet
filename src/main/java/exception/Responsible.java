package exception;

public interface Responsible {

    int getCode();

    void setCode(int code);

    String getMessage();

    boolean getDirectMessage();

    default ResponseErrorData getInitModelData() {
        return new ResponseAbleData(getCode(), getMessage());
    }

    default ResponseErrorData getInitModelData(String msg) {
        return new ResponseAbleData(getCode(), msg);
    }

    default ResponseErrorData getInitModelData(String msg, int code) {
        setCode(code);
        return new ResponseAbleData(getCode(), msg);
    }
}

package service.transaction.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchaseResponseModel {


    private boolean success;
    private String doTime;
    private String message;
    private int code;
    private String referenceNumber;
}



package service.transaction.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TransactionRequest {

    @NotNull
    private Long amount;

    @NotNull
    private Long walletCardId;

    @NotNull
    private Long merchantId;
}

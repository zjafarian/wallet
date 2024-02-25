package service.transaction.model;

import base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import model.Merchant;
import model.WalletCard;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import service.merchant.model.MerchantRequest;
import service.wallet.model.WalletCardRequest;


@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PurchaseRequest {

    @NotNull
    private Long amount;

    @NotNull
    private Long walletCardId;

    @NotNull
    private Long financierId;

    @NotNull
    private Long merchantId;
}

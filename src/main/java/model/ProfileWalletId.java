package model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileWalletId {
    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "wallet_id")
    private Long walletId;
}

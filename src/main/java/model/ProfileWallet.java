package model;

import base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Builder
@Table(name = "profile_wallet")
@NoArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
public class ProfileWallet extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_wallet_id")
    private Long id;

    @EmbeddedId
    private ProfileWalletId profileWalletId;

    @ManyToOne
    @MapsId("profileId")
    private Profile profile;

    @ManyToOne
    @MapsId("walletId")
    private Wallet wallet;

}

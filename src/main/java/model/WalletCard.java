package model;

import base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Builder
@Table(name = "wallet_card")
@NoArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
public class WalletCard extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_card_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "wallet_card_type")
    @Enumerated(value = EnumType.STRING)
    private WalletCardType walletCardType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Financier_id", nullable = false)
    private Financier financier;

    @ManyToOne
    @JoinColumn(name = "wallet_id", referencedColumnName="id")
    private Wallet wallet;
}

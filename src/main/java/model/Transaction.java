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
@Table(name = "transaction")
@NoArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "transaction_status")
    @Enumerated(value = EnumType.STRING)
    private TransactionStatus transactionStatus;


    @ManyToOne
    @JoinColumn(name = "wallet_card_id", referencedColumnName="id")
    private WalletCard walletCard;

    @ManyToOne
    @JoinColumn(name = "merchant_id", referencedColumnName="id")
    private Merchant merchant;

}

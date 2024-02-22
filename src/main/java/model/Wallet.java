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
@Table(name = "wallet")
@NoArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
public class Wallet  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "total_balance")
    private Long totalBalance;



}

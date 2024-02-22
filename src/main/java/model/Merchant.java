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
@Table(name = "merchant")
@NoArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
public class Merchant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_id")
    private Long id;


    @Column(name = "name")
    private String name;
}

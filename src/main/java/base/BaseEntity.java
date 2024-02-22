package base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@JsonIgnoreProperties(
        value = {"createdOn", "updatedBy", "createdBy", "updatedOn"},
        allowGetters = true
)
@Data
public abstract class BaseEntity {
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false/*, columnDefinition = "bigint default 1"*/)
    private Long createdBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    @Column(name = "created_on", nullable = false, updatable = false/*, columnDefinition = "timestamp without time zone default now()"*/)
    private Date createdOn;

    @LastModifiedBy
    @Column(name = "updated_by", nullable = false/*, columnDefinition = "bigint default 1"*/)
    private Long updatedBy;

    @LastModifiedDate
    @Column(name = "updated_on", nullable = false/*, columnDefinition = "timestamp without time zone default now()"*/)
    @Temporal(TIMESTAMP)
    private Date updatedOn;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private Boolean isDeleted = false;

}

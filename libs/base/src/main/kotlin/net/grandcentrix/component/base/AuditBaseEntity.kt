package net.grandcentrix.component.base

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditBaseEntity(

    id: UUID = UUID.randomUUID(),

    @CreatedDate
    @Column(updatable = false)
    var createdDate: Instant = Instant.now(),

    @LastModifiedDate
    var updatedDate: Instant = Instant.now(),
) : BaseEntity(id)

package net.grandcentrix.component.base.entity.example

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import net.grandcentrix.component.base.entity.AuditBaseEntity
import java.util.UUID

@Entity
class ComplexEntity(
    @OneToMany(cascade = [CascadeType.PERSIST], fetch = FetchType.EAGER) var compositeList: MutableList<BaseEntityImpl>,
    id: UUID = UUID.randomUUID(),
) : AuditBaseEntity(id)

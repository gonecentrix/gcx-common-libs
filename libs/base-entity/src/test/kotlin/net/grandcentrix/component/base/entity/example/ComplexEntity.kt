package net.grandcentrix.component.base.entity.example

import net.grandcentrix.component.base.entity.AuditBaseEntity
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany

@Entity
class ComplexEntity(
    @OneToMany(cascade = [CascadeType.PERSIST], fetch = FetchType.EAGER) var compositeList: MutableList<BaseEntityImpl>,
    id: UUID = UUID.randomUUID(),
) : AuditBaseEntity(id)

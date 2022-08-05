package net.grandcentrix.component.base.entity.example

import net.grandcentrix.component.base.entity.BaseEntity
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToOne

@Entity
class LazyFetchedParent(
    @ManyToOne(
        optional = false,
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST]
    ) var lazyFetchedEntity: BaseEntityImpl
) : BaseEntity()

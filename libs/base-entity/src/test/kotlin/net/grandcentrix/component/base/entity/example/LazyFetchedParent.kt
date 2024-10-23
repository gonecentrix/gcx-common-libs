package net.grandcentrix.component.base.entity.example

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne
import net.grandcentrix.component.base.entity.BaseEntity

@Entity
class LazyFetchedParent(
    @ManyToOne(
        optional = false,
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST],
    ) var lazyFetchedEntity: BaseEntityImpl,
) : BaseEntity()

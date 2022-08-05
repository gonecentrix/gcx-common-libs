package net.grandcentrix.component.base.entity.example

import net.grandcentrix.component.base.repository.BaseJpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LazyFetchedParentRepository : BaseJpaRepository<LazyFetchedParent>

package net.grandcentrix.component.base.repository

import net.grandcentrix.component.base.entity.BaseEntity
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.UUID

/**
 * Base PagingAndSortingRepository that declares the correct primary key type for [BaseEntity]
 */
@NoRepositoryBean
interface BasePagingAndSortingRepository<T : BaseEntity> : PagingAndSortingRepository<T, UUID>

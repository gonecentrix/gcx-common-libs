package net.grandcentrix.component.base.repository

import net.grandcentrix.component.base.entity.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.UUID

/**
 * Base JPA repository that declares the correct primary key type for [BaseEntity]
 */
@NoRepositoryBean
interface BaseJpaRepository<T : BaseEntity> : JpaRepository<T, UUID>

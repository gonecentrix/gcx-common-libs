package net.grandcentrix.component.base.repository

import net.grandcentrix.component.base.config.DataSourceProperties
import net.grandcentrix.component.base.entity.BaseEntity
import org.springframework.stereotype.Component
import java.util.UUID
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import kotlin.reflect.KClass

@Component
class RepositoryWithExclusiveLock(
    private val entityManager: EntityManager,
    private val customRepositoryContext: CustomRepositoryContext,
    private val dataSourceProperties: DataSourceProperties,
) {
    @Suppress("UNCHECKED_CAST")
    fun <T : BaseEntity> findAndObtainExclusiveLockOnItById(ofClass: KClass<T>, id: UUID): T? {
        customRepositoryContext.setLockTimeout(entityManager, dataSourceProperties.lockTimeoutMs)

        val query = entityManager.createQuery(
            "SELECT c FROM " + ofClass.simpleName + " c where c.id = :id"
        ).apply {
            setParameter("id", id)
            lockMode = LockModeType.PESSIMISTIC_WRITE
        }

        val result = query.resultStream.findFirst().orElse(null)

        customRepositoryContext.afterQueryHook()

        return result as T?
    }

    final inline fun <reified T : BaseEntity> findAndObtainExclusiveLockOnItById(id: UUID): T? {
        return findAndObtainExclusiveLockOnItById(T::class, id)
    }
}

package net.grandcentrix.component.base.repository

import jakarta.persistence.EntityManager
import jakarta.persistence.LockModeType
import net.grandcentrix.component.base.config.DataSourceProperties
import net.grandcentrix.component.base.entity.BaseEntity
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.reflect.KClass

/**
 * This class facilitates selecting entities for update (i.e. using pessimistic locking/write).
 * It selects any record by its id applying a lock timeout based on your configuration properties.
 *
 * The use case for selecting an entity for update and acquiring a lock on that specific database record is when your
 * system tries to concurrently select the same entity and then tries to update it.
 *
 * By default, JPA repositories use Optimistic locking strategies, which means that if two processes load
 * the same entity to memory they will both have the same `version`.
 *
 * However, one of these processes will be able to persist the changes to that entity before the other,
 * increasing the `version` of that entity. When the second process tries to persist its changes, the second
 * process will fail due to an `OptmisticLockException`.
 *
 * This repository enables the synchronisation of these processes, the first process tries to load the entity
 * into memory (via a select for update), PostgreSQL will set a lock on that record, so the second process will
 * be blocked (and will not load that entity into memory) until the first process has committed its transaction.
 *
 * To use this component, just add it to your service dependencies, and call it specifying your entity as a return type:
 * @Service
 * class MyService(private val repositoryWithExclusiveLock: RepositoryWithExclusiveLock) {
 *     fun getEntityById(id: UUID): Entity = repositoryWithExclusiveLock
 *         .findAndObtainExclusiveLockOnItById(id) ?: throw NotFoundException("Entity with id $id was not found.")
 * }
 *
 * You can also just use the annotation @Lock(LockModeType.PESSIMISTIC_WRITE), however you cannot set the lock timeout
 * which defaults to NO_WAIT
 *
 * @see net.grandcentrix.component.base.config.DataSourceProperties
 * @see https://www.postgresql.org/docs/current/runtime-config-client.html#GUC-LOCK-TIMEOUT
 *
 * @param entityManager The entity manager injected automatically
 * @param customRepositoryContext The custom repository context which sets the lock timeout
 * @param dataSourceProperties Data source properties containing the lock timeout duration
 *
 */
@Component
class RepositoryWithExclusiveLock(
    private val entityManager: EntityManager,
    private val customRepositoryContext: CustomRepositoryContext,
    private val dataSourceProperties: DataSourceProperties
) {
    @Suppress("UNCHECKED_CAST")
    fun <T : BaseEntity> findAndObtainExclusiveLockOnItById(ofClass: KClass<T>, id: UUID): T? {
        customRepositoryContext.setLockTimeout(entityManager, dataSourceProperties.lockTimeout.toMillis())

        val query = entityManager.createQuery(
            "SELECT c FROM " + ofClass.simpleName + " c WHERE c.id = :id"
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

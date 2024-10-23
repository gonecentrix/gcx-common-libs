package net.grandcentrix.component.base.repository

import jakarta.persistence.EntityManager
import jakarta.persistence.Query
import org.springframework.stereotype.Component

/**
 * This class provides a function to programmatically set a lock timeout for PostgreSQL databases.
 * In addition, it provides an after query hook that is called from RepositoryWithExclusiveLock, after executing
 * a select query.
 *
 * @see net.grandcentrix.component.base.repository.RepositoryWithExclusiveLock
 */
@Component
class CustomRepositoryContext {
    /**
     * Used in integration tests, this function is used keep the CPU busy and raise timeout exceptions,
     * in order to ensure that the lock timeout has been set correctly.
     */
    fun afterQueryHook() {
        // do nothing
    }

    /**
     * Sets the lock timeout
     *
     * @param entityManager The entity manager
     * @param timeoutDurationInMs the duration of the timeout in milliseconds
     */
    fun setLockTimeout(
        entityManager: EntityManager,
        timeoutDurationInMs: Long,
    ) {
        val query: Query = entityManager.createNativeQuery("SET LOCAL LOCK_TIMEOUT = $timeoutDurationInMs")
        query.executeUpdate()
    }
}

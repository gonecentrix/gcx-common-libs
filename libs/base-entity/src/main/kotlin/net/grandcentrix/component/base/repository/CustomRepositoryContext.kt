package net.grandcentrix.component.base.repository

import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.persistence.Query

@Component
class CustomRepositoryContext {
    fun afterQueryHook() {
        // do nothing
    }

    fun setLockTimeout(entityManager: EntityManager, timeoutDurationInMs: Long) {
        val query: Query = entityManager.createNativeQuery("set local lock_timeout = $timeoutDurationInMs")
        query.executeUpdate()
    }
}

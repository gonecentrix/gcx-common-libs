package net.grandcentrix.component.base.entity

import assertk.all
import assertk.assertThat
import assertk.assertions.isNotNull
import assertk.assertions.prop
import com.ninjasquad.springmockk.SpykBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import net.grandcentrix.component.base.entity.example.ComplexEntity
import net.grandcentrix.component.base.entity.example.ComplexEntityRepository
import net.grandcentrix.component.base.repository.CustomRepositoryContext
import net.grandcentrix.component.base.repository.RepositoryWithExclusiveLock
import net.grandcentrix.component.testcontainers.DataJpaIntegrationTest
import org.hibernate.PessimisticLockException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import java.lang.Thread.sleep
import java.util.UUID
import java.util.concurrent.Executors

@BaseLibraryTest
@DataJpaIntegrationTest
@Transactional(propagation = Propagation.NEVER)
class RepositoryWithExclusiveLockIntTest(
    @Autowired private val exampleRepository: ComplexEntityRepository,
    @Autowired private val repositoryWithExclusiveLock: RepositoryWithExclusiveLock,
    @Autowired private val transactionTemplate: TransactionTemplate
) {

    @SpykBean
    private lateinit var customRepositoryContext: CustomRepositoryContext

    @Transactional
    @Test
    fun `find by id should obtain exclusive lock and find entity successfully`() {
        val logicController = exampleRepository.save(createEntity())

        findEntity(logicController.id)
    }

    @Test
    fun `consecutive finds should execute concurrently without errors`() {
        val logicController = exampleRepository.saveAndFlush(
            createEntity()
        )

        every { customRepositoryContext.afterQueryHook() } just Runs

        val es = Executors.newFixedThreadPool(2)

        val f1 = es.submit {
            findAndAssertResult(logicController.id)
        }
        val f2 = es.submit {
            findAndAssertResult(logicController.id)
        }

        f1.get()
        f2.get()
    }

    @Test
    fun `second find by id should timeout due to long running lock acquired previously tx`() {
        val complexEntity = exampleRepository.saveAndFlush(
            createEntity()
        )

        val es = Executors.newFixedThreadPool(2)

        val lock = Object()

        val f2 = es.submit {
            synchronized(lock) {
                lock.wait(1000)
                assertThrows<PessimisticLockException> {
                    findEntity(complexEntity.id)
                }
            }
        }

        val f1 = es.submit {
            assertDoesNotThrow {
                transactionTemplate.execute {
                    synchronized(lock) {
                        repositoryWithExclusiveLock.findAndObtainExclusiveLockOnItById<ComplexEntity>(complexEntity.id)!!
                        lock.notifyAll()
                    }
                    sleep(3010)
                }
            }
        }

        f1.get()
        f2.get()

        es.shutdown()
    }

    private fun findAndAssertResult(id: UUID) {
        val logicController = findEntity(id)
        assertThatEntityIsRetrievedCorrectly(logicController)
    }

    private fun createEntity() = ComplexEntity(
        compositeList = mutableListOf()
    )

    private fun findEntity(id: UUID): ComplexEntity {
        return transactionTemplate.execute {
            repositoryWithExclusiveLock.findAndObtainExclusiveLockOnItById(id)!!
        }!!
    }

    private fun assertThatEntityIsRetrievedCorrectly(exampleEntity: ComplexEntity) {
        assertThat(exampleEntity).all {
            prop(ComplexEntity::id).isNotNull()
        }
    }
}

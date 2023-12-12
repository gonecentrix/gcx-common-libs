package net.grandcentrix.component.base.entity

import assertk.all
import assertk.assertThat
import assertk.assertions.isNotNull
import assertk.assertions.prop
import com.ninjasquad.springmockk.SpykBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import jakarta.persistence.PessimisticLockException
import net.grandcentrix.component.base.entity.example.ComplexEntity
import net.grandcentrix.component.base.entity.example.ComplexEntityRepository
import net.grandcentrix.component.base.repository.CustomRepositoryContext
import net.grandcentrix.component.base.repository.RepositoryWithExclusiveLock
import net.grandcentrix.component.testcontainers.DataJpaIntegrationTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import java.lang.Thread.sleep
import java.util.UUID
import java.util.concurrent.Executors
import kotlin.concurrent.thread

@BaseLibraryTest
@DataJpaIntegrationTest
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

    @Transactional
    @Test
    fun `consecutive finds should execute concurrently without errors`() {
        val logicController = exampleRepository.save(
            createEntity()
        )

        val thread1 = thread(start = false) {
            every { customRepositoryContext.afterQueryHook() } just Runs
            findAndAssertResult(logicController.id)
        }
        val thread2 = thread(start = false) {
            every { customRepositoryContext.afterQueryHook() } just Runs
            findAndAssertResult(logicController.id)
        }

        thread1.start()
        thread2.start()

        thread1.join()
        thread2.join()
    }

    @Transactional
    @Test
    fun `second find by id should timeout due to long running lock acquired previously tx`() {
        val logicController = exampleRepository.save(
            createEntity()
        )

        val es = Executors.newFixedThreadPool(2)

        val f1 = es.submit {
            every { customRepositoryContext.afterQueryHook() } answers {
                sleep(3000)
            }
            findAndAssertResult(logicController.id)
        }

        val f2 = es.submit {
            every { customRepositoryContext.afterQueryHook() } just Runs
            assertThrows<PessimisticLockException> {
                findEntity(logicController.id)
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

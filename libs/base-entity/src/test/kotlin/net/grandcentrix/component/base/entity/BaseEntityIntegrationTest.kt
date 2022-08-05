package net.grandcentrix.component.base.entity

import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isInstanceOf
import net.grandcentrix.component.base.entity.example.BaseEntityImpl
import net.grandcentrix.component.base.entity.example.ComplexEntity
import net.grandcentrix.component.base.entity.example.ComplexEntityRepository
import net.grandcentrix.component.base.entity.example.LazyFetchedParent
import net.grandcentrix.component.base.entity.example.LazyFetchedParentRepository
import net.grandcentrix.component.testcontainers.BaseDatabaseIntegrationTest
import org.hibernate.proxy.HibernateProxy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import java.time.Instant
import java.util.UUID

class BaseEntityIntegrationTest {

    @BaseLibraryTest
    internal class BaseEntityTest(@Autowired val repo: ComplexEntityRepository) : BaseDatabaseIntegrationTest() {

        @Test
        fun `when save is called the persist operation is cascaded`() {
            val composite1 = BaseEntityImpl()
            val composite2 = BaseEntityImpl()
            /*
             The complex entity has CascadeType.PERSIST defined and we test it by only saving the complex entity and
             not the CompositeEntity.
             */
            val complexEntity = repo.save(ComplexEntity(mutableListOf(composite1, composite2)))

            assertThat(repo.findByIdOrNull(complexEntity.id)!!.compositeList).containsExactlyInAnyOrder(
                composite1,
                composite2
            )
        }

        @Test
        fun `when save is called the created date and updated data are saved correctly`() {
            val id = UUID.randomUUID()
            val now = Instant.now()

            repo.save(ComplexEntity(mutableListOf(), id))
            var complexEntity = repo.findByIdOrNull(id)!!

            assertThat(complexEntity.createdDate).isGreaterThan(now)
            assertThat(complexEntity.updatedDate).isGreaterThan(now)

            Thread.sleep(10)

            complexEntity.compositeList.add(BaseEntityImpl())
            repo.save(complexEntity)

            complexEntity = repo.findByIdOrNull(id)!!

            assertThat(complexEntity.updatedDate).isGreaterThan(complexEntity.createdDate)
        }
    }

    @BaseLibraryTest
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    internal class LazyFetchTest(
        @Autowired val repo: LazyFetchedParentRepository,
        @Autowired val transactionTemplate: TransactionTemplate
    ) : BaseDatabaseIntegrationTest() {

        @Test
        fun `Check if entities can be fetched lazy`() {
            val entityId = repo.save(LazyFetchedParent(BaseEntityImpl())).id

            transactionTemplate.execute {
                val newlyFetchedEntity = repo.findByIdOrNull(entityId)!!

                assertThat(newlyFetchedEntity.lazyFetchedEntity).isInstanceOf(HibernateProxy::class)
            }
        }

        @Test
        fun `lazy fetched entities are equal to original object`() {
            val baseEntity = BaseEntityImpl()
            val entityId = repo.save(LazyFetchedParent(baseEntity)).id

            transactionTemplate.execute {
                val newlyFetchedEntity = repo.findByIdOrNull(entityId)!!

                assertThat(baseEntity).isEqualTo(newlyFetchedEntity.lazyFetchedEntity)
                assertThat(newlyFetchedEntity.lazyFetchedEntity).isEqualTo(baseEntity)
            }
        }
    }
}

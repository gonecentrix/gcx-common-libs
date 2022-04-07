package net.grandcentrix.component.base

import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import net.grandcentrix.component.testcontainers.BaseDatabaseIntegrationTest
import org.hibernate.proxy.HibernateProxy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
class BaseEntityImpl : BaseEntity()

@Entity
class ComplexEntity(
    @OneToMany(cascade = [CascadeType.PERSIST], fetch = FetchType.EAGER) var compositeList: MutableList<BaseEntityImpl>
) : BaseEntity()

@Repository
interface ComplexEntityRepository : BaseJpaRepository<ComplexEntity>

@Entity
class LazyFetchedParent(
    @ManyToOne(
        optional = false,
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST]
    ) var lazyFetchedEntity: BaseEntityImpl
) : BaseEntity()

@Repository
interface LazyFetchedParentRepository : BaseJpaRepository<LazyFetchedParent>

class BaseEntityIntegrationTest {

    @BaseLibraryTest
    internal open class BaseEntityTest(@Autowired val repo: ComplexEntityRepository) : BaseDatabaseIntegrationTest() {

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
    }

    @BaseLibraryTest
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    internal open class LazyFetchTest(
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

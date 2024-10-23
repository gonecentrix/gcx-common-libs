package net.grandcentrix.component.base.entity

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID

internal class BaseEntityTest {
    class BaseEntityImpl(id: UUID = UUID.randomUUID()) : BaseEntity(id)

    class AuditBaseEntityImpl(id: UUID = UUID.randomUUID()) : AuditBaseEntity(id)

    @Nested
    inner class Equals {
        @Test
        fun `Reflexive equivalence`() {
            val entity = BaseEntityImpl()

            assertThat(entity).isEqualTo(entity)
        }

        @Test
        fun `Symmetrical equivalence`() {
            val id = UUID.randomUUID()
            val entity1 = BaseEntityImpl(id)
            // We enforce the creation of a new UUID object to avoid a false positive for reference equality
            val entity2 = BaseEntityImpl(UUID.fromString(id.toString()))

            assertThat(entity1).isEqualTo(entity2)
            assertThat(entity2).isEqualTo(entity1)
        }

        @Test
        fun `Transitive equivalence`() {
            val id = UUID.randomUUID()
            val entity1 = BaseEntityImpl(id)
            val entity2 = BaseEntityImpl(UUID.fromString(id.toString()))
            val entity3 = BaseEntityImpl(UUID.fromString(id.toString()))

            assertThat(entity1).isEqualTo(entity2)
            assertThat(entity2).isEqualTo(entity3)
            assertThat(entity1).isEqualTo(entity3)
        }

        @Test
        fun `Subclasses are not equal`() {
            val id = UUID.randomUUID()
            val entity1 = BaseEntityImpl(id)
            val entity2 = AuditBaseEntityImpl(UUID.fromString(id.toString()))

            assertThat(entity1).isNotEqualTo(entity2)
        }

        @Test
        fun `Comparing with null is false`() {
            assertThat(BaseEntityImpl()).isNotEqualTo(null)
        }
    }

    @Nested
    inner class Hashcode {
        @Test
        fun `Equal objects have same hashcode`() {
            val id = UUID.randomUUID()
            val entity1 = BaseEntityImpl(id)
            val entity2 = AuditBaseEntityImpl(UUID.fromString(id.toString()))

            assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode())
        }
    }
}

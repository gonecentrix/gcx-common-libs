package net.grandcentrix.component.base.entity

import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Version
import org.hibernate.Hibernate
import java.util.UUID

/**
 * This class represents a base entity. All other created entities in the project should inherit
 * from [BaseEntity].
 * The [BaseEntity] contains:
 * @property id : the primary key of the entity. It s an random UUID
 * @property version: will be automatically incremented to avoid concurrent update of the same entry
 */
@MappedSuperclass
abstract class BaseEntity(
    /** only for internal usage, not to be used publicly */
    @Id var id: UUID = UUID.randomUUID(),
) {
    @Version
    var version: Long? = null

    override fun equals(other: Any?): Boolean =
        when {
            this === other -> true
            other == null -> false
            /*
             When we fetch an entity lazy, Hibernate creates a proxy. Since the proxy and the actual object is
             representing the same entity, we need to consider that here.
             */
            Hibernate.getClass(this) != Hibernate.getClass(other) -> false
            else -> id == (other as BaseEntity).id
        }

    override fun hashCode(): Int = id.hashCode()
}

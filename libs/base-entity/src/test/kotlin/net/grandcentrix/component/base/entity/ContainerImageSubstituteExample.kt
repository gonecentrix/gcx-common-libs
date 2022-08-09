package net.grandcentrix.component.base.entity

import net.grandcentrix.component.testcontainers.BaseContainerImageSubstitute

/**
 * Example of an implementation of a net.grandcentrix.component.testcontainers.BaseContainerImageSubstitute
 * Replaces the static PostgreSQL image version by a custom one for the base entity tests
 *
 * You also need to include the properties file `testcontainers.properties` in your test resources' folder
 * See more in: https://www.testcontainers.org/features/image_name_substitution/
 */
class ContainerImageSubstituteExample : BaseContainerImageSubstitute(
    imageVersion = IMAGE_VERSION
) {
    companion object {
        const val IMAGE_VERSION = "14-alpine"
    }
}

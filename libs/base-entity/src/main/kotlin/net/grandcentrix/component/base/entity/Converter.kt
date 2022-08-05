package net.grandcentrix.component.base.entity

@Deprecated("The Converter SAM interface will be moved into its own library")
fun interface Converter<A, B> {
    fun convert(input: A): B
}

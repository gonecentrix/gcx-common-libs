package net.grandcentrix.component.base.entity

fun interface Converter<A, B> {
    fun convert(input: A): B
}

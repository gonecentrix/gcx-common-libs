package net.grandcentrix.component.base

fun interface Converter<A, B> {
    fun convert(input: A): B
}

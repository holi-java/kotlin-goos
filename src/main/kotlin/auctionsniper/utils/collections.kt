package auctionsniper.utils

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


inline fun <V, R> Map<String, V>.capitalize(crossinline transform: (V?) -> R): ReadOnlyProperty<Any?, R> = select({ it.name.capitalize() }, transform)

@Suppress("NOTHING_TO_INLINE")
inline fun <K, V> Map<K, V>.required(alias: K): ReadOnlyProperty<Any?, V> = select({ alias }) { it!! }

inline fun <K, V, R> Map<K, V>.alias(alias: K, crossinline transform: (V?) -> R): ReadOnlyProperty<Any?, R> = select({ alias }, transform)

inline fun <K, V, R> Map<K, V>.select(crossinline selector: (KProperty<*>) -> K, crossinline transform: (V?) -> R): ReadOnlyProperty<Any?, R> {
    return object : ReadOnlyProperty<Any?, R> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): R = transform(get(selector(property)))
    }
}
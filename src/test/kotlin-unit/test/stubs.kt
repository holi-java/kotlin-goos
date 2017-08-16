package test

import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

inline fun <reified T : Any> ignored(): T = proxy(T::class) { _, _, _ -> }
inline fun <reified T : Any> unused(): T = proxy(T::class) { _, _, _ -> throw IllegalStateException() }

@PublishedApi internal fun <T : Any> proxy(type: KClass<T>, handler: (proxy: Any, method: Method, args: Array<Any>?) -> Any) = type.java.let {
    it.cast(Proxy.newProxyInstance(it.classLoader, arrayOf(it), handler))
}
package test

import java.lang.reflect.Proxy

inline fun <reified T> unused(): T {
    return T::class.java.let {
        it.cast(Proxy.newProxyInstance(it.classLoader, arrayOf(it)) { _, _, _ -> throw IllegalStateException() })
    }
}
package auctionsniper.utils

fun CharSequence.split(vararg delimiters: Char, limit: Int = 0): List<String> {
    return split(delimiters = *delimiters, ignoreCase = false, limit = limit)
            .dropLastWhile { limit == 0 && it.isBlank() }
}

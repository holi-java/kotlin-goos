package auctionsniper


enum class SniperState(closed: () -> SniperState = { throw IllegalStateException() }) {
    JOINING({ LOST }),
    BIDDING({ LOST }),
    WINNING({ WON }),
    WON,
    LOST;

    val closed by lazy { closed() }
}
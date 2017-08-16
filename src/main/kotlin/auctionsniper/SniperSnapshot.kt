package auctionsniper

import auctionsniper.SniperState.*

data class SniperSnapshot(val itemId: String, val lastPrice: Int, val lastBid: Int, val state: SniperState) {

    fun bidding(lastPrice: Int, lastBid: Int) = copy(lastPrice = lastPrice, lastBid = lastBid, state = BIDDING)

    fun winning(winningPrice: Int) = copy(lastPrice = winningPrice, lastBid = winningPrice, state = WINNING)

    val closed get() = copy(state = state.closed)

    fun sameAs(other: SniperSnapshot) = itemId == other.itemId

    companion object {
        fun joining(itemId: String) = SniperSnapshot(itemId, 0, 0, JOINING)
    }
}
package auctionsniper

import auctionsniper.PriceSource.FromSniper

class AuctionSniper(itemId: String, private val auction: Auction) : AuctionEventListener {
    private val listeners by lazy { mutableListOf<SniperListener>() }

    var snapshot: SniperSnapshot = SniperSnapshot.joining(itemId)
        private set(value) {
            field = value
            listeners.forEach { it.sniperStateChanged(value) }
        }

    override fun currentPrice(currentPrice: Int, increment: Int, source: PriceSource) {
        if (source == FromSniper) {
            snapshot = snapshot.winning(currentPrice)
        } else {
            val amount = currentPrice + increment
            auction.bid(amount)
            snapshot = snapshot.bidding(currentPrice, amount)
        }
    }

    override fun auctionClosed() {
        snapshot = snapshot.closed
    }

    fun addSniperListener(listener: SniperListener) {
        listeners += listener
    }
}
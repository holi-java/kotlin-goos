package auctionsniper

import auctionsniper.PriceSource.FromSniper

class AuctionSniper(itemId: String,
                    private val auction: Auction,
                    private val listener: SniperListener) : AuctionEventListener {

    private var snapshot: SniperSnapshot = SniperSnapshot.joining(itemId); set(value) {
        field = value
        listener.sniperStateChanged(value)
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


}
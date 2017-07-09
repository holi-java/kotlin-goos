package auctionsniper

class AuctionSniper(private val auction: Auction, private val listener: SniperListener) : AuctionEventListener {
    override fun currentPrice(currentPrice: Int, increment: Int) {
        auction.bid(currentPrice + increment)
        listener.sniperBidding()
    }

    override fun auctionClosed() = listener.sniperLost()

}
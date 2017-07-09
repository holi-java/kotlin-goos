package auctionsniper

import auctionsniper.PriceSource.*

class AuctionSniper(private val auction: Auction, private val listener: SniperListener) : AuctionEventListener {
    private var winning = false

    override fun currentPrice(currentPrice: Int, increment: Int, source: PriceSource) {
        winning = source == FromSniper

        if (winning) {
            listener.sniperWinning()
        } else {
            auction.bid(currentPrice + increment)
            listener.sniperBidding()
        }
    }

    override fun auctionClosed() = if (winning) listener.sniperWon() else listener.sniperLost()

}
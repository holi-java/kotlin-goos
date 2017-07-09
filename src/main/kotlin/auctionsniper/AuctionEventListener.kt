package auctionsniper

interface AuctionEventListener {

    fun currentPrice(currentPrice: Int, increment: Int, source: PriceSource)

    fun auctionClosed()

}

enum class PriceSource {
    FromSniper,
    FromOtherBidder;
}
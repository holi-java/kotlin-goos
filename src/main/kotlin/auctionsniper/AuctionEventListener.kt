package auctionsniper

interface AuctionEventListener {

    fun currentPrice(currentPrice: Int, increment: Int)

    fun auctionClosed()

}
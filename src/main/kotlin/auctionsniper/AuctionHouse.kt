package auctionsniper

interface AuctionHouse {

    fun disconnect()

    fun auctionFor(itemId: String): Auction

}
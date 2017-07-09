package auctionsniper

interface Auction {
    fun join()
    fun bid(amount: Int)
}
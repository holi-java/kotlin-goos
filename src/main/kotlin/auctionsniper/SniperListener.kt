package auctionsniper

interface SniperListener {
    fun sniperBidding()

    fun sniperWinning()

    fun sniperWon()

    fun sniperLost()
}
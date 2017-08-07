package auctionsniper

interface SniperListener {
    fun sniperStateChanged(snapshot: SniperSnapshot)
}
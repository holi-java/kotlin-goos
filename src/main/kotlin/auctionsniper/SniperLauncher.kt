package auctionsniper

import auctionsniper.ui.SwingThreadSniperListener

class SniperLauncher(private val auctionHouse: AuctionHouse, private val collector: SniperCollector) : UserRequestListener {
    private val snipers: MutableList<Auction> = mutableListOf()
    override fun joinAuction(itemId: String) {
        val auction = auctionHouse.auctionFor(itemId)
        val sniper = AuctionSniper(itemId, auction)
//        TODO: how to register SwingThreadSniperListener
//        sniper.addSniperListener(SwingThreadSniperListener(collector))
        auction.addAuctionEventListener(sniper)
        auction.join()
        collector.addSniper(sniper)
        snipers += auction
    }
}
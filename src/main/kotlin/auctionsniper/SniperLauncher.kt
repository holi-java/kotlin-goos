package auctionsniper

class SniperLauncher(private val auctionHouse: AuctionHouse, private val collector: SniperCollector) : UserRequestListener {
    override fun joinAuction(itemId: String) {
        val auction = auctionHouse.auctionFor(itemId)
        val sniper = AuctionSniper(itemId, auction)
        //violates Law of Demeter
        auction.addAuctionEventListener(sniper)
        collector.addSniper(sniper)
        //violates Law of Demeter
        auction.join()
    }
}
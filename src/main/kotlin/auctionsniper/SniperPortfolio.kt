package auctionsniper


class SniperPortfolio : SniperCollector {
    private val snipers = mutableListOf<AuctionSniper>()
    private val listeners = mutableListOf<SniperPortfolioListener>()
    override fun addSniper(sniper: AuctionSniper) {
        snipers += sniper
        listeners.forEach { it.sniperAdded(sniper) }
    }

    fun addPortfolioListener(listener: SniperPortfolioListener) {
        listeners += listener
    }
}
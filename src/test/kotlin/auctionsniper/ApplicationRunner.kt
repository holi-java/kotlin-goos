package auctionsniper

import auctionsniper.FakeAuctionServer.Companion.XMPP_HOSTNAME
import auctionsniper.SniperState.*
import auctionsniper.ui.SNIPER_APPLICATION_NAME
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit.SECONDS


class ApplicationRunner {
    private var driver: ApplicationDriver? = null

    companion object {
        const val SNIPER_ID = "sniper"
        const val SNIPER_PASSWORD = "sniper"
        const val SNIPER_XMPP_ID = "$SNIPER_ID@$XMPP_HOSTNAME/$AUCTION_RESOURCE"
    }

    fun startBiddingIn(vararg auctions: FakeAuctionServer) {
        startSniper()
        driver = ApplicationDriver(SECONDS.toMillis(1)).also { it.hasTitle(SNIPER_APPLICATION_NAME); it.hasColumnTitles() }

        for (auction in auctions) {
            driver!!.run { startBiddingFor(auction.itemId); showsSniperStatus(auction.itemId, 0, 0, JOINING) }
        }
    }


    private fun startSniper() {
        CompletableFuture.runAsync { Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD) }.join()
    }

    fun hasShownSniperIsBidding(auction: FakeAuctionServer, lastPrice: Int, lastBid: Int) = driver?.showsSniperStatus(auction.itemId, lastPrice, lastBid, BIDDING)

    fun hasShownSniperIsWinning(auction: FakeAuctionServer, winningBid: Int) = driver?.showsSniperStatus(auction.itemId, winningBid, winningBid, WINNING)

    fun showsSniperHasWonAuction(auction: FakeAuctionServer, lastPrice: Int) = driver?.showsSniperStatus(auction.itemId, lastPrice, lastPrice, WON)

    fun showsSniperHasLostAuction(auction: FakeAuctionServer, lastPrice: Int, lastBid: Int) = driver?.showsSniperStatus(auction.itemId, lastPrice, lastBid, LOST)

    fun stop() {
        driver?.dispose()
    }
}



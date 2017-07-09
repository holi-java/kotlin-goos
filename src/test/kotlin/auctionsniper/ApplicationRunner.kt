package auctionsniper

import auctionsniper.FakeAuctionServer.Companion.XMPP_HOSTNAME
import auctionsniper.ui.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit.SECONDS
import javax.swing.SwingUtilities.invokeAndWait

private const val SNIPER_ID = "sniper"
private const val SNIPER_PASSWORD = "sniper"

const val SNIPER_XMPP_ID = "$SNIPER_ID@$XMPP_HOSTNAME/$AUCTION_RESOURCE"

class ApplicationRunner {
    private var driver: ApplicationDriver? = null

    fun startBiddingIn(auction: FakeAuctionServer) {
        startSniper(auction)
        driver = ApplicationDriver(SECONDS.toMillis(1)).apply { showsSniperStatus(STATUS_JOINING) }
    }

    private fun startSniper(auction: FakeAuctionServer) {
        CompletableFuture.runAsync { Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.itemId) }.join()
        invokeAndWait { }
    }

    fun hasShownSniperIsBidding() = driver?.showsSniperStatus(STATUS_BIDDING)

    fun hasShownSniperIsWinning() = driver?.showsSniperStatus(STATUS_WINNING)

    fun showsSniperHasWonAuction() =driver?.showsSniperStatus(STATUS_WON)

    fun showsSniperHasLostAuction() = driver?.showsSniperStatus(STATUS_LOST)

    fun stop() {
        driver?.dispose()
    }
}



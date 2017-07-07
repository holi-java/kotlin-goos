package auctionsniper

import auctionsniper.FakeAuctionServer.Companion.XMPP_HOSTNAME
import auctionsniper.ui.STATUS_BIDDING
import auctionsniper.ui.STATUS_LOST
import auctionsniper.ui.STATUS_JOINING
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.concurrent.thread

private const val SNIPER_ID = "sniper"
private const val SNIPER_PASSWORD = "sniper"

const val SNIPER_XMPP_ID = "$SNIPER_ID@$XMPP_HOSTNAME/$AUCTION_RESOURCE"

class ApplicationRunner {
    private var driver: ApplicationDriver? = null

    fun startBiddingIn(auction: FakeAuctionServer) {
        startSniper(auction)
        driver = ApplicationDriver(SECONDS.toMillis(2)).apply { showsSniperStatus(STATUS_JOINING) }
    }

    private fun startSniper(auction: FakeAuctionServer) {
        CompletableFuture.supplyAsync {
            Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.itemId)
        }.join()
    }

    fun showsSniperHasLostAuction() = driver?.showsSniperStatus(STATUS_LOST)
    fun hasShownSniperIsBidding() = driver?.showsSniperStatus(STATUS_BIDDING)

    fun stop() {
        driver?.dispose()
    }
}



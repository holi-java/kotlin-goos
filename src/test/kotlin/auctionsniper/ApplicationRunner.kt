package auctionsniper

import auctionsniper.FakeAuctionServer.Companion.XMPP_HOSTNAME
import auctionsniper.ui.STATUS_BIDDING
import auctionsniper.ui.STATUS_JOINING
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.concurrent.thread

private const val SNIPER_ID = "sniper"
private const val SNIPER_PASSWORD = "sniper"

class ApplicationRunner {
    private var driver: ApplicationDriver? = null

    fun startBiddingIn(auction: FakeAuctionServer) {
        thread(isDaemon = true, name = "sniper thread") {
            Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.itemId)
        }

        driver = ApplicationDriver(SECONDS.toMillis(1)).apply { showsSniperStatus(STATUS_JOINING) }
    }

    fun showsSniperHasLostAuction() = driver?.showsSniperStatus(STATUS_BIDDING)
    fun stop(){
        driver?.dispose()
    }
}



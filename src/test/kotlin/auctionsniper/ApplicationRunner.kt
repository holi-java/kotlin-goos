package auctionsniper

import auctionsniper.FakeAuctionServer.Companion.XMPP_HOSTNAME
import auctionsniper.SniperState.*
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
        driver = ApplicationDriver(SECONDS.toMillis(1))

        driver!!.run {
            hasTitle(SNIPER_APPLICATION_NAME)
            hasColumnTitles()
            showsSniperStatus("", 0, 0, JOINING)
        }
    }

    private fun startSniper(auction: FakeAuctionServer) {
        CompletableFuture.runAsync { Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.itemId) }.join()
    }

    fun hasShownSniperIsBidding(auction: FakeAuctionServer, lastPrice: Int, lastBid: Int) = driver?.showsSniperStatus(auction.itemId, lastPrice, lastBid, BIDDING)

    fun hasShownSniperIsWinning(auction: FakeAuctionServer, winningBid: Int) = driver?.showsSniperStatus(auction.itemId, winningBid, winningBid, WINNING)

    fun showsSniperHasWonAuction(auction: FakeAuctionServer, lastPrice: Int) = driver?.showsSniperStatus(auction.itemId, lastPrice, lastPrice, WON)

    fun showsSniperHasLostAuction(auction: FakeAuctionServer, lastPrice: Int, lastBid: Int) = driver?.showsSniperStatus(auction.itemId, lastPrice, lastBid, LOST)

    fun stop() {
        driver?.dispose()
    }
}



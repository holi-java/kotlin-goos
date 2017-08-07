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

    private lateinit var itemId: String

    fun startBiddingIn(auction: FakeAuctionServer) {
        itemId = auction.itemId
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

    fun hasShownSniperIsBidding(lastPrice: Int, lastBid: Int) = driver?.showsSniperStatus(itemId, lastPrice, lastBid, BIDDING)

    fun hasShownSniperIsWinning(winningBid: Int) = driver?.showsSniperStatus(itemId, winningBid, winningBid, WINNING)

    fun showsSniperHasWonAuction(lastPrice: Int) = driver?.showsSniperStatus(itemId, lastPrice, lastPrice, WON)

    fun showsSniperHasLostAuction(lastPrice: Int, lastBid: Int) = driver?.showsSniperStatus(itemId, lastPrice, lastBid, LOST)

    fun stop() {
        driver?.dispose()
    }
}



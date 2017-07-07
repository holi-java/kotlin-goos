@file:Suppress("MemberVisibilityCanPrivate")

import auctionsniper.ApplicationRunner
import auctionsniper.FakeAuctionServer
import auctionsniper.SNIPER_XMPP_ID
import org.junit.After
import org.junit.Test

class AuctionSniperEndToEndTest {
    val auction = FakeAuctionServer("item-54321")
    val application = ApplicationRunner()


    @Test
    fun `sniper joins an auction until auction closes`() {
        auction.startSellingItem()

        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)

        auction.announceClosed()
        application.showsSniperHasLostAuction()
    }


    @Test
    fun `sniper makes a higher bid but loses auction`() {
        auction.startSellingItem()

        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)

        auction.reportPrice(1000, 98, "other bidder")
        application.hasShownSniperIsBidding()
        auction.hasReceivedBid(1098, SNIPER_XMPP_ID)

        auction.announceClosed()
        application.showsSniperHasLostAuction()
    }

    @After fun `stop auction`() = auction.close()

    @After fun `stop application`() = application.stop()
}


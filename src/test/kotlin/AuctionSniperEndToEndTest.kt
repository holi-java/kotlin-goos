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
        application.showsSniperHasLostAuction(auction, 0, 0)
    }

    @Test
    fun `sniper makes a higher bid but loses auction`() {
        auction.startSellingItem()

        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)

        auction.reportPrice(1000, 98, "other bidder")
        application.hasShownSniperIsBidding(auction, 1000, 1098)
        auction.hasReceivedBid(1098, SNIPER_XMPP_ID)

        auction.announceClosed()
        application.showsSniperHasLostAuction(auction, 1000, 1098)
    }

    @Test
    fun `sniper wins an auction by bidding higher`() {
        auction.startSellingItem()

        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)

        auction.reportPrice(1000, 98, "other bidder")
        application.hasShownSniperIsBidding(auction, 1000, 1098)
        auction.hasReceivedBid(1098, SNIPER_XMPP_ID)

        auction.reportPrice(1098, 100, SNIPER_XMPP_ID)
        application.hasShownSniperIsWinning(auction, 1098)

        auction.announceClosed()
        application.showsSniperHasWonAuction(auction, 1098)
    }

    @After fun `stop auction`() = auction.close()

    @After fun `stop application`() = application.stop()
}


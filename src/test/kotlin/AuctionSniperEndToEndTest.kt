@file:Suppress("MemberVisibilityCanPrivate")

import auctionsniper.ApplicationRunner
import auctionsniper.FakeAuctionServer
import auctionsniper.ApplicationRunner.Companion.SNIPER_XMPP_ID
import org.junit.After
import org.junit.Test

class AuctionSniperEndToEndTest {
    val auction = FakeAuctionServer("item-54321")
    val auction2 = FakeAuctionServer("item-65432")
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

    @Test
    fun `sniper bids multiple items`() {
        arrayOf(auction, auction2).forEach { it.startSellingItem() }

        application.startBiddingIn(auction, auction2)
        auction.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)
        auction2.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)

        auction.reportPrice(100, 23, "other bidder")
        auction.hasReceivedBid(123, SNIPER_XMPP_ID)

        auction2.reportPrice(200, 60, "other bidder")
        auction2.hasReceivedBid(260, SNIPER_XMPP_ID)

        auction.reportPrice(123, 100, SNIPER_XMPP_ID)
        auction2.reportPrice(260, 100, SNIPER_XMPP_ID)
        application.hasShownSniperIsWinning(auction, 123)
        application.hasShownSniperIsWinning(auction2, 260)

        arrayOf(auction, auction2).forEach { it.announceClosed() }

        application.showsSniperHasWonAuction(auction, 123)
        application.showsSniperHasWonAuction(auction2, 260)
    }

    @After fun `stop auctions`() = arrayOf(auction, auction2).forEach { it.close() }

    @After fun `stop application`() = application.stop()
}


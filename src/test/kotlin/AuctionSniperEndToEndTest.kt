@file:Suppress("MemberVisibilityCanPrivate")

import auctionsniper.ApplicationRunner
import auctionsniper.FakeAuctionServer
import org.junit.After
import org.junit.Test

class AuctionSniperEndToEndTest {
    val auction = FakeAuctionServer("item-54321")
    val application = ApplicationRunner()


    @Test
    fun `sniper joins an auction until auction closes`() {
        auction.startSellingItem()

        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFromSniper()

        auction.announceClosed()
        application.showsSniperHasLostAuction()
    }


    @After fun `stop auction`() = auction.close()

    @After fun `stop application`() = application.stop()
}


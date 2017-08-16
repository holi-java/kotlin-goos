package integ.xmpp

import auctionsniper.ApplicationRunner.Companion.SNIPER_ID
import auctionsniper.ApplicationRunner.Companion.SNIPER_PASSWORD
import auctionsniper.ApplicationRunner.Companion.SNIPER_XMPP_ID
import auctionsniper.AuctionEventListener
import auctionsniper.AuctionHouse
import auctionsniper.FakeAuctionServer
import auctionsniper.FakeAuctionServer.Companion.XMPP_HOSTNAME
import auctionsniper.PriceSource
import auctionsniper.xmpp.XMPPAuctionHouse
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit.SECONDS

class XMPPAuctionTest {
    private val server = FakeAuctionServer("item-54321")
    private val auctionHouse: AuctionHouse by lazy { XMPPAuctionHouse.connect(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD) }

    @Before
    fun `start server`() = server.startSellingItem()

    @Test
    fun `retrieves events from auction server after joining`() {
        val closed = CountDownLatch(1)
        val auction = auctionHouse.auctionFor(server.itemId).apply { addAuctionEventListener(counting(closed)) }

        auction.join()
        server.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)

        server.announceClosed()

        assert(closed.await(1, SECONDS))
    }

    private fun counting(closed: CountDownLatch): AuctionEventListener {
        return object : AuctionEventListener {
            override fun auctionClosed() = closed.countDown()
            override fun currentPrice(currentPrice: Int, increment: Int, source: PriceSource) = Unit
        }
    }

    @After
    fun `stop server`() = server.close()

    @After
    fun `stop connection`() = auctionHouse.disconnect()

}


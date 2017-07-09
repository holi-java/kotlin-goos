@file:Suppress("MemberVisibilityCanPrivate")

package test.auctionsniper

import auctionsniper.Auction
import auctionsniper.AuctionSniper
import auctionsniper.PriceSource.*
import auctionsniper.SniperListener
import checking
import org.jmock.States
import org.jmock.auto.Auto
import org.jmock.auto.Mock
import org.jmock.integration.junit4.JUnitRuleMockery
import org.junit.Rule
import org.junit.Test

class AuctionSniperTest {
    @get:Rule val context = JUnitRuleMockery()
    @Mock lateinit var listener: SniperListener
    @Mock lateinit var auction: Auction
    @Auto lateinit var sniperState: States

    val sniper by lazy { AuctionSniper(auction, listener) }

    @Test
    fun `reports lost when auction closes immediately`() {
        context.checking { -> oneOf(listener).sniperLost() }

        sniper.auctionClosed()
    }

    @Test
    fun `bids higher and reports bidding when new price comes from other bidder`() {
        val price = 123
        val increment = 456

        context.checking { ->
            oneOf(auction).bid(price + increment)
            atLeast(1).of(listener).sniperBidding()
        }

        sniper.currentPrice(price, increment, FromOtherBidder)
    }

    @Test
    fun `reports sniper winning when new price comes from sniper`() {
        context.checking { ->
            atLeast(1).of(listener).sniperWinning()
        }

        sniper.currentPrice(123, 456, FromSniper)
    }

    @Test
    fun `reports lost if auction is closed when bidding`() {
        context.checking { ->
            ignoring(auction)
            allowing(listener).sniperBidding();then(sniperState.`is`("bidding"))
            oneOf(listener).sniperLost();`when`(sniperState.`is`("bidding"))
        }

        sniper.currentPrice(123, 45, FromOtherBidder)
        sniper.auctionClosed()
    }

    @Test
    fun `reports won if auction is closed when winning`() {
        allowingSniperWinning()
        context.checking { -> oneOf(listener).sniperWon();`when`(sniperState.`is`("winning")) }

        sniper.currentPrice(123, 45, FromSniper)
        sniper.auctionClosed()
    }

    @Test
    fun `bids higher and reports bidding if new price comes from other bidder when winning`() {
        val price = 123
        val increment = 456

        allowingSniperWinning()

        context.checking { ->
            oneOf(auction).bid(price + increment)
            atLeast(1).of(listener).sniperBidding(); `when`(sniperState.`is`("winning"))
        }

        sniper.currentPrice(100, 10, FromSniper)
        sniper.currentPrice(price, increment, FromOtherBidder)
    }


    @Test
    fun `reports lost if auction is closed when sniper becoming winning to bidding`() {
        allowingSniperWinning()

        context.checking { ->
            ignoring(auction)
            atLeast(1).of(listener).sniperBidding(); `when`(sniperState.`is`("winning"));then(sniperState.`is`("bidding"))

            oneOf(listener).sniperLost(); `when`(sniperState.`is`("bidding"))
        }

        sniper.currentPrice(100, 10, FromSniper)
        sniper.currentPrice(123, 456, FromOtherBidder)

        sniper.auctionClosed()
    }

    private fun allowingSniperWinning() {
        context.checking { -> allowing(listener).sniperWinning();then(sniperState.`is`("winning")) }
    }
}

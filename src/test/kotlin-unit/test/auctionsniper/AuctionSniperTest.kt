@file:Suppress("MemberVisibilityCanPrivate")

package test.auctionsniper

import auctionsniper.*
import auctionsniper.PriceSource.FromOtherBidder
import auctionsniper.PriceSource.FromSniper
import auctionsniper.SniperState.*
import checking
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.hasProperty
import org.jmock.States
import org.jmock.auto.Auto
import org.jmock.auto.Mock
import org.jmock.integration.junit4.JUnitRuleMockery
import org.junit.Rule
import org.junit.Test

private const val ITEM_ID = "ITEM"


class AuctionSniperTest {
    @get:Rule val context = JUnitRuleMockery()
    @Mock lateinit var listener: SniperListener
    @Mock lateinit var auction: Auction
    @Auto lateinit var sniperState: States


    val sniper by lazy { AuctionSniper(ITEM_ID, auction).apply{ addSniperListener(listener)  } }


    @Test
    fun `reports lost when auction closes immediately`() {
        context.checking { -> oneOf(listener).sniperStateChanged(SniperSnapshot(ITEM_ID, 0, 0, LOST)) }

        sniper.auctionClosed()
    }

    @Test
    fun `bids higher and reports bidding when new price comes from other bidder`() {
        val price = 123
        val increment = 456

        context.checking { ->
            oneOf(auction).bid(price + increment)
            atLeast(1).of(listener).sniperStateChanged(SniperSnapshot(ITEM_ID, price, price + increment, BIDDING))
        }

        sniper.currentPrice(price, increment, FromOtherBidder)
    }

    @Test
    fun `reports sniper winning when new price comes from sniper`() {
        context.checking { ->
            atLeast(1).of(listener).sniperStateChanged(SniperSnapshot(ITEM_ID, 123, 123, WINNING))
        }

        sniper.currentPrice(123, 456, FromSniper)
    }

    @Test
    fun `reports lost if auction is closed when bidding`() {
        context.checking { ->
            ignoring(auction)
            allowing(listener).sniperStateChanged(with(aSniperThatIs(BIDDING)));then(sniperState.`is`("bidding"))
            oneOf(listener).sniperStateChanged(SniperSnapshot(ITEM_ID, 123, 168, LOST));`when`(sniperState.`is`("bidding"))
        }

        sniper.currentPrice(123, 45, FromOtherBidder)
        sniper.auctionClosed()
    }


    @Test
    fun `reports won if auction is closed when winning`() {
        allowingSniperWinning()
        context.checking { -> oneOf(listener).sniperStateChanged(SniperSnapshot(ITEM_ID, 123, 123, WON));`when`(sniperState.`is`("winning")) }

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
            atLeast(1).of(listener).sniperStateChanged(with(aSniperThatIs(BIDDING))); `when`(sniperState.`is`("winning"))
        }

        sniper.currentPrice(100, 10, FromSniper)
        sniper.currentPrice(price, increment, FromOtherBidder)
    }


    @Test
    fun `reports lost if auction is closed when sniper becoming winning to bidding`() {
        allowingSniperWinning()

        context.checking { ->
            ignoring(auction)
            atLeast(1).of(listener).sniperStateChanged(with(aSniperThatIs(BIDDING))); `when`(sniperState.`is`("winning"));then(sniperState.`is`("bidding"))

            oneOf(listener).sniperStateChanged(SniperSnapshot(ITEM_ID, 123, 579, LOST)); `when`(sniperState.`is`("bidding"))
        }

        sniper.currentPrice(100, 10, FromSniper)
        sniper.currentPrice(123, 456, FromOtherBidder)

        sniper.auctionClosed()
    }

    private fun allowingSniperWinning() {
        context.checking { ->
            allowing(listener).sniperStateChanged(with(aSniperThatIs(WINNING)));then(sniperState.`is`("winning"))
        }
    }


}

private fun aSniperThatIs(state: SniperState): Matcher<SniperSnapshot>? = hasProperty<SniperSnapshot>("state", Matchers.equalTo(state))
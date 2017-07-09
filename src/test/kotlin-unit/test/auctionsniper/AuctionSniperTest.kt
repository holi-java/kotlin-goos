@file:Suppress("MemberVisibilityCanPrivate")

package test.auctionsniper

import auctionsniper.Auction
import auctionsniper.AuctionSniper
import auctionsniper.SniperListener
import checking
import org.jmock.auto.Mock
import org.jmock.integration.junit4.JUnitRuleMockery
import org.junit.Rule
import org.junit.Test

class AuctionSniperTest {
    @get:Rule val context = JUnitRuleMockery()
    @Mock lateinit var listener: SniperListener
    @Mock lateinit var auction: Auction

    val sniper by lazy { AuctionSniper(auction, listener) }

    @Test
    fun `reports auction lost when auction closes`() {
        context.checking { -> oneOf(listener).sniperLost() }

        sniper.auctionClosed()
    }

    @Test
    fun `bids higher and reports bidding when new price arrives`() {
        val price = 123
        val increment = 456

        context.checking { ->
            oneOf(auction).bid(price + increment)
            atLeast(1).of(listener).sniperBidding()
        }

        sniper.currentPrice(price, increment)
    }
}

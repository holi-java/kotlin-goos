@file:Suppress("MemberVisibilityCanPrivate")

package test.auctionsniper

import auctionsniper.AuctionSniper
import auctionsniper.SniperPortfolio
import auctionsniper.SniperPortfolioListener
import checking
import org.jmock.auto.Mock
import org.jmock.integration.junit4.JUnitRuleMockery
import org.junit.Rule
import org.junit.Test
import test.ignored

class SniperPortfolioTest {
    @get:Rule
    val context = JUnitRuleMockery()
    val listener: SniperPortfolioListener = context.mock(SniperPortfolioListener::class.java)

    val portfolio = SniperPortfolio().apply { addPortfolioListener(listener) }

    @Test
    fun `notify all listeners after sniper added`() {
        val sniper = AuctionSniper("item", ignored())

        context.checking { ->
            oneOf(listener).sniperAdded(sniper)
        }

        portfolio.addSniper(sniper)
    }
}
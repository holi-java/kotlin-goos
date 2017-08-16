@file:Suppress("MemberVisibilityCanPrivate")

package test.auctionsniper

import auctionsniper.*
import checking
import org.hamcrest.Matchers.*
import org.jmock.AbstractExpectations.returnValue
import org.jmock.States
import org.jmock.auto.Auto
import org.jmock.auto.Mock
import org.jmock.integration.junit4.JUnitRuleMockery
import org.junit.Rule
import org.junit.Test

class SniperLauncherTest {
    @get:Rule
    val context = JUnitRuleMockery()

    @Mock lateinit var auction: Auction
    @Mock lateinit var auctionHouse: AuctionHouse
    @Mock lateinit var collector: SniperCollector
    @Auto lateinit var phase: States


    @Test
    fun `adds new sniper to collector and then join auction`() {
        val itemId = "item"
        val launcher = SniperLauncher(auctionHouse, collector)

        context.checking { ->
            allowing(auctionHouse).auctionFor(itemId);will(returnValue(auction))

            oneOf(auction).addAuctionEventListener(with(any(AuctionEventListener::class.java))); `when`(phase.isNot("collected"))
            oneOf(collector).addSniper(with(aSniperFor(itemId))); then(phase.`is`("collected"))

            oneOf(auction).join();`when`(phase.`is`("collected"))
        }

        launcher.joinAuction(itemId)
    }

    private fun aSniperFor(itemId: String) = hasProperty<AuctionSniper>("snapshot", hasProperty<Any>("itemId", equalTo(itemId)))
}
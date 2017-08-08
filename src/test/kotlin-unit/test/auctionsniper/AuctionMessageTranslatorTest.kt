@file:Suppress("MemberVisibilityCanPrivate")

package test.auctionsniper

import auctionsniper.AuctionEventListener
import auctionsniper.AuctionMessageTranslator
import auctionsniper.PriceSource.FromOtherBidder
import auctionsniper.PriceSource.FromSniper
import checking
import org.jivesoftware.smack.packet.Message
import org.jmock.auto.Mock
import org.jmock.integration.junit4.JUnitRuleMockery
import org.junit.Rule
import org.junit.Test

private val UNUSED_CHAT = null

class AuctionMessageTranslatorTest {
    val sniper = "sniper"
    @get:Rule val context = JUnitRuleMockery()
    @field:Mock lateinit var listener: AuctionEventListener

    val translator by lazy { AuctionMessageTranslator(sniper, listener) }

    @Test
    fun `notifies auction closed when a close message received`() {
        context.checking { -> oneOf(listener).auctionClosed() }

        translator.translate("SOLVersion: 1.1; Event: CLOSE;")
    }

    @Test
    fun `notifies bid details when current price message comes from other bidder`() {
        val currentPrice = 192
        val increment = 7

        context.checking { -> oneOf(listener).currentPrice(currentPrice, increment, FromOtherBidder) }

        translator.translate("SOLVersion: 1.1; Event: PRICE; CurrentPrice: $currentPrice; Increment: $increment; Bidder: Someone else;")
    }


    @Test
    fun `notifies bid details when current price message comes from sniper`() {
        val currentPrice = 123
        val increment = 456

        context.checking { -> oneOf(listener).currentPrice(currentPrice, increment, FromSniper) }

        translator.translate("SOLVersion: 1.1; Event: PRICE; CurrentPrice: $currentPrice; Increment: $increment; Bidder: $sniper;")
    }

    private fun AuctionMessageTranslator.translate(body: String) {
        processMessage(UNUSED_CHAT, Message().also { it.body = body })
    }
}



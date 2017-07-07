@file:Suppress("MemberVisibilityCanPrivate")

package test.auctionsniper

import auctionsniper.AuctionEventListener
import auctionsniper.AuctionMessageTranslator
import checking
import org.jivesoftware.smack.packet.Message
import org.jmock.auto.Mock
import org.jmock.integration.junit4.JUnitRuleMockery
import org.junit.Rule
import org.junit.Test

private val UNUSED_CHAT = null

class AuctionMessageTranslatorTest {
    @get:Rule val context = JUnitRuleMockery()
    @field:Mock lateinit var listener: AuctionEventListener

    val translator by lazy { AuctionMessageTranslator(listener) }

    @Test
    fun `notifies auction closed when a close message received`() {
        context.checking { -> oneOf(listener).auctionClosed() }

        translator.translate("SOLVersion: 1.1; Event: CLOSE;")
    }

    @Test
    fun `notifies bid details when current price message received`() {
        val currentPrice = 192
        val increment = 7

        context.checking { -> oneOf(listener).currentPrice(currentPrice, increment) }

        translator.translate("SOLVersion: 1.1; Event: PRICE; CurrentPrice: $currentPrice; Increment: $increment; Bidder: Someone else;")
    }

    private fun AuctionMessageTranslator.translate(body: String) {
        processMessage(UNUSED_CHAT, Message().also { it.body = body })
    }
}



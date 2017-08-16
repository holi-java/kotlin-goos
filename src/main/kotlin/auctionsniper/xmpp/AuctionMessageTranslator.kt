package auctionsniper.xmpp

import auctionsniper.AuctionEventListener
import auctionsniper.PriceSource.FromOtherBidder
import auctionsniper.PriceSource.FromSniper
import auctionsniper.utils.capitalize
import auctionsniper.utils.required
import auctionsniper.utils.split
import auctionsniper.xmpp.AuctionEvent.Companion.CLOSE_EVENT
import auctionsniper.xmpp.AuctionEvent.Companion.PRICE_EVENT
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.packet.Message

class AuctionMessageTranslator(private val sniper: String, private val listener: AuctionEventListener) : MessageListener {

    override fun processMessage(chat: Chat?, message: Message) {
        AuctionEvent.from(message.body).run {
            when (type) {
                CLOSE_EVENT -> listener.auctionClosed()
                PRICE_EVENT -> listener.currentPrice(currentPrice, increment, this from sniper)
                else -> TODO()
            }
        }
    }

}


private class AuctionEvent(context: Map<String, String>) {
    private val capitalizedToInt = context.capitalize { it!!.toInt() }

    val type by context.required("Event")

    val currentPrice by capitalizedToInt

    val increment by capitalizedToInt

    val bidder by context.required("Bidder")

    infix fun from(sniper: String) = when (sniper) {
        bidder -> FromSniper
        else -> FromOtherBidder
    }

    companion object {
        const val CLOSE_EVENT = "CLOSE"
        const val PRICE_EVENT = "PRICE"

        fun from(body: String): AuctionEvent {
            return AuctionEvent(body.fields().associateBy({ it.first() }) { it.last() })
        }

        private fun String.fields() = split(';').map { it.split(':').asSequence().map { it.trim() } }
    }
}
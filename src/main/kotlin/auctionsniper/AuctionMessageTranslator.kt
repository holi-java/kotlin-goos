package auctionsniper

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.packet.Message
import auctionsniper.utils.split

class AuctionMessageTranslator(private val listener: AuctionEventListener) : MessageListener {

    override fun processMessage(chat: Chat?, message: Message) {
        val event = message.body.split(';')
                .map { it.split(':').map { it.trim() }.toList() }
                .associateBy({ it[0] }) { it[1] }

        when (event["Event"]) {
            "CLOSE" -> listener.auctionClosed()
            "PRICE" -> listener.currentPrice(event["CurrentPrice"]!!.toInt(), event["Increment"]!!.toInt())
            else -> TODO()
        }
    }

}
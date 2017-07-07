package auctionsniper

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit.SECONDS


private const val AUCTION_PASSWORD = "auction"

class FakeAuctionServer(val itemId: String) {
    companion object {
        const val XMPP_HOSTNAME = "localhost"
    }

    private val connection = XMPPConnection(XMPP_HOSTNAME)

    private lateinit var currentChat: Chat

    private val listener = SingleMessageListener()

    fun startSellingItem() {
        connection.connect()
        connection.login(ITEM_ID_AS_LOGIN.format(itemId), AUCTION_PASSWORD, AUCTION_RESOURCE)
        connection.chatManager.addChatListener { chat, _ ->
            currentChat = chat;
            currentChat.addMessageListener(listener)
        }
    }

    fun hasReceivedJoinRequestFromSniper(sniper: String) = assertIncomingMessageFromSniper(sniper, equalTo(JOIN_COMMAND))


    fun hasReceivedBid(bid: Int, sniper: String) {
        assertIncomingMessageFromSniper(sniper, equalTo(bidCommand(bid)))
    }


    private fun assertIncomingMessageFromSniper(sniper: String, bodyMatcher: Matcher<String?>) {
        listener.receivesAMessage(bodyMatcher)
        assert.that(currentChat.participant, equalTo(sniper))
    }

    fun reportPrice(currentPrice: Int, increment: Int, bidder: String) = publish { aPriceEvent(currentPrice, increment, bidder) }

    fun announceClosed() = publish { aCloseEvent() }

    private fun publish(event: () -> String) = currentChat.sendMessage(Message().apply { body = aMessage(event) })


    private fun aCloseEvent() = "Event: CLOSE;"

    private fun aPriceEvent(currentPrice: Int, increment: Int, bidder: String): String {
        return """
                  Event: PRICE;
                  CurrentPrice: $currentPrice;
                  Increment: $increment;
                  Bidder: $bidder;
               """.trimIndent().trim()
    }

    fun close() = connection.disconnect()
}

private class SingleMessageListener : MessageListener {
    private val incomingMessages = ArrayBlockingQueue<Message>(1)

    override fun processMessage(chat: Chat?, message: Message?) {
        incomingMessages.add(message)
    }

    fun receivesAMessage(bodyMatcher: Matcher<String?>) {
        assert.that(incomingMessages.poll(1, SECONDS), has("body", { it?.body }, bodyMatcher)) { "message" }
    }

}


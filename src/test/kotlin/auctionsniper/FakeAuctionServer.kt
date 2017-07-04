package auctionsniper

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.present
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

    fun hasReceivedJoinRequestFromSniper() = listener.receivesAMessage()
    fun announceClosed() = currentChat.sendMessage("")
    fun close() = connection.disconnect()
}

private class SingleMessageListener : MessageListener {
    private val incomingMessages = ArrayBlockingQueue<Message>(1)

    override fun processMessage(chat: Chat?, message: Message?) {
        incomingMessages.add(message)
    }

    fun receivesAMessage() = assert.that(incomingMessages.poll(1, SECONDS), present()) { "message" }

}

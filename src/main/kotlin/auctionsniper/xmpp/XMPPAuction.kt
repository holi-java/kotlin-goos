package auctionsniper.xmpp

import auctionsniper.*
import org.jivesoftware.smack.XMPPConnection

val JOIN_COMMAND = aMessage { "Command:JOIN;" }
fun bidCommand(bid: Int) = aMessage { "Command: BID; Amount: $bid" }
inline fun aMessage(type: () -> String) = "SOLVersion: 1.1; ${type()}"

class XMPPAuction(connection: XMPPConnection, itemId: String) : Auction {
    private val chat = connection.chatManager.createChat(connection toAuctionId itemId, null)

    private val listeners: MutableList<AuctionEventListener> = mutableListOf()

    init {
        chat.addMessageListener(translatorFor(connection))
    }

    private fun translatorFor(connection: XMPPConnection) = AuctionMessageTranslator(connection.user, delegating(listeners))


    private fun delegating(listeners: List<AuctionEventListener>) = object : AuctionEventListener {
        override fun currentPrice(currentPrice: Int, increment: Int, source: PriceSource) {
            listeners.forEach { it.currentPrice(currentPrice, increment, source) }
        }

        override fun auctionClosed() {
            listeners.forEach { it.auctionClosed() }
        }
    }

    override fun join() = chat.sendMessage(JOIN_COMMAND)

    override fun bid(amount: Int) = chat.sendMessage(bidCommand(amount))

    override fun addAuctionEventListener(listener: AuctionEventListener) = run { listeners += listener }
}

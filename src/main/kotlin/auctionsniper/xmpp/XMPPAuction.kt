package auctionsniper.xmpp

import auctionsniper.*
import org.jivesoftware.smack.XMPPConnection

val JOIN_COMMAND = aMessage { "Command:JOIN;" }
fun bidCommand(bid: Int) = aMessage { "Command: BID; Amount: $bid" }
inline fun aMessage(event: () -> String) = "SOLVersion: 1.1; ${event()}"

private const val JID_FORMAT = "$ITEM_ID_AS_LOGIN@%s/$AUCTION_RESOURCE"

@Suppress("NOTHING_TO_INLINE")
private inline infix fun XMPPConnection.toAuctionId(itemId: String) = JID_FORMAT.format(itemId, serviceName)

class XMPPAuction(connection: XMPPConnection, itemId: String) : Auction {
    private val listeners: MutableList<AuctionEventListener> = mutableListOf()

    private val chat = connection.chatManager.createChat(connection toAuctionId itemId, null).also {
        it.addMessageListener(translatorFor(connection))
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

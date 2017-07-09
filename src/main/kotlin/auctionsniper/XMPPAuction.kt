package auctionsniper

import org.jivesoftware.smack.Chat

val JOIN_COMMAND = aMessage { "Command:JOIN;" }
fun bidCommand(bid: Int) = aMessage { "Command: BID; Amount: $bid" }
inline fun aMessage(type: () -> String) = "SOLVersion: 1.1; ${type()}"

class XMPPAuction(private val chat: Chat) : Auction {
    override fun join() = chat.sendMessage(JOIN_COMMAND)
    override fun bid(amount: Int) = chat.sendMessage(bidCommand(amount))

}

package auctionsniper.xmpp

import auctionsniper.AuctionHouse
import org.jivesoftware.smack.XMPPConnection
internal const val AUCTION_RESOURCE = "Auction"
internal const val ITEM_ID_AS_LOGIN = "auction-%s"

class XMPPAuctionHouse private constructor(val connection: XMPPConnection) : AuctionHouse {
    companion object {
        fun connect(hostname: String, sniper: String, password: String) = XMPPAuctionHouse(XMPPConnection(hostname).apply {
            connect();login(sniper, password, AUCTION_RESOURCE)
        })
    }

    override fun disconnect() = connection.disconnect()

    override fun auctionFor(itemId: String) = XMPPAuction(connection, itemId)
}




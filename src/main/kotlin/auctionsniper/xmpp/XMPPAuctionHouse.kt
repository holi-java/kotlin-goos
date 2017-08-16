package auctionsniper.xmpp

import auctionsniper.AUCTION_RESOURCE
import auctionsniper.AuctionHouse
import org.jivesoftware.smack.XMPPConnection

class XMPPAuctionHouse private constructor(val connection: XMPPConnection) : AuctionHouse {
    companion object {
        fun connect(hostname: String, sniper: String, password: String) = XMPPAuctionHouse(XMPPConnection(hostname).apply {
            connect();login(sniper, password, AUCTION_RESOURCE)
        })
    }

    override fun disconnect() = connection.disconnect()

    override fun auctionFor(itemId: String) = XMPPAuction(connection, itemId)
}
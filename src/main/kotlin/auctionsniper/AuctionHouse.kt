package auctionsniper

import auctionsniper.xmpp.XMPPAuction

interface AuctionHouse {
    fun disconnect()
    fun auctionFor(itemId: String): XMPPAuction

}
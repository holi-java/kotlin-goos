package auctionsniper

import auctionsniper.ui.*
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.XMPPConnection
import javax.swing.SwingUtilities

internal const val AUCTION_RESOURCE = "Auction"
internal const val ITEM_ID_AS_LOGIN = "auction-%s"
internal const val JID_FORMAT = "$ITEM_ID_AS_LOGIN@%s/$AUCTION_RESOURCE"

class Main {

    init {
        startUserInterface()
    }

    private lateinit var ui: MainWindow;

    private var notBeGcd: Chat? = null

    private fun startUserInterface() {
        SwingUtilities.invokeAndWait { ui = MainWindow() }
    }

    private fun joinAuction(itemId: String, connection: XMPPConnection) {
        ui.whenClosed(connection::disconnect)

        val chat = connection.chatManager.createChat(connection toAuctionId itemId, null)
        val auction = XMPPAuction(chat)
        chat.addMessageListener(AuctionMessageTranslator(connection.user, AuctionSniper(auction, SwingThreadSniperListener(SniperStateDisplayer()))))
        auction.join()
        notBeGcd = chat
    }

    inner class SniperStateDisplayer : SniperListener {

        override fun sniperBidding() = show(STATUS_BIDDING)

        override fun sniperWinning() = show(STATUS_WINNING)

        override fun sniperWon() = show(STATUS_WON)

        override fun sniperLost() = show(STATUS_LOST)

        private fun show(status: String) = let { ui.status = status }
    }


    companion object {
        private lateinit var notBeGCd: Main;
        fun main(vararg args: String): Unit {
            val main = Main()
            val (hostname, sniper, password, itemId) = args
            main.joinAuction(itemId, connection(hostname, sniper, password))

            notBeGCd = main
        }

    }
}

private fun connection(hostname: String, sniper: String, password: String): XMPPConnection {
    return XMPPConnection(hostname).apply {
        connect()
        login(sniper, password, AUCTION_RESOURCE)
    }
}

@Suppress("NOTHING_TO_INLINE")
private inline infix fun XMPPConnection.toAuctionId(itemId: String) = JID_FORMAT.format(itemId, serviceName)


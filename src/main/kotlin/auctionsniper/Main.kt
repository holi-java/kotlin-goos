package auctionsniper

import auctionsniper.ui.MainWindow
import auctionsniper.ui.SnipersTableModel
import auctionsniper.ui.SwingThreadSniperListener
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.XMPPConnection
import javax.swing.SwingUtilities

internal const val AUCTION_RESOURCE = "Auction"
internal const val ITEM_ID_AS_LOGIN = "auction-%s"
internal const val JID_FORMAT = "$ITEM_ID_AS_LOGIN@%s/$AUCTION_RESOURCE"

class Main {

    private lateinit var ui: MainWindow;

    private val snipers = SnipersTableModel()

    private val notBeGcd: MutableList<Chat> = arrayListOf<Chat>()

    init {
        startUserInterface()
    }

    private fun startUserInterface() {
        SwingUtilities.invokeAndWait { ui = MainWindow(snipers) }
    }

    private fun addUserRequestListenerFor(connection: XMPPConnection) {
        ui.addUserRequestListener { itemId ->
            snipers.addSniper(SniperSnapshot.joining(itemId))
            val chat = connection.chatManager.createChat(connection toAuctionId itemId, null)
            val auction = XMPPAuction(chat)
            chat.addMessageListener(AuctionMessageTranslator(connection.user, AuctionSniper(itemId, auction, SwingThreadSniperListener(snipers))))
            auction.join()
            notBeGcd += chat
        }
    }

    private fun whenClosed(action: () -> Unit) = ui.whenClosed(action)

    companion object {
        private lateinit var notBeGCd: Main;
        fun main(vararg args: String): Unit {
            val main = Main()
            val (hostname, sniper, password) = args
            val connection = connect(hostname, sniper, password)
            main.whenClosed(connection::disconnect)
            main.addUserRequestListenerFor(connection)
            notBeGCd = main
        }

    }
}

private fun connect(hostname: String, sniper: String, password: String): XMPPConnection {
    return XMPPConnection(hostname).apply {
        connect()
        login(sniper, password, AUCTION_RESOURCE)
    }
}

@Suppress("NOTHING_TO_INLINE")
private inline infix fun XMPPConnection.toAuctionId(itemId: String) = JID_FORMAT.format(itemId, serviceName)


package auctionsniper

import auctionsniper.ui.MainWindow
import auctionsniper.ui.STATUS_BIDDING
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
        val chat = connection.chatManager.createChat(itemId toAuctionId connection) { _, _ ->
            SwingUtilities.invokeLater {
                ui.status = STATUS_BIDDING
            }
        }
        notBeGcd = chat;
        chat.sendMessage("")
    }

    companion object {


        fun main(vararg args: String): Unit {
            val main = Main()

            val (hostname, sniper, password, itemId) = args

            main.joinAuction(itemId, connection(hostname, sniper, password))
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
private inline infix fun String.toAuctionId(connection: XMPPConnection) = JID_FORMAT.format(this, connection.serviceName)
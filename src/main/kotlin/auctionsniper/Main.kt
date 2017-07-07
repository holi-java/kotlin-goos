package auctionsniper

import auctionsniper.ui.MainWindow
import auctionsniper.ui.STATUS_BIDDING
import auctionsniper.ui.STATUS_LOST
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import javax.swing.SwingUtilities

internal const val AUCTION_RESOURCE = "Auction"
internal const val ITEM_ID_AS_LOGIN = "auction-%s"
internal const val JID_FORMAT = "$ITEM_ID_AS_LOGIN@%s/$AUCTION_RESOURCE"

val JOIN_COMMAND = aMessage { "Command:JOIN;" }
fun bidCommand(bid: Int) = aMessage { "Command: BID; Amount: $bid" }
inline fun aMessage(type: () -> String) = "SOLVersion: 1.1; ${type()}"

class Main : AuctionEventListener {
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

        val chat = connection.chatManager.createChat(itemId toAuctionId connection, AuctionMessageTranslator(this))
        notBeGcd = chat;
        chat.sendMessage(JOIN_COMMAND)
    }

    override fun currentPrice(currentPrice: Int, increment: Int) {
        SwingUtilities.invokeLater {
            ui.status = STATUS_BIDDING
        }
    }

    override fun auctionClosed() {
        SwingUtilities.invokeLater {
            ui.status = STATUS_LOST
        }
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

package auctionsniper

import auctionsniper.ui.MainWindow
import auctionsniper.ui.SnipersTableModel
import auctionsniper.ui.SwingThreadSniperListener
import auctionsniper.xmpp.XMPPAuctionHouse
import org.jivesoftware.smack.XMPPConnection
import javax.swing.SwingUtilities

internal const val AUCTION_RESOURCE = "Auction"
internal const val ITEM_ID_AS_LOGIN = "auction-%s"
@PublishedApi internal const val JID_FORMAT = "$ITEM_ID_AS_LOGIN@%s/$AUCTION_RESOURCE"

class Main {

    private lateinit var ui: MainWindow;

    private val snipers = SnipersTableModel()

    private val notBeGcd: MutableList<Auction> = mutableListOf()

    init {
        startUserInterface()
    }

    private fun startUserInterface() {
        SwingUtilities.invokeAndWait { ui = MainWindow(snipers) }
    }

    private fun addUserRequestListenerFor(auctionHouse: XMPPAuctionHouse) {
        ui.addUserRequestListener { itemId ->
            snipers.addSniper(SniperSnapshot.joining(itemId))

            val auction = auctionHouse.auctionFor(itemId)
            auction.addAuctionEventListener(AuctionSniper(itemId, auction, SwingThreadSniperListener(snipers)))
            auction.join()

            notBeGcd += auction
        }
    }


    private fun whenClosed(action: () -> Unit) = ui.whenClosed(action)

    companion object {
        private lateinit var notBeGCd: Main;
        fun main(vararg args: String): Unit {
            val main = Main()
            val (hostname, sniper, password) = args
            val auctionHouse = XMPPAuctionHouse.connect(hostname, sniper, password)
            main.whenClosed(auctionHouse::disconnect)
            main.addUserRequestListenerFor(auctionHouse)
            notBeGCd = main
        }

    }
}


@Suppress("NOTHING_TO_INLINE")
inline infix fun XMPPConnection.toAuctionId(itemId: String) = JID_FORMAT.format(itemId, serviceName)


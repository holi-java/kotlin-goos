package auctionsniper

import auctionsniper.ui.MainWindow
import auctionsniper.ui.SnipersTableModel
import auctionsniper.xmpp.XMPPAuctionHouse
import javax.swing.SwingUtilities

class Main {

    private lateinit var ui: MainWindow;

    private val portfolio = SniperPortfolio()
    
    private val snipers = SnipersTableModel().also { portfolio.addPortfolioListener(it) }

    init {
        startUserInterface()
    }

    private fun startUserInterface() {
        SwingUtilities.invokeAndWait { ui = MainWindow(snipers) }
    }

    private fun addUserRequestListenerFor(auctionHouse: XMPPAuctionHouse) {
        ui.addUserRequestListener(SniperLauncher(auctionHouse, portfolio))
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



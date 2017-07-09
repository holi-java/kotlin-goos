package auctionsniper.ui

import auctionsniper.Main
import auctionsniper.SniperListener
import javax.swing.SwingUtilities

class SwingThreadSniperListener(private val target: Main.SniperStateDisplayer) : SniperListener {
    override fun sniperBidding() = target::sniperBidding.runInUiThread()

    override fun sniperWinning() = target::sniperWinning.runInUiThread()

    override fun sniperWon() = target::sniperWon.runInUiThread()

    override fun sniperLost() = target::sniperLost.runInUiThread()

    private fun (() -> Unit).runInUiThread() = SwingUtilities.invokeLater(this)
}
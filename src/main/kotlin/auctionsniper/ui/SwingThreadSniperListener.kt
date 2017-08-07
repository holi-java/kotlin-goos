package auctionsniper.ui

import auctionsniper.SniperListener
import auctionsniper.SniperSnapshot
import javax.swing.SwingUtilities.invokeLater

class SwingThreadSniperListener(private val target: SniperListener) : SniperListener {
    override fun sniperStateChanged(snapshot: SniperSnapshot) = invokeLater {
        target.sniperStateChanged(snapshot)
    }

}
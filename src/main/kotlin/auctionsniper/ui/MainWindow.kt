package auctionsniper.ui

import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JLabel

const val MAIN_WINDOW_NAME = "Sniper Application Window"
const val SNIPER_STATUS_NAME = "sniper status"
const val STATUS_JOINING = "JOINING"
const val STATUS_BIDDING = "BIDDING"

private val SNIPER_APPLICATION_NAME = "Sniper Application"

class MainWindow : JFrame(SNIPER_APPLICATION_NAME) {
    private val sniperStatus = JLabel(STATUS_JOINING).apply {
        name = SNIPER_STATUS_NAME
        border = BorderFactory.createLineBorder(Color.black)
    }

    var status; get() = sniperStatus.text; set(value) {
        sniperStatus.text = value
    }


    init {
        name = MAIN_WINDOW_NAME
        defaultCloseOperation = EXIT_ON_CLOSE
        add(sniperStatus)
        pack()
        isVisible = true
    }

}
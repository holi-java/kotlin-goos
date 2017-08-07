package auctionsniper.ui

import java.awt.BorderLayout.CENTER
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTable

const val MAIN_WINDOW_NAME = "Sniper Application Window"
const val SNIPER_APPLICATION_NAME = "Sniper Application"

class MainWindow constructor(snipers: SnipersTableModel) : JFrame(SNIPER_APPLICATION_NAME) {

    init {
        name = MAIN_WINDOW_NAME
        defaultCloseOperation = EXIT_ON_CLOSE
        fillContents(makesSniperTable(snipers))
        pack()
        isVisible = true
    }

    private fun fillContents(sniperTable: JTable) {
        add(JScrollPane(sniperTable), CENTER)
    }

    private fun makesSniperTable(snipers: SnipersTableModel): JTable {
        return JTable(snipers);
    }

    fun whenClosed(action: () -> Unit) {
        addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) = action()
        })
    }

}


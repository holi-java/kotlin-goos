package auctionsniper.ui

import auctionsniper.SniperPortfolio
import auctionsniper.UserRequestListener
import java.awt.BorderLayout.CENTER
import java.awt.BorderLayout.NORTH
import java.awt.FlowLayout
import java.awt.FlowLayout.LEFT
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*

const val MAIN_WINDOW_NAME = "Sniper Application Window"
const val SNIPER_APPLICATION_NAME = "Sniper Application"
const val ITEM_ID_FIELD_NAME = "Item Id"
const val JOIN_BUTTON_NAME = "Join Auction Button"

class MainWindow : JFrame {

    constructor(portfolio: SniperPortfolio) : super(SNIPER_APPLICATION_NAME) {
        val snipers = SnipersTableModel().also { portfolio.addPortfolioListener(it) }
        name = MAIN_WINDOW_NAME
        defaultCloseOperation = EXIT_ON_CLOSE
        fillContents(makeControls(), makeSnipersTable(snipers))
        pack()
        isVisible = true
    }

    private val listeners = arrayListOf<UserRequestListener>()


    private fun makeControls() = JPanel(FlowLayout(LEFT)).apply {
        val itemField = JTextField(20).apply { name = ITEM_ID_FIELD_NAME }
        add(JLabel("Item:")).also { add(itemField) }
        add(JButton("Join Auction").apply {
            name = JOIN_BUTTON_NAME
            addActionListener {
                listeners.forEach { it.joinAuction(itemField.text) }
            }
        })
    }

    private fun fillContents(control: JComponent, sniperTable: JTable) = add(control, NORTH).also { add(JScrollPane(sniperTable), CENTER) }

    private fun makeSnipersTable(snipers: SnipersTableModel): JTable {
        return JTable(snipers);
    }

    fun addUserRequestListener(listener: (item: String) -> Unit) = addUserRequestListener(object : UserRequestListener {
        override fun joinAuction(itemId: String) = listener.invoke(itemId)
    })

    fun addUserRequestListener(listener: UserRequestListener) {
        listeners += listener
    }

    fun whenClosed(action: () -> Unit) {
        addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) = action()
        })
    }

}


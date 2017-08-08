package auctionsniper

import auctionsniper.ui.ITEM_ID_FIELD_NAME
import auctionsniper.ui.JOIN_BUTTON_NAME
import auctionsniper.ui.MAIN_WINDOW_NAME
import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.*
import com.objogate.wl.swing.gesture.GesturePerformer
import com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching
import com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText
import javax.swing.JButton
import javax.swing.JTextField
import javax.swing.table.JTableHeader

class ApplicationDriver(timeout: Long) :
        JFrameDriver(GesturePerformer(), topLevelFrame(named(MAIN_WINDOW_NAME), showingOnScreen()), AWTEventQueueProber(timeout, 100)) {

    fun startBiddingFor(itemId: String) = itemIdField().replaceAllText(itemId).also { joinButton().click() }

    private fun itemIdField() = JTextFieldDriver(this, JTextField::class.java, ComponentDriver.named(ITEM_ID_FIELD_NAME)).also {
        it.replaceAllText("")
        it.selectAll()
        it.deleteSelectedText()
        it.replaceAllText("")
    }

    private fun joinButton() = JButtonDriver(this, JButton::class.java, ComponentDriver.named(JOIN_BUTTON_NAME))

    fun showsSniperStatus(itemId: String, lastPrice: Int, lastBid: Int, status: SniperState) {
        JTableDriver(this).hasRow(matching(
                withLabelText(itemId),
                withLabelText("$lastPrice"),
                withLabelText("$lastBid"),
                withLabelText(status.name)
        ))
    }

    fun hasColumnTitles() {
        JTableHeaderDriver(this, JTableHeader::class.java).run {
            hasHeaders(matching(
                    withLabelText("Item"),
                    withLabelText("Last Price"),
                    withLabelText("Last Bid"),
                    withLabelText("State")
            ))
        }
    }

}
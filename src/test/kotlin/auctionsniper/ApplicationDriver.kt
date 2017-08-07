package auctionsniper

import auctionsniper.ui.MAIN_WINDOW_NAME
import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.JFrameDriver
import com.objogate.wl.swing.driver.JTableDriver
import com.objogate.wl.swing.driver.JTableHeaderDriver
import com.objogate.wl.swing.gesture.GesturePerformer
import com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching
import com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText
import javax.swing.table.JTableHeader

class ApplicationDriver(timeout: Long) :
        JFrameDriver(GesturePerformer(), topLevelFrame(named(MAIN_WINDOW_NAME), showingOnScreen()), AWTEventQueueProber(timeout, 100)) {

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
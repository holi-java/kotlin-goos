package auctionsniper

import auctionsniper.ui.MAIN_WINDOW_NAME
import auctionsniper.ui.SNIPER_STATUS_NAME
import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.JFrameDriver
import com.objogate.wl.swing.driver.JLabelDriver
import com.objogate.wl.swing.gesture.GesturePerformer
import org.hamcrest.CoreMatchers

class ApplicationDriver(timeout: Long) :
        JFrameDriver(GesturePerformer(), topLevelFrame(named(MAIN_WINDOW_NAME), showingOnScreen()), AWTEventQueueProber(timeout, timeout / 10)) {

    fun showsSniperStatus(status: String) {
        JLabelDriver(this, named(SNIPER_STATUS_NAME)).hasText(CoreMatchers.equalTo(status))
    }

}
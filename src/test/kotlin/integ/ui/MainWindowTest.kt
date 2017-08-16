package integ.ui

import auctionsniper.ApplicationDriver
import auctionsniper.SniperPortfolio
import auctionsniper.ui.MainWindow
import auctionsniper.ui.SnipersTableModel
import com.objogate.wl.swing.probe.ValueMatcherProbe
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Test

class MainWindowTest {
    val ui = MainWindow(SniperPortfolio())
    val driver = ApplicationDriver(1000)

    @Test
    fun `makes user request when join button clicked`() {
        val probe = ValueMatcherProbe(equalTo("item-123"), "item")

        ui.addUserRequestListener { itemId -> probe.setReceivedValue(itemId) }

        driver.startBiddingFor("item-123")

        driver.check(probe)
    }

    @After
    fun `stop application`() = driver.dispose()
}
package test.auctionsniper.ui

import auctionsniper.SniperSnapshot
import auctionsniper.SniperState.LOST
import auctionsniper.ui.SnipersTableModel.Column.*
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class ColumnTest {

    @Test
    fun `retrieves value from a sniper snapshot`() {
        val snapshot = SniperSnapshot("item", 1, 2, LOST)

        assert.that(IDENTIFIER.valueIn(snapshot), equalTo<Any>("item"))
        assert.that(LAST_PRICE.valueIn(snapshot), equalTo<Any>(1))
        assert.that(LAST_BID.valueIn(snapshot), equalTo<Any>(2))
        assert.that(SNIPER_STATUS.valueIn(snapshot), equalTo<Any>("LOST"))
    }
}
@file:Suppress("MemberVisibilityCanPrivate")

package test.auctionsniper.ui

import auctionsniper.AuctionSniper
import auctionsniper.PriceSource.FromOtherBidder
import auctionsniper.SniperSnapshot
import auctionsniper.SniperState
import auctionsniper.ui.SnipersTableModel
import auctionsniper.ui.SnipersTableModel.Column
import auctionsniper.ui.SnipersTableModel.Column.*
import checking
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.hamcrest.Matchers
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.samePropertyValuesAs
import org.jmock.States
import org.jmock.auto.Mock
import org.jmock.integration.junit4.JUnitRuleMockery
import org.jmock.lib.concurrent.Synchroniser
import org.junit.Rule
import org.junit.Test
import test.ignored
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelEvent.*
import javax.swing.event.TableModelListener

class SnipersTableModelTest {
    val synchroniser = Synchroniser()
    @get:Rule
    val context = JUnitRuleMockery().apply { setThreadingPolicy(synchroniser) }
    @Mock lateinit var listener: TableModelListener

    val snipers by lazy {
        SnipersTableModel().apply {
            addTableModelListener(listener)
        }
    }

    @Test
    fun `sets up column headings`() {
        Column.values().forEach {
            assert.that(snipers.getColumnName(it.ordinal), equalTo(it.columnName))
        }
    }

    @Test
    fun `has enough columns`() {
        assert.that(snipers.columnCount, equalTo(Column.values().size))
    }

    @Test
    fun `sets up sniper values in columns`() {
        val sniperState: States = context.states("sniper").startsAs("joining")
        val sniper = sniper("item id")

        context.checking { -> allowing(listener).tableChanged(with(anyInsertionEvent())) }
        snipers.sniperAdded(sniper)

        context.checking { -> atLeast(1).of(listener).tableChanged(with(aChangeInRow(0)));then(sniperState.`is`("bidding")) }

        sniper.currentPrice(1000, 98, FromOtherBidder)

        synchroniser.waitUntil(sniperState.`is`("bidding"), 100)
        assert.that(snapshotAt(0), equalTo(sniper.snapshot))
    }

    @Test
    fun `notifies listener when adding a sniper`() {
        assert.that(snipers.rowCount, equalTo(0))

        context.checking { ->
            atLeast(1).of(listener).tableChanged(with(anInsertionRowAt(0)))
        }

        snipers.sniperAdded(sniper("item id"))

        assert.that(snipers.rowCount, equalTo(1))
        assert.that(snapshotAt(0), equalTo(sniper("item id").snapshot))
    }

    @Test
    fun `holds snipers in addition order`() {
        context.checking { -> ignoring(listener) }
        val sniper1 = sniper("item id")
        val sniper2 = sniper("item-2")

        snipers.sniperAdded(sniper1)
        snipers.sniperAdded(sniper2)

        assert.that(snapshotAt(0), equalTo(sniper1.snapshot))
        assert.that(snapshotAt(1), equalTo(sniper2.snapshot))
    }

    @Test
    fun `update correct rows for snipers`() {
        val sniper1 = sniper("item-1")
        val sniper2 = sniper("item-2")
        val bidding = sniper2.snapshot.bidding(123, 45)

        context.checking { -> allowing(listener).tableChanged(with(anyInsertionEvent())) }
        snipers.sniperAdded(sniper1)
        snipers.sniperAdded(sniper2)

        context.checking { -> atLeast(1).of(listener).tableChanged(with(aChangeInRow(1))) }

        snipers.sniperStateChanged(bidding)

        assert.that(snapshotAt(0), equalTo(sniper1.snapshot))
        assert.that(snapshotAt(1), equalTo(bidding))
    }

    private fun sniper(itemId: String) = AuctionSniper(itemId, ignored())

    private fun snapshotAt(row: Int): SniperSnapshot {
        return SniperSnapshot(itemId = valueIn(row, IDENTIFIER), lastPrice = valueIn(row, LAST_PRICE), lastBid = valueIn(row, LAST_BID), state = SniperState.valueOf(valueIn(row, SNIPER_STATUS)))
    }

    private inline fun <reified T> valueIn(row: Int, column: Column): T = snipers.getValueAt(row, column.ordinal) as T

    private fun aChangeInRow(row: Int) = samePropertyValuesAs(TableModelEvent(snipers, row, row, ALL_COLUMNS, UPDATE))
    private fun anyInsertionEvent() = hasProperty<TableModelEvent>("type", Matchers.equalTo(INSERT))
    private fun anInsertionRowAt(row: Int) = samePropertyValuesAs(TableModelEvent(snipers, row, row, ALL_COLUMNS, INSERT))
}

@file:Suppress("MemberVisibilityCanPrivate")

package test.auctionsniper.ui

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
import org.jmock.auto.Mock
import org.jmock.integration.junit4.JUnitRuleMockery
import org.junit.Rule
import org.junit.Test
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelEvent.*
import javax.swing.event.TableModelListener

class SnipersTableModelTest {
    @get:Rule val context = JUnitRuleMockery()
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
        val snapshot = SniperSnapshot.joining("item id")
        val bidding = snapshot.bidding(1000, 1098)
        context.checking { -> allowing(listener).tableChanged(with(anyInsertionEvent())) }
        snipers.addSniper(snapshot)

        context.checking { -> atLeast(1).of(listener).tableChanged(with(aChangeInRow(0))) }

        snipers.sniperStateChanged(bidding)

        assert.that(snapshotAt(0), equalTo(bidding))
    }

    @Test
    fun `notifies listener when adding a sniper`() {
        val snapshot = SniperSnapshot.joining("ITEM")
        assert.that(snipers.rowCount, equalTo(0))

        context.checking { ->
            atLeast(1).of(listener).tableChanged(with(anInsertionRowAt(0)))
        }

        snipers.addSniper(snapshot)

        assert.that(snipers.rowCount, equalTo(1))
        assert.that(snapshotAt(0), equalTo(snapshot))
    }

    @Test
    fun `holds snipers in addition order`() {
        context.checking { -> ignoring(listener) }
        val sniper1 = SniperSnapshot.joining("item-1")
        val sniper2 = SniperSnapshot.joining("item-2")

        snipers.addSniper(sniper1)
        snipers.addSniper(sniper2)

        assert.that(snapshotAt(0), equalTo(sniper1))
        assert.that(snapshotAt(1), equalTo(sniper2))
    }

    @Test
    fun `update correct rows for snipers`() {
        val sniper1 = SniperSnapshot.joining("item-1")
        val sniper2 = SniperSnapshot.joining("item-2")
        val bidding = sniper2.bidding(123, 45)
        context.checking { -> allowing(listener).tableChanged(with(anyInsertionEvent())) }
        snipers.addSniper(sniper1)
        snipers.addSniper(sniper2)

        context.checking { -> atLeast(1).of(listener).tableChanged(with(aChangeInRow(1))) }

        snipers.sniperStateChanged(bidding)

        assert.that(snapshotAt(0), equalTo(sniper1))
        assert.that(snapshotAt(1), equalTo(bidding))
    }

    private fun snapshotAt(row: Int): SniperSnapshot {
        return SniperSnapshot(itemId = valueIn(row, IDENTIFIER), lastPrice = valueIn(row, LAST_PRICE), lastBid = valueIn(row, LAST_BID), state = SniperState.valueOf(valueIn(row, SNIPER_STATUS)))
    }

    private inline fun <reified T> valueIn(row: Int, column: Column): T = snipers.getValueAt(row, column.ordinal) as T

    private fun aChangeInRow(row: Int) = samePropertyValuesAs(TableModelEvent(snipers, row, row, ALL_COLUMNS, UPDATE))
    private fun anyInsertionEvent() = hasProperty<TableModelEvent>("type", Matchers.equalTo(INSERT))
    private fun anInsertionRowAt(row: Int) = samePropertyValuesAs(TableModelEvent(snipers, row, row, ALL_COLUMNS, INSERT))
}
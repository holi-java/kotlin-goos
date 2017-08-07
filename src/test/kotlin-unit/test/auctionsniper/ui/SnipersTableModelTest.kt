@file:Suppress("MemberVisibilityCanPrivate")

package test.auctionsniper.ui

import auctionsniper.SniperSnapshot
import auctionsniper.SniperState.*
import auctionsniper.ui.SnipersTableModel
import auctionsniper.ui.SnipersTableModel.Column
import auctionsniper.ui.SnipersTableModel.Column.*
import checking
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.hamcrest.Matchers.*
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
        context.checking { ->
            atLeast(1).of(listener).tableChanged(with(aRowChangedEvent(0)))
        }

        snipers.sniperStateChanged(SniperSnapshot("item id", 1000, 1098, BIDDING))

        assert.that(valueIn(IDENTIFIER), equalTo("item id"))
        assert.that(valueIn(LAST_PRICE), equalTo(1000))
        assert.that(valueIn(LAST_BID), equalTo(1098))
        assert.that(valueIn(SNIPER_STATUS), equalTo("BIDDING"))
    }

    private fun aRowChangedEvent(row: Int) = samePropertyValuesAs(TableModelEvent(snipers, row, row, ALL_COLUMNS, UPDATE))

    private inline fun <reified T> valueIn(column: Column): T = snipers.getValueAt(0, column.ordinal) as T
}
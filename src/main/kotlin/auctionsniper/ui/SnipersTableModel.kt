package auctionsniper.ui

import auctionsniper.SniperListener
import auctionsniper.SniperSnapshot
import auctionsniper.SniperState.JOINING
import javax.swing.table.AbstractTableModel

class SnipersTableModel : AbstractTableModel(), SniperListener {

    private var snapshot: SniperSnapshot = SniperSnapshot("", 0, 0, JOINING)
    override fun getRowCount() = 1

    override fun getColumnCount() = Column.values().size

    override fun getColumnName(column: Int) = Column.at(column).columnName

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        return Column.at(columnIndex).valueIn(snapshot)
    }

    override fun sniperStateChanged(snapshot: SniperSnapshot) {
        this.snapshot = snapshot
        super.fireTableRowsUpdated(0, 0)
    }

    enum class Column(val columnName: String) {
        IDENTIFIER("Item") {
            override fun valueIn(snapshot: SniperSnapshot) = snapshot.itemId
        },
        LAST_PRICE("Last Price") {
            override fun valueIn(snapshot: SniperSnapshot) = snapshot.lastPrice
        },
        LAST_BID("Last Bid") {
            override fun valueIn(snapshot: SniperSnapshot) = snapshot.lastBid
        },
        SNIPER_STATUS("State") {
            override fun valueIn(snapshot: SniperSnapshot) = snapshot.state.name
        };

        companion object {
            fun at(index: Int) = values()[index]
        }

        abstract fun valueIn(snapshot: SniperSnapshot): Any

    }
}
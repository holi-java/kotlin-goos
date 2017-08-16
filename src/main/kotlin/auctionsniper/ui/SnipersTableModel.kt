package auctionsniper.ui

import auctionsniper.AuctionSniper
import auctionsniper.SniperListener
import auctionsniper.SniperPortfolioListener
import auctionsniper.SniperSnapshot
import javax.swing.table.AbstractTableModel

class SnipersTableModel : AbstractTableModel(), SniperListener, SniperPortfolioListener {

    private val snapshots: MutableList<SniperSnapshot> = arrayListOf()

    override fun getRowCount() = snapshots.size

    override fun getColumnCount() = Column.values().size

    override fun getColumnName(column: Int) = Column.at(column).columnName

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        return Column.at(columnIndex).valueIn(snapshots[rowIndex])
    }

    override fun sniperStateChanged(snapshot: SniperSnapshot) = refresh(snapshot).let { fireTableRowsUpdated(it, it) }

    private fun refresh(snapshot: SniperSnapshot) = indexOf(snapshot).also { snapshots[it] = snapshot }

    private fun indexOf(snapshot: SniperSnapshot) =
            snapshots.indexOfFirst { it.sameAs(snapshot) }
                    .also { if (it < 0) TODO("handle exception when no snapshot for updating") }

    override fun sniperAdded(sniper: AuctionSniper) {
        snapshots.size.let {
            snapshots += sniper.snapshot
            sniper.addSniperListener(SwingThreadSniperListener(this))
            fireTableRowsInserted(it, it)
        }
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
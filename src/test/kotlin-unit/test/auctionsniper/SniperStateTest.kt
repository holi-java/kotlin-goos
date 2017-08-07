package test.auctionsniper

import auctionsniper.SniperState.*
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.throws
import org.junit.Test

class SniperStateTest {

    @Test
    fun `sniper lost if auction closed when bidding | joining`() {
        arrayOf(JOINING, BIDDING).forEach {
            assert.that(it.closed, equalTo(LOST))
        }
    }

    @Test
    fun `sniper won if auction closed when winning`() {
        assert.that(WINNING.closed, equalTo(WON))
    }

    @Test
    fun `reports sniper with invalid state if auction closed when won | lost`() {
        arrayOf(WON, LOST).forEach {
            assert.that({ it.closed }, throws(isA<IllegalStateException>()))
        }
    }
}
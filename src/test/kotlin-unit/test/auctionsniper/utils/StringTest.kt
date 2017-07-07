package test.auctionsniper.utils

import auctionsniper.utils.split
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class StringTest {
    @Test
    fun `split drop last blank strings`() {
        assert.that("foo;bar;;".split(';'), equalTo(listOf("foo", "bar")))
    }
}


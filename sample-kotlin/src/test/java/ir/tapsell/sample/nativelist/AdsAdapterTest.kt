package ir.tapsell.sample.nativelist

import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class AdsAdapterTest {

    private lateinit var adapter: AdsAdapter

    @Before
    fun setup() {
        adapter = AdsAdapter(
            delegatedAdapter = mockk(relaxed = true),
            adInterval = 5,
            callback = mockk(relaxed = true),
            activity = mockk(relaxed = true),
            onAdIdRequest = mockk(relaxed = true)
        )
    }

    @Test
    fun `isAdPosition works fine`() {
        adapter.isAdPosition(0) shouldBe false
        adapter.isAdPosition(1) shouldBe false
        adapter.isAdPosition(2) shouldBe false
        adapter.isAdPosition(3) shouldBe false
        adapter.isAdPosition(4) shouldBe false
        adapter.isAdPosition(5) shouldBe true
        adapter.isAdPosition(6) shouldBe false
        adapter.isAdPosition(7) shouldBe false
        adapter.isAdPosition(8) shouldBe false
        adapter.isAdPosition(9) shouldBe false
        adapter.isAdPosition(10) shouldBe false
        adapter.isAdPosition(11) shouldBe true
        adapter.isAdPosition(12) shouldBe false
    }

    @Test
    fun `toDelegatedPosition works fine`() {
        adapter.toDelegatedPosition(0) shouldBe 0
        adapter.toDelegatedPosition(1) shouldBe 1
        adapter.toDelegatedPosition(2) shouldBe 2
        adapter.toDelegatedPosition(3) shouldBe 3
        adapter.toDelegatedPosition(4) shouldBe 4
        adapter.toDelegatedPosition(5) shouldBe -1
        adapter.toDelegatedPosition(6) shouldBe 5
        adapter.toDelegatedPosition(7) shouldBe 6
        adapter.toDelegatedPosition(8) shouldBe 7
        adapter.toDelegatedPosition(9) shouldBe 8
        adapter.toDelegatedPosition(10) shouldBe 9
        adapter.toDelegatedPosition(11) shouldBe -1
        adapter.toDelegatedPosition(12) shouldBe 10
    }
}
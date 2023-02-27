package gay.kanwi.oopfTest

import gay.kanwi.oopf.Setup
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Testing {
    @Test
    fun test() {
        Setup.runExercises()
        assertEquals(true, true)
    }
}
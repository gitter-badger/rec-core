package net.kimleo.rec

import net.kimleo.rec.API.*
import net.kimleo.rec.accessor.lexer.Lexer.buildFieldMapPair
import net.kimleo.rec.sepval.parser.ParseConfig
import org.junit.Test
import kotlin.test.assertEquals

class RecordWrapperTest {
    @Test
    fun shouldBuildAccessorMapSuccessfully() {
        val (map1, leastCapacity1) = buildFieldMapPair("name", "age", "...", "email", "{1}", "comment")

        assertEquals(map1["name"], 0)
        assertEquals(map1["comment"], -1)
        assertEquals(map1["email"], -3)

        assertEquals(leastCapacity1, 3)

        val (map2, leastCapacity2) = buildFieldMapPair("hello", "{3}", "world", "...", "is", "{12}", "beep", "{6}", "end")

        assertEquals(map2["hello"], 0)
        assertEquals(map2["world"], 4)
        assertEquals(map2["is"], -21)
        assertEquals(leastCapacity2, 21)
    }

    @Test
    fun shouldAccessCorrectRecord() {
        val record = rec("Kimmy, Leo, 10, male, 1993/07/09")

        val fact = accessor(rec("first name, {1}, age, ..., dob"))
        val kimmy = fact.of(record)

        // operator fun get:
        assertEquals(kimmy["first name"], "Kimmy")

        assertEquals(kimmy["age"], "10")
        assertEquals(kimmy["dob"], "1993/07/09")
    }



    @Test
    fun shouldAccessBlankSeparatorRecord() {
        val record = rec("[INFO] 2015-02-10 12:35:20PM+8:00 net.kimleo.net.kimleo.rec.Application \"hello world\"", ParseConfig(' ', '"'))

        val fact = accessor(rec("level, date, ..., message"))
        val kimmy = fact.of(record)

        assertEquals(kimmy["level"], "[INFO]")
        assertEquals(kimmy["message"], "hello world")
    }

}
package net.kimleo.rec

import net.kimleo.rec.record.builder.RecCollectBuilder
import net.kimleo.rec.record.builder.RecTypeBuilder
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class RecTypeTest {
    @Test
    fun shouldReturnType() {
        val lines = lines("rec_test.txt.rec")

        val type = RecTypeBuilder().build(lines)

        assertNotNull(type)
    }

    @Test
    fun shouldParseRecord() {
        val records = lines("rec_test.txt")
        val rec = lines("rec_test.txt.rec")

        val type = RecTypeBuilder().build(rec)

        val collect = RecCollectBuilder().build(records, type)

        assertNotNull(collect)

        assertEquals(collect.where("first name", "Kim").records.size, 3)
        assertEquals(collect.where("first name", "Kimm").records.size, 1)

    }

    private fun lines(file: String): List<String> {
        val stream = javaClass.classLoader.getResourceAsStream(file)
        val reader = BufferedReader(InputStreamReader(stream))

        val lines = reader.readLines()
        reader.close()
        return lines
    }
}
package com.mercer.chestnut

import org.apache.commons.codec.digest.DigestUtils
import org.junit.Test

import org.junit.Assert.*
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        val file = File("src/main/java/com/mercer/chestnut/MainActivity.kt")
        println(file.exists())
        println(file.absolutePath)
        println(DigestUtils.md5Hex(file.readBytes()).toUpperCase())
        val content = file.readText().trim()
        println(DigestUtils.md5Hex(content).toUpperCase())

    }

}
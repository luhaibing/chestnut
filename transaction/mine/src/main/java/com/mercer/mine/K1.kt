package com.mercer.mine

import com.mercer.mine.function.Test
import com.mercer.mine.p3.J3
import com.mercer.mine.p3.K3
import com.mercer.mine.p4.J4
import com.mercer.mine.p4.K4
import com.mercer.mine.p5.J5
import com.mercer.mine.p5.K5

/**
 * @author      ：mercer
 * @date        ：2021-02-02  17:20
 * @description ：
 */
class K1 : Test {

    override fun doAction() {

        val j1 = J1()
        val j3 = J3()
        val j4 = J4()
        val j5 = J5()
        println("-------- ${j1.javaClass.simpleName} -------- ")
        println("-------- ${j3.javaClass.simpleName} -------- ")
        println("-------- ${j4.javaClass.simpleName} -------- ")
        println("-------- ${j5.javaClass.simpleName} -------- ")

        val k1 = K1()
        val k3 = K3()
        val k4 = K4()
        val k5 = K5()

        println("-------- ${k1.javaClass.simpleName} -------- ")
        println("-------- ${k3.javaClass.simpleName} -------- ")
        println("-------- ${k4.javaClass.simpleName} -------- ")
        println("-------- ${k5.javaClass.simpleName} -------- ")

    }

    override val name: String = javaClass.simpleName

    override val age: Int = 0

}


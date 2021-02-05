@file:Suppress("SpellCheckingInspection", "unused", "RemoveEmptyClassBody")

/**
 * 我按照的是发布者的公司或者名字划分
 */

// 一般的依赖
object Deps {

}

object Google {
    const val core_ktx = "androidx.core:core-ktx:1.2.0"
    const val appcompat = "androidx.appcompat:appcompat:1.1.0"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:1.1.3"
    const val material = "com.google.android.material:material:1.1.0"
    const val gson = "com.google.code.gson:gson:2.8.6"
}

object UnitTest {
    const val junit = "junit:junit:4.13.1"
}

object InstrumentationTest {
    const val junit = "androidx.test.ext:junit:1.1.1"
    const val espresso_core = "androidx.test.espresso:espresso-core:3.2.0"
}

object Jetbrains {
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"

    // org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2
    const val kotlinx_coroutines_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinx_coroutines}"

    const val kotlinx_coroutines_core =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinx_coroutines}"
    const val kotlinx_coroutines_guava =
        "org.jetbrains.kotlinx:kotlinx-coroutines-guava:${Versions.kotlinx_coroutines}"
}
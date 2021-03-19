import org.gradle.api.Project

/**
 * @author          : mercer
 * @date            : 2021-02-05  17:26
 * @canonicalName   : Named
 * @description     :
 */
@Suppress(
    "EnumEntryName", "unused", "SpellCheckingInspection", "MemberVisibilityCanBePrivate",
    "SameParameterValue"
)
enum class Nameds(val value: String, val group: Int) {

    app("app", Nameds.CONTAINER),

    // 基础
    sdk("sdk", Nameds.FOUNDATION),
    resource("resource", Nameds.FOUNDATION),

    // 功能呢
    annotate("annotate", Nameds.FEATURE),
    core("core", Nameds.FEATURE),
    compiler("compiler", Nameds.FEATURE),
    aspect("aspect", Nameds.FEATURE),
    dev("dev-support", Nameds.FEATURE),

    // 业务
    mine("mine", Nameds.TRANSACTION),
    social("social", Nameds.TRANSACTION),

    // 插件
    magician("magician", Nameds.PLUGIN),

    ;

    fun get(target: Project): Project {
        return target.project(PREFIX + value)
    }

    companion object {

        /**
         * 容器
         */
        const val CONTAINER = 1

        /**
         * 基础
         */
        const val FOUNDATION = CONTAINER shl 1

        /**
         * 功能
         */
        const val FEATURE = FOUNDATION shl 1

        /**
         * 业务
         */
        const val TRANSACTION = FEATURE shl 1

        /**
         * 插件
         */
        const val PLUGIN = TRANSACTION shl 1

        private const val PREFIX = ":"

        private fun name(named: Nameds): String {
            return if (named.value.startsWith(PREFIX)) {
                named.value.substring(PREFIX.length)
            } else {
                named.value
            }
        }

        @JvmStatic
        fun isNotPlugins(target: Project): Boolean {
            return target.name != name(magician)
        }

        @JvmStatic
        fun isContainer(target: Project): Boolean {
            return distinguish(target, CONTAINER)
        }

        @JvmStatic
        fun isPlugin(target: Project): Boolean {
            return distinguish(target, PLUGIN)
        }

        private fun distinguish(target: Project, group: Int): Boolean {
            return try {
                valueOf(target.name).group == group
            } catch (e: Exception) {
                false
            }
        }

    }


}
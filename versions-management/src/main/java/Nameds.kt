import org.gradle.api.Project

/**
 * @author          : mercer
 * @date            : 2021-02-05  17:26
 * @canonicalName   : Named
 * @description     :
 */
@Suppress("EnumEntryName", "unused")
enum class Nameds(private val value: String) {

    // 基础
    sdk(":sdk"),
    resource(":resource"),

    // 功能呢
    annotate(":annotation"),
    core(":core"),
    compiler(":compiler"),
    aspect(":aspect"),
    dev(":dev-support"),

    // 业务
    mine(":mine"),
    social(":social"),

    // 插件
    magician(":magician"),

    ;

    fun get(target: Project): Project {
        return target.project(value)
    }

    companion object {
        @JvmStatic
        fun isNotPlugins(project: Project): Boolean {
            val value = magician.value
            val prefix = ":"
            return if (value.startsWith(prefix)) {
                project.name != value.substring(prefix.length)
            } else {
                project.name != value
            }
        }
    }

}
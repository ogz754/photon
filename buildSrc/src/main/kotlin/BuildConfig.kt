import org.gradle.api.Project

// Photon mod'unun temel yapılandırma dosyası
// Versiyon numaraları ve mod bilgileri burada tanımlanır
object BuildConfig {
    val MINECRAFT_VERSION: String = "1.21.11"
    val NEOFORGE_VERSION: String = "21.11.10-beta"
    val FABRIC_LOADER_VERSION: String = "0.18.1"
    val FABRIC_API_VERSION: String = "0.140.0+1.21.11"
    val SUPPORT_FRAPI: Boolean = true
    val PARCHMENT_VERSION: String? = null

    // Photon versiyon numarası
    var MOD_VERSION: String = "0.1.0"

    // Modrinth'e yüklenecek versiyon string'ini oluşturur
    fun createVersionString(project: Project): String {
        val builder = StringBuilder()
        val isReleaseBuild = project.hasProperty("build.release")
        val buildId = System.getenv("GITHUB_RUN_NUMBER")
        if (isReleaseBuild) {
            builder.append(MOD_VERSION)
        } else {
            builder.append(MOD_VERSION.substringBefore('-'))
            builder.append("-SNAPSHOT")
        }
        builder.append("+mc").append(MINECRAFT_VERSION)
        if (!isReleaseBuild) {
            if (buildId != null) {
                builder.append("-build.${buildId}")
            } else {
                builder.append("-local")
            }
        }
        return builder.toString()
    }
}
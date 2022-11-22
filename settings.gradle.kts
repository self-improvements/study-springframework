rootProject.name = "study-springframework"

// Includes subprojects in root directory as modules automatically.
list(rootProject.projectDir.toPath())
        .filter { it.isDirectory && it.name.startsWith("spring-") }
        .filter {
            list(it.toPath()).anyMatch { child ->
                child.isFile && child.name.matches(Regex("^\\w+\\.gradle\\.kts$"))
            }
        }
        .forEach { include(it.name) }

rootProject.children.forEach {
    val projectName = it.name.replace("spring-", "")
    it.name = projectName
    it.buildFileName = "$projectName.gradle.kts"
}

// -------------------------------------------------------------------------------------------------

fun list(path: java.nio.file.Path): java.util.stream.Stream<File> {
    return java.nio.file.Files.list(path).map { file(it) }
}

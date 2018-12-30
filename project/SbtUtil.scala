import java.io.File

import sbt.io.syntax.fileToRichFile
import sbt.io.{CopyOptions, IO}

object SbtUtils {

  sealed trait RunMode

  object RunMode {
    case object Dev        extends RunMode
    case object Production extends RunMode
  }

  def copyGenerated(
    projectName: String,
    baseDirectory: File,
    mode: RunMode = RunMode.Production
  ) = {
    val buildDir = baseDirectory / "build"

    val scripts = {
      val js = mode match {
        case RunMode.Dev        => s"$projectName-fastopt-bundle.js"
        case RunMode.Production => s"$projectName-opt-bundle.js"
      }

      val inDir = baseDirectory / "target/scala-2.12/scalajs-bundler/main"
      (inDir / js) -> (buildDir / "js" / "bundle.js")
    }

    IO.copy(
      Seq(scripts),
      CopyOptions(overwrite = true, preserveLastModified = true, preserveExecutable = false)
    )
  }
}

package core.markdown

import cats.effect.IO
import common.facades.marked.Marked

object MarkdownParser {

  /** Returns the html compiled from the given markdown string. */
  def parse(markdown: String): IO[String] = IO {
    Marked(markdown)
  }
}

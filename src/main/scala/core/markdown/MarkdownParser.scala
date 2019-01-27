package core.markdown

import common.Const.Language
import fastparse.NoWhitespace._
import fastparse.{parse => fparse, _}

import scala.language.implicitConversions
import scala.util.Try

object MarkdownParser {

  object md {
    sealed trait Value

    sealed trait EmptyLine extends Value

    sealed trait Text extends Value {
      def get: String

      override def toString: String = get
    }

    sealed trait HasChildren[A] extends Value {
      def children: Seq[A]

      def toStringSeparator: String = ", "

      override def toString: String = {
        this.getClass.getSimpleName + "(" + children.mkString(toStringSeparator) + ")"
      }

    }

    sealed trait Multiline[A] extends HasChildren[A] {
      override def children: Seq[A] = lines

      override def toStringSeparator: String = "; "

      def lines: Seq[A]
    }

    case class Document(body: Seq[Value], linkDefs: Seq[LinkDef])

    object Document {
      def apply(body: Value*): Document = Document(body, Seq.empty)
    }

    case class Line(children: Value*) extends HasChildren[Value]

    case object EmptyLine extends EmptyLine

    case class Paragraph(lines: Line*) extends Multiline[Value]

    case class Blank(emptyLines: Int) extends Value

    case class Link(altText: String, url: String) extends Value

    /**
      * A reference to a link definition. This is to support
      * reference-style links. For example:
      * {{{
      *   [I'm a reference-style link][text]
      *   ...
      *   [text]: https://www.mozilla.org
      * }}}
      */
    case class LinkRef(altText: String, name: String) extends Value {
      def raw: String = s"[$altText][$name]"
    }

    case class LinkDef(name: String, url: String) extends Value

    case class Image(altText: String, url: String) extends Value

    /**
      * A reference to a link definition. This is to support
      * reference-style images. For example:
      * {{{
      *   ![an image][img]
      *   ...
      *   [img]: beautiful.jpg
      * }}}
      */
    case class ImageRef(altText: String, name: String) extends Value {
      def raw: String = s"![$altText][$name]"
    }

    case class Plain(get: String) extends Text

    case class Space(get: String) extends Text

    case class Italic(get: String) extends Text

    case class Bold(get: String) extends Text

    case class Heading(level: Int, get: String) extends Text

    case class Quote(get: String) extends Text

    case class BlockQuote(lines: String*) extends Multiline[String]

    case class Code(get: String) extends Text

    case class CodeBlock(lang: Language, lines: String*) extends Multiline[String] {
      override def toString: String = {
        val cls = this.getClass.getSimpleName
        cls + "(lang=" + lang.toString + ", " + children.mkString(toStringSeparator) + ")"
      }
    }

    object CodeBlock {
      def apply(lines: String*): CodeBlock = CodeBlock(Language.Plaintext, lines: _*)
    }

    case class List(lines: Line*) extends Multiline[Line]

    case class Youtube(src: String) extends Value
  }

  @SuppressWarnings(Array("org.wartremover.warts.All"))
  object rules {

    private object predicates {
      def notLineBreak(c: Char): Boolean = c != '\r' && c != '\n'

      def notLineBreakOrUnderscore(c: Char): Boolean =
        notLineBreak(c) && c != '_'

      def notLineBreakOrAsterisk(c: Char): Boolean =
        notLineBreak(c) && c != '*'

      def notLineBreakOrBacktick(c: Char): Boolean =
        notLineBreak(c) && c != '`'
    }

    // ---

    import predicates._

    private def lineBreak[_ : P]: P[Unit] = P { CharIn("\r\n") }

    private def space[_ : P]: P[md.Space] =
      P { CharPred(_ == ' ').rep(1).! } map md.Space.apply

    // ---

    private def emptyLine[_ : P]: P[md.EmptyLine] =
      P { " ".rep ~ lineBreak } map (_ => md.EmptyLine)

    private def plainText[_ : P]: P[md.Plain] =
      P { CharPred(c => c != ' ' && notLineBreak(c)).rep(1).! } map md.Plain

    private def notLinkDef[_ : P]: P[md.Plain] = {
      plainText.filter(p => if(p.get.startsWith("[") && p.get.contains("]:")) false else true)
    }

    private def invalidParagraphLinePrefix[_ : P]: P[Unit] =
      P {
        ("```" ~ lineBreak) | ("```" ~ CharIn("a-z")
          .rep(min = 1, max = 10) ~ lineBreak) | "![" | "> " | "- " | "!youtube("
      }

    private def lineInner[_ : P]: P[Seq[md.Value]] =
      P { (italic | bold | link | code | linkRef | space | notLinkDef).rep(1) }

    /**
      * A line of markdown. Consecutive words will be merged together
      * into a `Plain`.
      */
    private def line[_ : P]: P[md.Line] = {
      P {
        !invalidParagraphLinePrefix ~ lineInner ~ lineBreak.?
      } map { parts =>
        val merged = parts.foldLeft(Seq.empty[md.Value]) { (acc, p) =>
          if(acc.length <= 1) {
            acc :+ p
          }
          else {
            val (pre, last2) = acc.splitAt(acc.length - 2)
            (last2, p) match {
              case (Seq(word: md.Plain, spc: md.Space), next: md.Plain) =>
                pre :+ md.Plain(word.get + spc.get + next.get)
              case _ =>
                acc :+ p
            }
          }
        }
        md.Line(merged: _*)
      }
    }

    private def wordRightBound[_ : P]: P[Unit] =
      P { End | lineBreak | " " | "," | "." | "?" | "!" | ";" }

    /**
      * Either a char that is not '_' or a '_' that doesn't end with space
      * or at the end of the input.
      */
    private def italicInner[_ : P]: P[String] =
      P { (CharPred(notLineBreakOrUnderscore) | "_" ~ !wordRightBound).rep(1).! }

    private def italic[_ : P]: P[md.Italic] = {
      P { "_" ~ italicInner ~ "_" ~ &(wordRightBound) } map md.Italic
    }

    /**
      * Either a char that is not '*' or a '*' that doesn't end with '* '
      * or at the end of the input.
      */
    private def boldInner[_ : P]: P[String] =
      P { (CharPred(notLineBreakOrAsterisk) | ("*" ~ !("*" ~ wordRightBound))).rep(1).! }

    private def bold[_ : P]: P[md.Bold] =
      P { "**" ~ boldInner ~ "**" ~ &(wordRightBound) } map md.Bold

    private def altText[_ : P] =
      P { CharsWhile(c => notLineBreak(c) && c != ']') }

    private def url[_ : P] =
      P { CharsWhile(c => notLineBreak(c) && c != ')') }

    private def image[_ : P]: P[md.Image] = {
      P {
        "![" ~ altText.! ~ "](" ~/ url.! ~ ")" ~ lineBreak.?
      } map {
        case (alt, imgUrl) => md.Image(alt, imgUrl)
      }
    }

    private def imageRef[_ : P]: P[md.ImageRef] = {
      P {
        "![" ~ altText.! ~ "][" ~/ altText.! ~ "]" ~ lineBreak.?
      } map {
        case (alt, refName) => md.ImageRef(alt, refName)
      }
    }

    private def link[_ : P]: P[md.Link] = {
      P {
        "[" ~ altText.! ~ "](" ~/ url.! ~ ")"
      } map {
        case (alt, linkUrl) => md.Link(alt, linkUrl)
      }
    }

    private def linkRef[_ : P]: P[md.LinkRef] = {
      P {
        "[" ~ altText.! ~ "][" ~/ altText.! ~ "]"
      } map {
        case (alt, refName) => md.LinkRef(alt, refName)
      }
    }

    private def linkDef[_ : P]: P[md.LinkDef] = {
      P {
        "[" ~ altText.! ~ "]: " ~/ url.! ~ lineBreak.?
      } map {
        case (refName, url) => md.LinkDef(refName, url)
      }
    }

    private def quote[_ : P]: P[String] =
      P { ("> " ~/ CharPred(notLineBreak).rep.! ~ lineBreak.?) | (">" ~ lineBreak).map(_ => "") }

    /**
      * Either a char that is not '`' or a '`' that doesn't end with space
      * or at the end of the input.
      */
    private def codeInner[_ : P]: P[String] =
      P { (CharPred(notLineBreakOrBacktick) | "`" ~ !wordRightBound).rep(1).! }

    private def code[_ : P]: P[md.Code] =
      P { "`" ~ codeInner ~ "`" ~ &(wordRightBound) } map md.Code

    private def heading[_ : P]: P[md.Heading] = {
      P {
        "#".rep(min = 1, max = 6).! ~ " " ~/ CharPred(notLineBreak).rep.! ~ lineBreak.?
      } map {
        case (hashes, text) => md.Heading(hashes.length, text)
      }
    }

    private def blank[_ : P]: P[md.Blank] =
      P { emptyLine.rep(1) } map (ls => md.Blank(ls.length))

    private def paragraph[_ : P]: P[md.Paragraph] =
      P { line.rep(1) } map (ls => md.Paragraph(ls: _*))

    private def blockQuote[_ : P]: P[md.BlockQuote] =
      P { quote.rep(1) } map (qs => md.BlockQuote(qs: _*))

    private def codeLang[_ : P]: P[String] =
      P { CharIn("a-z").rep(1).! }

    private def codeLine[_ : P]: P[String] =
      P { !"```" ~ CharPred(notLineBreak).rep.! ~ lineBreak }

    private def codeBlock[_ : P]: P[md.CodeBlock] = {
      P {
        "```" ~ codeLang.? ~ lineBreak ~/ codeLine.rep(1) ~ "```" ~ lineBreak.?
      } map {
        case (lang, lines) =>
          val finalLang = lang.flatMap(Language.parse(_).toOption).getOrElse(Language.Plaintext)
          md.CodeBlock(finalLang, lines: _*)
      }
    }

    private def listItem[_ : P]: P[md.Line] =
      P { "- " ~/ line }

    private def list[_ : P]: P[md.List] = {
      P { listItem.rep(1) } map (ls => md.List(ls: _*))
    }

    private def youtube[_ : P]: P[md.Youtube] =
      P { "!youtube(" ~/ url.! ~ ")" ~ lineBreak.? } map md.Youtube

    // ---

    private def body[_ : P]: P[Seq[md.Value]] = {
      P {
        (blank | heading | image | imageRef | youtube | blockQuote |
          codeBlock | list | linkDef | paragraph).rep
      }
    }

    def expr[_ : P]: P[md.Document] = {
      P { body } map { values =>
        val start = (Vector.empty[md.Value], Vector.empty[md.LinkDef])
        val partitioned = values.foldLeft(start) {
          case ((others, linkDefs), v) =>
            v match {
              case l: md.LinkDef => (others, linkDefs :+ l)
              case _             => (others :+ v, linkDefs)
            }
        }
        md.Document(partitioned._1, partitioned._2)
      }
    }
  }

  def doParse(s: String): md.Document = {
    val trimmed = s.trim // this is important!
    fparse(trimmed, rules.expr(_)) match {
      case Parsed.Success(rs, _) => rs
      case f: Parsed.Failure     => throw new Exception(f.trace().longMsg)
    }
  }

  def parse(markdown: String): Try[md.Document] = Try { doParse(markdown) }
}

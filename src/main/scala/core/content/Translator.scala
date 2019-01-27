package core.content

import common.Api
import common.facades.highlighter.Highlighter
import common.Implicits._
import core.markdown.MarkdownParser.md
import core.markdown.MarkdownParser.md._
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.vdom.html_<^._

/** Translate a markdown document into vdom. */
object Translator {

  def apply(markdown: Document, postInfo: PostInfo): VdomNode = {
    val tags = markdown.body.map(translate(_, markdown.linkDefs, postInfo)).toTagMod
    <.div(tags)
  }

  private val imageClassName = "content-img"
  private val imageWrapperClassName = "content-img-wrapper"
  private val anchorClassName = "post-anchor"
  private val videoWrapperClassName = "content-video-wrapper"
  private val videoClassName = "content-video"
  private val videoSize = (640, 360) // width x height

  private def translate(v: Value, linkDefs: Seq[LinkDef], postInfo: PostInfo): TagMod = {
    val recurse = translate(_, linkDefs, postInfo)

    import japgolly.scalajs.react.vdom.all._

    v match {
      case blank: Blank      => Seq.fill(blank.emptyLines / 2)(br()).toTagMod
      case line: Line        => line.children.map(recurse).toTagMod
      case pl: Plain         => pl.toString
      case italic: Italic    => i(italic.toString)
      case bold: Bold        => b(bold.toString)
      case para: Paragraph   => p(para.lines.map(recurse).intersperse(br()).toTagMod)
      case quote: BlockQuote => blockquote(quote.lines.mkString("\n"))
      case link: Link        => a(href := link.url, link.altText)
      case list: md.List     => ul(list.lines.map(l => li(recurse(l))).toTagMod)
      case cde: md.Code      => code(cde.get)
      case h: Heading =>
        h.level match {
          case 1 => h1(cls := "is-1", h.get)
          case 2 => createAnchorHeading(h, postInfo)
          case 3 => h3(cls := "is-3", h.get)
          case 4 => h4(cls := "is-4", h.get)
          case 5 => h5(cls := "is-5", h.get)
          case 6 => h6(cls := "is-6", h.get)
        }
      case codeBlock: md.CodeBlock =>
        Highlighter(codeBlock.lang.toString)(codeBlock.lines.mkString("\n"))
      case linkRef: LinkRef =>
        linkDefs.find(_.name == linkRef.name) match {
          case Some(definition) => a(href := definition.url, linkRef.altText)
          case None             => linkRef.raw
        }
      case imgRef: ImageRef =>
        linkDefs.find(_.name == imgRef.name) match {
          case Some(definition) =>
            val correctUrl = Api.postResource(postInfo.id, definition.url).value
            div(
              cls := imageWrapperClassName,
              img(cls := imageClassName, src := correctUrl, alt := imgRef.altText)
            )
          case None => imgRef.raw
        }
      case youtube: Youtube =>
        // allows minimizing the player
        val pictureInPicture = VdomAttr[Boolean]("picture-in-picture")

        div(
          cls := videoWrapperClassName,
          iframe(
            cls := videoClassName,
            src := youtube.src,
            frameBorder := 0,
            autoPlay := false,
            allowFullScreen := true,
            pictureInPicture := true,
            width := videoSize._1.toString,
            height := videoSize._2.toString
          )
        )
      case other => other.toString
    }
  }

  private def createAnchorHeading(h: Heading, postInfo: PostInfo): VdomNode = {
    import japgolly.scalajs.react.vdom.all._

    val headerId = getHeaderId(h)
    val ref = s"${Api.post(postInfo.id).value}#$headerId"
    h2(cls := "is-2", id := headerId, a(cls := anchorClassName, href := ref, "# "), h.get)
  }

  private def getHeaderId(h: Heading): String = {
    h.toString.toUnderscore
  }
}

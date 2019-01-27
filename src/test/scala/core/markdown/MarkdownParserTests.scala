package core.markdown

import common.Const.Language
import core.markdown.MarkdownParser.md._
import testing.Suite

class MarkdownParserTests extends Suite {

  private val parser = MarkdownParser

  private def check(s: String, expect: Document): Unit = {
    withClue(s"input: $s\n") {
      parser.doParse(s) shouldEqual expect
    }
  }

  private def check(s: String, expect: Value): Unit = {
    val ep = expect match {
      case l: Line => Document(Paragraph(l))
      case _       => Document(expect)
    }
    check(s, ep)
  }

  private val spc = Space(" ")

  implicit class StringToMDValue(s: String) {
    def b = Bold(s)
    def c = Code(s)
    def i = Italic(s)
    def p = Plain(s)
  }

  test("empty") {
    check("", expect = Document())
  }

  test("h1") {
    check("# a", expect = Heading(level = 1, get = "a"))
  }

  test("h2") {
    check("## b", expect = Heading(level = 2, get = "b"))
  }

  test("h1 & h2") {
    check(
      """
        |# a
        |## b
      """.stripMargin,
      expect = Document(
        Heading(level = 1, get = "a"),
        Heading(level = 2, get = "b")
      )
    )

    check(
      """
        |# a
        |
        |## c
      """.stripMargin,
      expect = Document(
        Heading(level = 1, get = "a"),
        Blank(1),
        Heading(level = 2, get = "c")
      )
    )

    check(
      """
        |# a
        |## b
        |## c
      """.stripMargin,
      expect = Document(
        Heading(level = 1, get = "a"),
        Heading(level = 2, get = "b"),
        Heading(level = 2, get = "c")
      )
    )
  }

  test("italic") {
    check("_a_", expect = Line("a".i))
    check("_abc _", expect = Line("abc ".i))
    check("a _bc_", expect = Line("a".p, spc, "bc".i))
    check(
      "a _bc_ d",
      expect = Line("a".p, spc, "bc".i, spc, "d".p)
    )
    check(
      "a _b c_ d",
      expect = Line("a".p, spc, "b c".i, spc, "d".p)
    )
    check(
      """
        |_a_
        |b
        |_c_
      """.stripMargin,
      expect = Paragraph(
        Line("a".i),
        Line("b".p),
        Line("c".i)
      )
    )
  }

  test("allows flexible text inside italic") {
    check("a_b_c", expect = Line("a_b_c".p))
    check("_", expect = Line("_".p))
    check("_a", expect = Line("_a".p))
    check("a _", expect = Line("a _".p))
    check("a _b", expect = Line("a _b".p))
    check("a b_", expect = Line("a b_".p))
    check(
      "a _b_c_ d",
      expect = Line("a".p, spc, "b_c".i, spc, "d".p)
    )
    check(
      "a _b_*_c_ d",
      expect = Line("a".p, spc, "b_*_c".i, spc, "d".p)
    )
    check("_a__ b__c_", expect = Line("a_".i, spc, "b__c_".p))
  }

  test("bold") {
    check("**a**", expect = Line(Bold("a")))
    check("**abc **", expect = Line(Bold("abc ")))
    check("a **bc**", expect = Line("a".p, spc, "bc".b))
    check("a **bc** d", expect = Line("a".p, spc, "bc".b, spc, "d".p))
    check("a **bc**. d", expect = Line("a".p, spc, "bc".b, ". d".p))
    check("a **b c** d", expect = Line("a".p, spc, "b c".b, spc, "d".p))
    check(
      """**a**
        |b
        |**c**
      """.stripMargin,
      expect = Paragraph(
        Line("a".b),
        Line("b".p),
        Line("c".b)
      )
    )
  }

  test("allows flexible text inside bold") {
    check("a**b*c", expect = Line("a**b*c".p))
    check("*", expect = Line("*".p))
    check("*a", expect = Line("*a".p))
    check("a *", expect = Line("a *".p))
    check("a *b", expect = Line("a *b".p))
    check("a b*", expect = Line("a b*".p))
    check("a b**", expect = Line("a b**".p))
    check(
      "a **b*c** d",
      expect = Line("a".p, spc, "b*c".b, spc, "d".p)
    )
    check(
      "a **b_*_c** d",
      expect = Line("a".p, spc, "b_*_c".b, spc, "d".p)
    )
    check("**a*** b**c*", expect = Line("a*".b, spc, "b**c*".p))
  }

  test("bold & italic") {
    check("_a_ **bc**", expect = Line("a".i, spc, "bc".b))
    check(
      "a **bc** _d_",
      expect = Line("a".p, spc, "bc".b, spc, "d".i)
    )
  }

  test("image") {
    check(
      """![GitHub Logo](/images/logo.png)""",
      expect = Image("GitHub Logo", "/images/logo.png")
    )
    check(
      """![sana](https://data.whicdn.com/images/250306187/large.jpg)""",
      expect = Image("sana", "https://data.whicdn.com/images/250306187/large.jpg")
    )
    check(
      """
        |a
        |![b](c)
      """.stripMargin,
      expect = Document(
        Paragraph(Line("a".p)),
        Image("b", "c")
      )
    )

    check(
      """
        |![a][b]
        |[b]: img.jpg
      """.stripMargin,
      expect = Document(
        body = Seq(ImageRef("a", "b")),
        linkDefs = Seq(LinkDef("b", "img.jpg"))
      )
    )
  }

  test("link") {
    check(
      """[GitHub Logo](/images/logo.png)""",
      expect = Line(Link("GitHub Logo", "/images/logo.png"))
    )
    check(
      """[sana](https://data.whicdn.com/images/250306187/large.jpg)""",
      expect = Line(Link("sana", "https://data.whicdn.com/images/250306187/large.jpg"))
    )
    check(
      """
        |[a][b]
        |[b]: link
      """.stripMargin,
      expect = Document(
        body = Seq(Paragraph(Line(LinkRef("a", "b")))),
        linkDefs = Seq(LinkDef("b", "link"))
      )
    )
  }

  test("quote") {
    check("> a", expect = BlockQuote("a"))
    check(
      """
        |a
        |> b
      """.stripMargin,
      expect = Document(
        Paragraph(Line("a".p)),
        BlockQuote("b")
      )
    )
    check("> a > b", expect = BlockQuote("a > b"))
    check(
      """
        |> a > b
        |> c
      """.stripMargin,
      expect = BlockQuote("a > b", "c")
    )
    check(
      """
        |> a
        |
        |> b
        |> c
      """.stripMargin,
      expect = Document(
        BlockQuote("a"),
        Blank(1),
        BlockQuote("b", "c")
      )
    )
  }

  test("code snippet") {
    check("`a`", expect = Line(Code("a")))
    check("a `b`", expect = Line("a".p, spc, "b".c))
    check("a `b`. c", expect = Line("a".p, spc, "b".c, ". c".p))
    check(
      "a `b c ` `d` e",
      expect = Line("a".p, spc, "b c ".c, spc, "d".c, spc, "e".p)
    )
  }

  test("code block") {
    check(
      """
        |```
        |a
        |```
      """.stripMargin,
      expect = CodeBlock(Language.Plaintext, "a")
    )
    check(
      """
        |a
        |```
        |b
        |```
      """.stripMargin,
      expect = Document(
        Paragraph(Line("a".p)),
        CodeBlock(Language.Plaintext, "b")
      )
    )
    check(
      """
        |```
        |a
        |b
        |
        |c
        |```
      """.stripMargin,
      expect = CodeBlock(Language.Plaintext, "a", "b", "", "c")
    )
    check(
      """
        |```css
        |a
        |```
      """.stripMargin,
      expect = CodeBlock(Language.CSS, "a")
    )
    check(
      """
        |Code:
        |```bash
        |npm install
        |```
        |
        |[react]: react.jpg
      """.stripMargin,
      expect = Document(
        Seq(
          Paragraph(Line("Code:".p)),
          CodeBlock(Language.Bash, "npm install"),
          Blank(1)
        ),
        linkDefs = Seq(LinkDef("react", "react.jpg"))
      )
    )
  }

  test("list") {
    check(
      """
        |- a
        |- b
      """.stripMargin,
      expect = List(
        Line("a".p),
        Line("b".p)
      )
    )
    check(
      """
        |- a `code` **bold**
        |- _italic_ b
      """.stripMargin,
      expect = List(
        Line("a".p, spc, "code".c, spc, "bold".b),
        Line("italic".i, spc, "b".p)
      )
    )
  }

  test("youtube") {
    check("""!youtube(a)""", expect = Youtube("a"))
    check(
      """
        |a
        |!youtube(b)
        |!youtube(c)
      """.stripMargin,
      expect = Document(
        Paragraph(Line("a".p)),
        Youtube("b"),
        Youtube("c")
      )
    )
  }

  test("complex document") {
    check(
      """
        |# title
        |## subtitle
        |
        |**Look!**, _italic text_
        |I saw `that` and this code:
        |```
        |if(1 < 2) {
        |  println("Hello world")
        |}
        |```
        |
        |It means
        |> if 1 < 2
        |> then print "Hello world"
        |
        |For more, see [here](https://www.undertherain.io)
        |
        |Author:
        |- Tom
        |- Jerry
        |
        |!youtube(bye.mp4)
        |
        |Goodbye
        |![goodbye image](bye.jpg)
      """.stripMargin,
      expect = Document(
        Heading(level = 1, "title"),
        Heading(level = 2, "subtitle"),
        Blank(1),
        Paragraph(
          Line("Look!".b, ",".p, spc, "italic text".i),
          Line("I saw".p, spc, "that".c, spc, "and this code:".p)
        ),
        CodeBlock("if(1 < 2) {", """  println("Hello world")""", "}"),
        Blank(1),
        Paragraph(Line("It means".p)),
        BlockQuote(
          "if 1 < 2",
          """then print "Hello world""""
        ),
        Blank(1),
        Paragraph(Line("For more, see".p, spc, Link("here", "https://www.undertherain.io"))),
        Blank(1),
        Paragraph(Line("Author:".p)),
        List(Line("Tom".p), Line("Jerry".p)),
        Blank(1),
        Youtube("bye.mp4"),
        Blank(1),
        Paragraph(Line("Goodbye".p)),
        Image("goodbye image", "bye.jpg")
      )
    )
  }
}

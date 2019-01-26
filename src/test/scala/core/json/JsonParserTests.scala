package core.json

import testing.Suite
import upickle.default._

class JsonParserTests extends Suite {

  private val parser = JsonParser

  case class Complex(a: Int, b: Seq[String], c: Option[Int] = None)
  implicit def OptionReader[T : Reader]: Reader[Option[T]] = parser.option
  implicit val rA: Reader[Complex] = macroR

  case class Simple(b: Seq[String])
  implicit val rB: Reader[Simple] = macroR

  test("simple json") {
    val json = """{ "b": ["b1", "b2"] }"""
    val rs = parser.parse[Simple](json)

    val expect = Simple(Seq("b1", "b2"))

    rs.unsafeRunSync() shouldEqual expect
  }

  test("complex json") {
    val json =
      """
        |{
        |  "a": 1,
        |  "b": ["b1", "b2"],
        |  "c": 2
        |}
      """.stripMargin
    val rs = parser.parse[Complex](json)

    val expect = Complex(1, Seq("b1", "b2"), Some(2))

    rs.unsafeRunSync() shouldEqual expect
  }

  test("complex json with optional field missing") {
    val json =
      """
        |{
        |  "a": 1,
        |  "b": ["b1", "b2"]
        |}
      """.stripMargin
    val rs = parser.parse[Complex](json)

    val expect = Complex(1, Seq("b1", "b2"), None)

    rs.unsafeRunSync() shouldEqual expect
  }
}

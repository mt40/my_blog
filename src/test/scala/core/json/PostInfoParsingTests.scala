package core.json

import common.Date
import core.content.{Metadata, PostInfo}
import testing.Suite

class PostInfoParsingTests extends Suite {

  private val parser = JsonParser

  test("json of post info") {
    val json =
      """
        |{
        |  "posts": [
        |    {
        |      "id": "1",
        |      "title": "title",
        |      "createDate": "2019-01-26",
        |      "tags": ["tag1", "tag2"],
        |      "file": "file.md",
        |      "image": "img.jpg",
        |      "summary": "summary"
        |    }
        |  ]
        |}
      """.stripMargin
    val rs = parser.parse[Metadata](json)

    val expect = Metadata(
      PostInfo(
        id = "1",
        title = "title",
        createDate = Date("2019-01-26").get,
        tags = Seq("tag1", "tag2"),
        file = "file.md",
        image = Some("img.jpg"),
        summary = Some("summary")
      )
    )

    rs.unsafeRunSync() shouldEqual expect
  }

  test("json of post info without optional fields") {
    val json =
      """
        |{
        |  "posts": [
        |    {
        |      "id": "1",
        |      "title": "title",
        |      "createDate": "2019-01-26",
        |      "tags": ["tag1", "tag2"],
        |      "file": "file.md"
        |    }
        |  ]
        |}
      """.stripMargin
    val rs = parser.parse[Metadata](json)

    val expect = Metadata(
      PostInfo(
        id = "1",
        title = "title",
        createDate = Date("2019-01-26").get,
        tags = Seq("tag1", "tag2"),
        file = "file.md",
        image = None,
        summary = None
      )
    )

    rs.unsafeRunSync() shouldEqual expect
  }
}

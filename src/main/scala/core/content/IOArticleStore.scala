package core.content

import cats.effect.IO
import com.softwaremill.sttp.Response
import common.Api
import core.http.{HttpClient, IOHttpClient}
import core.json.JsonParser
import core.markdown.MarkdownParser
import io.circe.generic.auto._

class IOArticleStore(httpClient: HttpClient[IO, Response[String]]) extends ArticleStore[IO] {

  override def get(info: ArticleInfo): IO[Article] = {
    for {
      res  <- httpClient.get(Api.article(info.url).value)
      body <- IO { res.unsafeBody }
      html <- MarkdownParser.parse(body)
    } yield Article(info, body, html)
  }

  override def getMetadata: IO[Metadata] = {
    for {
      res      <- httpClient.get(Api.metadata.value)
      body     <- IO { res.unsafeBody }
      metadata <- JsonParser.parse[Metadata](body)
    } yield metadata
  }
}

object IOArticleStore {
  lazy val default = new IOArticleStore(IOHttpClient)
}

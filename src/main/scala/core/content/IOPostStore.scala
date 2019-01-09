package core.content

import cats.effect.IO
import com.softwaremill.sttp.Response
import common.Api
import core.http.{HttpClient, IOHttpClient}
import core.json.JsonParser
import core.markdown.MarkdownParser
import io.circe.generic.auto._

class IOPostStore(httpClient: HttpClient[IO, Response[String]]) extends PostStore[IO] {

  override def get(info: PostInfo): IO[Post] = {
    for {
      res  <- httpClient.get(Api.post(info.url).value)
      body <- IO { res.unsafeBody }
      html <- MarkdownParser.parse(body)
    } yield Post(info, body, html)
  }

  override def getMetadata: IO[Metadata] = {
    for {
      res      <- httpClient.get(Api.metadata.value)
      body     <- IO { res.unsafeBody }
      metadata <- JsonParser.parse[Metadata](body)
    } yield metadata
  }
}

object IOPostStore {
  lazy val default = new IOPostStore(IOHttpClient)
}

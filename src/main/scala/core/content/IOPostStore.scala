package core.content

import cats.effect.IO
import common.Api
import core.http.{HttpClient, HttpResponse, IOHttpClient}
import core.json.JsonParser
import core.markdown.MarkdownParser

class IOPostStore(httpClient: HttpClient[IO, HttpResponse]) extends PostStore[IO] {

  override def get(info: PostInfo): IO[Post] = {
    for {
      res  <- httpClient.get(Api.postResource(info.id, info.file).value)
      body <- IO { res.body }
      html <- MarkdownParser.parse(body)
    } yield Post(info, body, html)
  }

  override def getMetadata: IO[Metadata] = {
    for {
      res      <- httpClient.get(Api.metadata.value)
      body     <- IO { res.body }
      metadata <- JsonParser.parse[Metadata](body)
    } yield metadata
  }
}

object IOPostStore {
  lazy val default = new IOPostStore(IOHttpClient)
}

package core.content

import cats.effect.IO
import common.Api
import core.http.{HttpClient, HttpResponse, IOHttpClient}
import core.json.JsonParser

class IOPostStore(httpClient: HttpClient[IO, HttpResponse]) extends PostStore[IO] {

  override def get(info: PostInfo): IO[Post] = {
    for {
      res      <- httpClient.get(Api.postResource(info.id, info.file).value)
      markdown <- IO { res.body }
    } yield Post(info, markdown)
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

package core.http

import cats.effect.IO
import com.softwaremill.sttp.{FetchBackend, Response, SttpBackend, sttp, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object IOHttpClient extends HttpClient[IO, Response[String]] {

  // We still have to use `Future` here because 'sttp' only supports that monad
  private implicit lazy val backend: SttpBackend[Future, Nothing] = FetchBackend()

  override def get(endpoint: String): IO[Response[String]] = IO.fromFuture {
    IO(sttp.get(uri"$endpoint").send())
  }
}

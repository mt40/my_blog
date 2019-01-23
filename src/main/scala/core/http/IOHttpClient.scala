package core.http

import cats.effect.IO
import org.scalajs.dom.ext.Ajax

import scala.concurrent.ExecutionContext

object IOHttpClient extends HttpClient[IO, HttpResponse] {

  private implicit val ec: ExecutionContext = scalajs.concurrent.JSExecutionContext.queue

  override def get(endpoint: String): IO[HttpResponse] = IO.fromFuture {
    IO{
      Ajax
        .get(endpoint, headers = Map("Content-Encoding" -> "gzip"))
        .map(res => HttpResponse(res.status, res.responseText))
    }
  }
}

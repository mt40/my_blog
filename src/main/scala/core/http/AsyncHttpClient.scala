//package core.http
//
//import com.softwaremill.sttp.{FetchBackend, Response, SttpBackend, sttp, _}
//
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//
//object AsyncHttpClient extends HttpClient[Future, Response[String]] {
//
//  private implicit lazy val backend: SttpBackend[Future, Nothing] = FetchBackend()
//
//  override def get(endpoint: String): Future[Response[String]] = {
//    sttp.get(uri"$endpoint").send()
//  }
//}

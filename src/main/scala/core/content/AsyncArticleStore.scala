//package core.content
//
//import com.softwaremill.sttp.Response
//import common.Api
//import common.implicits._
//import core.http.{AsyncHttpClient, HttpClient}
//
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//
//class AsyncArticleStore(httpClient: HttpClient[Future, Response[String]])
//    extends ArticleStore[Future] {
//
//  def getAll: Future[Seq[Article]] = {
//    getMetadata
//      .map(_.articles)
//      .flatMap { info =>
//        val articles = info.map(this.get)
//        Future.sequence(articles)
//      }
//  }
//
//  override def get(art: ArticleInfo): Future[Article] = {
//    for {
//      res  <- httpClient.get(Api.article(art.name).value)
//      body <- res.body.toFuture
//      html <- MarkdownParser.parse(body)
//    } yield Article(art, body, html)
//  }
//
//  override def getMetadata: Future[Metadata] = {
//    for {
//      res      <- httpClient.get(Api.metadata.value)
//      body     <- res.body.toFuture
//      metadata <- JsonParser.parse(body)
//    } yield metadata
//  }
//}
//
//object AsyncArticleStore {
//  lazy val default: AsyncArticleStore = new AsyncArticleStore(AsyncHttpClient)
//}

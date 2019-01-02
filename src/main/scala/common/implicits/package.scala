package common

import scala.concurrent.Future

package object implicits {

  implicit class EitherOps[A, B](val e: Either[A, B]) extends AnyVal {

    def toFuture: Future[B] = {
      e match {
        case Left(err) => Future.failed(new Exception(err.toString))
        case Right(r)  => Future.successful(r)
      }
    }
  }
}

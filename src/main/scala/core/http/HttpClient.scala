package core.http

import scala.language.higherKinds

/**
  * @tparam M   type of monad to use
  * @tparam Res type of response
  */
trait HttpClient[M[_], Res] {

  /** Sends a GET request. */
  def get(uri: String): M[Res]
}

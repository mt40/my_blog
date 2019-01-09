package core.content

import scala.language.higherKinds

/**
  * This design pattern is called "Tagless Final".
  *
  * @see https://blog.scalac.io/exploring-tagless-final.html
  *      http://www.beyondthelines.net/programming/introduction-to-tagless-final/
  */
trait PostStore[M[_]] {

  def get(info: PostInfo): M[Post]

  def getMetadata: M[Metadata]
}

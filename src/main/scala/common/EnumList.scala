package common

import scala.util.Try

/**
  * Represent an object that contains a list of Enum. For example:
  * {{{
  *   case class Color(name: String) extends Enum[String]
  *
  *   object Color extends EnumList[String, Color] {
  *     val red = add("red")
  *     val blue = add ("blue")
  *   }
  * }}}
  *
  * @tparam A    the type of the value stored inside an Enum
  */
trait EnumList[A, Repr <: Enum[A]] extends Serializable {
  private var knownEnums = Map[A, Repr]()

  protected def add[B <: Repr](enum: B): B = {
    knownEnums += (enum.get -> enum)
    enum
  }

  def parse(from: A): Try[Repr] = Try {
    knownEnums.get(from) match {
      case Some(e) => e
      case None    => throw new Exception(s"Unknown enum: ${from.toString}")
    }
  }

  /** Returns all added constants, the add order is not preserved. */
  def values: Seq[Repr] = {
    // Cannot use `toSeq` here because it will produce a Stream,
    // which is not serializable
    knownEnums.values.toIndexedSeq
  }
}

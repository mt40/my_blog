package core.json

import cats.effect.IO
import upickle.default._

object JsonParser {

  /**
    * A custom reader for `Option`.
    *
    * To use this, you have to declare an implicit named exactly 'OptionReader'.
    * {{{
    *   implicit def OptionReader[T : Reader]: Reader[Option[T]] = JsonParser.option
    * }}}
    *
    * This is because we want to override the reader of the same type in 'upickle'.
    * That one accepts an array instead of a single value (see the issue below).
    *
    * @see https://github.com/lihaoyi/upickle/issues/75
    */
  def option[T : Reader]: Reader[Option[T]] = reader[ujson.Value].map {
    case ujson.Null => None
    case jsValue    => Some(read[T](jsValue))
  }

  def parse[A : Reader](json: String): IO[A] = IO {
    read[A](json)
  }
}

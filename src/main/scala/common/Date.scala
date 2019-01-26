package common

import moment.Moment
import upickle.default._

import scala.util.{Failure, Success, Try}

/**
  * Representation of a local date. Backed by 'Moment.js'.
  *
  * @note Locale feature is not supported and its code is ignore
  *       in the final bundle. If you ever want to use it, you must
  *       modify the webpack 'IgnorePlugin' rule to allow the code
  *       to be bundled.
  * @see http://momentjs.com/
  */
class Date(momentDate: moment.Date) {

  def year: Int = momentDate.year()

  def month: Int = momentDate.month() + 1 // month in 'moment' is 0-indexed

  /** Returns day of month. */
  def day: Int = momentDate.date()

  def dayOfWeek: String = momentDate.format(Formats.dayOfWeek)

  /**
    * For the full list, see the link.
    * @see http://momentjs.com/docs/#/displaying/
    */
  private object Formats {
    val dayOfWeek = "dddd" // e.g. Monday, Tueday
    val shortMonth = "MMM" // e.g. Jan, Feb
    val dayOfMonth = "D"
    val year = "YYYY"
  }

  def prettyString: String = {
    momentDate.format(
      s"${Formats.dayOfWeek}, ${Formats.shortMonth} ${Formats.dayOfMonth}, ${Formats.year}"
    )
  }

  // testme: looks like output is not in correct format
  override def toString: String = f"$year%04d-$month%02d-$day%02d"

  override def equals(obj: Any): Boolean = {
    obj match {
      case d: Date => toString == d.toString
      case _       => false
    }
  }
}

object Date {

  /**
    * Parses a local date string in ISO 8601 format.
    *
    * @see https://www.wikiwand.com/en/ISO_8601
    *      http://momentjs.com/docs/#/parsing/string/
    */
  def apply(s: String): Try[Date] = Try {
    new Date(Moment(s))
  }

  def zero = new Date(Moment.utc(0))

  implicit def ascOrdering: Ordering[Date] = Ordering.by(_.toString)

  def descOrdering: Ordering[Date] = Ordering.fromLessThan(_.toString > _.toString)

  implicit val jsonReader: Reader[Date] = {
    reader[String].map[Date] { s =>
      Date(s) match {
        case Success(v) => v
        case Failure(e) => throw e
      }
    }
  }
}

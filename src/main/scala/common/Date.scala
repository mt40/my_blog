package common

import io.circe.Decoder
import moment.Moment

import scala.util.Try

/**
  * Representation of a local date. Backed by Moment.js
  *
  * @see http://momentjs.com/
  */
class Date(momentDate: moment.Date) {

  def year: Int = momentDate.year()

  def month: Int = momentDate.month()

  def day: Int = momentDate.day()

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

  override def toString: String = s"$year-$month-$day"
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

  /** For 'circe' to decode json. */
  implicit val decoder: Decoder[Date] = Decoder.decodeString.emapTry(Date.apply)
}

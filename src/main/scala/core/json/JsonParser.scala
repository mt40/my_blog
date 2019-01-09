package core.json

import cats.effect.IO
import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.parser.decode

// todo: add a test to check that no post inside 'metadata.json' has the same name
object JsonParser {

  // Allow default values for missing fields
  implicit val config: Configuration = Configuration.default.withDefaults

  def parse[A : Decoder](json: String): IO[A] = IO.fromEither {
    decode[A](json)
  }
}

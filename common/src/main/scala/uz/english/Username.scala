package uz.english

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Username (
  name: String
)

object Username {
  implicit val usernameDecoder: Decoder[Username] = deriveDecoder[Username]
  implicit val usernameEncoder: Encoder[Username] = deriveEncoder[Username]
}

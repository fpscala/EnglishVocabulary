package uz.english

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import uz.english.User._

case class User (
  id: UserId,
  name: UserName
)

object User {
  type UserId = Int
  type UserName = String
  
  implicit val userDecoder: Decoder[User] = deriveDecoder[User]
  implicit val userEncoder: Encoder[User] = deriveEncoder[User]
}
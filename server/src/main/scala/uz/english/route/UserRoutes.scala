package uz.english.route

import cats.*
import cats.effect.kernel.*
import org.http4s.{EntityDecoder, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import cats.implicits.*
import io.circe.generic.auto.*
import org.http4s.circe.CirceInstances
import uz.english.Username

object UserRoutes extends CirceInstances:

  def apply[F[_]: Async]: HttpRoutes[F] = {
    implicit object dsl extends Http4sDsl[F]; import dsl._

    implicit val usernameDecoder: EntityDecoder[F, Username]  = jsonOf[F, Username]

    HttpRoutes.of[F] {
      case request @ POST -> Root / "create" =>
        for {
          user <- request.as[Username]
          _ = println("Username:"  + user.name)
          resp <- Ok("Successfully created!")
        } yield (resp)
    }
  }
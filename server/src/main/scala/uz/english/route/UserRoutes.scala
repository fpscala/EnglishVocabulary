package uz.english.route

import cats.effect.kernel.Async
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.EntityEncoder
import org.http4s.circe._

object UserRoutes:
  implicit def entityEncoder[F[_]]: EntityEncoder[F, String] = jsonEncoderOf[F, String]
  def apply[F[_]: Async]: HttpRoutes[F] = {
    implicit object dsl extends Http4sDsl[F]; import dsl._

    HttpRoutes.of[F] {
      case request @ GET -> Root / "create" as username =>
        println("Username:"  + username)
        Ok("Successfully added".asJson)

    }
  }

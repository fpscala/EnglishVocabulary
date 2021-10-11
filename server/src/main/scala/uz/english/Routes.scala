package uz.english

import cats.data.Kleisli
import cats.effect.IO
import org.http4s.Method.GET
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.{HttpRoutes, Request, Response}
import scalatags.Text.all._
import uz.english.views.Index

object Routes {
  val dsl: Http4sDsl[IO] = new Http4sDsl[IO] {}


  private val Routes: HttpRoutes[IO] =
    HttpRoutes.of {
      case _ @GET -> Root  => Index()
      case GET -> Root / _ => NotFound()
    }

  val AppRouter: Kleisli[IO, Request[IO], Response[IO]] =
    Router("/" -> Routes).orNotFound

}

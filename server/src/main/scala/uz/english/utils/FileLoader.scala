package uz.english.utils

import cats.*
import cats.effect.IO
import cats.implicits._
import cats.effect.kernel.Async
import fs2.io.file.{Files, Path}
import org.http4s.Status.NotFound
import org.http4s.dsl.Http4sDsl
import org.http4s.{Request, Response, StaticFile}

import scala.concurrent.ExecutionContext

trait FileLoader[F[_]]:

  def page(path: String, request: Request[F])(implicit dsl: Http4sDsl[F]): F[Response[F]]

  def assets(path: String, request: Request[F])(implicit dsl: Http4sDsl[F]): F[Response[F]]

object FileLoader:

  def apply[F[_]](implicit ev: FileLoader[F]): FileLoader[F] = ev

  val PagePath: String = "./views/"

  implicit def fileLoader[F[_]: Async]: FileLoader[F] =
    new FileLoader[F] {

      override def page(
        path: String,
        request: Request[F]
      )(implicit dsl: Http4sDsl[F]): F[Response[F]] = {
        import dsl.*
        StaticFile.fromResource(PagePath + path, request.some).getOrElseF(NotFound())
      }

      override def assets(
        path: String,
        request: Request[F]
      )(implicit dsl: Http4sDsl[F]): F[Response[F]] = {
        import dsl.*
        StaticFile.fromResource(path, request.some).getOrElseF(NotFound())
      }
    }

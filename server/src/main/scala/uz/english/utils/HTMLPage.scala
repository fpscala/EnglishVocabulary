package uz.english.utils

import cats.*
import cats.effect.IO
import cats.effect.kernel.Async
import fs2.io.file.{Files, Path}
import org.http4s.Status.NotFound
import org.http4s.dsl.Http4sDsl
import org.http4s.{Request, Response, StaticFile}

import scala.concurrent.ExecutionContext

trait HTMLPage[F[_]] {

  def page(pagePath: String, request: Request[F])(implicit dsl: Http4sDsl[F]): F[Response[F]]

}
object HTMLPage {

  def apply[F[_]](implicit ev: HTMLPage[F]): HTMLPage[F] = ev

  private def pathResolver: String => String = pagePath => "./views/" + pagePath

  implicit def pageLoader[F[_]: Async]: HTMLPage[F] =
    new HTMLPage[F] {

      override def page(
        pagePath: String,
        request: Request[F]
      )(implicit dsl: Http4sDsl[F]): F[Response[F]] = {
        import dsl.*
        println(pathResolver(pagePath))
        StaticFile.fromResource(pathResolver(pagePath), Some(request)).getOrElseF(NotFound())
      }
    }
}

package uz.english.utils

import cats.*
import cats.effect.IO
import fs2.io.file.{Files, Path}
import org.http4s.Status.NotFound
import org.http4s.dsl.Http4sDsl
import org.http4s.{Request, Response, StaticFile}

import java.io.File
import scala.concurrent.ExecutionContext

trait HTMLPage[F[_]] {

  def page(pagePath: String, request: Request[F])(implicit dsl: Http4sDsl[F]): F[Response[F]]

}
object HTMLPage {

  def apply[F[_]](implicit ev: HTMLPage[F]): HTMLPage[F] = ev

  private def pathResolver: String => Path = pagePath => Path("uz/english/views/" + pagePath)

  implicit def pageLoader[F[_]: Files: MonadThrow]: HTMLPage[F] =
    new HTMLPage[F] {

      override def page(
        pagePath: String,
        request: Request[F]
      )(implicit dsl: Http4sDsl[F]): F[Response[F]] = {
        import dsl.*
        println(pathResolver(pagePath))
        println(new File("../views/" + pagePath).canRead)
        StaticFile.fromPath(pathResolver(pagePath), Some(request)).getOrElseF(NotFound())
      }
    }
}

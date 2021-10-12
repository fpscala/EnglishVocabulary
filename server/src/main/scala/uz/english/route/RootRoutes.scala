package uz.english.route

import cats.effect.kernel.Async
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import uz.english.utils.HTMLPage

object RootRoutes {

  def apply[F[_]: Async: HTMLPage]: HttpRoutes[F] = {
    implicit object dsl extends Http4sDsl[F]; import dsl._

    HttpRoutes.of[F] {
      case request @ GET -> Root => HTMLPage[F].page("index.html", request)
      case GET -> Root / _ => NotFound()
    }
  }

}

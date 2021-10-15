package uz.english.route

import cats.effect.kernel.Async
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import uz.english.utils.FileLoader

object RootRoutes:

  val supportedStaticExtensions = List(".css", ".png", ".ico", ".jpg", ".jpeg", ".otf", ".ttf", ".woff2", ".woff")
  
  def apply[F[_]: Async: FileLoader]: HttpRoutes[F] = {
    implicit object dsl extends Http4sDsl[F]; import dsl._

    HttpRoutes.of[F] {
      case request @ GET -> Root => FileLoader[F].page("index.html", request)
      case request if supportedStaticExtensions.exists(request.pathInfo.toString.endsWith) =>
        FileLoader[F].assets(request.pathInfo.toString, request)
    }
  }

package uz

import cats.data.NonEmptyList
import cats.effect.IO
import org.http4s.CacheDirective.`no-cache`
import org.http4s.dsl.io._
import org.http4s.headers.{`Cache-Control`, `Content-Type`}
import org.http4s.{Charset, MediaType, Response}
import scalatags.Text.all.{Modifier, _}

package object english {

  object Page {
    def apply(
      headContent: List[Modifier] = Nil,
      bodyContent: List[Modifier] = Nil,
      scripts: List[Modifier] = Nil
    ): IO[Response[IO]#Self#Self] =
      Ok(html(
        head(
          headContent :+ link(rel := "shortcut icon", media := "image/png", href := "/assets/images/favicon.png"): _*),
        body(bodyContent ++ scripts: _*)).render)
        .map(_.withContentType(`Content-Type`(MediaType.text.html, Charset.`UTF-8`))
          .putHeaders(`Cache-Control`(NonEmptyList.of(`no-cache`()))))
  }
}

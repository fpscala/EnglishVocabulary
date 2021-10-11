package uz.english.views

import cats.effect.IO
import org.http4s.Response
import scalatags.Text.all._
import uz.english.Page

object Index {

  val contentBody = List(div(id := "app"))
  val scripts = List(script(src := "scalajs-client-fastopt-bundle.js"))

  def apply(): IO[Response[IO]#Self#Self] = Page(bodyContent = contentBody, scripts = scripts)
}

package uz.english

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.blaze.server.BlazeServerBuilder
import uz.english.Routes.AppRouter

import scala.concurrent.ExecutionContext.global

object Server extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO](global)
      .bindHttp(9999, "localhost")
      .withHttpApp(AppRouter)
      .serve
  }.compile.lastOrError
}

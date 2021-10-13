package uz.english.route

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.http4s.implicits.*
import org.http4s.server.staticcontent.WebjarServiceBuilder
import org.http4s.{Method, Request, Response, Status, Uri}
import org.specs2.Specification
import uz.english.Server

import scala.concurrent.ExecutionContext

class RootRoutesSpec extends Specification:

  def is =
    s2"""
        Root routes
          on GET request
            should return 200 OK status       $return200
          on POST request
            should return 404 NotFound status $return404
          when ask webjars library
            should return 200 OK status       $webjarsReturn200
        """

  private[this] def retIndexPage(method: Method): Response[IO] = {
    implicit val cs: IO[ExecutionContext] = IO.executionContext

    val postIndexPage = Request[IO](method, uri"/")

    RootRoutes[IO]
      .orNotFound(postIndexPage)
      .unsafeRunSync()
  }

  private[this] def retWebjars(path: String): Response[IO] = {
    implicit val cs: IO[ExecutionContext] = IO.executionContext

    val getBootstrap = Request[IO](Method.GET, Uri().withPath(path = Uri.Path.unsafeFromString(path)))

    Server.webjars[IO]
      .orNotFound(getBootstrap)
      .unsafeRunSync()
  }

  private[this] def return200 =
    retIndexPage(Method.GET).status must beEqualTo(Status.Ok)

  private[this] def return404 =
    retIndexPage(Method.POST).status must beEqualTo(Status.NotFound)

  private[this] def webjarsReturn200 =
    retWebjars("/bootstrap/5.1.2/css/bootstrap.min.css").status must beEqualTo(Status.Ok)
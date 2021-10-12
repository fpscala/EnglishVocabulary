package uz.english

import cats.effect.kernel.{Async, Resource}
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.{Router, Server}
import uz.english.route.RootRoutes

import scala.concurrent.ExecutionContext.global

object Server {

  def run[F[_]: Async]: Resource[F, Server] =
    server[F](Router[F]("/" -> RootRoutes[F]))

  private[this] def server[F[_]: Async](routes: HttpRoutes[F]): Resource[F, Server] = {
    import org.http4s.implicits.*

    BlazeServerBuilder[F]
      .withExecutionContext(global)
      .bindHttp(9000)
      .withHttpApp(routes.orNotFound)
      .resource
  }

}

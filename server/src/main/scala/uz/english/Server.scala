package uz.english

import cats.syntax.all._
import cats.effect.kernel.{Async, Resource}
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.staticcontent.{WebjarService, WebjarServiceBuilder}
import org.http4s.server.{Router, Server}
import uz.english.route.RootRoutes

import scala.concurrent.ExecutionContext.global

object Server:

  def webjars[F[_]: Async]: HttpRoutes[F] = WebjarServiceBuilder.apply[F].toRoutes

  def router[F[_]: Async]: HttpRoutes[F] = Router[F](
    "/" -> RootRoutes[F],
    "/webjars" -> webjars[F]
  )

  def run[F[_]: Async]: Resource[F, Server] =
    server[F](router)

  private[this] def server[F[_]: Async](router: HttpRoutes[F]): Resource[F, Server] = {
    import org.http4s.implicits.*

    BlazeServerBuilder[F]
      .withExecutionContext(global)
      .bindHttp(9000)
      .withHttpApp(router.orNotFound)
      .resource
  }


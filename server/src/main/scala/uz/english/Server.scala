package uz.english

import cats.*
import cats.syntax.all.*
import cats.effect.kernel.{Async, Resource}
import org.http4s.HttpRoutes
import cats.effect.std.Console
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.staticcontent.{WebjarService, WebjarServiceBuilder}
import org.http4s.server.{Router, Server}
import uz.english.route.{RootRoutes, UserRoutes}
import uz.english.config.{Config, DBConfig, ServerConfig}
import uz.english.service.UserService

import scala.concurrent.ExecutionContext.global

object Server:

  def webjars[F[_]: Async]: HttpRoutes[F] = WebjarServiceBuilder.apply[F].toRoutes

  def userService[F[_]: Async: Console](config: DBConfig): UserService[F] = UserService[F](config)

  def router[F[_]: Async: Console](config: Config): HttpRoutes[F] = Router[F](
    "/" -> RootRoutes[F],
    "/user" -> UserRoutes[F](userService(config.db)),
    "/webjars" -> webjars[F]
  )

  def run[F[_]: Async: Console]: Resource[F, Server] =
    for {
      conf <- configLoader[F]
      srv <- server[F](conf.server, router(conf))
    } yield srv

  def configLoader[F[_]: Async]: Resource[F, Config]  = {
    for {
      config  <-  Resource.eval(Applicative[F].pure(Config(
        ServerConfig("localhost", 9000),
        DBConfig("localhost", 5432, "english", "prince", "123")
      )))
    } yield config
  }

  private[this] def server[F[_]: Async](conf: ServerConfig, router: HttpRoutes[F]): Resource[F, Server] = {
    import org.http4s.implicits.*

    BlazeServerBuilder[F]
      .withExecutionContext(global)
      .bindHttp(conf.port, conf.host)
      .withHttpApp(router.orNotFound)
      .resource
  }


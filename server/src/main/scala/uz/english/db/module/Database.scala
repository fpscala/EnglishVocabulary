package uz.english.db.module

import cats.implicits.*
import cats.effect.*
import cats.effect.kernel.Async
import cats.effect.std.Console
import skunk.*
import natchez.Trace.Implicits.noop
import uz.english.config.DBConfig
import uz.english.db.algebra.UserAlgebra

trait Database[F[_]]:
  val user: Resource[F, UserAlgebra[F]]

object Database:

  def apply[F[_]: Async: Console](config: DBConfig) = new Database[F]:
    private val session: Resource[F, Session[F]] =
      Session.single(
        host = config.host,
        port = config.port,
        database = config.database,
        user = config.user,
        password = config.password.some)

    override val user: Resource[F, UserAlgebra[F]] = session.map(UserAlgebra[F](_))

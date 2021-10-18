package uz.english.db.algebra

import cats.implicits._
import cats.effect.kernel.Async
import skunk.{Command, Session}
import uz.english.Username
import uz.english.db.service.{UserSql, UserSqlImpl}

trait UserAlgebra[F[_]]:
  def create(username: Username): F[Unit]

object UserAlgebra:
  val userSql: UserSql = UserSqlImpl()

  def apply[F[_]: Async](session: Session[F]) =
    new UserAlgebra[F]:
      override def create(username: Username): F[Unit] =
        session.prepare(userSql.insert).use(_.execute(username)).void
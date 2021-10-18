package uz.english.db.algebra

import cats.implicits._
import cats.effect.kernel.Async
import skunk.{Command, Session}
import uz.english.Username
import uz.english.db.service.UserSql

trait UserAlgebra[F[_]]:
  def create(username: Username): F[Unit]

object UserAlgebra:
  import UserSql._

  def apply[F[_]: Async](session: Session[F]) =
    new UserAlgebra[F]:
      override def create(username: Username): F[Unit] =
        session.prepare(insert).use(_.execute(username)).void
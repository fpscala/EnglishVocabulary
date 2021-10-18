package uz.english.service

import cats.effect.std.Console
import cats.effect.kernel.Async
import uz.english.Username
import uz.english.config.DBConfig
import uz.english.db.module.Database

trait UserService[F[_]]:
  def create(username: Username): F[Unit]

object UserService:

  def apply[F[_]: Async: Console](config: DBConfig): UserService[F] =
    new UserService[F]:
      import cats.implicits.*
      private val database = Database[F](config)

      def create(username: Username): F[Unit] = database.user.use(_.create(username))

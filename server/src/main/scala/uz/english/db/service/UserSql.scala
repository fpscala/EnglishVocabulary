package uz.english.db.service

import cats.syntax.all.*
import cats.effect.*
import cats.implicits._
import cats.effect.kernel.*
import cats.effect.kernel.implicits.*
import cats.effect.std.*
import skunk._
import skunk.codec.all._
import skunk.implicits._
import uz.english.{User, Username}

object UserSql:

  val codec: Codec[User] =
    (int4 ~ varchar).imap {
      case (i ~ n) => User(i, n)
    }(c => c.id ~ c.name)

  val insert: Command[Username] =
    sql"""INSERT INTO "user" VALUES (DEFAULT, $varchar)"""
     .command
     .gcontramap[Username]

  val selectAll: Query[Void, User] =
    sql"""SELECT * FROM "user" """.query(codec)

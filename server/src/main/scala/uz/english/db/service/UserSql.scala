package uz.english.db.service

import cats.effect._
import cats.implicits._
import natchez.Trace.Implicits.noop
import skunk._
import skunk.codec.all._
import skunk.implicits._
import uz.english.Username

trait UserSql:
  val insert: Command[Username]

object UserSqlImpl:

  def apply() = new UserSql {
    override val insert: Command[Username] =
      sql"""INSERT INTO "user" VALUES (DEFAULT, $varchar)"""
       .command
       .gcontramap[Username]
  }


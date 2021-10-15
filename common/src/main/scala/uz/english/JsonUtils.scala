package uz.english
import io.circe.{Decoder, Encoder, Printer}
import io.circe.parser.parse
import io.circe.syntax.EncoderOps

object JsonUtils {
  val printer: Printer = Printer.noSpaces.copy(dropNullValues = true)

  def toJsonString[T: Encoder](t: T): String = t.asJson.printWith(printer)

  def fromJson[T: Decoder](s: String): T =
    parse(s).fold(throw _, json => json).as[T].fold(throw _, identity)
}

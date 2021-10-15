package uz.english.style

import scalacss.internal.{FontFace, NonEmptyVector}
import uz.english.CssSettings.*

import scala.language.postfixOps

object AppStyle extends StyleSheet.Inline {
  import dsl._

  val frijole: FontFace[String] = fontFace("Frijole")(
    _.src("url(/assets/font/Frijole.ttf)")
     .fontStyle.normal
     .fontWeight._400
  )

  val formShadowColor = c"#ffa580"

  val box: StyleA = style(
    background := c"#f8f4e5",
    padding(50px, 100px),
    border(2px, solid, rgba(0,0,0,1)),
    boxShadow := s"15px 15px 1px #ffa580, 15px 15px 1px 2px rgba(0,0,0,1)"
  )

  val button: StyleA = style (
    display.block,
    fontSize(14 pt),
    background := formShadowColor,
    lineHeight(28 px),
    margin(0 px, auto),
    transition := ".2s all ease-in-out",
    fontFamily(frijole),
    outline.none,
    border(1 px, solid, black),
    boxShadow := "3px 3px 1px #95a4ff, 3px 3px 1px 1px black",
    padding(0 px, 20 px),

    &.hover(
      backgroundColor(black),
      color(white),
      border(1 px, solid, black)
    ),
  )

  val input: StyleA = style(
    display.block,
    width(100%%),
    fontSize(14 pt),
    background := c"#f8f4e5",
    lineHeight(28 pt),
    marginBottom(28 pt),
    borderTop.none,
    borderLeft.none,
    borderRight.none,
    fontFamily(frijole),
    borderBottom(5 px, solid, rgb(117, 117, 117)),
    minWidth(250 px),
    paddingLeft(5 px),
    outline.none,
    color(rgb(117, 117, 117)),

    &.hover(
      borderBottom(5 px, solid, formShadowColor)
    ),
  )

}

package uz.english.style

import scalacss.internal.{FontFace, NonEmptyVector}
import uz.english.CssSettings.*

import scala.language.postfixOps

object AppStyle extends StyleSheet.Inline {
  import dsl._

  val ff = fontFace("Fjalla One")(
    _.src("url(font.woff2)")
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
    fontFamily(ff),
    borderBottom(5 px, solid, rgba(0,0,0,1)),
    minWidth(250 px),
    paddingLeft(5 px),
    outline.none,
    color(rgba(0,0,0,1)),

    &.hover(
      borderBottom(5 px, solid, formShadowColor)
    ),
  )

}

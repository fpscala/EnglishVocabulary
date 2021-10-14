package uz.english

import japgolly.scalajs.react.ScalaComponent.{BackendScope, Component}
import japgolly.scalajs.react.vdom.html_<^.*
import japgolly.scalajs.react.vdom.all.VdomTagOf
import japgolly.scalajs.react.{CtorType, ScalaComponent}
import org.scalajs.dom.document
import org.scalajs.dom.html.Div
import scalacss.ScalaCssReact.*
import uz.english.style.AppStyle
import uz.english.style.AppStyle.*
import uz.english.CssSettings._
import scalacss.toStyleSheetInlineJsOps


object Main extends App {
  case class State()
  type AppComponent = Component[Unit, State, Backend, CtorType.Nullary]

  class Backend($ : BackendScope[Unit, State]) {
    def createUserForm =
      <.div(box)(<.input(^.placeholder := "Name", input), <.input(^.placeholder := "Group", input))

    def render(implicit state: State): VdomTagOf[Div] =
      <.div(createUserForm)
  }

  val App: AppComponent =
    ScalaComponent
      .builder[Unit]
      .initialState(State())
      .renderBackend[Backend]
      .build

  AppStyle.addToDocument()
  App().renderIntoDOM(document.getElementById("app"))

}

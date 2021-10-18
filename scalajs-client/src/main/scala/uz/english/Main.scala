package uz.english

import japgolly.scalajs.react.ScalaComponent.{BackendScope, Component}
import japgolly.scalajs.react.callback.Callback$package.Callback
import japgolly.scalajs.react.facade.SyntheticEvent
import org.scalajs.dom.raw.{HTMLInputElement, HTMLTextAreaElement}
import japgolly.scalajs.react.vdom.html_<^.*
import japgolly.scalajs.react.vdom.all.VdomTagOf
import japgolly.scalajs.react.{CtorType, ScalaComponent}
import org.scalajs.dom.document
import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.HTMLInputElement
import scalacss.ScalaCssReact.*
import uz.english.style.AppStyle
import uz.english.style.AppStyle.*
import uz.english.CssSettings.*
import scalacss.toStyleSheetInlineJsOps


object Main extends App with AjaxImplicits {
  case class State(username: String = "")
  type AppComponent = Component[Unit, State, Backend, CtorType.Nullary]

  class Backend($ : BackendScope[Unit, State]) {

    def onClickCreate(implicit state: State): Callback =
      if (state.username.isEmpty)
        Callback.alert("Please enter your name!")
      else
        post[Username]("/user/create", Username(state.username))
          .fail(onError)
          .done[String] { result =>
            $.setState(State()) >> Callback.alert(result)
          }.asCallback

    def onUserNameChange(e: SyntheticEvent[HTMLInputElement]): Callback =
      $.modState(_.copy(username = e.target.value))


    def createUserForm(implicit state: State): VdomTagOf[Div] =
      <.div(box)(
        <.input(^.placeholder := "Name", ^.onChange ==> onUserNameChange, ^.value := state.username, input),
        <.button(button, ^.onClick --> onClickCreate)("Create")
      )

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

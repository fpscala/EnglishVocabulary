package uz.english

import io.circe.{Decoder, Encoder}
import japgolly.scalajs.react.callback.Callback$package.Callback
import japgolly.scalajs.react.extra.Ajax
import japgolly.scalajs.react.extra.Ajax.Step2
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.AjaxException
import uz.english.JsonUtils.{fromJson, toJsonString}

import scala.scalajs.js
import scala.scalajs.js.JSON

trait AjaxImplicits {
  case class Token(name: String, value: String)
  def onError(exception: AjaxException): Callback = exception match {
    case error if (error.xhr.status == 200 || error.xhr.status == 400) && error.xhr.responseText.nonEmpty =>
      Callback.alert(error.xhr.responseText)
    case _ =>
      Callback.alert("Something went wrong!")
  }

  def postCSRF[T: Encoder](url: String, body: T)(implicit token: Token): Step2 =
    Ajax("POST", url).setRequestContentTypeJsonUtf8.setRequestHeader(token.name, token.value).send(toJsonString(body))

  def post[T: Encoder](url: String, body: T): Step2 =
    Ajax("POST", url).setRequestContentTypeJsonUtf8.send(toJsonString(body))

  def get(url: String): Step2 = Ajax("GET", url).setRequestContentTypeJsonUtf8.send

  implicit class Step2Ops(val step2: Step2) {
    def isSuccessFull: XMLHttpRequest => Boolean = xhr =>
      xhr.status == 200 && xhr.getResponseHeader("Content-Type") == "application/json"

    final def fail(onFailure: AjaxException => Callback): Step2 =
      step2.onComplete(xhr => Callback.unless(isSuccessFull(xhr))(onFailure(AjaxException(xhr))))

    final def done[T: Decoder](onSuccess: T => Callback): Step2 = {
      step2.onComplete(xhr => Callback.when(isSuccessFull(xhr))(onSuccess(fromJson[T](xhr.responseText))))
    }
  }

}

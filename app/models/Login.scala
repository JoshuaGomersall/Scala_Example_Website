package models

import play.api.data._
import play.api.data.Forms._

import scala.collection.mutable.ArrayBuffer

case class Login(username: String, password: String){

}

object Login {
  val userList = ArrayBuffer[Login](
    Login("admin", "admin")
  )

  val form: Form[Login] = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
    )(Login.apply)(Login.unapply)
  )
}

package controllers

import java.io.File

import javax.inject.Inject
import models._
import play.api.mvc._
import Login._
import akka.util.ByteString
import play.api.data.Form
import play.api.data.Forms.{date, ignored, longNumber, mapping, optional}
import views.html
import javax.inject.Inject
import models._
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import views._


import scala.concurrent.ExecutionContext
import scala.io.Source

/**
  * Manage a database of players
  */
class LoginController @Inject()(playerService: playerRepository,
                                gamestatusService: gamestatusRepository,
                                cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  val Home = Redirect(routes.HomeController.list(0, 2, ""))
  val loggedIn = false
  private val logger = play.api.Logger(this.getClass)

  val playerForm = Form(
    mapping(
      "id" -> ignored(None: Option[Long]),
      "name" -> nonEmptyText,
      "introduced" -> optional(longNumber),
      "discontinued" -> optional(longNumber),
      "gamestatus" -> optional(longNumber)
    )(player.apply)(player.unapply)
  )

  def login = Action.async { implicit request =>
    playerForm.bindFromRequest.fold(
      formWithErrors => gamestatusService.options.map { options =>
        BadRequest(html.login(formWithErrors, options))
      },
      player => {
        playerService.insert(player).map { _ =>
          Home.flashing("success" -> "User Has Loggedin".format(player.name))
        }
      }
    )
  }


  def postlogin() = Action { implicit request =>
    if (true) {
      Redirect("/")
    }
    else {
      Redirect("/login")
    }
  }

}

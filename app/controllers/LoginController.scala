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
  * Manage a database of computers
  */
class LoginController @Inject()(computerService: ComputerRepository,
                                companyService: CompanyRepository,
                                cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  val Home = Redirect(routes.HomeController.list(0, 2, ""))
  val loggedIn = false
  private val logger = play.api.Logger(this.getClass)

  val computerForm = Form(
    mapping(
      "id" -> ignored(None: Option[Long]),
      "name" -> nonEmptyText,
      "introduced" -> optional(longNumber),
      "discontinued" -> optional(longNumber),
      "company" -> optional(longNumber)
    )(Computer.apply)(Computer.unapply)
  )

  def login = Action.async { implicit request =>
    computerForm.bindFromRequest.fold(
      formWithErrors => companyService.options.map { options =>
        BadRequest(html.login(formWithErrors, options))
      },
      computer => {
        computerService.insert(computer).map { _ =>
          Home.flashing("success" -> "User Has Loggedin".format(computer.name))
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

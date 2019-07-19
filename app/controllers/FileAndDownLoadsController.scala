package controllers

import javax.inject.Inject
import models._
import play.api.data.Form
import play.api.data.Forms.{date, ignored, longNumber, mapping, optional, _}
import play.api.mvc._
import views.html

import scala.concurrent.ExecutionContext

/**
  * Manage a database of computers
  */
class FileAndDownLoadsController @Inject()(computerService: ComputerRepository,
                                           companyService: CompanyRepository,
                                           cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {




  def basicDownload() = Action { implicit request =>
      Redirect("/downloadz")
  }
}

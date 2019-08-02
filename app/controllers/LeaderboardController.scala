package controllers

import javax.inject.Inject
import models._
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import views._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Manage a database of players
  */
class LeaderboardController @Inject()(playerService: playerRepository,
                               gamestatusService: gamestatusRepository,
                               cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def leaderboard(page: Int, orderBy: Int, filter: String) = Action.async { implicit request =>
    playerService.list(page = page, orderBy = orderBy, filter = ("%" + filter + "%")).map { page =>
      Ok(html.manageplayers(page, orderBy, filter,false))
    }
  }
}

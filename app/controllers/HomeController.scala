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
class HomeController @Inject()(playerService: playerRepository,
                               gamestatusService: gamestatusRepository,
                               cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  private val logger = play.api.Logger(this.getClass)

  /**
    * This result directly redirect to the application home.
    */
  val Home = Redirect(routes.HomeController.list(0, 2, ""))

  val loggedIn = true

  /**
    * Describe the player form (used in both edit and create screens).
    */
  val playerForm = Form(
    mapping(
      "id" -> ignored(None: Option[Long]),
      "name" -> nonEmptyText,
      "win" -> optional(longNumber (min = 0)),
      "loss" -> optional(longNumber (min = 0)),
      "gamestatus" -> optional(longNumber)
    )(player.apply)(player.unapply)
  )

  // -- Actions
  //    Default path requests, redirect to players list
  def index = Action {
    if (loggedIn) {
      Home
    }
    else {
      Redirect("/")
    }
  }

    /**
    * Display the paginated list of players.
    *
    * @param page    Current page number (starts from 0)
    * @param orderBy Column to be sorted
    * @param filter  Filter applied on player names
    */
  def list(page: Int, orderBy: Int, filter: String) = Action.async { implicit request =>
    playerService.list(page = page, orderBy = orderBy, filter = ("%" + filter + "%")).map { page =>
      Ok(html.list(page, orderBy, filter))
    }
  }

  /**
    * Display the 'edit form' of a existing player.
    *
    * @param id Id of the player to edit
    */
  def edit(id: Long) = Action.async { implicit request =>
    playerService.findById(id).flatMap {
      case Some(player) =>
        gamestatusService.options.map { options =>
          Ok(html.editForm(id, playerForm.fill(player), options))
        }
      case other =>
        Future.successful(NotFound)
    }
  }

  /**
    * Handle the 'edit form' submission
    *
    * @param id Id of the player to edit
    */
  def update(id: Long) = Action.async { implicit request =>
    playerForm.bindFromRequest.fold(
      formWithErrors => {
        logger.warn(s"form error: $formWithErrors")
        gamestatusService.options.map { options =>
          BadRequest(html.editForm(id, formWithErrors, options))
        }
      },
      player => {
        playerService.update(id, player).map { _ =>
          Home.flashing("success" -> "Player %s has been updated".format(player.name))
        }
      }
    )
  }

  /**
    * Display the 'new player form'.
    */
  def create = Action.async { implicit request =>
    gamestatusService.options.map { options =>
      Ok(html.createForm(playerForm, options))
    }
  }

  /**
    * Handle the 'new player form' submission.
    */
  def save = Action.async { implicit request =>
    playerForm.bindFromRequest.fold(
      formWithErrors => gamestatusService.options.map { options =>
        BadRequest(html.createForm(formWithErrors, options))
      },
      player => {
        playerService.insert(player).map { _ =>
          Home.flashing("success" -> "Player %s has been created".format(player.name))
        }
      }
    )
  }

  /**
    * Handle player deletion.
    */
  def delete(id: Long) = Action.async {
    playerService.delete(id).map { _ =>
      Home.flashing("success" -> "Player has been deleted")
    }
  }

}

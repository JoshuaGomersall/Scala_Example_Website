package models

import java.util.Date
import javax.inject.Inject

import anorm.SqlParser.{ get, scalar }
import anorm._
import play.api.db.DBApi

import scala.concurrent.Future

case class player(id: Option[Long] = None,
                    name: String,
                    win: Option[Long],
                    loss: Option[Long],
                  gamestatusId: Option[Long])

object player {
  implicit def toParameters: ToParameterList[player] =
    Macro.toParameters[player]
}

/**
 * Helper for pagination.
 */
case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}

@javax.inject.Singleton
class playerRepository @Inject()(dbapi: DBApi, gamestatusRepository: gamestatusRepository)(implicit ec: DatabaseExecutionContext) {

  private val db = dbapi.database("default")

  // -- Parsers

  /**
   * Parse a player from a ResultSet
   */
  private val simple = {
    get[Option[Long]]("player.id") ~
      get[String]("player.name") ~
      get[Option[Long]]("player.win") ~
      get[Option[Long]]("player.loss") ~
      get[Option[Long]]("player.gamestatus_id") map {
      case id ~ name ~ win ~ loss ~ gamestatusId =>
        player(id, name, win, loss, gamestatusId)
    }
  }

  /**
   * Parse a (player,gamestatus) from a ResultSet
   */
  private val withgamestatus = simple ~ (gamestatusRepository.simple.?) map {
    case player ~ gamestatus => player -> gamestatus
  }

  // -- Queries

  /**
   * Retrieve a player from the id.
   */
  def findById(id: Long): Future[Option[player]] = Future {
    db.withConnection { implicit connection =>
      SQL"select * from player where id = $id".as(simple.singleOpt)
    }
  }(ec)

  /**
   * Return a page of (player,gamestatus).
   *
   * @param page Page to display
   * @param pageSize Number of players per page
   * @param orderBy player property used for sorting
   * @param filter Filter applied on the name column
   */
  def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Future[Page[(player, Option[gamestatus])]] = Future {

    val offset = pageSize * page

    db.withConnection { implicit connection =>

      val players = SQL"""
        select * from player
        left join gamestatus on player.gamestatus_id = gamestatus.id
        where player.name like ${filter}
        order by ${orderBy} nulls last
        limit ${pageSize} offset ${offset}
      """.as(withgamestatus.*)

      val totalRows = SQL"""
        select count(*) from player
        left join gamestatus on player.gamestatus_id = gamestatus.id
        where player.name like ${filter}
      """.as(scalar[Long].single)

      Page(players, page, offset, totalRows)
    }
  }(ec)

  /**
   * Update a player.
   *
   * @param id The player id
   * @param player The player values.
   */
  def update(id: Long, player: player) = Future {
    db.withConnection { implicit connection =>
      SQL("""
        update player set name = {name}, win = {win},
          loss = {loss}, gamestatus_id = {gamestatusId}
        where id = {id}
      """).bind(player.copy(id = Some(id)/* ensure */)).executeUpdate()
      // case class binding using ToParameterList,
      // note using SQL(..) but not SQL.. interpolation
    }
  }(ec)

  /**
   * Insert a new player.
   *
   * @param player The player values.
   */
  def insert(player: player): Future[Option[Long]] = Future {
    db.withConnection { implicit connection =>
      SQL("""
        insert into player values (
          (select next value for player_seq),
          {name}, {win}, {loss}, {gamestatusId}
        )
      """).bind(player).executeInsert()
    }
  }(ec)

  /**
   * Delete a player.
   *
   * @param id Id of the player to delete.
   */
  def delete(id: Long) = Future {
    db.withConnection { implicit connection =>
      SQL"delete from player where id = ${id}".executeUpdate()
    }
  }(ec)

}

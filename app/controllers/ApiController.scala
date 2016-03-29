package controllers

import models.User
import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits._
import services.UserService

class ApiController extends Controller {

  implicit val writesItem = Writes[User] {
    case User(id, firstName, lastName, mobile, email) =>
      Json.obj(
        "id" -> id,
        "firstName" -> firstName,
        "lastName" -> lastName,
        "email" -> email,
        "mobile" -> mobile
      )
  }

  def getUser(id: Long) = Action.async { implicit request =>
    UserService.getUser(id).map {
      case None => NotFound(Json.obj("error" -> "Not Found"))
      case Some(user) => Ok(Json.toJson(user))
    }
  }
}

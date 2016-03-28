package controllers

import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits._
import services.UserService

class ApiController extends Controller {
  def getUser(id: Long) = Action.async { implicit request =>
    UserService.getUser(id).map {
      case None => NotFound(Json.obj("error" -> "Not Found"))
      case Some(user) => Ok(Json.obj(
        "firstname" -> user.firstName,
        "email" -> user.email
      ))
    }
  }
}

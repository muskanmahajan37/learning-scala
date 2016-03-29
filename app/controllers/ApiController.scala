package controllers

import models.{User, UserFormData}
import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{Reads, __}
import play.api.libs.functional.syntax._
import services.UserService

class ApiController extends Controller {

  implicit val readsUser: Reads[UserFormData] = (
      (__ \ "firstName").read[String] ~
      (__ \ "lastName").read[String] ~
      (__ \ "mobile").read[Long] ~
      (__ \ "email").read[String]
    ) (UserFormData.apply _)

  implicit val writeUser = Writes[User] {
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

  def createUser = Action(parse.json) { implicit request =>
    println(request.body)
    request.body.validate[UserFormData] match {
      case JsSuccess(user, _) =>
        val newUser = User(0, user.firstName, user.lastName, user.mobile, user.email)
        UserService.addUser(newUser).map(res =>
          println(res)
        )
        Ok("")
      case JsError(errors) =>
        println(errors)
        BadRequest
    }
  }

}

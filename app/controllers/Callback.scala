package controllers

import play.api.mvc._
import play.api.libs.json.JsValue

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import helpers.Auth0Config
import play.api.Configuration
import play.api.http.{HeaderNames, MimeTypes}
import play.api.libs.json._
import play.api.libs.ws._
import play.api.cache._

class Callback @Inject() (
    ws: WSClient,
    cc: ControllerComponents,
    cache: SyncCacheApi,
    config: Configuration
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  def callback(codeOpt: Option[String] = None) = Action.async {
    (for {
      code <- codeOpt
    } yield {
      getToken(code)
        .flatMap { case (idToken, accessToken) =>
          getUser(accessToken).map { user =>
            cache.set(idToken + "profile", user)
            Redirect(routes.User.index())
              .withSession(
                "idToken" -> idToken,
                "accessToken" -> accessToken
              )
          }

        }
        .recover { case ex: IllegalStateException =>
          Unauthorized(ex.getMessage)
        }
    }).getOrElse(Future.successful(BadRequest("No parameters supplied")))
  }

  def getToken(code: String): Future[(String, String)] = {
    val auth0Config = new Auth0Config(config).get()
    val tokenResponse = ws
      .url(String.format("https://%s/oauth/token", auth0Config.domain))
      .withHttpHeaders(HeaderNames.ACCEPT -> MimeTypes.JSON)
      .post(
        Json.obj(
          "client_id" -> auth0Config.clientId,
          "client_secret" -> auth0Config.secret,
          "redirect_uri" -> auth0Config.callbackURL,
          "code" -> code,
          "grant_type" -> "authorization_code"
        )
      )

    tokenResponse.flatMap { response =>
      (for {
        idToken <- (response.json \ "id_token").asOpt[String]
        accessToken <- (response.json \ "access_token").asOpt[String]
      } yield {
        Future.successful((idToken, accessToken))
      }).getOrElse(
        Future.failed[(String, String)](
          new IllegalStateException("Tokens not sent")
        )
      )
    }

  }

  def getUser(accessToken: String): Future[JsValue] = {
    val auth0Config = new Auth0Config(config).get()
    val userResponse = ws
      .url(String.format("https://%s/userinfo", auth0Config.domain))
      .addQueryStringParameters("access_token" -> accessToken)
      .get()

    userResponse.flatMap(response => Future.successful(response.json))
  }
}

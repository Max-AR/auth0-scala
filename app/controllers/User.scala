package controllers

import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.cache._

import javax.inject.Inject

class User @Inject() (cc: ControllerComponents, cache: SyncCacheApi)
    extends AbstractController(cc) {
  def AuthenticatedAction(
      f: Request[AnyContent] => Result
  ): Action[AnyContent] = {
    Action { request =>
      (request.session.get("idToken").flatMap { idToken =>
        cache.get[JsValue](idToken + "profile")
      } map { profile =>
        f(request)
      }).orElse {
        Some(Redirect(routes.AuthPage.index()))
      }.get
    }
  }

  def index = AuthenticatedAction { request =>
    val idToken = request.session.get("idToken").get
    val profile = cache.get[JsValue](idToken + "profile").get
    Ok(views.html.user(profile))
  }
}

package controllers

import helpers.Auth0Config
import play.api.Configuration
import play.api.mvc._

import javax.inject.Inject

class AuthPage @Inject() (cc: ControllerComponents, config: Configuration)
    extends AbstractController(cc) {

  def index: Action[AnyContent] = Action {
    val auth0Config = new Auth0Config(config)
    Ok(views.html.index(auth0Config.get()))
  }
}

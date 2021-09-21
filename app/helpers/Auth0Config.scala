package helpers

import play.api.Configuration

import javax.inject.Inject

case class Auth0ConfigStruct(
                              secret: String,
                              clientId: String,
                              callbackURL: String,
                              domain: String
                            )

class Auth0Config @Inject() (config: Configuration) {
  def get(): Auth0ConfigStruct = Auth0ConfigStruct(
    config.get[String]("auth0.clientSecret"),
    config.get[String]("auth0.clientId"),
    config.get[String]("auth0.callbackURL"),
    config.get[String]("auth0.domain")
  )
}

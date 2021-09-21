# Auth0 - Scala

I have updated this repo from [the original auth0 example](https://github.com/auth0/auth0-scala) to work with play 2.8.

I have tested on scala 2.13.6 and JDK 11.

## Running the example

Create an auth0 account and create a new "Regular web application". Go to the settings tab and set:  

- `Allowed Callback URLs` - `http://127.0.0.1:9000/callback`
- `Allowed web origins` - `http://127.0.0.1:9000`
- `Allowed origins (CORS)` - `http://127.0.0.1:9000`

Save your settings.

Get the `domain`, `Client ID` and `Client Secret` from the settings page and them to your `.env` file.

Run `sbt run` to start the app and try calling [http://127.0.0.1:9000/](http://127.0.0.1:9000/)


## Issue Reporting

If you have found a bug or if you have a feature request, please report them at this repository issues section.
Please do not report security vulnerabilities on the public GitHub issue tracker.
The [Responsible Disclosure Program](https://auth0.com/whitehat) details the procedure for disclosing security issues.


## Authors

[Auth0](auth0.com)

[Max Rosenbaum](https://www.rtechservices.io)

## License

This project is licensed under the MIT license. See the [LICENSE](LICENSE) file for more info.

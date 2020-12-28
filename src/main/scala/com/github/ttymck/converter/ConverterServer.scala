package com.github.ttymck.converter

import cats.effect.{ContextShift, IO, Timer}
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.global

object ConverterServer {

  implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  def stream(implicit T: Timer[IO]): Stream[IO, Nothing] = {
    val service = ConverterService.impl
    // Combine Service Routes into an HttpApp.
    // Can also be done via a Router if you
    // want to extract a segments not checked
    // in the underlying routes.
    val httpApp = ConverterRoutes.routes(service).orNotFound

    // With Middlewares in place
    val finalHttpApp = Logger.httpApp(true, true)(httpApp)
    for {
      exitCode <- BlazeServerBuilder[IO](global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}

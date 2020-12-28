package com.github.ttymck.converter

import cats.data.EitherT
import cats.effect.IO
import io.circe._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher

object ConverterRoutes {

  private object CountryQueryParamMatcher extends QueryParamDecoderMatcher[String]("units")

  private final case class Result(unitName: String, private val factor: Double) {
    val multiplicationFactor: String = String.format("%.14G", factor)
  }

  private implicit val resultEncoder: Encoder[Result] = Encoder.instance[Result] { result =>
    Json.obj(
      ("unit_name", result.unitName.asJson),
      ("multiplication_factor", result.multiplicationFactor.asJson)
    )
  }

  def routes(converterService: ConverterService[IO]): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO]{}
    import dsl._
    HttpRoutes.of[IO] {
      case GET -> Root / "units" / "si" :? CountryQueryParamMatcher(units) =>
        println(units)
        EitherT(converterService.parse(units))
          .map(dim => Result(dim.symbol, dim.factor))
          .biSemiflatMap(
            err => BadRequest(err.getMessage),
            result => Ok(result.asJson),
          ).merge
    }
  }
}

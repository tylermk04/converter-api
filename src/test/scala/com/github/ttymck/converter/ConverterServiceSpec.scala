package com.github.ttymck.converter

import cats.effect.IO
import io.circe._
import org.http4s._
import org.http4s.circe._
import org.http4s.implicits._

class ConverterServiceSpec extends org.specs2.mutable.Specification {

  "ConverterService" >> {
    "convert the example" >> {
      conversionTest("(degree/minute)", "(rad/s)",.00029088820866572)
    }
  }.append(
    testCases.map { case (name, input, expUnits, expFactor) =>
      name >> {
        conversionTest(input, expUnits, expFactor)
      }
    }
  )

  private val testCases = Seq(
    ("convert hectares/day", "(ha/day)", "(m²/s)", 0.11574074074074),
    ("convert L*t/(arcminute*h)", "L*t/('*h)", "m³*kg/(rad*s)", 0.95492965855137),
    ("respect superfluous parentheses", "(((L*min)))/((min*(min)*arcsecond))", "(((m³*s)))/((s*(s)*rad))", 3437746.7707849)
  )

  private def conversionTest(input: String, expectedUnits: String, expectedFactor: Double) = {
    val resp: Json = getConversion(input).as[Json].unsafeRunSync()
    resp.hcursor.get[String]("unit_name").get must beEqualTo (expectedUnits)
    resp.hcursor.get[Double]("multiplication_factor").get must beEqualTo (expectedFactor)
  }


  private[this] def getConversion(units: String): Response[IO] = {
    val route = Request[IO](Method.GET, Uri.fromString(s"/units/si?units=$units").getOrElse(throw new Exception("bad uri")))
    val api = ConverterService.impl
    ConverterRoutes
      .routes(api)
      .orNotFound(route)
      .unsafeRunSync()
  }

  implicit class EitherUnwrapOps[A](either: Either[_, A]) {
    def get: A = either.toOption.get
  }
}

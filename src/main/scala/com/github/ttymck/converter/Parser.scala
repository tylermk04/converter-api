package com.github.ttymck.converter

import cats.data.NonEmptyList
import com.github.ttymck.converter.Dimension._

import scala.util.parsing.combinator.RegexParsers

object Parser extends RegexParsers {
  case class ParseException(private val message: String) extends Exception {
    override def getMessage: String = s"Could not parse units: $message"
  }

  override def skipWhitespace = true

  def parse(input: String): Either[Exception, Dimension] = {
    parseAll(term, input) match {
      case Success(result, _) =>
        Right(result)
      case Failure(msg, _) =>
        Left(ParseException(msg))
      case Error(msg, _) =>
        Left(ParseException(msg))
    }
  }

  private def term: Parser[Dimension] = {
     mul | div | factor
  }

  private def div: Parser[Quotient] =
    (factor<~"/")~term ^^ { case l~r => Quotient(l, r) }

  private def mul: Parser[Dimension] =
    (factor<~"*")~term ^^ { case l~r => Product(NonEmptyList.of(l, r)) }

  private def factor: Parser[Dimension] =
    singleUnit | parens

  private def parens: Parser[Dimension]  =
    "(" ~> term <~ ")" ^^ { dim => Parens(dim) }

  private def singleUnit: Parser[Dimension] = {
    UnitRegex.^?(
      Unit.parse.andThen(SingleUnit.apply _),
      unmatched => s"$unmatched is not a supported unit"
    )
  }

  private lazy val UnitRegex = "[a-zA-Z\\-²³\'\"]+".r
}


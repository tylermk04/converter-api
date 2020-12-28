package com.github.ttymck.converter

import cats.data.NonEmptyList

sealed trait Dimension {
  def factor: Double
  def symbol: String
}

object Dimension {
  final case class SingleUnit(unit: Unit) extends Dimension {
    override def factor: Double = unit.siFactor

    override def symbol: String = unit.siUnit.symbol
  }
  final case class Parens(dimension: Dimension) extends Dimension {
    override def factor: Double = dimension.factor

    override def symbol: String = "(" + dimension.symbol + ")"
  }
  final case class Product(units: NonEmptyList[Dimension]) extends Dimension {
    override def factor: Double = units.foldLeft(1d)(_ / _.factor)

    override def symbol: String = units.map(_.symbol).toList.mkString("*")
  }
  final case class Quotient(numerator: Dimension, denominator: Dimension) extends Dimension {
    override def factor: Double = numerator.factor / denominator.factor

    override def symbol: String = numerator.symbol + "/" + denominator.symbol
  }
}

package com.github.ttymck.converter

import com.github.ttymck.converter.Unit.SiUnit

sealed trait Unit {
  def symbol: String
  def siFactor: Double

  def siUnit: SiUnit
}

object Unit {
  def parse: PartialFunction[String, Unit] = {
    case "second" | "s" => Second
    case "minute" | "min" => Minute
    case "hour" | "h" => Hour
    case "day" | "d" => Day
    case "radians" | "rad" => Radians
    case "degree" | "°" => Degree
    case "arcminute" | "'" => ArcMinute
    case "arcsecond" | "\"" => ArcSecond
    case "square-meter" | "m²" => SquareMeter
    case "hectare" | "ha" => Hectare
    case "cubic-meter" | "m³" => CubicMeter
    case "litre" | "L" => Litre
    case "kilogram" | "kg" => Kilogram
    case "tonne" | "t" => Tonne
  }

  sealed trait SiUnit extends Unit {
    override def siFactor: Double = 1d
  }

  sealed trait TimeUnit extends Unit {
    final def siUnit: SiUnit = Second
  }
  case object Second extends TimeUnit with SiUnit {
    def symbol: String = "s"
  }
  case object Minute extends TimeUnit {
    def symbol: String = "min"
    def siFactor: Double = 60.0d
  }
  case object Hour extends TimeUnit {
    def symbol: String = "h"
    def siFactor: Double = 3600d
  }
  case object Day extends TimeUnit {
    override def symbol: String = "d"

    override def siFactor: Double = 86400d
  }

  sealed trait PlaneAngle extends Unit {
    final def siUnit: SiUnit = Radians
  }
  case object Radians extends PlaneAngle with SiUnit {
    def symbol: String = "rad"
  }
  case object Degree extends PlaneAngle {
    override def symbol: String = "°"

    override def siFactor: Double = (math.Pi / 180d)
  }
  case object ArcMinute extends PlaneAngle {
    override def symbol: String = "'"

    override def siFactor: Double = (math.Pi / 10800d)
  }
  case object ArcSecond extends PlaneAngle {
    override def symbol: String = "\""

    override def siFactor: Double = (math.Pi / 648000d)
  }

  sealed trait Area extends Unit {
    final def siUnit: SiUnit = SquareMeter
  }
  case object SquareMeter extends Area with SiUnit {
    def symbol: String = "m²"
  }
  case object Hectare extends Area {
    override def symbol: String = "ha"

    override def siFactor: Double = 10000d
  }

  sealed trait Volume extends Unit {
    final def siUnit: SiUnit = CubicMeter
  }
  case object CubicMeter extends Volume with SiUnit {
    def symbol: String = "m³"
  }
  case object Litre extends Volume {
    override def symbol: String = "L"

    override def siFactor: Double = 0.001d
  }

  sealed trait Mass extends Unit {
    final def siUnit: SiUnit = Kilogram
  }
  case object Kilogram extends Mass with SiUnit {
    def symbol: String = "kg"
  }
  case object Tonne extends Mass {
    override def symbol: String = "t"

    override def siFactor: Double = 1000d
  }
}

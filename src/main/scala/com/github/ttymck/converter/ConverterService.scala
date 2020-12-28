package com.github.ttymck.converter

import cats.effect.IO

trait ConverterService[F[_]]{
  def parse(n: String): F[Either[Exception, Dimension]]
}

object ConverterService {
  implicit def apply[F[_]](implicit ev: ConverterService[F]): ConverterService[F] = ev

  def impl: ConverterService[IO] = new ConverterService[IO]{
    def parse(input: String): IO[Either[Exception, Dimension]] =
      IO.pure(Parser.parse(input))
  }
}

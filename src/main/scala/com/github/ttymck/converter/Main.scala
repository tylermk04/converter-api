package com.github.ttymck.converter

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    ConverterServer.stream.compile.drain.as(ExitCode.Success)
}

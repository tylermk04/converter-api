FROM mozilla/sbt

COPY ./project ./project
COPY build.sbt ./build.sbt
COPY ./src ./src

RUN sbt dist

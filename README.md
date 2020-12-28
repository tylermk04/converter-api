# converter

This project is built with sbt.

To run with docker, run 

```sbt docker:publishLocal```

The image should be tagged `converter:0.0.1-SNAPSHOT`, you can verify this in the sbt output.

Then run:

```docker run -p"8080:8080" -it converter:0.0.1-SNAPSHOT```

And you can query the API like so (i.e. web browser or any other `GET` client):

```http://localhost:8080/units/si?units=(min*(ha/L))```

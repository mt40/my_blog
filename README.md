This blog is a static site implemented entirely using Scala.

## Build

You can build this site using [SBT][sbt] by running:
```bash
sbt build
```

This will generate folder `build`. The final site contains the folder `build`
and the file `index.html` at the root of this repo
(let's call this **the artifacts**)

## Run

To run, you can simply serve `index.html` as a static site.

A simple way to do that is to use [Docker][docker] and the config already
 written in `./Dockerfile` and `./docker-compose.yml`.
 
- First, you need to install docker by following the
[install guide][docker_install]
- Then go to the root of this directory and run:
```bash
docker-compose up
```
- now you should be able to see the site by going to `localhost:3000` in your
 browser

## Development flow

- Use [IntelliJ] as an IDE for coding.
- Use SBT as your build tool. In your terminal, run `sbt` to start sbt console
 and run `~build` to watch for code changes and rebuild automatically.
- Start docker container to serve the site.
- Now, if you change the code or `./index.html`, SBT will rebuild and then
 Docker will catch the changes in the artifacts. Hence, if you refresh the
 site in your browser, you can see the changes.

## FAQ

### Docker references (syntax, concept)?
Visit https://docs.docker.com/engine/reference/builder/

### Changes in `Dockerfile` is not applied?
Force rebuilding the image


[sbt]: https://www.scala-sbt.org/download.html
[docker]: https://www.docker.com/
[docker_install]: https://docs.docker.com/install/
[intellij]: https://www.jetbrains.com/idea/
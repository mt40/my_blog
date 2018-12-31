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

To run, you can simply serve `index.html` as a static site with a simple
http server like [NPM http-server][npm_hs]

## Development flow

- Use [IntelliJ] as an IDE for coding.
- Use SBT as your build tool. In your terminal, run `sbt` to start sbt console
 and run `~build` to watch for code changes and rebuild automatically.
- Assuming you are using [http-server][npm_hs], serve the site with
`http-server -p 3000 .`.
- Type `localhost:3000` in your browser to see the site.
- Now, if you change the code or `./index.html`, SBT will rebuild the site.
After refresh, you should be able to see the updated site in your browser.


[sbt]: https://www.scala-sbt.org/download.html
[npm_hs]: https://www.npmjs.com/package/http-server
[intellij]: https://www.jetbrains.com/idea/

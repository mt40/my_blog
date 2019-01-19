This blog is a static site implemented entirely using Scala.

## Build

Scala code and Sass scripts can be built with:
```bash
npm run build_production
# or
npm run build_dev
```

This will generate folder `build`. The final site contains the folder `build`
and the file `index.html` at the root of this repo
(let's call this **the artifacts**)

This is done by [Grunt][grunt] and build definitions are in `Gruntfile.js`.

Notes:
- We don't use [sbt][sbt] to build because it is too heavy, especially on laptops which doesn't have good specs.
- It is important that packages in `package.json` are declared in
 `dependencies` section because Heroku will remove packages in
  `devDependencies` before our build process completes.

## Run

To run, you can simply serve `index.html` as a static site with a simple
http server like [NPM http-server][npm_hs]

## Development flow

- Use [IntelliJ] as an IDE for coding.
- Build the project.
- Assuming you are using [http-server][npm_hs], serve the site with
`http-server -p 3000 .`.
- Type `localhost:3000` in your browser to see the site.
After refresh, you should be able to see the updated site in your browser.


[grunt]: https://gruntjs.com/
[sbt]: https://www.scala-sbt.org/download.html
[npm_hs]: https://www.npmjs.com/package/http-server
[intellij]: https://www.jetbrains.com/idea/

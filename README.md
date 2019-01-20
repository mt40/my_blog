[![Build Status](https://travis-ci.org/mt40/my_blog.svg?branch=master)](https://travis-ci.org/mt40/my_blog)
![github_issue](https://img.shields.io/github/issues/mt40/my_blog.svg)
[![Heroku](https://heroku-badge.herokuapp.com/?app=under-the-rain&style=flat&svg=1)]()

This blog is a static site implemented entirely using Scala.

## Build

Scala code and Sass scripts can be built with:
```bash
npm run build_production
# or
npm run build_dev
```

This will generate folder `build`. The site only needs this folder and the
html files at project root to run.

Notes:
- This build is run by [Grunt][grunt] and build definitions are in
`Gruntfile.js`.
- We don't use [sbt][sbt] to build because it is too heavy, especially on laptops which doesn't have good specs.
- It is important that packages in `package.json` are declared in
 `dependencies` section because Heroku will remove packages in
  `devDependencies` before our build process completes.

## Run

To run, you can simply serve `index.html` as a static site with a simple
http server like [NPM http-server][npm_hs]

## Deploy

Steps:
- push/merge/rebase to branch `master`
- [Travis CI][travis] will automatically build and push the result to branch
`deploy`.
- [Heroku][heroku] will automatically pull from this branch and deploy the
app on its server.

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
[travis]: https://travis-ci.org/
[heroku]: https://dashboard.heroku.com/

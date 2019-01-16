![Scala logo][logo]

Scala is a programming language released in 2004 by Martin Odersky. It provides support for functional programming and is designed to be concise and compiled to Java bytecode so that a Scala application can be executed on a Java Virtual Machine (JVM).

Let’s check at the core features of the language.

## Hello World

First, let’s see how to implement a hello world in Scala:
```scala
package io.teivah.helloworld

object HelloWorld {
  def main(args: Array[String]) {
    println("Hello, World!")
  }
}
```

We defined a `HelloWorld` object containing a `main` method. This method takes an array of `String` as an input.

In the `main`, we called the method `println` which takes an object as an input to print something in the console.

Meanwhile, `HelloWorld` is also part of the `io.teivah.helloworld` package.

Read more on [Hacker Noon][more].

[logo]: scala.jpg
[more]: https://hackernoon.com/a-10-minute-introduction-to-scala-d1fed19eb74c
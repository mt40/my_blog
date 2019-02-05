![scala][scala]

If you are looking for a new programming language to learn this year, [Scala][scala_lang] is a good choice. Here are 5 reasons why:

## Flexibility

If it is 10 years ago, I would never recommend Scala to anyone. Because a programming language can be very fast, elegant, and feature rich but is still not good to learn if nobody else is using it. However, that is not the case anymore because Scala has been growing quickly and is now used in many areas such as:

- Web frontend with [ScalaJS][scalajs] and [ScalaJS React][react]
- Web server with [Play][play], [Lift][lift], and [Akka HTTP][akka]
- Android app with [Scala on Android][android]
- Data processing with [Spark][spark] and [Kafka][kafka]

There is even an [open-source chess server][chess] written in Scala.

## Elegance

Having used C++, C#, Java, and Python, I can say that Scala has a very concise and expressive syntax.

For example, to print "hello world" on console:

```scala
println("hello world")
```

To declare a value (or variable in Java):
```scala
val i: Int = 6
val j = 9 // automatically inferred as `Int`
```

To declare a method:
```scala
def add(a: Int, b: Int) = a + b
```

To remove all odd numbers in a list:
```scala
val list = List(1, 2, 3)
list.filter(x => isEven(x)) // result: List(2)
// or more like English
list filter isEven // result: List(2)
```

You can omit punctuations to have a more English-like syntax for extra expressiveness, this is called [infix notation][infix].

## Rich API

Scala built-in library comes with an extremely rich API. Filtering a list in the code snippet above is a very typical example.

`List` API also contains many other methods. For example, to loop through each item in a list:
```scala
list.foreach(println)
```

To sum a list of numbers:
```scala
list.sum // result: 6
```
It works as long as the list contains numbers (e.g. integers, doubles...)

![it_just_works][it_just_works]

Computing the intersection of 2 lists is also simple:
```scala
val l1 = List(1, 2, 3)
val l2 = List(2, 3, 4)
l1.intersect(l2) // result: List(2, 3)
```

## Great community & libraries

Yes, indeed. Compared to NodeJS or Python, Scala has a much smaller community and far fewer libraries (not counting Java ones). But it is not really bad because:

- Most of the time, we only use a few top libraries and those in Scala have high quality to make up for quantity
- People are very active and willing to help each other. Especially Gitter channels like [Spark with Scala][spark_gitter] and [typelevel/cats][cat]

## Java Interoperability

Simply said, **Java methods** can be called and **Java classes** can be extended in Scala code. This means you have access to all Java libraries out there.

## Conclusion

I was once questioning why should I use a language that looks so complicated. But after I understand Scala and realize how my life becomes much more easier, I'm in love with it. If the 5 reasons above are interesting to you,  make sure to check out [a short tour of Scala][tour].


If you have any question or feedback, please drop a comment.
 
[scala]: scala_logo.jpg
[scala_lang]: https://www.scala-lang.org/
[scalajs]: https://www.scala-js.org/
[react]: https://github.com/japgolly/scalajs-react
[play]: https://github.com/playframework/playframework
[lift]: https://github.com/lift/framework
[akka]: https://github.com/akka/akka-http
[android]: https://github.com/scala-android/sbt-android
[spark]: https://github.com/apache/spark
[kafka]: https://github.com/apache/kafka
[chess]: https://github.com/ornicar/lila
[it_just_works]: it_just_works.png
[infix]: https://docs.scala-lang.org/style/method-invocation.html
[spark_gitter]: https://gitter.im/spark-scala/Lobby
[cat]: https://gitter.im/typelevel/cats
[tour]: https://docs.scala-lang.org/tour/tour-of-scala.html
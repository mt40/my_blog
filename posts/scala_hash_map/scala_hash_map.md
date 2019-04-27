![hash_map_img][hash_map_img]

Implementing a [Hash Map][hash_map] is a good way to get familiar with a new language, let's see how it can be done in Scala. This article assumes that you are familiar with the data structure. If not, Hacker Earth has a really [nice article][hash_map_guide] to get you started.

Now, for this implementation, there are a few things I want to achieve:

- use [chaining][chaining] to resolve collisions
- support 3 operations: `add`, `remove`, and `get`
- immutability (all modifications return a new map instead of changing the existing one)

With those requirements in mind, let's write a short outline:

```scala
case class Entry[K, V](key: K, value: V)

class HashMap[K, V] private (entries: Vector[Vector[Entry[K, V]]]) {

  def add(key: K, value: V): HashMap[K, V] = ???

  def remove(key: K): HashMap[K, V] = ???

  def get(key: K): Option[V] = ???
}
```

- Class `Entry` represents a key-value pair
- I use `Vector` to hold the keys. Each key contains another `Vector` that holds all the values. Hence we have `Vector[Vector[Entry[K, V]]]`
- Since my hash map is immutable, `add` must return a new hash map. The original map is left untouched.
- `get` returns an `Option` that contains the value of that key if it exists and `None` otherwise

Method `add` is quite straight forward:
```scala
def add(key: K, value: V): HashMap[K, V] = {
  val idx = indexFor(key)
  
  // if the table is empty, initialize and then run 'add' again
  if(entries.isEmpty) init.add(key, value)
  // otherwise, if 'key' exists, replace its old value
  // if not, associate 'value' with 'key'
  else {
    val chain = entries(idx)
    chain.indexWhere(_.key == key) match {
      case -1 => // key not found
        val e = Entry(key, value)
        new HashMap(entries.updated(idx, e +: chain))
      case i =>
        val replaced = chain(i).copy(value = value)
        new HashMap(entries.updated(idx, chain.updated(i, replaced)))
    }
  }
}

private val initialCapacity = 16

private def init: HashMap[K, V] = {
  new HashMap(Vector.fill(initialCapacity)(Vector.empty))
}

/** Returns the index of this key in the internal entry vector. */
private def indexFor(key: K): Int = {
  key.hashCode() & entries.length
}
```

`remove` is a bit tricky to write since we cannot do an 'in-place' removal (immutability ...). Here, I use `filter` to remove which does the job but is not the most efficient way. This is because `filter` will go through the whole collection instead of stopping once the unwanted element is seen.
```scala
def remove(key: K): HashMap[K, V] = {
  val idx = indexFor(key)
  val updated = entries.updated(idx, entries(idx).filter(_.key != key))
  new HashMap(updated)
}
```

A more efficient approach to this case is to not remove anything at all but just 'mark' the element as removed.

And lastly, we need to write out `get` method:
```scala
def get(key: K): Option[V] = {
  val idx = indexFor(key)
  entries(idx).find(_.key == key).map(_.value)
}
```

Method `find` of `Vector` already returns an `Option` if there is no element match with `key` so we don't need to manually handle that. Utility methods like `find` is the reason why I love Scala so much. It allows me to leave work early.

![leave_work][leave_work]

And that's it! We have completed our Hash Map implementation that satisfied the requirements we made at the beginning. You can use it like this:

```scala
val map = new HashMap[Int, String](Vector.empty)
val web = map.add(1, 'web') // 1 -> 'web'
val dev = web.add(2, 'dev') // 1 -> 'web', 2 -> 'dev'
```


## Notes
- `Array` can be used for faster indexed access. Here, I use `Vector` to keep everything immutable.
- A more complete code can be found on [my Github][github_link].
- This implementation uses a constant capacity (and it shouldn't). In practice, you should add some logic in method `add` to increase the capacity when needed. This is important to maintain performance.

[hash_map_img]: hash_table.png
[hash_map]: https://en.wikipedia.org/wiki/Hash_table
[hash_map_guide]: https://www.hackerearth.com/practice/data-structures/hash-tables/basics-of-hash-tables/tutorial/
[chaining]: https://en.wikipedia.org/wiki/Hash_table#Separate_chaining
[leave_work]: leave_work.png
[github_link]: https://github.com/mt40/scala_interview/blob/master/src/main/scala/collection/HashMap.scala

package object common {

  object Implicits {
    implicit class SeqOps[A](val as: Seq[A]) extends AnyVal {

      def intersperse[B >: A](b: B): Seq[B] = {
        as.flatMap(a => Seq(b, a)).drop(1)
      }
    }

    implicit class StringsOps(val s: String) extends AnyVal {

      /**
        * Returns the underscore representation of this string.
        * All characters are converted into lowercase and consecutive spaces
        * are replaced by an underscore.
        */
      def toUnderscore: String =
        s.toLowerCase.replaceAll("\\s+", "_")
    }
  }
}

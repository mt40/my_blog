package common

object Const {

  object Language extends EnumList[String, Language] {
    val Plaintext = add(Language("plaintext"))
    val Scala = add(Language("scala"))
    val JS = add(Language("javascript"))
    val Html = add(Language("html"))
    val CSS = add(Language("css"))
    val Scss = add(Language("scss"))
    val Java = add(Language("java"))
    val Python = add(Language("python"))
    val Bash = add(Language("bash"))
  }

  case class Language(get: String) extends Enum[String]
}

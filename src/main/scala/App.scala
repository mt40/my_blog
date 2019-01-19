import org.scalajs.dom.document

object App {

  def main(args: Array[String]): Unit = {
    AppRouter().renderIntoDOM(document.getElementById("app"))
  }
}

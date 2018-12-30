import org.scalajs.dom.document

object App {

  def main(args: Array[String]): Unit = {
    AppCSS.load()
    AppRouter().renderIntoDOM(document.getElementById("app"))
  }
}

package components

import common.Api
import japgolly.scalajs.react.ScalaComponent

/**
  * Returns a comment section backed by Disqus
  * @see https://undertherain.disqus.com/admin/install/platforms/universalcode/
  */
object PostCommentsComp {
  case class Props(postId: String)

  private val component = {
    ScalaComponent
      .builder[Props]("CommentList")
      .render_P { props =>
        import japgolly.scalajs.react.vdom.all._

        // todo: looks like this will not work, try putting this in
        //   a js file and load use jquery to point to it at runtime
        //   or use a react disqus component from npm
        div(
          div(id := "disqus_thread"),
          script(
            s"""
            |console.log('loading disqus');
            |var disqus_config = function() {
            |this.page.url = '${Api.post(props.postId).value}';
            |this.page.identifier = '${props.postId}';
            |};
            |/* DON'T EDIT BELOW THIS LINE */
            |(function() {
            |var d = document, s = d.createElement('script');
            |s.src = 'https://undertherain.disqus.com/embed.js';
            |s.setAttribute('data-timestamp', +new Date());
            |(d.head || d.body).appendChild(s);
            |})();
          """.stripMargin
          ),
          noscript(
            "Please enable JavaScript to view the ",
            a(href := "https://disqus.com/?ref_noscript", "comments powered by Disqus.")
          )
        )
      }
      .build
  }

  def apply(postId: String) =
    component(Props(postId))
}

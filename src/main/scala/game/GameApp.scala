package game

import slinky.web.ReactDOM
import org.scalajs.dom

import game.components.App

object GameApp {
  def main(args: Array[String]): Unit = {
    val container = dom.document.getElementById("root")
    ReactDOM.render(App(), container)
  }
}

package com.raquo.domtestutils.matching

import com.raquo.domtypes.generic.keys.Style
import com.raquo.domtestutils.Utils.repr

// @TODO[SERVER]
import org.scalajs.dom

// @TODO[SERVER]
import scala.scalajs.js

class StyleRuleOps[V, N](val style: Style[V]) extends AnyVal {

  def is(expected: V): Rule[N] = new Rule[N] {
    def applyTo(testNode: ExpectedNode[N]): Unit = {
      testNode.addCheck(nodeStyleIs(style, expected))
    }
  }

  private def nodeStyleIs(style: Style[V], expectedValue: V)(node: dom.Node): MaybeError = {
    val maybeActualValue = node.asInstanceOf[js.Dynamic]
      .selectDynamic("style")
      .selectDynamic(style.jsName)
      .asInstanceOf[js.UndefOr[V]].toOption
    maybeActualValue match {
      case Some(actualValue) =>
        if (actualValue == expectedValue) {
          None
        } else {
          Some(s"Style `${style.jsName}` value is incorrect: actual value ${repr(actualValue)}, expected value ${repr(expectedValue)}")
        }
      case None =>
        Some(s"Style `${style.jsName}` is completely missing, expected ${repr(expectedValue)}")
    }
  }

  private def getStyle(element: dom.Element, style: Style[V]): Option[String] = {
    element.asInstanceOf[js.Dynamic]
      .selectDynamic("style")
      .selectDynamic(style.jsName)
      .asInstanceOf[js.UndefOr[String]].toOption
  }
}

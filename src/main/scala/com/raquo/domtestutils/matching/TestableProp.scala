package com.raquo.domtestutils.matching

import com.raquo.domtypes.generic.keys.Prop
import com.raquo.domtestutils.Utils.repr

// @TODO[SERVER]
import org.scalajs.dom

// @TODO[SERVER]
import scala.scalajs.js

// @TODO Create EventPropOps

class TestableProp[V](val prop: Prop[V]) extends AnyVal {

  def is(expected: V): Rule = (testNode: ExpectedNode) => {
    testNode.addCheck(nodePropIs(prop, Some(expected)))
  }

  def isEmpty: Rule = (testNode: ExpectedNode) => {
    testNode.addCheck(nodePropIs(prop, None))
  }

  private def nodePropIs(prop: Prop[V], maybeExpectedValue: Option[V])(node: dom.Node): MaybeError = {
    val maybeActualValue = getProp(node, prop)
    if (node.isInstanceOf[dom.Element]) {
      (maybeActualValue, maybeExpectedValue) match {
        case (None, None) => None
        case (None, Some(expectedValue)) =>
          Some(s"Prop `${prop.name}` is missing, expected ${repr(expectedValue)}")
        case (Some(actualValue), None) =>
          Some(s"Prop `${prop.name}` should not be present: actual value ${repr(actualValue)}, expected to be missing")
        case (Some(actualValue), Some(expectedValue)) =>
          if (actualValue != expectedValue) {
            Some(s"Prop `${prop.name}` value is incorrect: actual value ${repr(actualValue)}, expected value ${repr(expectedValue)}")
          } else {
            None
          }
      }
    } else {
      Some(s"Unable to verify Prop `${prop.name}` because node $node is not a DOM Element (might be a text node?)")
    }
  }

  private def getProp(node: dom.Node, prop: Prop[V]): Option[V] = {
    val propValue = node.asInstanceOf[js.Dynamic].selectDynamic(prop.name)
    val jsUndef = js.undefined
    propValue.asInstanceOf[Any] match {
      case str: String if str.length == 0 => None
      case `jsUndef` => None
      case null => None
      case _ => Some(propValue.asInstanceOf[V])
    }
  }
}

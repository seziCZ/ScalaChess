/*
 * Copyright (C) 2017, Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package cz.sezima.chess.core

import scala.language.implicitConversions

import cz.sezima.chess.core.Colors._

/**
  * Representation of a [[Color]].
  */
sealed trait Color {

  /**
    * Inversion of 'this' [[Color]]
    * @return [[White]] if 'this' is [[Black]], [[Black]] otherwise
    */
  def inv: Color
}

/**
  * Actual enumeration of known [[Color]]s
  */
object Colors {

  case object White extends Color {
    override def toString: String = "white"
    override def inv: Color = Black
  }

  case object Black extends Color {
    override def toString: String = "black"
    override def inv: Color = White
  }
}

/*
 * Copyright (C) 2017, Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package cz.sezima.chess.api.notation

import scala.util.Try

import cz.sezima.chess.core.board.{Board, Square}
import cz.sezima.chess.core.piece.Move

/**
  * [[Notation]] that understands [[Move]]s given in naive 'position-position'
  * format, e.g. 'c2-c4'.
  */
object SimpleNotation extends Notation {

  private val Delimiter: String = "-"

  override def decode(move: String, at: Board): Option[Move] =
    move.split(Delimiter) match {
      case Array(a, b) =>
        Try { at.pieceAt(Square(a.trim)).get ~> Square(b.trim) }.toOption
      case _ => None
    }
}

/*
 * Copyright (c) 2017 Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package cz.sezima.chess.core.piece

import cz.sezima.chess.core.Color
import cz.sezima.chess.core.Colors.White
import cz.sezima.chess.core.board.BoardExtensions._
import cz.sezima.chess.core.board.{Board, Square}

/**
  * A [[Bishop]].
  */
final case class Bishop(color: Color, atPos: Square) extends Piece {

  override val weight: Int = 3
  override val symbol: Char = if (color == White) '♗' else '♝'

  override def mayMoveTo(to: Square, at: Board): Boolean =
    at.isEmpty(to) && this.sharesDiag(to) && at.isEmptyDiag(atPos, to)

  override def mayCapture(pos: Square, at: Board): Boolean =
    at.isOccupiedBy(color.inv, pos) && this.sharesDiag(pos) &&
      at.isEmptyDiag(atPos, pos)

}

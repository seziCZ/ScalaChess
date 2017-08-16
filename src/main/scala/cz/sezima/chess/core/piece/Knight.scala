/*
 * Copyright (c) 2017 Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package cz.sezima.chess.core.piece

import scala.math._

import cz.sezima.chess.core.Color
import cz.sezima.chess.core.Colors.White
import cz.sezima.chess.core.board.BoardExtensions._
import cz.sezima.chess.core.board.{Board, Square}

/**
  * A [[Knight]].
  */
final case class Knight(color: Color, atPos: Square) extends Piece {

  override val weight: Int = 3
  override val symbol: Char = if (color == White) '♘' else '♞'

  override def mayMoveTo(to: Square, at: Board): Boolean =
    at.isEmpty(to) && mayInteractWith(to, at)

  override def mayCapture(pos: Square, at: Board): Boolean =
    at.isOccupiedBy(color.inv, pos) && mayInteractWith(pos, at)

  /**
    * Resolves whether given [[Square]] is reachable by 'this' [[Knight]].
    * @param to [[Square]] whose reachability is to be determined
    * @param at [[Board]] to be used for validation
    * @return 'True' if given [[Square]] is reachable by 'this' [[Knight]],
    *         'false' otherwise
    */
  private def mayInteractWith(to: Square, at: Board): Boolean = to match {
    case Square(f, r) if abs(atPos.file - f) == 2 => abs(atPos.rank - r) == 1
    case Square(f, r) if abs(atPos.rank - r) == 2 => abs(atPos.file - f) == 1
    case _ => false
  }
}

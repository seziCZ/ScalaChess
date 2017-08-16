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
  * A [[Queen]].
  */
final case class Queen(color: Color, atPos: Square) extends Piece {

  override val weight: Int = 9
  override val symbol: Char = if (color == White) '♕' else '♛'

  override def mayMoveTo(to: Square, at: Board): Boolean =
    at.isEmpty(to) && mayInteractWith(to, at)

  override def mayCapture(pos: Square, at: Board): Boolean =
    at.isOccupiedBy(color.inv, pos) && mayInteractWith(pos, at)

  /**
    * Resolves whether given [[Square]] is reachable by 'this' [[Queen]].
    * @param to [[Square]] whose reachability is to be determined
    * @param at [[Board]] to be used for validation
    * @return 'True' if given [[Square]] is reachable by 'this' [[Queen]],
    *         'false' otherwise
    */
  private def mayInteractWith(to: Square, at: Board): Boolean = to match {
    case dest if this.sharesFile(dest) => at.areEmptyFiles(atPos, dest)
    case dest if this.sharesRank(dest) => at.areEmptyRanks(atPos, dest)
    case dest if this.sharesDiag(dest) => at.isEmptyDiag(atPos, dest)
    case _ => false
  }
}

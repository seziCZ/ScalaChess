/*
 * Copyright (c) 2017 Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package cz.sezima.chess.core.piece

import cz.sezima.chess.core.Color
import cz.sezima.chess.core.Colors.{Black, White}
import cz.sezima.chess.core.board.BoardExtensions._
import cz.sezima.chess.core.board.{Board, Square}

/**
  * A [[Pawn]].
  */
final case class Pawn(color: Color, atPos: Square) extends Piece {

  override val weight: Int = 1
  override val symbol: Char = if (color == White) '♙' else '♟'

  override def mayMoveTo(to: Square, at: Board): Boolean = {
    lazy val isPawnMove: Boolean = this match {
      case Pawn(White, Square(f, 2)) if to.rank == 4 => at.isEmpty(Square(f, 3))
      case Pawn(Black, Square(f, 7)) if to.rank == 5 => at.isEmpty(Square(f, 6))
      case Pawn(White, from) => from.y + 1 == to.y
      case Pawn(Black, from) => from.y - 1 == to.y
    }

    at.isEmpty(to) && this.sharesFile(to) && isPawnMove
  }

  override def mayCapture(to: Square, at: Board): Boolean = {
    lazy val isThreatenByPawn: Boolean = color match {
      case White => atPos.y + 1 == to.y && math.abs(atPos.x - to.x) == 1
      case Black => atPos.y - 1 == to.y && math.abs(atPos.x - to.x) == 1
    }

    isThreatenByPawn && at.isOccupiedBy(color.inv, to) ||
    isThreatenByPawn && at.history.headOption.exists(enPassant(_, to))
  }

  override def reachCallback(at: Board): Board = at.history match {
    case a :: b :: tail if enPassant(b, a.dest) =>
      val updatedPieces = at.pieces.filterNot(_.atPos == b.dest)
      Board(updatedPieces, at.history)

    case m :: _ if m.dest.rank == 1 || m.dest.rank == 8 =>
      val updatedPieces = at.pieces.filterNot(_.atPos == m.dest)
      val newQueen = Queen(m.piece.color, m.dest)
      Board(updatedPieces :+ newQueen, at.history)

    case _ => at
  }

  /**
    * Determines whether 'en passant' rule takes place.
    * @param prev A move that occurred before 'this' move
    * @param curr A square to be used in 'this' move
    * @return 'True' if 'en passant' rule occurred, 'false' otherwise
    */
  private def enPassant(prev: Move, curr: Square): Boolean =
    prev.piece.isInstanceOf[Pawn] && (prev.piece until prev.dest).contains(curr)

}

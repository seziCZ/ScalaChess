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
  * A [[King]].
  */
final case class King(color: Color, atPos: Square) extends Piece {

  override val weight: Int = 20
  override val symbol: Char = if (color == White) '♔' else '♚'

  override def mayMoveTo(to: Square, at: Board): Boolean = {
    lazy val castlingTakesPlace: Boolean = to match {
      case Square('g', s) => isCastling(to, Square('h', s), at)
      case Square('c', s) => isCastling(to, Square('a', s), at)
      case _ => false
    }

    at.isEmpty(to) && (mayInteractWith(to, at) ||
    this.sharesRank(to) && castlingTakesPlace)
  }

  override def mayCapture(pos: Square, at: Board): Boolean =
    at.isOccupiedBy(color.inv, pos) && mayInteractWith(pos, at)

  override def reachCallback(at: Board): Board = at.history.head match {
    case Move(_, pos @ Square('g', r)) if pos.x - atPos.x > 1 =>
      val rook: Piece = at.pieceAt(Square('h', r)).get
      (rook ~> Square('f', r)).update(at).copy(history = at.history)

    case Move(_, pos @ Square('c', r)) if atPos.x - pos.x > 1 =>
      val rook: Piece = at.pieceAt(Square('a', r)).get
      (rook ~> Square('d', r)).update(at).copy(history = at.history)

    case _ => at
  }

  /**
    * Resolves whether given [[Square]] is reachable by 'this' [[King]].
    * @param to [[Square]] whose reachability is to be determined
    * @param at [[Board]] to be used for validation
    * @return 'True' if given [[Square]] is reachable by 'this' [[King]],
    *         'false' otherwise
    */
  private def mayInteractWith(to: Square, at: Board): Boolean =
    math.abs(atPos.file - to.file) <= 1 && math.abs(atPos.rank - to.rank) <= 1

  /**
    * Determines whether castling takes place.
    * @param dest  A [[Square]] [[King]] is about to move to
    * @param rook Position of a [[Rook]] that would move during the castling
    * @param b [[Board]] to be used for validation
    * @return 'True' if given setup describes castling, 'false' otherwise
    */
  private def isCastling(dest: Square, rook: Square, b: Board): Boolean = {
    lazy val isSafeRow: Boolean = {
      val boards: Seq[Board] =
        ((this until dest) :+ dest).map(s => (this ~> s).update(b))
      (b +: boards).forall(!_.isInCheck(color))
    }

    !hasPieceMoved(rook, b) && !hasPieceMoved(this, b) &&
    b.isEmptyRanks(this, rook) && isSafeRow
  }

  /**
    * Determines whether [[Piece]] at given [[Square]] has ever left its initial position
    * @param from A [[Square]] that hosts [[Piece]] whose movement is to be inspected
    * @param at [[Board]] to be used for validation
    * @return 'True' if [[Piece]] hosted by given [[Square]] has moved, 'false' otherwise
    */
  private def hasPieceMoved(from: Square, at: Board): Boolean =
    at.history.exists { case Move(p, d) => p.atPos == from || d == from }

}

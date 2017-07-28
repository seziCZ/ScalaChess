/*
 * Copyright (c) 2017 Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package cz.sezima.chess.core.piece

import cz.sezima.chess.core.Colors.{Black, White}
import cz.sezima.chess.core.board.Square._
import cz.sezima.chess.core.board.{Board, Square}
import cz.sezima.chess.core.piece
import org.scalatest.FunSuite

/**
 * A set of [[King]] relevant unit tests.
 */
class KingTests extends FunSuite {

  val king: King =
    King(White, Square('e', 1))

  val pieces: Seq[Piece] = Seq(
    king,
    Rook(White, Square('a', 1)),
    Rook(White, Square('h', 1)),
    Pawn(Black, Square('g', 2))
  )

  val board: Board =
    Board(pieces, Seq.empty)

  test("kings may move to expected positions"){

    // setup
    val expMoves = Seq(
      Square('d', 1),
      Square('d', 2),
      Square('e', 2),
      Square('f', 2),
      Square('f', 1), // filtered by [[Move]]
      Square('c', 1) // castling
    )

    // act
    val moves: Seq[Square] =
      All.filter(king.mayMoveTo(_, board))

    // assert
    assert(expMoves.toSet == moves.toSet)
  }

  test("white castling is handled correctly"){

    // setup
    val updatedKing: King =
      King(White, Square('c', 1))

    val castlingPieces: Seq[Piece] = Seq(
      updatedKing,
      Rook(White, Square('a', 1))
    )

    val castlingMove: Seq[Move] = Seq(
      king ~> updatedKing.atPos)

    val castlingBoard: Board =
      Board(castlingPieces, castlingMove)

    val expRook: Rook =
      Rook(White, Square('d', 1))

    // act
    val updated: Board =
      king.reachCallback(castlingBoard)

    // assert
    assert(2 == updated.pieces.length)
    assert(1 == updated.history.length)
    assert(updated.pieces.contains(updatedKing))
    assert(updated.pieces.contains(expRook))
  }
}

/*
 * Copyright (c) 2017 Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package cz.sezima.chess.core.piece

import cz.sezima.chess.core.Colors.{Black, White}
import cz.sezima.chess.core.board.{Board, Square}
import org.scalatest.FunSuite

/**
 * A set of [[Move]] relevant unit tests.
 */
class MoveTests extends FunSuite {

  val whiteKing = King(White, Square('c', 3))
  val blackKing = King(Black, Square('c', 8))

  val pieces: Seq[Piece] = Seq(
    whiteKing,
    blackKing,
    Rook(White, Square('d', 3))
  )

  val history: Seq[Move] = Seq(
    King(White, Square('c', 2)) ~> whiteKing.atPos
  )

  val board: Board =
    Board(pieces, history)

  test("'validateColorAt' function yields expected results"){

    // setup
    val validMove = blackKing ~> Square('b', 5)
    val invalidMove = whiteKing ~> Square('c', 4)

    // act, assert
    assert(validMove.validateColorAt(board).isEmpty)
    assert(invalidMove.validateColorAt(board).nonEmpty)
  }

  test("'validateMembershipAt' function yields expected results"){

    // setup
    val validMove = blackKing ~> Square('b', 5)
    val invalidMove = Pawn(White, Square('a', 2)) ~> Square('a', 3)

    // act, assert
    assert(validMove.validateMembershipAt(board).isEmpty)
    assert(invalidMove.validateMembershipAt(board).nonEmpty)
  }

  test("validateMovementAt'' function yields expected results"){

    // setup
    val validMove = blackKing ~> Square('b', 8)
    val invalidMove = blackKing ~> Square('a', 1)

    // act, assert
    assert(validMove.validateMovementAt(board).isEmpty)
    assert(invalidMove.validateMovementAt(board).nonEmpty)
  }

  test("validateKingAt'' function yields expected results"){

    // setup
    val validMove = blackKing ~> Square('b', 5)
    val invalidMove = blackKing ~> Square('d', 5)
    val validBoard = validMove.update(board)
    val invalidBoard = invalidMove.update(board)


    // act, assert
    assert(validMove.validateKingAt(validBoard).isLeft)
    assert(invalidMove.validateKingAt(invalidBoard).isRight)
  }

  test("board may be updated regardless of rules"){

    // setup
    val move: Move =
      whiteKing ~> Square('h', 8)

    val expPiece: King =
      King(White, Square('h', 8))

    // act
    val result: Board =
      move.update(board)

    // assert
    assert(!result.pieces.contains(whiteKing))
    assert(result.pieces.contains(expPiece))
  }
}

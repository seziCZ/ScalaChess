package cz.sezima.chess.core.piece

import cz.sezima.chess.core.Colors.{Black, White}
import cz.sezima.chess.core.board.Square._
import cz.sezima.chess.core.board.{Board, Square}
import org.scalatest.FunSuite

/**
  * A set of [[Queen]] relevant unit tests.
  */
class QueenTests extends FunSuite {

  val whiteQueen: Queen =
    Queen(White, Square('b', 2))

  val pieces: Seq[Piece] = Seq(whiteQueen, Pawn(Black, Square('d', 4)))

  val board: Board =
    Board(pieces, Seq.empty)

  test("queens may move in exected directions") {

    // setup
    val expMoves = {
      val horizontal = (1 to 8).map(Square('b', _))
      val vertical = ('a' to 'h').map(Square(_, 2))
      val diagonal = Set(Square('a', 3), Square('c', 1), Square('a', 1), Square('c', 3))
      (horizontal ++ vertical ++ diagonal)
        .filterNot(_ == pieces.head.atPos)
        .toSet
    }

    // act
    val result: Seq[Square] =
      All.filter(whiteQueen.mayMoveTo(_, board))

    // assert
    assert(expMoves == result.toSet)
  }

  test("queens may capture exected pieces") {

    // setup
    val expCaptures =
      Seq(pieces(1).atPos)

    // act
    val result: Seq[Square] =
      All.filter(whiteQueen.mayCapture(_, board))

    // assert
    assert(expCaptures == result)
  }
}

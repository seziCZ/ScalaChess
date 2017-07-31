package cz.sezima.chess.core.board

import cz.sezima.chess.core.Colors.{Black, White}
import cz.sezima.chess.core.piece._
import org.scalatest.FunSuite

import scala.collection.SeqView

/**
 * A set of [[Board]] relevant unit tests.
 */
class BoardTests extends FunSuite {

  val initBoard: Board =
    Board.Initial

  test("board recognises color of a player that moves"){

    // setup
    val history: Seq[Move] = Seq(
      Pawn(White, Square('a', 2)) ~> Square('a', 3),
      Pawn(Black, Square('a', 7)) ~> Square('a', 6))

    val board: Board =
      Board(Seq.empty, history)

    // act, assert
    assert(initBoard.onMove == White)
    assert(board.onMove == Black)
  }

  test("draw is recognised correctly"){

    // setup
    val history: Seq[Move] = Seq(
      King(White, Square('e', 1)) ~> Square('h', 2),
      King(Black, Square('e', 8)) ~> Square('a', 1) // avoids castling
    )

    val pieces: Seq[Piece] = Seq(
      Rook(White, Square('h', 2)),
      King(White, Square('c', 1)),
      King(Black, Square('a', 1))
    )

    val board: Board =
      Board(pieces, history)

    // act, assert
    assert(!board.mayPlay(Black))
  }

  test("check is recognised correctly"){

    // setup
    val pieces: Seq[Piece] = Seq(
      Rook(White, Square('h', 1)),
      King(White, Square('h', 2)),
      King(Black, Square('a', 1)))

    val board: Board =
      Board(pieces, Seq.empty)

    // act, assert
    assert(board.isInCheck(Black))
    assert(!board.isInCheck(White))
  }

  test("checkmate is recognised correctly"){

    // setup
    val pieces: Seq[Piece] = Seq(
      Rook(White, Square('h', 1)),
      King(White, Square('a', 3)),
      King(Black, Square('a', 1)))

    val board: Board =
      Board(pieces, Seq.empty)

    // act, assert
    assert(board.isCheckmated(Black))
    assert(!board.isCheckmated(White))
  }

  test("'pieceAt' yields expected values"){

    // setup
    val whiteRook: Rook = Rook(White, Square('a', 1))
    val blackKing: King = King(Black, Square('e', 8))

    // act, assert
    assert(initBoard.pieceAt(whiteRook.atPos).isDefined)
    assert(whiteRook == initBoard.pieceAt(whiteRook.atPos).get)
    assert(initBoard.pieceAt(blackKing.atPos).isDefined)
    assert(blackKing == initBoard.pieceAt(blackKing.atPos).get)
    assert(initBoard.pieceAt(Square('c', 4)).isEmpty)
  }

  test("'genMoves' function yields expected moves"){

    // setup
    val pawn: Pawn =
      Pawn(White, Square('a', 2))

    val board: Board =
      Board(Seq(pawn), Seq.empty)

    val expMoves: Set[Move] = Set(
      pawn ~> Square('a', 3),
      pawn ~> Square('a', 4))

    // act
    val whiteResult: SeqView[Move, Seq[_]] = board.genMoves(White)
    val blackResult: SeqView[Move, Seq[_]] = board.genMoves(Black)

    // assert
    assert(blackResult.isEmpty)
    assert(expMoves == whiteResult.toSet)
  }

  test("'pieceBy' function yields expected values"){

    // setup
    val whiteKing: King = King(White, Square('e', 1))
    val blackQueen: Queen = Queen(Black, Square('d', 8))

    // act
    val whiteResult: Option[King] =
      initBoard.pieceBy[King](_.color == White)

    val blackResult: Option[Queen] =
      initBoard.pieceBy[Queen](_.color == Black)

    val emptyResult: Option[Piece] =
      initBoard.pieceBy[Piece](_.atPos == Square('d', 4))

    // assert
    assert(whiteResult.isDefined)
    assert(whiteKing == whiteResult.get)
    assert(blackResult.isDefined)
    assert(blackQueen == blackResult.get)
    assert(emptyResult.isEmpty)
  }
}

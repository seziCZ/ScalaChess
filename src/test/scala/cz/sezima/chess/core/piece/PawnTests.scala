/*
 * Copyright (c) 2017 Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package cz.sezima.chess.core.piece

import cz.sezima.chess.core.Colors.{Black, White}
import cz.sezima.chess.core.board.Square._
import cz.sezima.chess.core.board.{Board, Square}
import org.scalatest.FunSuite

/**
 * A set of [[Pawn]] relevnt unit tests.
 */
class PawnTests extends FunSuite {

  val whitePawnA = Pawn(White, Square('a', 2))
  val whitePawnB = Pawn(White, Square('b', 2))
  val whitePawnC = Pawn(White, Square('c', 3))
  val blackPawnA = Pawn(Black, Square('a', 7))
  val blackPawnB = Pawn(Black, Square('b', 3))
  val blackPawnC = Pawn(Black, Square('c', 6))

  val pawns: Seq[Piece] = Seq(
    whitePawnA,
    whitePawnB,
    whitePawnC,
    blackPawnA,
    blackPawnB,
    blackPawnC
  )

  val board: Board =
    Board(pawns, Seq.empty)


  test("white may move in expected directions"){

    // setup
    val expAmoves = Set(Square('a', 3), Square('a', 4))
    val expCmoves = Set(Square('c', 4))
    val expBmoves = Set.empty[Square]

    // act
    val aMoves = All.filter(whitePawnA.mayMoveTo(_, board)).toSet
    val bMoves = All.filter(whitePawnB.mayMoveTo(_, board)).toSet
    val cMoves = All.filter(whitePawnC.mayMoveTo(_, board)).toSet

    // assert
    assert(expAmoves == aMoves)
    assert(expBmoves == bMoves)
    assert(expCmoves == cMoves)
  }

  test("black may move in expected directions"){

    // setup
    val expAmoves = Set(Square('a', 5), Square('a', 6))
    val expCmoves = Set(Square('c', 5))
    val expBmoves = Set.empty[Square]

    // act
    val aMoves = All.filter(blackPawnA.mayMoveTo(_, board)).toSet
    val bMoves = All.filter(blackPawnB.mayMoveTo(_, board)).toSet
    val cMoves = All.filter(blackPawnC.mayMoveTo(_, board)).toSet

    // assert
    assert(expAmoves == aMoves)
    assert(expBmoves == bMoves)
    assert(expCmoves == cMoves)
  }

  test("black pawns capture expected positions"){

    // setup
    val expCaptures: Seq[Square] =
      Seq(Square('a', 2))

    // act
    val captures: Seq[Square] =
      for{
        square <- All
        pawn <- pawns.filter(_.color == Black)
        if pawn.mayCapture(square, board)
      } yield square

    // assert
    assert(expCaptures == captures)
  }

  test("white pawns capture expected positions"){

    // setup
    val expCaptures: Seq[Square] =
      Seq(Square('b', 3))

    // act
    val captures: Seq[Square] =
      for{
        square <- All
        pawn <- pawns.filter(_.color == White)
        if pawn.mayCapture(square, board)
      } yield square

    // assert
    assert(expCaptures == captures)
  }

  test("black 'en passant' move is recognised correctly"){

    // setup
    val whitePawn = Pawn(White, Square('c', 4))
    val blackPawn = Pawn(Black, Square('d', 4))

    val enPassantPieces: Seq[Pawn] = Seq(
      whitePawnB,
      blackPawn
    )

    val enPassanHistory: Seq[Move] = Seq(
      Pawn(White, Square('c', 2)) ~> whitePawn.atPos
    )

    val enPassanSquare: Square =
      Square('c', 3)

    val enPassanBoard: Board =
      Board(enPassantPieces, enPassanHistory)

    // assert
    assert(blackPawn.mayCapture(enPassanSquare, enPassanBoard))
  }

  test("black 'en passant' move is handled correctly"){

    // setup
    val whitePawn = Pawn(White, Square('c', 4))
    val blackPawn = Pawn(Black, Square('c', 3))

    val enPassantPieces: Seq[Pawn] = Seq(
      whitePawn,
      blackPawn
    )

    val enPassanHistory: Seq[Move] = Seq(
      Pawn(Black, Square('d', 4)) ~> blackPawn.atPos,
      Pawn(White, Square('c', 2)) ~> whitePawn.atPos
    )

    val enPassanBoard: Board =
      Board(enPassantPieces, enPassanHistory)

    // act
    val updated: Board =
      blackPawnA.reachCallback(enPassanBoard)

    // assert
    assert(1 == updated.pieces.length)
    assert(2 == updated.history.length)
    assert(updated.pieces.contains(blackPawn))
    assert(!updated.pieces.contains(whitePawn))
  }

  test("white 'en passant' move is recognised correctly"){

    // setup
    val whitePawn = Pawn(White, Square('d', 5))
    val blackPawn = Pawn(Black, Square('c', 5))

    val enPassantPieces: Seq[Pawn] = Seq(
      whitePawn,
      blackPawn
    )

    val enPassanHistory: Seq[Move] = Seq(
      Pawn(Black, Square('c', 7)) ~> blackPawn.atPos
    )
    val enPassanSquare: Square =
      Square('c', 6)

    val enPassanBoard: Board =
      Board(enPassantPieces, enPassanHistory)

    // assert
    assert(whitePawn.mayCapture(enPassanSquare, enPassanBoard))
  }

  test("white 'en passant' move is handled correctly"){

    // setup
    val whitePawn = Pawn(White, Square('d', 6))
    val blackPawn = Pawn(Black, Square('d', 5))

    val enPassantPieces: Seq[Pawn] = Seq(
      whitePawn,
      blackPawn
    )

    val enPassanHistory: Seq[Move] = Seq(
      Pawn(White, Square('c', 5)) ~> whitePawn.atPos,
      Pawn(Black, Square('d', 7)) ~> blackPawn.atPos
    )

    val enPassanBoard: Board =
      Board(enPassantPieces, enPassanHistory)

    // act
    val result: Board =
      whitePawn.reachCallback(enPassanBoard)

    // assert
    assert(!result.pieces.contains(blackPawn))
  }

  test("white pawn is substituted by a queen when it reaches rank 8"){

    // setup
    val subPawn: Pawn =
      Pawn(White, Square('d', 8))

    val subMove: Seq[Move] = Seq(
      Pawn(White, Square('d', 7)) ~> Square('d', 8))

    val subBoard: Board =
      Board(Seq(subPawn), subMove)

    val expPiece: Queen =
      Queen(White, subPawn.atPos)

    // act
    val result: Board =
      subPawn.reachCallback(subBoard)

    // assert
    assert(1 == result.pieces.size)
    assert(result.pieces.contains(expPiece))
  }

  test("black pawn is substituted by a queen when it reaches rank 1"){

    // setup
    val subPawn: Pawn =
      Pawn(Black, Square('d', 1))

    val subMove: Seq[Move] = Seq(
      Pawn(Black, Square('d', 2)) ~> Square('d', 1))

    val subBoard: Board =
      Board(Seq(subPawn), subMove)

    val expPiece: Queen =
      Queen(Black, subPawn.atPos)

    // act
    val result: Board =
      subPawn.reachCallback(subBoard)

    // assert
    assert(1 == result.pieces.size)
    assert(result.pieces.contains(expPiece))
  }
}

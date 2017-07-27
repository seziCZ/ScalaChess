/*
 * Copyright (C) 2017, Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package cz.sezima.chess.core.piece

import cz.sezima.chess.core.Colors.{Black, White}
import cz.sezima.chess.core.board.Square._
import cz.sezima.chess.core.board.{Board, Square}
import org.scalatest.FunSuite

/**
 * A set of [[Knight]] relevant unit tests.
 */
class KnightTests extends FunSuite {

  val whiteKnightA = Knight(White, Square('b', 1))
  val whiteKnightB = Knight(White, Square('b', 2))
  val blackKnightC = Knight(Black, Square('c', 3))

  val knights: Seq[Knight] = Seq(
    whiteKnightA,
    whiteKnightB,
    blackKnightC
  )

  val board: Board =
    Board(knights, Seq.empty)

  test("knights may move to expected squares"){

    // setup
    val expAmoves = Set(Square('a', 3), Square('d', 2))
    val expBmoves = Set(Square('a', 4), Square('c', 4), Square('d', 1), Square('d', 3))
    val expCmoves = Set(
      Square('a', 2),
      Square('a', 4),
      Square('b', 5),
      Square('d', 1),
      Square('d', 5),
      Square('e', 2),
      Square('e', 4))

    // act
    val aMoves = All.filter(whiteKnightA.mayMoveTo(_, board))
    val bMoves = All.filter(whiteKnightB.mayMoveTo(_, board))
    val cMoves = All.filter(blackKnightC.mayMoveTo(_, board))

    // assert
    assert(expAmoves == aMoves.toSet)
    assert(expBmoves == bMoves.toSet)
    assert(expCmoves == cMoves.toSet)
  }

  test("knights may capture expected pieces"){

    // setup
    val expAcaptures = Seq(Square('c', 3))
    val expCcaptures = Seq(Square('b', 1))


    // act
    val aCaptures = All.filter(whiteKnightA.mayCapture(_, board))
    val bCaptures = All.filter(whiteKnightB.mayCapture(_, board))
    val cCaptures = All.filter(blackKnightC.mayCapture(_, board))

    // assert
    assert(expAcaptures == aCaptures)
    assert(bCaptures.isEmpty)
    assert(expCcaptures == cCaptures)
  }
}

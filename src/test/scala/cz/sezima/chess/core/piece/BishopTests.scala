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
  * A set of [[Bishop]] relevant unit tests.
  */
class BishopTests extends FunSuite {

  val whiteB = Bishop(White, Square('a', 3))
  val blackB = Bishop(Black, Square('d', 6))

  val bishops: Seq[Bishop] =
    Seq(whiteB, blackB)

  val board: Board =
    Board(bishops, Seq.empty)

  test("bishops may move to expected positions") {

    // setup
    val expWmoves =
      Seq(Square('b', 4), Square('c', 5), Square('b', 2), Square('c', 1))

    val expBmoves = Seq(
      Square('c', 5),
      Square('b', 4),
      Square('c', 7),
      Square('b', 8),
      Square('e', 7),
      Square('f', 8),
      Square('e', 5),
      Square('f', 4),
      Square('g', 3),
      Square('h', 2))

    // act
    val aMoves = All.filter(whiteB.mayMoveTo(_, board))
    val bMoves = All.filter(blackB.mayMoveTo(_, board))

    // assert
    assert(expWmoves.toSet == aMoves.toSet)
    assert(expBmoves.toSet == bMoves.toSet)
  }

  test("bishops may capture expected pieces") {

    // setup
    val expWCaptures = Seq(Square('d', 6))
    val expBCaptures = Seq(Square('a', 3))

    // act
    val aCaptures = All.filter(whiteB.mayCapture(_, board))
    val bCaptures = All.filter(blackB.mayCapture(_, board))

    // assert
    assert(expWCaptures.toSet == aCaptures.toSet)
    assert(expBCaptures.toSet == bCaptures.toSet)
  }
}

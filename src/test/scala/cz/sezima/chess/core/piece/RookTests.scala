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
 * A set of [[Rook]] relevant unit tests.
 */
class RookTests extends FunSuite {

  val blackRookA = Rook(Black, Square('a', 1))
  val blackRookB = Rook(Black, Square('d', 4))
  val whiteRookC = Rook(White, Square('a', 4))
  val whiteRookD = Rook(White, Square('a', 6))

  val rooks: Seq[Rook] = Seq(
    whiteRookC,
    whiteRookD,
    blackRookA,
    blackRookB
  )

  val board: Board =
    Board(rooks, Seq.empty)

  test("rooks may move in expected directions"){

    // setup
    val expAYmoves = (2 to 3).map(Square('a', _)).toSet
    val expAXmoves = ('b' to 'h').map(Square(_, 1)).toSet
    val expAmoves = expAXmoves ++ expAYmoves

    val expBYmoves = (('b' to 'c') ++ ('e' to 'h')).map(Square(_, 4)).toSet
    val expBXmoves = ((1 to 3) ++ (5 to 8)).map(Square('d', _)).toSet
    val expBmoves = expBXmoves ++ expBYmoves

    val expCYmoves = ((2 to 3) :+ 5).map(Square('a', _)).toSet
    val expCXmoves = ('b' to 'c').map(Square(_, 4)).toSet
    val expCmoves = expCYmoves ++ expCXmoves

    val expDYmoves = (5 +: (7 to 8)).map(Square('a', _)).toSet
    val expDXmoves = ('b' to 'h').map(Square(_, 6)).toSet
    val expDmoves = expDYmoves ++ expDXmoves

    // act
    val aMoves = All.filter(blackRookA.mayMoveTo(_, board))
    val bMoves = All.filter(blackRookB.mayMoveTo(_, board))
    val cMoves = All.filter(whiteRookC.mayMoveTo(_, board))
    val dMoves = All.filter(whiteRookD.mayMoveTo(_, board))

    // assert
    assert(expAmoves == aMoves.toSet)
    assert(expBmoves == bMoves.toSet)
    assert(expCmoves == cMoves.toSet)
    assert(expDmoves == dMoves.toSet)
  }

  test("rooks may capture expected positions") {

    // setup
    val expAcaptures = Set(Square('a', 4))
    val expBcaptures = Set(Square('a', 4))
    val expCcaptures = Set(Square('a', 1), Square('d', 4))
    val expDcaptures = Set()

    // act
    val aCaptures = All.filter(blackRookA.mayCapture(_, board))
    val bCaptures = All.filter(blackRookB.mayCapture(_, board))
    val cCaptures = All.filter(whiteRookC.mayCapture(_, board))
    val dCaptures = All.filter(whiteRookD.mayCapture(_, board))

    // assert
    assert(expAcaptures == aCaptures.toSet)
    assert(expBcaptures == bCaptures.toSet)
    assert(expCcaptures == cCaptures.toSet)
    assert(expDcaptures == dCaptures.toSet)
  }
}

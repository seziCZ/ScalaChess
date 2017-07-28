/*
 * Copyright (c) 2017 Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package cz.sezima.chess.core.board

import cz.sezima.chess.core.Colors.{White, Black}
import org.scalatest.FunSuite

/**
 * A set of [[Square]] relevant unit tests.
 */
class SquareTests extends FunSuite {

  test("'All' property holds all possible squares") {

    // assert
    assert(64 == Square.All.length)
    assert(32 == Square.All.count(_.color == White))
    assert(32 == Square.All.count(_.color == Black))
  }

  test("'x' and 'y' axis reflect square's coordinates"){

    // setup
    val firstSquare = Square('a', 1)
    val expFirstCoords = 0 -> 0

    val lastSquare = Square('h', 8)
    val expSecondCoords = 7 -> 7

    // act
    val firstCoords = firstSquare.x -> firstSquare.y
    val secondCoords = lastSquare.x -> lastSquare.y

    // assert
    assert(expFirstCoords == firstCoords)
    assert(expSecondCoords == secondCoords)
  }

  test("square color reflects it's coordinates"){

    //setup
    val blacks: Seq[Square] =
      Seq(
        Square('a', 1),
        Square('c', 5),
        Square('f', 2),
        Square('h', 8))

    val whites: Seq[Square] =
      Seq(
        Square('a', 8),
        Square('c', 6),
        Square('f', 1),
        Square('h', 1))

    // act
    val isAllBlacks = blacks.forall(_.color == Black)
    val isAllWhites = whites.forall(_.color == White)

    // assert
    assert(isAllBlacks)
    assert(isAllWhites)
  }

  test("'unitl' function yields expected results"){

    // setup
    val expFiles = Set(Square('a', 4), Square('a', 5))
    val expRanks = Set(Square('b', 3), Square('c', 3))
    val expDiag = Set(Square('b', 2))

    // act
    val fileResult = Square('a', 3) until Square('a', 6)
    val fileResultRev = Square('a', 6) until Square('a', 3)
    val rankResult = Square('a', 3) until Square('d', 3)
    val rankResultRev = Square('d', 3) until Square('a', 3)
    val diagResult = Square('a', 3) until Square('c', 1)
    val diagResultRev = Square('c', 1) until Square('a', 3)
    val emptyResult = Square('c', 2) until Square('c', 1)

    // assert
    assert(expFiles == fileResult.toSet)
    assert(expFiles == fileResultRev.toSet)
    assert(expRanks == rankResult.toSet)
    assert(expRanks == rankResultRev.toSet)
    assert(expDiag == diagResult.toSet)
    assert(expDiag == diagResultRev.toSet)
    assert(emptyResult.isEmpty)
  }

  test("'apply' function yields expected squares"){

    // act, assert
    assert(Square('d', 6) == Square("d6"))
    intercept[IllegalArgumentException](Square("uknown"))

  }
}

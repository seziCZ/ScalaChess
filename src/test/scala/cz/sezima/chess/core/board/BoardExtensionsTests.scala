/*
 * Copyright (c) 2017 Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package cz.sezima.chess.core.board

import cz.sezima.chess.core.Colors.{Black, White}
import cz.sezima.chess.core.piece.{King, Piece, Queen}
import org.scalatest.FunSuite

/**
  * A set of [[BoardExtensions]] relevant unit tests.
  */
class BoardExtensionsTests extends FunSuite {

  val whiteQueen = Queen(White, Square('c', 3))
  val whiteKing = King(White, Square('c', 6))
  val blackKing = King(Black, Square('f', 3))

  val pieces: Seq[Piece] =
    Seq(whiteQueen, whiteKing, blackKing)

  val board: Board =
    Board(pieces, Seq.empty)

  import BoardExtensions._

  test("square emptiness is recognised correctly") {

    // setup
    val expResult: Set[Square] =
      Square.All.filterNot(pieces.map(_.atPos).contains).toSet

    // act
    val result: Set[Square] =
      Square.All.filter(board.isEmpty(_)).toSet

    // assert
    assert(expResult == result)
    assert(!board.isEmpty(Square.All: _*))
  }

  test("file emptiness is recognised correctly") {

    // act, assert
    assert(board.areEmptyFiles(Square('c', 3), Square('c', 6)))
    assert(board.areEmptyFiles(Square('c', 6), Square('c', 3)))
    assert(board.areEmptyFiles(Square('c', 1), Square('c', 3)))
    assert(board.areEmptyFiles(Square('c', 6), Square('c', 8)))

    assert(!board.areEmptyFiles(Square('c', 1), Square('c', 8)))
    assert(!board.areEmptyFiles(Square('c', 2), Square('c', 4)))
    assert(!board.areEmptyFiles(Square('c', 5), Square('c', 7)))
    assert(!board.areEmptyFiles(Square('c', 2), Square('c', 7)))
  }

  test("rank emptiness is recognised correctly") {

    // act, assert
    assert(board.areEmptyRanks(Square('c', 3), Square('f', 3)))
    assert(board.areEmptyRanks(Square('f', 3), Square('c', 3)))
    assert(board.areEmptyRanks(Square('a', 3), Square('c', 3)))
    assert(board.areEmptyRanks(Square('f', 3), Square('h', 3)))

    assert(!board.areEmptyRanks(Square('a', 3), Square('h', 3)))
    assert(!board.areEmptyRanks(Square('b', 3), Square('d', 3)))
    assert(!board.areEmptyRanks(Square('e', 3), Square('g', 3)))
    assert(!board.areEmptyRanks(Square('b', 3), Square('g', 3)))
  }

  test("diagonal emptiness is recognised correctly") {

    // act, assert
    assert(board.isEmptyDiag(Square('c', 6), Square('f', 3)))
    assert(board.isEmptyDiag(Square('f', 3), Square('c', 6)))
    assert(board.isEmptyDiag(Square('h', 1), Square('f', 3)))
    assert(board.isEmptyDiag(Square('c', 6), Square('a', 8)))

    assert(!board.isEmptyDiag(Square('g', 2), Square('e', 4)))
    assert(!board.isEmptyDiag(Square('d', 5), Square('b', 7)))
    assert(!board.isEmptyDiag(Square('g', 2), Square('b', 7)))
    assert(!board.isEmptyDiag(Square('h', 1), Square('a', 8)))
  }

  test("square occupancy is recognised correctly") {

    // setup
    val expWhites: Set[Square] = Set(whiteQueen.atPos, whiteKing.atPos)
    val expBlacks: Set[Square] = Set(blackKing.atPos)

    // act
    val whites: Set[Square] =
      Square.All.filter(board.isOccupiedBy(White, _)).toSet

    val blacks: Set[Square] =
      Square.All.filter(board.isOccupiedBy(Black, _)).toSet

    // assert
    assert(expWhites == whites)
    assert(expBlacks == blacks)
  }

  test("total weight function yields expected results") {

    // setup
    val expWhiteWeight: Int = whiteQueen.weight + whiteKing.weight
    val expBlackWeight: Int = blackKing.weight

    // act
    val whiteWeight: Int = board.totalWeight(White)
    val blackWeigh: Int = board.totalWeight(Black)

    // assert
    assert(expWhiteWeight == whiteWeight)
    assert(expBlackWeight == blackWeigh)
  }

  test("total count function yields expected results") {

    // act
    val whiteCount: Int = board.totalCount(White)
    val blackCount: Int = board.totalCount(Black)

    // assert
    assert(2 == whiteCount)
    assert(1 == blackCount)
  }

  test("captures weight function yields expected results") {

    // setup
    val expWhiteWeight: Int = blackKing.weight

    // act
    val whiteWeight: Int = board.capturesWeight(White)
    val blackWeigh: Int = board.capturesWeight(Black)

    // assert
    assert(expWhiteWeight == whiteWeight)
    assert(0 == blackWeigh)
  }
}

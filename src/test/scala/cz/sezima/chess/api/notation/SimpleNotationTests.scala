/*
 * Copyright (C) 2017, Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package cz.sezima.chess.api.notation

import cz.sezima.chess.core.Colors.{Black, White}
import cz.sezima.chess.core.board.{Board, Square}
import cz.sezima.chess.core.piece.{Move, Pawn, Rook}
import org.scalatest.FunSuite

/**
  * A set of [[SimpleNotation]] relevant unit tests.
  */
class SimpleNotationTests extends FunSuite {

  test("moves are recognised correctly"){

    // setup
    val expLegalMove: Move = Pawn(White, Square('e', 2)) ~> Square('e', 4)
    val expIllegalMove: Move = Rook(Black, Square('a', 8)) ~> Square('a', 6)

    // act
    val legalMove = SimpleNotation.decode("e2 -e4 ", Board.Initial)
    val illegalMove = SimpleNotation.decode(" a8- a6", Board.Initial)
    val unknownMove = SimpleNotation.decode("unknown", Board.Initial)

    // assert
    assert(legalMove.contains(expLegalMove))
    assert(illegalMove.contains(expIllegalMove))
    assert(unknownMove.isEmpty)
  }
}

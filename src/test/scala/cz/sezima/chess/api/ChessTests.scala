/*
 * Copyright (C) 2017, Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package cz.sezima.chess.api

import cz.sezima.chess.api.player.Player
import cz.sezima.chess.core.Colors.{Black, White}
import cz.sezima.chess.core.board.Board._
import cz.sezima.chess.core.board.{Board, Square}
import cz.sezima.chess.core.piece.{Move, Pawn, Queen}
import org.scalatest.FunSuite

/**
  * A set of [[Chess]] relevant unit tests.
  */
class ChessTests extends FunSuite {

  val foolsMate: List[Move] = List(
    Pawn(White, Square('f', 2)) ~> Square('f', 3),
    Pawn(Black, Square('e', 7)) ~> Square('e', 5),
    Pawn(White, Square('g', 2)) ~> Square('g', 4),
    Queen(Black, Square('d', 8)) ~> Square('h', 4)
  )

  val player: Player = new Player {
    override def play(board: Board): Move =
      foolsMate(board.history.size)
  }

  test("chess game ends with expected result") {

    // setup
    val expResult: Board =
      replay(foolsMate, Initial)

    // act
    val result: Board =
      Chess.play(player, player)

    // assert
    assert(result.isValid)
    assert(expResult == result)
  }

  test("arbitrary valid board could be used to initiate chess game") {

    // setup
    val nonInit: Board = replay(foolsMate.take(2), Initial)
    val expResult: Board = replay(foolsMate, Initial)

    // act
    val result: Board =
      Chess.play(player, player, nonInit)

    // assert
    assert(result.isValid)
    assert(expResult == result)
  }


  // helpers

  private def replay(moves: List[Move], at: Board): Board =
    moves.reverse.foldRight(Initial)(_.performAt(_).left.get)

}

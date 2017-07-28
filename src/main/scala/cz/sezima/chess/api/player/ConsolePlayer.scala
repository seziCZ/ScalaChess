/*
 * Copyright (c) 2017 Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package cz.sezima.chess.api.player

import scala.io.StdIn

import cz.sezima.chess.api.notation.Notation
import cz.sezima.chess.core.board.Board
import cz.sezima.chess.core.piece.Move

/**
  * A console [[Player]].
  * @param notation A [[Notation]] to be used when decoding user given [[Move]]s
  */
case class ConsolePlayer(notation: Notation) extends Player {

  override def play(board: Board): Move = {
    notify(s"It's ${board.onMove}'s turn, board looks as follows: \n$board")
    val userInput: String = StdIn.readLine("move > ")
    notation.decode(userInput, board).getOrElse {
      notify("Given move is invalid, try again.")
      play(board)
    }
  }
}

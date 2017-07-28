/*
 * Copyright (c) 2017 Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package cz.sezima.chess.api

import scala.annotation.tailrec

import cz.sezima.chess.api.player.Player
import cz.sezima.chess.core.Colors._
import cz.sezima.chess.core.board.Board
import cz.sezima.chess.core.piece.Piece

/**
  * Representation of a [[Chess]] game.
  * TODO: get rid of hardcoded messages
  */
object Chess {

  /**
    * Initiates a [[Chess]] game between given [[Player]]s.
    * @param white A [[Player]] to play [[White]] [[Piece]]s
    * @param black A [[Player]] to play [[Black]] [[Piece]]s
    * @return A result of a [[Chess]] game
    */
  def play(white: Player, black: Player): Board =
    play(white, black, Board.Initial)

  /**
    * Initiates a [[Chess]] game between given [[Player]]s on given [[Board]]
    * @param white A [[Player]] to play [[White]] [[Piece]]s
    * @param black A [[Player]] to play [[Black]] [[Piece]]s
    * @param at A [[Board]] to start with
    * @return A result of a [[Chess]] game
    */
  @tailrec
  def play(white: Player, black: Player, at: Board): Board = at match {
    case board if board.isCheckmated(board.onMove) =>
      white.notify(s"Game over, ${board.onMove.inv} player won!")
      black.notify(s"Game over, ${board.onMove.inv} player won!")
      board

    case board if !board.mayPlay(board.onMove) =>
      white.notify("Game over, it's a draw!")
      black.notify("Game over, it's a draw!")
      board

    case board if White == board.onMove => play(white, black, play(white, at))
    case board if Black == board.onMove => play(white, black, play(black, at))
  }

  @tailrec
  private def play(player: Player, at: Board): Board =
    at.letPlay(player) match {
      case Left(updatedBoard) => updatedBoard
      case Right(msg) =>
        player.notify(msg)
        play(player, at)
    }
}

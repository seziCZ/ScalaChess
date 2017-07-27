/*
 * Copyright (C) 2017, Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package cz.sezima.chess.api.player

import cz.sezima.chess.core.board.Board
import cz.sezima.chess.core.piece.Move

/**
  * Representation of a [[Player]].
  */
trait Player {

  /**
    * This [[Player]]'s [[Move]] at given [[Board]].
    * @param board A [[Board]] [[Player]] is supposed to [[Move]] at
    * @return [[Player]]'s [[Move]] as a response to given [[Board]]
    */
  def play(board: Board): Move

  /**
    * A callback that provides [[Player]] with additional information on
    * currently played game.
    * @param msg A [[String]] message
    */
  def notify(msg: String): Unit =
    println(msg)

}

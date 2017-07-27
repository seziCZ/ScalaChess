/*
 * Copyright (C) 2017, Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package cz.sezima.chess

import cz.sezima.chess.api.Chess
import cz.sezima.chess.api.notation.SimpleNotation
import cz.sezima.chess.api.player.{AlphaBetaPlayer, ConsolePlayer, Player}

/**
 * A [[Chess]] game application.
 */
object Application extends App {

  val white: Player = ConsolePlayer(SimpleNotation)
  val black: Player = AlphaBetaPlayer(2560000)
  Chess.play(white, black)
}

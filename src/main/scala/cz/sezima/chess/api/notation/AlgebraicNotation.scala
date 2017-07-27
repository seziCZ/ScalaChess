/*
 * Copyright (C) 2017, Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package cz.sezima.chess.api.notation

import cz.sezima.chess.core.board.Board
import cz.sezima.chess.core.piece.Move

/**
  * An [[AlgebraicNotation]].
  * TODO: Implement
  */
object AlgebraicNotation extends Notation {

  override def decode(move: String, at: Board): Option[Move] = ???

}

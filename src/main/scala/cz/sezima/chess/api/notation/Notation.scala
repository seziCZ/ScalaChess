/*
 * Copyright (C) 2017, Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package cz.sezima.chess.api.notation

import cz.sezima.chess.core.board.Board
import cz.sezima.chess.core.piece.Move

/**
  * Implementations of [[Notation]] trait understand certain [[String]]
  * representation of a [[Move]].
  */
trait Notation {

  /**
    * Decodes given [[String]] move to its [[Move]] image
    * @param move [[String]] representation of a [[Move]] to be decoded
    * @param at Current [[Board]] that might help with [[Move]] resolution
    * @return [[Move]] object representing given [[String]] entry,
    *        [[None]] if given [[String]] could not be decoded.
    */
  def decode(move: String, at: Board): Option[Move]

}

/*
 * Copyright (c) 2017 Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package cz.sezima.chess.core.piece

import scala.language.{implicitConversions, reflectiveCalls}

import cz.sezima.chess.core.Color
import cz.sezima.chess.core.board.{Board, Square}

/**
  * All chess pieces are expected to extend [[Piece]] trait that provides
  * core functionality needed for a piece to be able to move on a [[Board]].
  */
trait Piece {

  /**
    * Trait implementations are known to be case classes whose
    * constructors expect a color and a square...
    */
  this: { def copy(a: Color, b: Square): Piece } =>

  /**
    * A [[Color]] of a piece.
    * @return [[Color]] of a piece
    */
  def color: Color

  /**
    * A [[Square]] that is being occupied by 'this' [[Piece]]
    * @return This [[Piece]]'s square
    */
  def atPos: Square

  /**
    * A weight of a [[Piece]] that might be used when analysing
    * effectivity of individual [[Move]]s. The higher the number is,
    * the more important is a [[Piece]].
    * @return [[Int]] weight of 'this' [[Piece]]
    */
  def weight: Int

  /**
    * Returns mnemonic of 'this' [[Piece]]
    * @return 'this' [[Piece]]'s mnemonic
    */
  def symbol: Char

  /**
    * Determines whether 'this' [[Piece]] is allowed to move given
    * [[Square]] at given [[Board]].
    * @param to [[Square]] whose reachability is to be checked
    * @param at [[Board]] to be used for validation
    * @return 'True' if given [[Square]] may be reached
    *         by 'this' [[Piece]], 'false' otherwise
    */
  def mayMoveTo(to: Square, at: Board): Boolean

  /**
    * Instantiates [[Move]] object that represents movement of
    * 'this' [[Piece]] to given [[Square]].
    * @param to [[Square]] to which 'this' [[Piece]] is to be moved
    * @return [[Move]] object that represents movement
    *        of 'this' [[Piece]] to given [[Square]]
    */
  def ~>(to: Square): Move = Move(this, to)

  /**
    * 'this' [[Piece]] moved to given [[Square]]
    * @param to A [[Square]][[Piece]] is to be moved to
    * @return 'this' [[Piece]] moved to given [[Square]]
    */
  def ~>>(to: Square): Piece = this.copy(color, to)

  /**
    * Determines whether 'this' [[Piece]] may capture a piece that
    * occupies given [[Square]] at given [[Board]]
    * @param pos [[Square]] that hosts opponent's [[Piece]]
    * @param at [[Board]] to be used for validation
    * @return 'True' if [[Piece]] at given [[Square]] may be captured
    *         by 'this' [[Piece]], 'false' otherwise.
    */
  def mayCapture(pos: Square, at: Board): Boolean

  /**
    * Combination of [[mayMoveTo]] and [[mayCapture]] functions: determines
    * whether 'this' [[Piece]] is able to interact with given [[Square]]
    * @param pos [[Square]] whose reachabilty is to be determined
    * @param at [[Board]] to be used for validation
    * @return 'True' if 'this' [[Piece]] may interact with given [[Square]],
    *         'false' otherwise
    */
  def mayReach(pos: Square, at: Board): Boolean =
    mayMoveTo(pos, at) || mayCapture(pos, at)

  /**
    * Function that allows [[Board]] refinemens after every successful [[Move]]
    * of 'this' [[Piece]]. For more info see implementation of 'en passant' rule
    * at [[Pawn]] class or 'castling' associated with s [[King]]. Note that misuse
    * of this function may break [[Board]] properties that are relied on by other
    * 'impl' package entities so use it with caution!
    * @param board [[Board]] to be updated after the move
    * @return An updated [[Board]]
    */
  private[core] def reachCallback(board: Board): Board = board

  /**
    * Implicit conversion from [[Piece]] to [[Square]] it occupies.
    * @param piece [[Piece]] to be transformed
    * @return [[Square]] associated with 'this' [[Piece]]
    */
  implicit def asPos(piece: Piece): Square =
    piece.atPos

}

/*
 * Copyright (c) 2017 Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package cz.sezima.chess.core.piece

import cz.sezima.chess.core.board.{Board, Square}

/**
  * Representation of a [[Piece]] movement.
  * TODO: Get rid of hardcoded messages
  * @param piece A [[Piece]] that moves
  * @param dest Destination of [[Piece]] movement
  */
case class Move(piece: Piece, dest: Square) {

  /**
    * Performs 'this' [[Move]] on given [[Board]]. If all requirements are
    * met, updated [[Board]] is returned, [[String]] error message otherwise.
    * @param board A [[Board]] to be used for [[Move]] application
    * @return [[Either]] updated [[Board]] if all requirements are met
    *        or [[String]] error message
    */
  def performAt(board: Board): Either[Board, String] = {
    val validationMsg: Option[String] =
      validateColorAt(board)
        .orElse(validateMembershipAt(board))
        .orElse(validateMovementAt(board))
        .orElse(validateKingAt(board))

    lazy val updated: Board =
      piece.reachCallback(update(board))

    validationMsg
      .map(Right.apply)
      .getOrElse(Left(updated))
  }

  /**
    * Updates given [[Board]] using 'this' [[Move]] without checking its validity.
    * Note that this function has to be used with caution as its misuse may
    * break [[Board]] properties used by logic in 'impl' package.
    * @param b [[Board]] to be updated
    * @return Result of [[Move]] application on given [[Board]]
    */
  private[core] def update(b: Board): Board = {
    val toRemove = b.pieceAt(dest).toSeq :+ piece
    val newPieces = b.pieces.filterNot(toRemove.contains) :+ piece ~>> dest
    Board(newPieces, this +: b.history)
  }

  // validation functions

  private[piece] def validateColorAt(board: Board): Option[String] =
    if (board.onMove == piece.color) None
    else Some(s"It's opponent's move.")

  private[piece] def validateMembershipAt(board: Board): Option[String] =
    if (board.pieces.contains(piece)) None
    else Some(s"There is no piece on square ${piece.atPos}.")

  private[piece] def validateMovementAt(board: Board): Option[String] =
    if (piece.mayReach(dest, board)) None
    else Some(s"Piece ${piece.symbol} at ${piece.atPos} could not move to $dest.")

  private[piece] def validateKingAt(board: Board): Option[String] =
    if (!update(board).isInCheck(piece.color)) None
    else Some(s"Your king must not end up being threaten.")

  override def toString: String =
    s"${piece.symbol}${piece.atPos} ~> $dest"

}

/*
 * Copyright (C) 2017, Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package cz.sezima.chess.core.board

import scala.collection.SeqView
import scala.reflect.ClassTag

import cz.sezima.chess.api.player.Player
import cz.sezima.chess.core.Color
import cz.sezima.chess.core.Colors.{Black, White}
import cz.sezima.chess.core.piece._

/**
  * Representation of a chess board.
  */
case class Board private[core] (
    pieces: Seq[Piece],
    history: Seq[Move]
) {

  /**
    * [[Color]] of a [[Player]] that is expected to make a next move.
    */
  val atMove: Color =
    history.headOption.map(_.piece.color.inv).getOrElse(White)

  /**
    * Core function that allows given [[Player]] to play a [[Move]] on
    * 'this' [[Board]] instance.
    * @param player [[Player]] whose [[Move]] is to be played on 'this' [[Board]]
    * @return An updated [[Board]] if [[Player]]'s [[Move]] is valid,
    *         an error [[String]] message otherwise
    */
  def letPlay(player: Player): Either[Board, String] =
    player.play(this).performAt(this)

  /**
    * Determines whether there a [[Move]] exists that might be played by a
    * player of given [[Color]]. In another words, 'false' returned by this
    * function indicates that draw (or checkmate) has occurred...
    * @param color [[Color]] whose possible [[Move]]s are to be investigated
    * @return 'True' if there are any [[Move]]s given [[Color]] may play, 'false' otherwise
    */
  def mayPlay(color: Color): Boolean =
    genBoards(color).nonEmpty

  /**
    * Determines whether 'this' [[Board]] is coherent.
    * @return 'True' if board describes valid game, 'false' otherwise
    */
  lazy val isValid: Boolean = {
    val init: Either[Board, String] = Left(Board.Initial)
    val replayed = history.foldRight(init) {
      case (_, err: Right[Board, String]) => err
      case (m, Left(b)) => m.performAt(b)
    }

    replayed.isLeft
  }

  /**
    * Determines whether [[King]] of given [[Color]] faces a check.
    * @param color [[Color]] of a [[King]] whose state is to be determined
    * @return 'True' if [[King]] of given [[Color]] faces a check, 'false' otherwise
    */
  def isInCheck(color: Color): Boolean = {
    val king: King = pieceBy((_: King).color == color).get
    pieces.exists(_.mayCapture(king.atPos, this))
  }

  /**
    * Determines whether [[King]] of given [[Color]] faces a checkmate.
    * @param color [[Color]] of a [[King]] whose state is to be determined
    * @return 'True' if [[King]] of given [[Color]] faces a checkmate, 'false' otherwise
    */
  def isCheckmated(color: Color): Boolean =
    isInCheck(color) && !mayPlay(color)

  /**
    * Retrieves [[Option]]al [[Piece]] that occupies given [[Square]]
    * @param pos [[Square]] that hosts [[Piece]] to be retrieved
    * @return A [[Piece]] that occupies given [[Square]],
    *         [[None]] if no such a [[Piece]] exists
    */
  private[chess] def pieceAt(pos: Square): Option[Piece] =
    pieceBy((_: Piece).atPos == pos)

  /**
    * Generates [[SeqView]] of all [[Move]]s that make sense.
    * for [[Piece]]s of given [[Color]].
    * @param color [[Color]] of [[Piece]]s whose [[Move]]s are to be generated
    * @return A lazy [[SeqView]] containing all possible [[Move]]s
    *         of all [[Piece]]s of a given [[Color]]
    */
  private[chess] def genMoves(color: Color): SeqView[Move, Seq[_]] =
    for {
      piece <- pieces.filter(_.color == color).view
      square <- Square.All.view
      if piece.mayReach(square, this)
    } yield Move(piece, square)

  /**
    * Generates [[SeqView]] of all [[Board]]s that could be reached from
    * 'this' [[Board]]
    * @param color [[Color]] of [[Piece]]s whose [[Move]]s are to be used for [[Board]] generation
    * @return A lazy [[SeqView]] contating all possible [[Board]]s
    *         reachable by [[Piece]] of given [[Color]]
    */
  private[chess] def genBoards(color: Color): SeqView[Board, Seq[_]] =
    genMoves(color).flatMap(_.performAt(this).left.toOption)

  /**
    * A generic version of [[pieceAt]] function. An interesting thing here is that
    * parameter is being type checked which allows queries like (k: King) => ...
    * @param req A condition to be used when looking for a [[Piece]]
    * @return [[Piece]] that matches given condition, [[None]] if no such a [[Piece]] exists
    */
  private[chess] def pieceBy[T <: Piece](req: T => Boolean)(
      implicit ct: ClassTag[T]): Option[T] =
    pieces
      .find { case piece: T => req(piece); case _ => false }
      .asInstanceOf[Option[T]]

  override def toString: String = {
    val toSymbol = pieceAt(_: Square).map(_.symbol).getOrElse("_")
    val symbols = Square.All.map(toSymbol).grouped(8)
    symbols.map(_.mkString("|", "|", "|")).mkString("\n")
  }
}

/**
  * Complementary [[Board]] object that holds static methods and properties.
  */
object Board {

  /**
    * A [[Board]] that describes initial game setup.
    */
  val Initial: Board = {

    case class Factory(
        create: (Color, Square) => Piece,
        files: Iterable[Char],
        wRank: Int = 1,
        bRank: Int = 8)

    val factories = Seq(
      Factory(Pawn.apply, 'a' to 'h', 2, 7),
      Factory(Rook.apply, Seq('a', 'h')),
      Factory(Knight.apply, Seq('b', 'g')),
      Factory(Bishop.apply, Seq('c', 'f')),
      Factory(Queen.apply, Some('d')),
      Factory(King.apply, Some('e'))
    )

    val pieces: Seq[Seq[Piece]] =
      for {
        factory <- factories
        file <- factory.files
      } yield
        Seq(
          factory.create(White, Square(file, factory.wRank)),
          factory.create(Black, Square(file, factory.bRank)))

    Board(pieces.flatten, Seq.empty)
  }
}

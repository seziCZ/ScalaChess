/*
 * Copyright (c) 2017 Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package cz.sezima.chess.core.board

import cz.sezima.chess.core.Color
import cz.sezima.chess.core.Colors.{Black, White}

/**
  * Representation of a single [[Board]] [[Square]],
  * i.e. a position on a [[Board]].
  */
case class Square(file: Char, rank: Int) {
  require('a' to 'h' contains file)
  require(1 to 8 contains rank)

  /**
    * An [[x]] index of 'this' [[Square]]. The mapping between
    * files and numbers is natural, i.e. 'a' -> 0, 'b' -> 1, 'c' -> 2, etc.
    */
  lazy val x: Int = file.toByte - 'a'.toByte

  /**
    * An [[y]] index of 'this' [[Square]]. The mapping between
    * ranks and numbers is natural, i.e. '1' -> 0, '2' -> 1, etc.
    */
  lazy val y: Int = rank - 1

  /**
    * A [[Color]] of 'this' square.
    */
  lazy val color: Color =
    if ((x - y) % 2 == 0) Black else White

  /**
    * Asserts that 'this' entry shares file with given [[Square]]
    * @param pos [[Square]] whose file is to be compared to 'this' file
    * @return 'True' if given [[Square]] shares file with 'this' entry,
    *         'false' otherwise
    */
  def sharesFile(pos: Square): Boolean =
    file == pos.file

  /**
    * Asserts that 'this' entry shares rank with given [[Square]]
    * @param pos [[Square]] whose rank is to be compared to 'this' rank
    * @return 'True' if given [[Square]] shares rank with 'this' entry,
    *         'false' otherwise
    */
  def sharesRank(pos: Square): Boolean =
    rank == pos.rank

  /**
    * Asserts that 'this' entry shares diagonal with given [[Square]]
    * @param pos [[Square]] whose diagonal is to be compared to 'this' diagonal
    * @return 'True' if given [[Square]] shares diagonal with 'this' entry,
    *         'false' otherwise
    */
  def sharesDiag(pos: Square): Boolean =
    math.abs(file - pos.file) == math.abs(rank - pos.rank)

  /**
    * Generates all [[Square]] instances that are between 'this' and
    * given [[Square]] entry, exclusive (!).
    * @return [[Seq]] of [[Square]]s that reside between 'this' and
    *        given [[Square]] entries
    */
  def until(coordinate: Square): Seq[Square] = coordinate match {
    case to if sharesDiag(to) =>
      iter(to, _.file).zip(iter(to, _.rank)).map { case (f, r) => Square(f.toChar, r) }
    case to if sharesFile(to) => iter(to, _.rank).map(Square(file, _))
    case to if sharesRank(to) => iter(to, _.file).map(f => Square(f.toChar, rank))
    case _ => Seq.empty
  }

  // private helpers

  private def iter(to: Square, f: Square => Int): Seq[Int] = {
    val step = if (f(this) <= f(to)) 1 else -1
    (f(this) until f(to) by step).drop(1)
  }

  override def toString: String =
    s"$file$rank"

}

/**
  * Complementary [[Square]] object that holds static methods and properties.
  */
object Square {

  /**
    * Allows creation of [[Square]] from human readable [[String]] (e.g. 'c4')
    * @param square [[String]] representation of a [[Square]] to be created
    * @return [[Square]] created from given [[String]] representation
    * @throws IllegalArgumentException if given [[String]] is not a valid [[Square]]
    */
  def apply(square: String): Square = {
    require(square.length == 2)
    Square(square.head.toLower, square.tail.toInt)
  }

  /**
    * Collection of all possible [[Square]] instances.
    */
  val All: Seq[Square] =
    for {
      rank <- 8 to 1 by -1
      file <- 'a' to 'h'
    } yield Square(file, rank)
}

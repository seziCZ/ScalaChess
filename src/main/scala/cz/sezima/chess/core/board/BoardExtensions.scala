package cz.sezima.chess.core.board

import cz.sezima.chess.core.Color
import cz.sezima.chess.core.piece.{Move, Piece}

/**
  * When in scope, [[BoardExtensions]] extend functionality of
  * [[Board]] instances.
  */
object BoardExtensions {

  /**
    * Analytic [[Board]] extensions.
    * @param board [[Board]] whose functionality is to be extended.
    */
  implicit class AnalyticExts(board: Board) {

    /**
      * An occupancy check.
      * @return 'True' if given [[Square]]s are occupied by given [[Color]],
      *         'false' otherwise
      */
    def isOccupiedBy(color: Color, square: Square*) =
      square.forall(board.pieceAt(_).exists(_.color == color))

    /**
      * An emptiness check.
      * @return 'True' if given [[Square]] is not occupied,
      *         'false' otherwise
      */
    def isEmpty(square: Square*): Boolean =
      square.forall(board.pieceAt(_).isEmpty)

    /**
      * A file (column) emptiness check.
      * @param fromFile A bound of an interval to be inspected, exclusive
      * @param toFile The other bound of an interval, exclusive
      * @return 'True' if there are no pieces covered by the interval,
      *         'false' otherwise
      * @throws IllegalArgumentException if given [[Square]]s do not share files
      */
    def isEmptyFiles(fromFile: Square, toFile: Square): Boolean = {
      require(fromFile.sharesFile(toFile))
      isEmpty(fromFile until toFile: _*)
    }

    /**
      * A rank (row) emptiness check.
      * @param fromRank A bound of an interval to be inspected, exclusive
      * @param toRank The other bound of an interval, exclusive
      * @return 'True' if there are no pieces covered by the interval,
      *         'false' otherwise
      * @throws IllegalArgumentException if given [[Square]]s do not share ranks
      */
    def isEmptyRanks(fromRank: Square, toRank: Square): Boolean = {
      require(fromRank.sharesRank(toRank))
      isEmpty(fromRank until toRank: _*)
    }

    /**
      * Diagonal emptiness check.
      * @param diagStart A bound of an interval to be inspected, exclusive
      * @param diagEnd The other bound of an interval, exclusive
      * @return 'True' if there are no pieces covered by the interval,
      *         'false' otherwise
      * @throws IllegalArgumentException if given [[Square]]s do not share diagonal
      */
    def isEmptyDiag(diagStart: Square, diagEnd: Square): Boolean = {
      require(diagStart.sharesDiag(diagEnd))
      isEmpty(diagStart until diagEnd: _*)
    }
  }

  /**
    * Statistic [[Board]] extensions.
    * @param board [[Board]] whose functionality is to be extended.
    */
  implicit class StatisticExts(board: Board) {

    /**
      * Sums weights of 'this' [[Board]]'s [[Piece]]s of a given [[Color]].
      * @param color [[Color]] of [[Piece]]s whose weights are to be summed
      * @return Sum of given [[Color]] [[Piece]]'s weights
      */
    def totalWeight(color: Color): Int =
      board.pieces.filter(_.color == color).map(_.weight).sum

    /**
      * Yields count of 'this' [[Board]]'s [[Piece]]s of a given [[Color]]
      * @param color [[Color]] of [[Piece]]s whose count is to be found
      * @return Number of [[Piece]]s of given [[Color]] on 'this' [[Board]]
      */
    def totalCount(color: Color): Int =
      board.pieces.count(_.color == color)

    /**
      * Yields number of [[Move]]s given [[Color]] may play
      * @param color [[Color]] whose possible [[Move]]s are to be counted
      * @return Total number of given [[Color]]'s possible [[Move]]s
      */
    def totalMoves(color: Color): Int =
      board.genBoards(color).length

    /**
      * Sums weights of all [[Piece]]s that might be captured by given [[Color]]
      * @return Weight of all [[Piece]]s that might be captured by given [[Color]]
      */
    def capturesWeight(color: Color): Int = {
      val weights: Iterable[Int] =
        for {
          capturing <- board.pieces.filter(_.color == color)
          captured <- board.pieces.filter(_.color != color)
          if capturing.mayCapture(captured.atPos, board)
        } yield captured.weight

      weights.sum
    }
  }
}

/*
 * Copyright (c) 2017 Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */
package cz.sezima.chess.api.player

import scala.math._

import cz.sezima.chess.api.player.AlphaBetaPlayer._
import cz.sezima.chess.core.board.Board
import cz.sezima.chess.core.board.BoardExtensions._
import cz.sezima.chess.core.piece.Move

/**
  * An artificial [[Player]] implemented using alpha-beta pruning.
  * For more info see e.g. 'https://chessprogramming.wikispaces.com/Alpha-Beta'
  * @param maxMoves Maximal allowed number of [[Move]]s to be generated
  */
case class AlphaBetaPlayer(maxMoves: Int) extends Player {

  override def play(seed: Board): Move = {
    val exp = log(maxMoves) / log(seed.totalMoves(seed.onMove))
    val depth: Int = max(2, exp.toInt - exp.toInt % 2)
    αβ(seed, depth).history.reverse(seed.history.length)
  }

  override def notify(msg: String): Unit = {
    // i.e. there is nobody to be notified...
  }
}

/**
  * Complementary [[AlphaBetaPlayer]] object that holds static methods and
  * properties.
  */
object AlphaBetaPlayer {

  /**
    * An alpha-beta pruning algorithm.
    * TODO: Both evaluation function and board generation logic suffers of
    * (obvious) performance bottlenecks that should be optimized/refined.
    * @param seed An initial state in form of a [[Board]]
    * @return Number of evaluation tree levels to be examined
    */
  private[player] def αβ(seed: Board, depth: Int): Board = {

    /**
      * Representation of either α or β bound together with
      * [[Board]] that produced the value.
      * @param bound  Either α or β depending on execution context
      * @param result A [[Board]] that produced the bound
      */
    case class Aβ(bound: Int, result: Board) {
      lazy val inv = Aβ(-bound, result)
    }

    /**
      * A maximizer.
      * @param b  Current [[Board]] whose children are to be examined
      * @param depth Remaining number of levels to be examined
      * @param α     the best [[αβMax]] value guaranteed by the level above
      * @param β     the best [[αβMin]] value guaranteed by the level above
      */
    def αβMax(b: Board, depth: Int, α: Aβ, β: Aβ): Aβ =
      b.genBoards(b.onMove) match {
        case children if children.isEmpty || depth == 0 => αβEval(b, depth)
        case children =>
          children.foldLeft(α) {
            case (newα, child) if newα.bound >= β.bound => return β
            case (newα, child) =>
              val childBound = αβMin(child, depth - 1, newα, β)
              if (childBound.bound > newα.bound) childBound else newα
          }
      }

    /**
      * A minimizer.
      * @param b  Current [[Board]] whose children are to be examined
      * @param depth Remaining number of levels to be examined
      * @param α     the best [[αβMax]] value guaranteed by the level above
      * @param β     the best [[αβMin]] value guaranteed by the level above
      */
    def αβMin(b: Board, depth: Int, α: Aβ, β: Aβ): Aβ =
      b.genBoards(b.onMove) match {
        case children if depth == 0 || children.isEmpty => αβEval(b, depth).inv
        case children =>
          children.foldLeft(β) {
            case (newβ, child) if newβ.bound <= α.bound => return α
            case (newβ, child) =>
              val childBound = αβMax(child, depth - 1, α, newβ)
              if (childBound.bound < newβ.bound) childBound else newβ
          }
      }

    /**
      * An evaluation function. For more info what next steps might be see
      * https://chessprogramming.wikispaces.com/Evaluation
      * @param step  A [[Board]] to be evaluated
      * @param depth '0' if desired depth has been reached,
      *              non zero integer if player did not have any moves left
      * @return An [[Aβ]] that contains resulting value
      */
    def αβEval(step: Board, depth: Int): Aβ = step match {
      case b if depth != 0 => αβEval(step, depth - 1).inv
      case b if b.isCheckmated(seed.onMove) => Aβ(999 * Byte.MinValue, b)
      case b if b.isCheckmated(seed.onMove.inv) => Aβ(999 * Byte.MaxValue, b)
      case b => Aβ(b.totalWeight(b.onMove) - b.totalWeight(b.onMove.inv), b)
    }

    val α = Aβ(Int.MinValue, seed)
    val β = Aβ(Int.MaxValue, seed)
    αβMax(seed, depth, α, β).result
  }
}

/*
 * Copyright (C) 2017, Tomas Sezima
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package cz.sezima.chess.core

import cz.sezima.chess.core.Colors.{Black, White}
import org.scalatest.FunSuite

/**
 * A set of [[Color]] relevant unit tests.
 */
class ColorTest extends FunSuite {

  test("inversion function yields expected colors"){

    // assert
    assert(White.inv == Black)
    assert(Black.inv == White)
  }
}

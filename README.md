# Scala Chess

An idiomatic Scala chess implementation that leverages the best of both
imperative and functional programming paradigms. My intention was to write a
code that is as concise as possible,
```
val white: Player = ConsolePlayer(notation = SimpleNotation)
val black: Player = AlphaBetaPlayer(maxMoves = 2560000)
Chess.play(white, black)
```
any contribution is highly appreciated though!
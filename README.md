# Scala Chess

An idiomatic Scala chess implementation that yileds the best of both imperative and functional programming paradigms. The code is as concise as possible,
```
val white: Player = ConsolePlayer(notation = SimpleNotation)
val black: Player = AlphaBetaPlayer(maxIterations = 2560000)
Chess.play(white, black)
```
any contribution is highly appreciated though!
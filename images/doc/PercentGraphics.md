# PercentGraphics

`PercentGraphics` is a wrapper around `Graphics2D` that allows you to draw on a `GameImage` using percentage-based coordinates instead of pixels. This is useful for creating resolution-independent graphics.

[Back to Menu](Menu.md)

## Getting a PercentGraphics instance

You can get a `PercentGraphics` instance from a `GameImage` using the `drawPercent` method.

```kotlin
val gameImage = GameImage(512, 512)
gameImage.drawPercent { percentGraphics ->
    // Draw using percentGraphics
}
```

## Drawing Shapes

You can draw various shapes using percentage values for coordinates and dimensions.

```kotlin
percentGraphics.color = RED
// Draw a line from (10%, 10%) to (90%, 90%)
percentGraphics.drawLine(0.1, 0.1, 0.9, 0.9)

percentGraphics.color = BLUE
// Draw a filled rectangle at (25%, 25%) with a size of (50%, 50%)
percentGraphics.fillRectangle(0.25, 0.25, 0.5, 0.5)
```

Available drawing methods include:

*   `drawLine`
*   `drawRectangle`, `fillRectangle`
*   `drawRoundRectangle`, `fillRoundRectangle`
*   `drawOval`, `fillOval`

## Drawing Text and Images

You can also draw text and images using percentage-based coordinates.

```kotlin
// Draw text at (50%, 50%)
percentGraphics.drawText(0.5, 0.5, "Centered Text", TextAlignment.CENTER)

// Draw an image at (10%, 10%)
val image: java.awt.Image = ...
percentGraphics.drawImage(0.1, 0.1, image)
```

[Back to Menu](Menu.md)

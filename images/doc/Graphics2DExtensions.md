# Graphics2D Extensions

This library provides several helpful extension functions for `java.awt.Graphics2D` to simplify common drawing tasks.

[Back to Menu](Menu.md)

## Drawing Text

### `drawText`

The `drawText` extension function allows you to draw a string with a specific alignment.

```kotlin
graphics2D.drawText(50, 50, "Hello, World!", TextAlignment.CENTER)
```

### `fillText`

The `fillText` extension function allows you to draw a filled text, with word wrapping.

```kotlin
graphics2D.fillText(50, 50, "This is a long text that will be wrapped.", limitHorizontal = 150)
```

## Drawing Images

You can easily draw a `GameImage` on a `Graphics2D` context.

```kotlin
val gameImage: GameImage = ...
graphics2D.drawImage(10, 10, gameImage)
graphics2D.drawImage(10, 10, 64, 64, gameImage) // Draw with scaling
graphics2D.drawPart(10, 10, 0, 0, 32, 32, gameImage) // Draw a part of the image
graphics2D.drawImageCenter(100, 100, gameImage) // Draw the image centered at a point
```

## Setting Colors

You can set the color of the `Graphics2D` context using the library's `Color` and `BaseColor` types.

```kotlin
import fr.khelp.zegaime.images.color.RED
import fr.khelp.zegaime.images.color.base.Blue

graphics2D.setColor(RED)
graphics2D.baseColor(Blue.BLUE_0500)
```

See [Color.md](Color.md) for more information on colors.

[Back to Menu](Menu.md)

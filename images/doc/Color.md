# Color

The library provides a flexible color system with support for ARGB and AYUV color models.

[Back to Menu](Menu.md)

## The `Color` Interface

The `Color` interface is the base for all color types in the library. The two main implementations are `ARGB` and `AYUV`.

*   **ARGB**: Represents a color with Alpha, Red, Green, and Blue components.
*   **AYUV**: Represents a color with Alpha, Luma (Y), and Chrominance (U, V) components.

## Creating Colors

You can create colors using the provided factory functions:

```kotlin
val red = rgb(255, 0, 0)
val transparentGray = gray(128, 128)
val customAYUV = ayuv(255, 128, 0, 0)
```

## Predefined Colors

The library provides a set of predefined colors for common use cases.

```kotlin
import fr.khelp.zegaime.images.color.RED
import fr.khelp.zegaime.images.color.GREEN
import fr.khelp.zegaime.images.color.BLUE
import fr.khelp.zegaime.images.color.WHITE
import fr.khelp.zegaime.images.color.BLACK
```

## Color Conversions

You can easily convert between different color representations.

```kotlin
val argbColor: ARGB = ...
val ayuvColor = argbColor.toAyuv

val color: Color = ...
val argbInt = color.argb // Get the ARGB integer value
```

## Color Manipulation

You can manipulate colors, for example, by changing their brightness.

```kotlin
val originalColor: Color = ...
val brighterColor = originalColor.changBrightness(1.5) // 50% brighter
val darkerColor = originalColor.changBrightness(0.5)   // 50% darker
```

[Back to Menu](Menu.md)

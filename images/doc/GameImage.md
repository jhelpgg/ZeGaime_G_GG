# GameImage

The `GameImage` class is the core of the image manipulation capabilities of this library. It provides a way to load, create, and modify images.

[Back to Menu](Menu.md)

## Creating a GameImage

### Loading from a Stream

You can load an image from an `InputStream` using the `GameImage.load` method.

```kotlin
val inputStream: InputStream = ...
val gameImage = GameImage.load(inputStream)
```

You can also create a thumbnail from an image stream:

```kotlin
val inputStream: InputStream = ...
val thumbnail = GameImage.loadThumbnail(inputStream, 64, 64) // Creates a 64x64 thumbnail
```

### Creating a blank image

You can create a blank `GameImage` with a specific width and height.

```kotlin
val blankImage = GameImage(128, 128) // Creates a 128x128 image
```

## Drawing on a GameImage

You can draw on a `GameImage` using a `Graphics2D` context.

```kotlin
val image = GameImage(256, 256)
image.draw { graphics2D ->
    graphics2D.setColor(java.awt.Color.BLUE)
    graphics2D.fillRect(50, 50, 150, 150)
}
```

You can also use `PercentGraphics` for percentage-based drawing. See [PercentGraphics.md](PercentGraphics.md) for more details.

## Image Manipulations

`GameImage` provides several methods for image manipulation:

*   `resize(width, height)`: Resizes the image.
*   `copy()`: Creates a copy of the image.
*   `gray()`: Converts the image to grayscale.
*   `tint(color)`: Tints the image with a given color.
*   `colorize(color)`: Colorizes the image with a given color.
*   `contrast(contrast)`: Adjusts the image contrast.
*   `rotate90()`, `rotate180()`, `rotate270()`: Rotates the image.
*   `flipHorizontal()`, `flipVertical()`, `flipBoth()`: Flips the image.

## Pixel Manipulation

You can directly access and modify the pixels of a `GameImage`.

*   `grabPixels()`: Returns an array of the image's pixels in ARGB format.
*   `putPixels(x, y, width, height, pixels)`: Replaces a portion of the image with the given pixel data.

[Back to Menu](Menu.md)

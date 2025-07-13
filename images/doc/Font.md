# Font

The library provides a `JHelpFont` class for working with fonts and rendering text.

[Back to Menu](Menu.md)

## Creating a JHelpFont

### From System Fonts

You can create a `JHelpFont` from a system-installed font.

```kotlin
val arial = JHelpFont("Arial", 16)
val boldTimes = JHelpFont("Times New Roman", 24, bold = true)
```

### From a Stream

You can load a font from an `InputStream`. This is useful for bundling custom fonts with your application.

```kotlin
val inputStream: InputStream = ...
val customFontFuture = JHelpFont.createFont(FontType.TRUE_TYPE, inputStream, 18,
                                            bold = FontValue.AS_DEFINED,
                                            italic = FontValue.AS_DEFINED,
                                            underline = false)

customFontFuture.onSucceed { customFont ->
    // Use the loaded font
}
```

## Using Fonts

Once you have a `JHelpFont` instance, you can use it to:

*   **Measure text:** `stringWidth()`, `stringSize()`, `computeTextParagraph()`
*   **Get font metrics:** `fontHeight`, `ascent`, `maximumCharacterWidth`
*   **Create shapes from text:** `shape()`

## Predefined Fonts

The library provides a set of predefined fonts for common UI elements.

```kotlin
import fr.khelp.zegaime.images.font.DEFAULT_FONT
import fr.khelp.zegaime.images.font.TEXT_FONT
import fr.khelp.zegaime.images.font.TITLE_FONT
```

## Text Paragraphs

The `computeTextParagraph` method allows you to lay out a block of text with alignment and width/height limits.

```kotlin
val font = JHelpFont("Arial", 14)
val paragraph = font.computeTextParagraph("This is a long text that will be wrapped and aligned.",
                                         TextAlignment.CENTER, limitWidth = 150)

// You can then iterate over the lines of the paragraph
for (textLine in paragraph.textLines) {
    // ...
}
```

[Back to Menu](Menu.md)

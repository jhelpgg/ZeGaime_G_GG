# IO

The `io` package provides utilities for working with files and streams.

## `BaseDirectories`

The `BaseDirectories` object provides access to common directories like the home directory and the temporary directory.

### Usage

```kotlin
val home = homeDirectory
val temp = temporaryDirectory
```

You can also create temporary files and directories.

```kotlin
val tempFile = createTemporaryFile("my_temp_file.txt")
val tempDir = createTemporaryDirectory()
```

## `FileExtensions`

The `FileExtensions` object provides extensions for the `File` class.

### `createDirectory` and `createFile`

The `createDirectory` and `createFile` extensions create a directory or a file, including the parent directories if necessary.

```kotlin
val myDir = File("my/new/dir")
myDir.createDirectory()

val myFile = File("my/new/file.txt")
myFile.createFile()
```

### `deleteFull`

The `deleteFull` extension deletes a file or a directory recursively.

```kotlin
myDir.deleteFull()
```

## `InputStreamExtensions` and `OutputStreamExtensions`

These objects provide extensions for `InputStream` and `OutputStream` to read and write primitive types and arrays.

### Usage

```kotlin
val outputStream = FileOutputStream("my_file.bin")
outputStream.writeInt(42)
outputStream.close()

val inputStream = FileInputStream("my_file.bin")
val myInt = inputStream.readInt()
inputStream.close()
```

[Menu](Menu.md)

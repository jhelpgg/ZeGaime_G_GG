# Sound

To create a sound, you must use the `SoundFactory`. It provides several methods to create sounds from different sources.

The library supports different sound formats like MP3, MIDI, and others. The correct file extension in the file name or URL is crucial for the library to correctly identify the sound format.

## Create a sound from a file

You can create a sound from a local file using `soundFromFile(file: File)`.

```kotlin
val soundFile = File("/path/to/my/sound.mp3")
val sound = soundFromFile(soundFile)
sound.play()
```

Make sure the file has the correct extension (e.g., `.mp3`, `.mid`, `.wav`).

## Create a sound from a URL

You can create a sound from a URL using `soundFromURL(url: URL)`. The sound will be downloaded and played.

```kotlin
val soundUrl = URL("http://example.com/sounds/music.mp3")
val sound = soundFromURL(soundUrl)
sound.play()
```

The URL should point directly to a sound file with a proper extension.

## Create a sound from a Stream

For more advanced use cases, you can create a sound from an `InputStream` using `soundFromStream(streamProducer: () -> InputStream, fileName: String)`.

You need to provide a function that returns an `InputStream` and a file name. The file name is important as its extension will be used to determine the sound type.

```kotlin
val inputStreamProducer = { FileInputStream("/path/to/my/sound.wav") }
val sound = soundFromStream(inputStreamProducer, "mySound.wav")
sound.play()
```

## Sound Controls

Once you have a `Sound` object, you can control its playback.

### Play

To start or resume the sound, use the `play()` method.

```kotlin
sound.play()
```

### Pause

To pause the sound, use the `pause()` method. You can resume it later with `play()`.

```kotlin
sound.pause()
```

### Stop

To stop the sound completely, use the `stop()` method. This will also stop any looping.

```kotlin
sound.stop()
```

### Loop

To play the sound in a loop, use the `loop(loop: Int)` method. By default, it loops indefinitely.

```kotlin
// Loop 5 times
sound.loop(5)

// Loop "infinitely"
sound.loop()
```

### Position

You can get or set the current playback position in bytes.

```kotlin
// Get current position
val currentPosition = sound.position

// Seek to a specific position (e.g., 1024 bytes)
sound.position = 1024L
```

### Destroy

When you are finished with a sound, you should call `destroy()` to release its resources. After this call, the sound object is no longer usable.

```kotlin
sound.destroy()
```

You can also set `destroyOnEnd` to `true` to automatically destroy the sound when it finishes playing.

```kotlin
sound.destroyOnEnd = true
sound.play() // The sound will be destroyed at the end
```

## Observing Sound State

The `Sound` class provides observables to monitor its state and progress.

- `soundStateObservable: Observable<SoundState>`: Notifies when the sound state changes (e.g., PLAYING, PAUSED, STOPPED).
- `soundProgressObservable: Observable<SoundProgress>`: Notifies about the playback progress, providing current position and total size.

```kotlin
sound.soundStateObservable.register { state ->
    println("Sound state changed: $state")
}

sound.soundProgressObservable.register { progress ->
    println("Sound progress: ${progress.position} / ${progress.total}")
}
```

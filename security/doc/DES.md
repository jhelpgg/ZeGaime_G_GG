# DES and TripleDES

The `des` package provides classes for encrypting and decrypting data using the DES and TripleDES algorithms.

## `CrypterDES`

The `CrypterDES` class provides a simple way to encrypt and decrypt data using a password.

### Usage

To create a `CrypterDES` instance, you need to provide a password.

```kotlin
val crypter = CrypterDES("my_password")
```

Then, you can use the `encrypt` and `decrypt` methods to encrypt and decrypt streams.

```kotlin
val clearText = "This is a secret message"
val clearStream = ByteArrayInputStream(clearText.toByteArray())
val encryptedStream = ByteArrayOutputStream()
crypter.encrypt(clearStream, encryptedStream)

val encryptedData = encryptedStream.toByteArray()

val encryptedInputStream = ByteArrayInputStream(encryptedData)
val decryptedStream = ByteArrayOutputStream()
crypter.decrypt(encryptedInputStream, decryptedStream)

val decryptedText = decryptedStream.toString()
```

## `TripleDES`

The `TripleDES` class provides a more secure way to encrypt and decrypt data using a login and a password. It uses three `CrypterDES` instances with different keys derived from the login and password.

### Usage

To create a `TripleDES` instance, you need to provide a login and a password.

```kotlin
val tripleDES = TripleDES("my_login", "my_password")
```

The usage is the same as `CrypterDES`.

[Menu](Menu.md)

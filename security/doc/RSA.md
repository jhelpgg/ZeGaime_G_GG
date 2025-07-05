# RSA

The `rsa` package provides classes for encrypting and decrypting data using the RSA algorithm.

## `RSAKeyPair` and `RSAPublicKey`

The `RSAKeyPair` class represents a pair of public and private keys. The `RSAPublicKey` class represents the public key, which can be shared with others.

### Creation

You can create a new `RSAKeyPair` to generate a new pair of keys.

```kotlin
val keyPair = RSAKeyPair()
val publicKey = keyPair.publicKey
```

You can also save and load a key pair using a `TripleDES` crypter.

```kotlin
val tripleDES = TripleDES("my_login", "my_password")
val outputStream = FileOutputStream("my_key_pair.bin")
keyPair.save(tripleDES, outputStream)

val inputStream = FileInputStream("my_key_pair.bin")
val loadedKeyPair = RSAKeyPair(tripleDES, inputStream)
```

### Encryption and decryption

You can encrypt data using the public key and decrypt it using the key pair.

```kotlin
val clearText = "This is a secret message"
val clearStream = ByteArrayInputStream(clearText.toByteArray())
val encryptedStream = ByteArrayOutputStream()
publicKey.encrypt(clearStream, encryptedStream)

val encryptedData = encryptedStream.toByteArray()

val encryptedInputStream = ByteArrayInputStream(encryptedData)
val decryptedStream = ByteArrayOutputStream()
keyPair.decrypt(encryptedInputStream, decryptedStream)

val decryptedText = decryptedStream.toString()
```

### Signature

You can sign a message using the key pair and verify the signature using the public key.

```kotlin
val message = "This is a message to sign"
val messageStream = ByteArrayInputStream(message.toByteArray())
val signatureStream = ByteArrayOutputStream()
keyPair.sign(messageStream, signatureStream)

val signature = signatureStream.toByteArray()

val messageInputStream = ByteArrayInputStream(message.toByteArray())
val signatureInputStream = ByteArrayInputStream(signature)
val valid = publicKey.validSignature(messageInputStream, signatureInputStream)
```

[Menu](Menu.md)

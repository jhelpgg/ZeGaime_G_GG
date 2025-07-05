# Signature

You can sign a message using the `RSAKeyPair` and verify the signature using the `RSAPublicKey`.

## Signing a message

To sign a message, you need an `RSAKeyPair`.

```kotlin
val keyPair = RSAKeyPair()
val message = "This is a message to sign"
val messageStream = ByteArrayInputStream(message.toByteArray())
val signatureStream = ByteArrayOutputStream()
keyPair.sign(messageStream, signatureStream)
val signature = signatureStream.toByteArray()
```

## Verifying a signature

To verify a signature, you need the corresponding `RSAPublicKey`.

```kotlin
val publicKey = keyPair.publicKey
val messageInputStream = ByteArrayInputStream(message.toByteArray())
val signatureInputStream = ByteArrayInputStream(signature)
val valid = publicKey.validSignature(messageInputStream, signatureInputStream)
```

[Menu](Menu.md)

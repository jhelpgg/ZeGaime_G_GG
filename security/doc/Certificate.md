# Certificate

You can create a self-signed certificate using the `RSAKeyPair`. A certificate is basically a signed public key.

## Creating a self-signed certificate

To create a self-signed certificate, you need to create an `RSAKeyPair` and then sign its public key with its own private key.

```kotlin
val keyPair = RSAKeyPair()
val publicKey = keyPair.publicKey

// The "message" is the public key itself
val publicKeyStream = ByteArrayOutputStream()
publicKey.save(publicKeyStream)
val publicKeyBytes = publicKeyStream.toByteArray()
val messageStream = ByteArrayInputStream(publicKeyBytes)

val signatureStream = ByteArrayOutputStream()
keyPair.sign(messageStream, signatureStream)
val signature = signatureStream.toByteArray()
```

The certificate is the combination of the public key and the signature.

## Verifying a self-signed certificate

To verify a self-signed certificate, you need the public key and the signature.

```kotlin
val messageInputStream = ByteArrayInputStream(publicKeyBytes)
val signatureInputStream = ByteArrayInputStream(signature)
val valid = publicKey.validSignature(messageInputStream, signatureInputStream)
```

[Menu](Menu.md)

package me.lambdatamer.kandroid.security

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.ironz.binaryprefs.encryption.ValueEncryption

internal class TinkValueEncryption(context: Context, private val aead: Aead) : ValueEncryption {
    private val signature = CryptoUtils.getSignatureSha(context).takeLast(16).toByteArray()

    override fun encrypt(plaintext: ByteArray): ByteArray =
        Base64.encode(aead.encrypt(plaintext, signature), Base64.DEFAULT)

    override fun decrypt(cipher: ByteArray): ByteArray =
        aead.decrypt(Base64.decode(cipher, Base64.DEFAULT), signature)
}
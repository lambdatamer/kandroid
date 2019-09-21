package me.lambdatamer.kandroid.security

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.DeterministicAead
import com.ironz.binaryprefs.encryption.KeyEncryption

internal class TinkKeyEncryption(
    context: Context,
    private val aead: DeterministicAead
) : KeyEncryption {
    private val signature = CryptoUtils.getSignatureSha(context)
    private val encoderFlags = Base64.NO_WRAP or Base64.URL_SAFE

    override fun encrypt(plaintext: String): String = Base64.encodeToString(
        aead.encryptDeterministically(plaintext.toByteArray(), signature),
        encoderFlags
    )

    override fun decrypt(cipher: String) =
        String(aead.decryptDeterministically(Base64.decode(cipher, encoderFlags), signature))
}
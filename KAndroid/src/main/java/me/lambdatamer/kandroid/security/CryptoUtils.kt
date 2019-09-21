package me.lambdatamer.kandroid.security

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@Suppress("unused", "MemberVisibilityCanBePrivate")
internal object CryptoUtils {

    @Suppress("DEPRECATION")
    @TargetApi(Build.VERSION_CODES.P)
    @SuppressLint("PackageManagerGetSignatures")
    fun getSignatureSha(context: Context): ByteArray {
        val signatures = with(context.packageManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                    .signingInfo
                    .apkContentsSigners
            } else {
                getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES).signatures
            }
        }

        return sha256(signatures.first().toByteArray())
    }

    fun sha256(text: String, encodedBase64: Boolean = false): String {
        val hashBytes = sha256(text)

        return if (encodedBase64) {
            Base64.encodeToString(hashBytes, Base64.NO_WRAP)
        } else {
            hashBytes.map { String.format("%02x", it) }.reduce { acc, s -> acc + s }
        }
    }

    fun sha256(text: String) = sha256(text.toByteArray())

    fun sha256(byteArray: ByteArray): ByteArray {
        val digest = try {
            MessageDigest.getInstance("SHA-256")
        } catch (e: NoSuchAlgorithmException) {
            MessageDigest.getInstance("SHA")
        }

        return with(digest) {
            update(byteArray)
            digest()
        }
    }

    fun base64Encode(bytes: ByteArray): String = Base64.encodeToString(bytes, Base64.DEFAULT)

    fun base64Decode(string: String): ByteArray = Base64.decode(string, Base64.DEFAULT)
}
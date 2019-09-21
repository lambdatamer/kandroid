package me.lambdatamer.kandroid.security

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.DeterministicAead
import com.google.crypto.tink.aead.AeadFactory
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.config.TinkConfig
import com.google.crypto.tink.daead.DeterministicAeadFactory
import com.google.crypto.tink.daead.DeterministicAeadKeyTemplates
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.ironz.binaryprefs.Preferences

object EncryptedPreferencesUtils {
    private const val KEYSET_NAME = "master_keyset"
    private const val PREFERENCE_FILE = "master_key_preference"
    private const val MASTER_KEY_URI = "android-keystore://master_key"

    private const val DKEYSET_NAME = "dmaster_keyset"
    private const val DPREFERENCE_FILE = "dmaster_key_preference"
    private const val DMASTER_KEY_URI = "android-keystore://dmaster_key"

    init {
        TinkConfig.register()
    }

    fun create(
        context: Context,
        aead: Aead,
        daead: DeterministicAead
    ): Preferences = BinaryPreferencesBuilder(context)
        .keyEncryption(TinkKeyEncryption(context, daead))
        .valueEncryption(TinkValueEncryption(context, aead))
        .build()

    fun createAead(
        context: Context,
        keysetName: String = KEYSET_NAME,
        preferenceFile: String = PREFERENCE_FILE,
        masterKeyUri: String = MASTER_KEY_URI
    ): Aead = AndroidKeysetManager.Builder()
        .withSharedPref(context, keysetName, preferenceFile)
        .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
        .withMasterKeyUri(masterKeyUri)
        .build()
        .keysetHandle
        .let(AeadFactory::getPrimitive)

    fun createDaead(
        context: Context,
        dKeysetName: String = DKEYSET_NAME,
        dPreferenceFile: String = DPREFERENCE_FILE,
        dMasterKey: String = DMASTER_KEY_URI
    ): DeterministicAead = AndroidKeysetManager.Builder()
        .withSharedPref(context, dKeysetName, dPreferenceFile)
        .withKeyTemplate(DeterministicAeadKeyTemplates.AES256_SIV)
        .withMasterKeyUri(dMasterKey)
        .build()
        .keysetHandle
        .let(DeterministicAeadFactory::getPrimitive)
}

@Suppress("FunctionName", "unused")
fun EncryptedPreferences(
    context: Context,
    aead: Aead = EncryptedPreferencesUtils.createAead(context),
    daead: DeterministicAead = EncryptedPreferencesUtils.createDaead(context)
) = EncryptedPreferencesUtils.create(context, aead, daead)
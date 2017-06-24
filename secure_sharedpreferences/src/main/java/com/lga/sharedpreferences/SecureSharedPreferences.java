package com.lga.sharedpreferences;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;

import com.lga.sharedpreferences.encryption.EncryptionAlgorithm;
import com.lga.sharedpreferences.encryption.EncryptionHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by Jay.X
 *
 * Decorates SharedPreferences with AES Encryption.
 */
class SecureSharedPreferences implements SharedPreferences {

    private SharedPreferences prefs;
    private EncryptionAlgorithm encryption;
    private EncryptionHelper helper;

    /**
     * Initializes with a single {@link SharedPreferences}
     * and the {@link com.lga.sharedpreferences.encryption.Encryption} to use.
     *
     * @param preferences The {@link SharedPreferences} to use.
     * @param encryption The {@link com.lga.sharedpreferences.encryption.Encryption} to use.
     */
    SecureSharedPreferences(SharedPreferences preferences, EncryptionAlgorithm encryption) {
        this.prefs = preferences;
        this.encryption = encryption;
        helper = new EncryptionHelper(encryption);
    }

    @Override
    public boolean contains(String key) {
        return prefs.contains(key);
    }

    @Override
    public SecuredEditor edit() {
        return new SecuredEditor(helper, prefs.edit());
    }

    @Override
    public Map<String, ?> getAll() {
        return prefs.getAll();
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return helper.readAndDecodeTemplate(prefs, key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return helper.readAndDecodeTemplate(prefs, key, defValue);
    }

    @Override
    public int getInt(String key, int defValue) {
        return helper.readAndDecodeTemplate(prefs, key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return helper.readAndDecodeTemplate(prefs, key, defValue);
    }

    @Override
    public String getString(String key, String defValue) {
        return helper.readAndDecodeTemplate(prefs, key, defValue);
    }

    @TargetApi(value = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return helper.readAndDecodeTemplate(prefs, key, defValues);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }

    protected EncryptionAlgorithm getEncryption() {
        return encryption;
    }

    protected SharedPreferences getPrefs() {
        return prefs;
    }

    protected void setHelper(EncryptionHelper helper) {
        this.helper = helper;
    }
}

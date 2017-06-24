package com.lga.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.lga.sharedpreferences.encryption.Encryption;
import com.lga.sharedpreferences.encryption.EncryptionAlgorithm;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

/**
 * Created by Jay.X
 *
 * A factory import static android.R.attr.author;
 * class to ease the creation of the SecureSharedPreferences instance.
 */
public final class SecureFactory {

    private static final String INITIALIZATION_ERROR = "Can not initialize SecureSharedPreferences";
    private static final int VERSION_1 = 1;
    public static final int LATEST_VERSION = VERSION_1;

    /**
     * Hidden util constructor.
     */
    private SecureFactory() {
    }

    public static SharedPreferences getPreferences(Context context) {
        String name = context.getPackageName();
        String password = name;
        return getPreferences(context, name, password);
    }

    /**
     * Creates a {@link SecureSharedPreferences} instance.
     *
     * @param context The current context.
     * @param preferencesName The name of the {@link SharedPreferences}.
     * @param password The password
     * @return The initialized {@link SecureSharedPreferences}.
     */
    public static SecureSharedPreferences getPreferences(Context context, String preferencesName, String password) {
        try {
            return getPreferences(context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE), new Encryption(password));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new SecurityException(INITIALIZATION_ERROR, e);
        }
    }

    /**
     * Creates the {@link SecureSharedPreferences} instance with a given original and an {@link EncryptionAlgorithm}.
     * This function does a version check and the required migrations when the local structure is outdated or not encrypted yet.
     *
     * @param original The original {@link SharedPreferences}, which can be also a {@link SecureSharedPreferences} instance.
     * @param password The password to use. This will use the {@link Encryption} implementation of the {@link EncryptionAlgorithm}.
     * @return A {@link SecureSharedPreferences} instance.
     * @throws SecurityException When the {@link EncryptionAlgorithm} can not be initialized.
     */
    public static SecureSharedPreferences getPreferences(SharedPreferences original, String password) throws SecurityException {
        try {
            EncryptionAlgorithm encryption = new Encryption(password);
            return getPreferences(original, encryption);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new SecurityException(INITIALIZATION_ERROR, e);
        }
    }

    /**
     * Creates the {@link SecureSharedPreferences} instance with a given original and an {@link EncryptionAlgorithm}.
     * This function does a version check and the required migrations when the local structure is outdated or not encrypted yet.
     *
     * @param original The original {@link SharedPreferences}, which can be also a {@link SecureSharedPreferences} instance.
     * @param encryption The {@link EncryptionAlgorithm} to use.
     * @return A {@link SecureSharedPreferences} instance.
     */
    public static SecureSharedPreferences getPreferences(SharedPreferences original, EncryptionAlgorithm encryption) {
        SecureSharedPreferences sharedPreferences;
        if (original instanceof SecureSharedPreferences) {
            sharedPreferences = (SecureSharedPreferences) original;
        } else {
            sharedPreferences = new SecureSharedPreferences(original, encryption);
        }
        if (SecureUtils.getVersion(sharedPreferences) < VERSION_1) {
            SecureUtils.migrateData(original, sharedPreferences, VERSION_1);
        }
        return sharedPreferences;
    }
}

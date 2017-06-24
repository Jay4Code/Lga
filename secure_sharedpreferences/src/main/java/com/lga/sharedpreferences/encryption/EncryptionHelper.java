package com.lga.sharedpreferences.encryption;

import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Jay.X
 *
 * Encrypting / decrypting support algorithms and type conversions.
 */
public class EncryptionHelper {

    private EncryptionAlgorithm encryption;

    /**
     * Initializes with ecryption.
     *
     * @param encryption The {@link EncryptionAlgorithm} to use.
     */
    public EncryptionHelper(EncryptionAlgorithm encryption) {
        super();
        this.encryption = encryption;
    }

    /**
     * Reads a value from a {@link SharedPreferences}.
     *
     * @param <T> The type of the result and the default value.
     * @param prefs The preferences to use.
     * @param key The key to read.
     * @param defValue The default value, when the key does not exist.
     * @return Return the T type of result.
     */
    public <T> T readAndDecodeTemplate(SharedPreferences prefs, String key, T defValue) {
        T result = defValue;
        ObjectInputStream ois = readDecoded(prefs, key);
        if (ois != null) {
            try {
                result = (T) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Encodes a single value to string. May result null on an internal problem.
     *
     * @param <T> The type of the value.
     * @param value The T type of value to encrypt.
     * @return The encrypted value as string.
     */
    public <T> String encode(T value) {
        String result = null;
        if (value != null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(value);
                byte[] byteArray = baos.toByteArray();
                byte[] encrypt = encryption.encrypt(byteArray);
                result = Base64.encodeToString(encrypt, Base64.DEFAULT);
            } catch (IOException | EncryptionException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private ObjectInputStream readDecoded(SharedPreferences prefs, String key) {
        String stringValue = prefs.getString(key, null);
        ObjectInputStream result;
        if (stringValue != null) {
            try {
                result = createDecodedObjectStream(stringValue);
            } catch (EncryptionException e) {
                e.printStackTrace();
                result = null;
            }
        } else {
            result = null;
        }
        return result;
    }

    private ObjectInputStream createDecodedObjectStream(String stringValue) throws EncryptionException {
        byte[] decodedBytes = Base64.decode(stringValue, Base64.DEFAULT);
        byte[] decoded = encryption.decrypt(decodedBytes);
        try {
            return new ObjectInputStream(new ByteArrayInputStream(decoded));
        } catch (IOException e) {
            throw new EncryptionException(e);
        }
    }
}

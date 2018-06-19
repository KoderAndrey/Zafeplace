package com.zafeplace.sdk.stellarsdk.sdk;

import com.zafeplace.sdk.stellarsdk.sdk.xdr.SignerKey;
import com.zafeplace.sdk.stellarsdk.sdk.xdr.SignerKeyType;
import com.zafeplace.sdk.stellarsdk.sdk.xdr.Uint256;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Signer is a helper class that creates {@link com.zafeplace.sdk.stellarsdk.sdk.xdr.SignerKey} objects.
 */
public class Signer {
    /**
     * Create <code>ed25519PublicKey</code> {@link com.zafeplace.sdk.stellarsdk.sdk.xdr.SignerKey} from
     * a {@link com.zafeplace.sdk.stellarsdk.sdk.KeyPair}
     * @param keyPair
     * @return com.zafeplace.sdk.stellarsdk.sdk.xdr.SignerKey
     */
    public static SignerKey ed25519PublicKey(KeyPair keyPair) {
        checkNotNull(keyPair, "keyPair cannot be null");
        return keyPair.getXdrSignerKey();
    }

    /**
     * Create <code>sha256Hash</code> {@link com.zafeplace.sdk.stellarsdk.sdk.xdr.SignerKey} from
     * a sha256 hash of a preimage.
     * @param hash
     * @return com.zafeplace.sdk.stellarsdk.sdk.xdr.SignerKey
     */
    public static SignerKey sha256Hash(byte[] hash) {
        checkNotNull(hash, "hash cannot be null");
        SignerKey signerKey = new SignerKey();
        Uint256 value = Signer.createUint256(hash);

        signerKey.setDiscriminant(SignerKeyType.SIGNER_KEY_TYPE_HASH_X);
        signerKey.setHashX(value);

        return signerKey;
    }

    /**
     * Create <code>preAuthTx</code> {@link com.zafeplace.sdk.stellarsdk.sdk.xdr.SignerKey} from
     * a {@link com.zafeplace.sdk.stellarsdk.sdk.xdr.Transaction} hash.
     * @param tx
     * @return com.zafeplace.sdk.stellarsdk.sdk.xdr.SignerKey
     */
    public static SignerKey preAuthTx(Transaction tx) {
        checkNotNull(tx, "tx cannot be null");
        SignerKey signerKey = new SignerKey();
        Uint256 value = Signer.createUint256(tx.hash());

        signerKey.setDiscriminant(SignerKeyType.SIGNER_KEY_TYPE_PRE_AUTH_TX);
        signerKey.setPreAuthTx(value);

        return signerKey;
    }

    /**
     * Create <code>preAuthTx</code> {@link com.zafeplace.sdk.stellarsdk.sdk.xdr.SignerKey} from
     * a transaction hash.
     * @param hash
     * @return com.zafeplace.sdk.stellarsdk.sdk.xdr.SignerKey
     */
    public static SignerKey preAuthTx(byte[] hash) {
        checkNotNull(hash, "hash cannot be null");
        SignerKey signerKey = new SignerKey();
        Uint256 value = Signer.createUint256(hash);

        signerKey.setDiscriminant(SignerKeyType.SIGNER_KEY_TYPE_PRE_AUTH_TX);
        signerKey.setPreAuthTx(value);

        return signerKey;
    }

    private static Uint256 createUint256(byte[] hash) {
        if (hash.length != 32) {
            throw new RuntimeException("hash must be 32 bytes long");
        }
        Uint256 value = new Uint256();
        value.setUint256(hash);
        return value;
    }
}
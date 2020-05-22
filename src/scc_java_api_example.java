// TODO

abstract interface SecureCryptoConfigInterface {

    public SCCCiphertext encrypt(key SCCKey, String plaintext);
    public SCCCiphertextStream encrypt(key SCCKey, Stream plaintext);
    public SCCCiphertext[] encrypt(key SCCKey[], String plaintext);
    public SCCCiphertext reEncrypt(key SCCKey, String plaintext);
    public String decrypt(key SCCKey, SCCCiphertext sccciphertext);

    public SCCHash hash(String plaintext);
    public SCCHash reHash(String plaintext);
    public boolean verify(plaintext, SCCHash scchash); // plaintext.verify(hash) / hash.verify(plaintext)
    
    public SCCPasswordHash passwordHash(String password);
    public boolean verifyPassword(String password, SCCPasswordHash sccpasswordhash);

}

abstract class SCCCiphertext extends String {

    SCCCiphertext SCCCiphertext(String ciphertext, SCCAlgorithmParameters parameters) {
        // create string with ciphertext and parameters
    }

    AlgorithmIdentifier getAlgorithmIdentifier(SCCCiphertext sccciphertext) {
        //sccciphertext.substring()
    }

}

abstract class AlgorithmIdentifier {
    // const 
}

abstract class SCCKey extends java.crypto.SecureKey {
    
    enum SCCKeyType {
        Symmetric,
        Asymmetric
    }
    
    SCCKey Key(byte[] bytes) {
        SCCKey key = new SCCKey();
        key.fromBytes(bytes);
        return key;
    }

    SCCKeyType getSCCKeyType() {
        // determine key type
        return SCCKeyType.Symmetric;
    }
    
}

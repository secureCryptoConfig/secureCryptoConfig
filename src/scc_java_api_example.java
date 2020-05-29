// TODO

abstract interface SecureCryptoConfigInterface {
	//COSE/CBOR returning?
	//Plaintext as String / byte [] ?
	//methods for symmetric encryption
    public SCCCiphertext symmetricEncrypt(key SCCKey, String plaintext);  
    public SCCCiphertext reSymmetricEncrypt(key SCCKey, String plaintext); 
    public String symmetricDecrypt(key SCCKey, SCCCiphertext sccciphertext);  
	
	//should they be realized?
	//public SCCCiphertextStream encrypt(key SCCKey, Stream plaintext);
    //public SCCCiphertext[] encrypt(key SCCKey[], String plaintext);
	
	//methods for asymmetric encryption
	public SCCCiphertext asmmetricEncrypt(key SCCKey, String plaintext); 
    public SCCCiphertext reAsymmetricEncrypt(key SCCKey, String plaintext); 
    public String asymmetricDecrypt(key SCCKey, SCCCiphertext ciphertext);  
	
	//should they be realized?
	//public SCCCiphertextStream encrypt(key SCCKey, Stream plaintext);
    //public SCCCiphertext[] encrypt(key SCCKey[], String plaintext);
	
	//methods for hashing
    public SCCHash hash(String plaintext); 
    public SCCHash reHash(String plaintext);
    public boolean verifyHash(plaintext, SCCHash hash); // plaintext.verify(hash) / hash.verify(plaintext)
	
	//methods for signing
	public SCCSignature sign(privateKey SCCKey, String plaintext);  
    public SCCSignature reSign(key SCCKey, String plaintext); 
    public boolean validteSignature(publicKey SCCKey, SCCSignature signature); 
	
	//methods for password hashing
    public SCCPasswordHash passwordHash(String password);
    public boolean verifyPassword(String password, SCCPasswordHash passwordhash);
	
	//methods for key generation?
	
	//methods for CSPRNG?

}

abstract class SCCCiphertext extends String {

    SCCCiphertext sCCCiphertext(String ciphertext, SCCAlgorithmParameters parameters) {
        // create string with ciphertext and parameters
    }

    AlgorithmIdentifier getAlgorithmIdentifier(SCCCiphertext sccciphertext) {
        //sccciphertext.substring()
    }

}

abstract class SCCAlgorithmParameters(){

}

abstract class AlgorithmIdentifier {
	//named defined in IANA registry
    enum AlgorithmID {
        AEAD_AES_256_GCM,
        AEAD_AES_512_GCM,
		sha3-512, 
		//...
    }
	
	//get AlgorithmID out of CBORObject e.g. for decrypt
	AlgorithmID getIDfromCBOR(CBORObject o){
		//..
		return algorithm
	}
}

abstract class SCCKey extends java.crypto.SecreteKey {
    
    enum SCCKeyType {
        Symmetric,
        Asymmetric
    }
    
    SCCKey createKey(byte[] bytes) {
        SCCKey key = new SCCKey();
        key.fromBytes(bytes);
        return key;
    }

    SCCKeyType getSCCKeyType() {
        // determine key type
        return SCCKeyType.Symmetric;
    }
    
}

abstract class SCCHash(){

}

abstract class SCCPasswordHash(){

}

abstract class SCCSignature(){

}

package main;

import java.nio.charset.Charset;
import COSE.CoseException;
import main.SCCKey.KeyType;

abstract interface SecureCryptoConfigInterface {

	// Symmetric Encryption
	
	public AbstractSCCCiphertext encryptSymmetric(AbstractSCCKey key, PlaintextContainerInterface plaintext)
			throws CoseException;

	public AbstractSCCCiphertext encryptSymmetric(AbstractSCCKey key, byte[] plaintext) throws CoseException;

	public AbstractSCCCiphertext reEncryptSymmetric(AbstractSCCKey key, AbstractSCCCiphertext ciphertext)
			throws CoseException;

	public PlaintextContainerInterface decryptSymmetric(AbstractSCCKey key, AbstractSCCCiphertext sccciphertext)
			throws CoseException;

	// Asymmetric
	
	public AbstractSCCCiphertext encryptAsymmetric(AbstractSCCKey keyPair, PlaintextContainerInterface plaintext)
			throws CoseException;

	public AbstractSCCCiphertext encryptAsymmetric(AbstractSCCKey keyPair, byte[] plaintext) throws CoseException;

	public AbstractSCCCiphertext reEncryptAsymmetric(AbstractSCCKey keyPair, AbstractSCCCiphertext ciphertext)
			throws CoseException;

	public PlaintextContainerInterface decryptAsymmetric(AbstractSCCKey keyPair, AbstractSCCCiphertext ciphertext)
			throws CoseException;

	// Hashing

	public AbstractSCCHash hash(PlaintextContainerInterface plaintext) throws CoseException;

	public AbstractSCCHash hash(byte[] plaintext) throws CoseException;

	public AbstractSCCHash updateHash(PlaintextContainerInterface plaintext, AbstractSCCHash hash) throws CoseException;

	public AbstractSCCHash updateHash(byte[] plaintext, AbstractSCCHash hash) throws CoseException;

	public boolean validateHash(PlaintextContainerInterface plaintext, AbstractSCCHash hash) throws CoseException;

	public boolean validateHash(byte[] plaintext, AbstractSCCHash hash) throws CoseException;

	// Digital Signature
	
	public AbstractSCCSignature sign(AbstractSCCKey keyPair, PlaintextContainerInterface plaintext)
			throws CoseException;

	public AbstractSCCSignature sign(AbstractSCCKey keyPair, byte[] plaintext) throws CoseException;

	public AbstractSCCSignature updateSignature(AbstractSCCKey keyPair, PlaintextContainerInterface plaintext)
			throws CoseException;
	
	public AbstractSCCSignature updateSignature(AbstractSCCKey keyPair, byte[] plaintext)
			throws CoseException;
	
	public boolean validateSignature(AbstractSCCKey keyPair, AbstractSCCSignature signature);
	
	public boolean validateSignature(AbstractSCCKey keyPair, byte[] signature);

	public AbstractSCCPasswordHash passwordHash(PlaintextContainerInterface password) throws CoseException;

	public AbstractSCCPasswordHash passwordHash(byte[] password) throws CoseException;

	public boolean validatePasswordHash(PlaintextContainerInterface password, AbstractSCCPasswordHash passwordhash)
			throws CoseException;

	public boolean validatePasswordHash(byte[] password, AbstractSCCPasswordHash passwordhash)
			throws CoseException;
}

abstract interface PlaintextContainerInterface {

	abstract byte[] toBytes();

	abstract String toString(Charset c);

	abstract boolean validateHash(AbstractSCCHash hash);

	abstract boolean validatePasswordHash(AbstractSCCPasswordHash passwordHash);

	abstract AbstractSCCCiphertext encryptSymmetric(AbstractSCCKey key);

	abstract AbstractSCCCiphertext encryptAsymmetric(AbstractSCCKey pair);

	abstract AbstractSCCSignature sign(AbstractSCCKey keyPair);

	abstract AbstractSCCHash hash();

	abstract AbstractSCCPasswordHash passwordHash();

}

abstract class AbstractSCCCiphertext {
	
	byte[] msg;

	public AbstractSCCCiphertext(byte[] msg) {
		this.msg = msg;
	}

	abstract byte[] toBytes();

	abstract String toString(Charset c);

	abstract PlaintextContainerInterface decryptSymmetric(AbstractSCCKey key);

	abstract PlaintextContainerInterface decryptAsymmetric(AbstractSCCKey keyPair);

	abstract AbstractSCCCiphertext reEncryptSymmetric(AbstractSCCKey key);

	abstract AbstractSCCCiphertext reEncryptAsymmetric(AbstractSCCKey keyPair);

}

abstract class AbstractSCCKey {

	KeyType type;
	byte[] key, privateKey, publicKey;
	String algorithm;
	
	protected AbstractSCCKey(KeyType type, byte[] publicKey, byte[] privateKey, String algorithm)
	{
		this.type = type;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.algorithm = algorithm;
		
	}
	
	protected AbstractSCCKey(KeyType type, byte[] key, String algorithm)
	{
		this.type = type;
		this.key = key;
		this.algorithm = algorithm;
		
	}

	abstract byte[] toBytes();

	abstract byte[] getPublicKeyBytes();
	
	abstract byte[] getPrivateKeyBytes();
	
	abstract String getAlgorithm();

}

abstract class AbstractSCCHash {

	byte[] hashMsg;

	public AbstractSCCHash(byte[] hashMsg) {
		this.hashMsg = hashMsg;
	}

	abstract byte[] toBytes();
	
	abstract String toString(Charset c);
	
	abstract boolean validateHash(PlaintextContainerInterface plaintext);
	
	abstract boolean validateHash(byte[] plaintext);

	abstract AbstractSCCHash updateHash(PlaintextContainerInterface plaintext);
	
	abstract AbstractSCCHash updateHash(byte[] plaintext);
	

}

abstract class AbstractSCCPasswordHash {

	byte[] hashMsg;
	

	public AbstractSCCPasswordHash(byte[] hashMsg) {
		this.hashMsg = hashMsg;
	}

	abstract byte[] toBytes();
	
	abstract String toString(Charset c);
	
	abstract boolean validatePasswordHash(PlaintextContainerInterface password);
	
	abstract boolean validatePasswordHash(byte[] password);

}

abstract class AbstractSCCSignature {
	byte[] signatureMsg;

	public AbstractSCCSignature(byte[] signatureMasg) {
		this.signatureMsg = signatureMasg;
	}

	abstract byte[] toBytes();

	abstract String toString(Charset c);

	abstract boolean validateSignature(AbstractSCCKey keyPair);

	abstract AbstractSCCSignature updateSignature(PlaintextContainerInterface plaintext, AbstractSCCKey keyPair);

}



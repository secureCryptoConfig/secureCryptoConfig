package main;

import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import COSE.CoseException;
import main.SCCKey.SCCKeyAlgorithm;

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
	public AbstractSCCCiphertext encryptAsymmetric(AbstractSCCKeyPair keyPair, PlaintextContainerInterface plaintext)
			throws CoseException;

	public AbstractSCCCiphertext encryptAsymmetric(AbstractSCCKeyPair keyPair, byte[] plaintext) throws CoseException;

	public AbstractSCCCiphertext reEncryptAsymmetric(AbstractSCCKeyPair keyPair, AbstractSCCCiphertext ciphertext)
			throws CoseException;

	public PlaintextContainerInterface decryptAsymmetric(AbstractSCCKeyPair keyPair, AbstractSCCCiphertext ciphertext)
			throws CoseException;

	// Hashing
	public AbstractSCCHash hash(PlaintextContainerInterface plaintext) throws CoseException;

	public AbstractSCCHash hash(byte[] plaintext) throws CoseException;

	public AbstractSCCHash updateHash(PlaintextContainerInterface plaintext, AbstractSCCHash hash) throws CoseException;

	public AbstractSCCHash updateHash(byte[] plaintext, AbstractSCCHash hash) throws CoseException;

	public boolean validateHash(PlaintextContainerInterface plaintext, AbstractSCCHash hash) throws CoseException;

	public boolean validateHash(byte[] plaintext, AbstractSCCHash hash) throws CoseException;

	// Digital Signature
	public AbstractSCCSignature sign(AbstractSCCKeyPair key, PlaintextContainerInterface plaintext)
			throws CoseException;

	public AbstractSCCSignature sign(AbstractSCCKeyPair key, byte[] plaintext) throws CoseException;

	public AbstractSCCSignature updateSignature(PlaintextContainerInterface plaintext, AbstractSCCKeyPair keyPair)
			throws CoseException;
	
	public AbstractSCCSignature updateSignature(byte[] plaintext, AbstractSCCKeyPair keyPair)
			throws CoseException;

	public boolean validateSignature(AbstractSCCKeyPair keyPair, AbstractSCCSignature signature);

	// Password Hashing
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

	abstract SCCCiphertext encryptSymmetric(AbstractSCCKey key);

	abstract SCCCiphertext encryptAsymmetric(AbstractSCCKeyPair pair);

	abstract SCCSignature sign(AbstractSCCKeyPair keyPair);

	abstract SCCHash hash();

	abstract SCCPasswordHash passwordHash();

}

abstract class AbstractSCCCiphertext {
	byte[] msg;

	public AbstractSCCCiphertext(byte[] msg) {
		this.msg = msg;
	}

	abstract byte[] toBytes();

	abstract String toString(Charset c);

	abstract PlaintextContainer decryptAsymmetric(AbstractSCCKeyPair keyPair);

	abstract PlaintextContainer decryptSymmetric(AbstractSCCKey key);

	abstract SCCCiphertext reEncryptSymmetric(AbstractSCCKey key);

	abstract SCCCiphertext reEncryptAsymmetric(AbstractSCCKeyPair keyPair);

}

abstract class AbstractSCCKey {

	byte[] key;
	SCCKeyAlgorithm algorithm;

	protected AbstractSCCKey(byte[] key, SCCKeyAlgorithm algorithm) {
		this.key = key;
		this.algorithm = algorithm;

	}

	abstract byte[] toBytes();

}

abstract class AbstractSCCKeyPair {
	KeyPair pair;

	protected AbstractSCCKeyPair(KeyPair pair) {
		this.pair = pair;
	}

	abstract byte[] getPublicKeyBytes();

	abstract byte[] getPrivateKeyBytes();

}

abstract class AbstractSCCHash {

	byte[] hashMsg;

	public AbstractSCCHash(byte[] hashMsg) {
		this.hashMsg = hashMsg;
	}

	abstract byte[] toBytes();
	
	abstract String toString(Charset c);
	
	abstract boolean validateHash(PlaintextContainerInterface plaintext);

	abstract SCCHash updateHash(PlaintextContainerInterface plaintext);
	

}

abstract class AbstractSCCPasswordHash {

	byte[] hashMsg;
	

	public AbstractSCCPasswordHash(byte[] hashMsg) {
		this.hashMsg = hashMsg;
	}

	abstract byte[] toBytes();
	
	abstract String toString(Charset c);
	
	abstract boolean validatePasswordHash(PlaintextContainerInterface password);

}

abstract class AbstractSCCSignature {
	byte[] signatureMsg;

	public AbstractSCCSignature(byte[] signatureMasg) {
		this.signatureMsg = signatureMasg;
	}

	abstract byte[] toBytes();

	abstract String toString(Charset c);

	abstract boolean validateSignature(AbstractSCCKeyPair keyPair);

	abstract SCCSignature updateSignature(PlaintextContainerInterface plaintext, AbstractSCCKeyPair keyPair);

}
}

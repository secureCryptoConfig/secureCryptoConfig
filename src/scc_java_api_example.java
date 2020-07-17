import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import COSE.CoseException;
import main.SCCKey.SCCKeyAlgorithm;
import main.SCCKeyPair.SCCKeyPairAlgorithm;

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
	public AbstractSCCSignature sign(AbstractSCCKeyPair keyPair, PlaintextContainerInterface plaintext)
			throws CoseException;

	public AbstractSCCSignature sign(AbstractSCCKeyPair keyPair, byte[] plaintext) throws CoseException;

	public AbstractSCCSignature updateSignature(AbstractSCCKeyPair keyPair, PlaintextContainerInterface plaintext)
			throws CoseException;
	
	public AbstractSCCSignature updateSignature(AbstractSCCKeyPair keyPair, byte[] plaintext)
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

	abstract AbstractSCCCiphertext encryptSymmetric(AbstractSCCKey key);

	abstract AbstractSCCCiphertext encryptAsymmetric(AbstractSCCKeyPair pair);

	abstract AbstractSCCSignature sign(AbstractSCCKeyPair keyPair);

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

	abstract PlaintextContainerInterface decryptAsymmetric(AbstractSCCKeyPair keyPair);

	abstract PlaintextContainerInterface decryptSymmetric(AbstractSCCKey key);

	abstract AbstractSCCCiphertext reEncryptSymmetric(AbstractSCCKey key);

	abstract AbstractSCCCiphertext reEncryptAsymmetric(AbstractSCCKeyPair keyPair);

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
	KeyPair keyPair;

	protected AbstractSCCKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
	}

	abstract byte[] getPublicKeyBytes();
	
	abstract byte[] getPrivateKeyBytes();
	
	abstract PublicKey getPublicKey();

	abstract PrivateKey getPrivateKey();

	abstract KeyPair getKeyPair();

}

abstract class AbstractSCCHash {

	byte[] hashMsg;

	public AbstractSCCHash(byte[] hashMsg) {
		this.hashMsg = hashMsg;
	}

	abstract byte[] toBytes();
	
	abstract String toString(Charset c);
	
	abstract boolean validateHash(PlaintextContainerInterface plaintext);

	abstract AbstractSCCHash updateHash(PlaintextContainerInterface plaintext);
	

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

	abstract AbstractSCCSignature updateSignature(PlaintextContainerInterface plaintext, AbstractSCCKeyPair keyPair);

}


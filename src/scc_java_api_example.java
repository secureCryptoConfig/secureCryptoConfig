package org.securecryptoconfig;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import org.securecryptoconfig.SCCKey.KeyType;
import org.securecryptoconfig.SCCKey.KeyUseCase;

import COSE.CoseException;

public abstract interface SecureCryptoConfigInterface {

//Symmetric 
public AbstractSCCCiphertext encryptSymmetric(AbstractSCCKey key, PlaintextContainerInterface plaintext)
    throws SCCException;

public AbstractSCCCiphertext encryptSymmetric(AbstractSCCKey key, byte[] plaintext)
    throws SCCException;

public AbstractSCCCiphertext reEncryptSymmetric(AbstractSCCKey key, AbstractSCCCiphertext ciphertext)
    throws SCCException;

public PlaintextContainerInterface decryptSymmetric(AbstractSCCKey key, AbstractSCCCiphertext sccciphertext)
    throws SCCException;

// Asymmetric
public AbstractSCCCiphertext encryptAsymmetric(AbstractSCCKey key, PlaintextContainerInterface plaintext)
    throws SCCException;

public AbstractSCCCiphertext encryptAsymmetric(AbstractSCCKey key, byte[] plaintext)
    throws SCCException;

public AbstractSCCCiphertext reEncryptAsymmetric(AbstractSCCKey key, AbstractSCCCiphertext ciphertext)
    throws SCCException;

public PlaintextContainerInterface decryptAsymmetric(AbstractSCCKey key, AbstractSCCCiphertext ciphertext)
    throws SCCException;

// Hashing
public AbstractSCCHash hash(PlaintextContainerInterface plaintext) 
    throws SCCException;

public AbstractSCCHash hash(byte[] plaintext) 
    throws SCCException;

public AbstractSCCHash updateHash(PlaintextContainerInterface plaintext, AbstractSCCHash hash)
    throws SCCException;

public AbstractSCCHash updateHash(byte[] plaintext, AbstractSCCHash hash) 
    throws SCCException;

public boolean validateHash(PlaintextContainerInterface plaintext, AbstractSCCHash hash)
    throws SCCException;

public boolean validateHash(byte[] plaintext, AbstractSCCHash hash) 
    throws SCCException;

// Digital Signature
public AbstractSCCSignature sign(AbstractSCCKey key, PlaintextContainerInterface plaintext)
    throws SCCException;

public AbstractSCCSignature sign(AbstractSCCKey key, byte[] plaintext)
    throws SCCException;

public AbstractSCCSignature updateSignature(AbstractSCCKey key, PlaintextContainerInterface plaintext)
    throws SCCException;

public AbstractSCCSignature updateSignature(AbstractSCCKey key, byte[] plaintext)
    throws SCCException;

public boolean validateSignature(AbstractSCCKey key, AbstractSCCSignature signature)
    throws SCCException;

public boolean validateSignature(AbstractSCCKey key, byte[] signature) 
    throws SCCException;

// Password Hashing
public AbstractSCCPasswordHash passwordHash(PlaintextContainerInterface password)
    throws SCCException;

public AbstractSCCPasswordHash passwordHash(byte[] password) 
    throws SCCException;

public boolean validatePasswordHash(PlaintextContainerInterface password, AbstractSCCPasswordHash passwordhash)
    throws SCCException;

public boolean validatePasswordHash(byte[] password, AbstractSCCPasswordHash passwordhash)
    throws SCCException;
}

abstract interface PlaintextContainerInterface {

public abstract byte[] toBytes();

public abstract String toString(Charset c);

@Override
public abstract String toString();

public abstract boolean validateHash(AbstractSCCHash hash) 
    throws SCCException;

public abstract boolean validatePasswordHash(AbstractSCCPasswordHash passwordHash) 
    throws SCCException;

public abstract AbstractSCCCiphertext encryptSymmetric(AbstractSCCKey key) 
    throws SCCException;

public abstract AbstractSCCCiphertext encryptAsymmetric(AbstractSCCKey key) 
    throws SCCException;

public abstract AbstractSCCSignature sign(AbstractSCCKey key) 
    throws SCCException;
    
public abstract boolean validateSignature (AbstractSCCSignature signature, AbstractSCCKey key) 
    throws SCCException;

public abstract AbstractSCCHash hash() 
    throws SCCException;

public abstract AbstractSCCPasswordHash passwordHash() 
    throws SCCException;
}

abstract class AbstractSCCCiphertext {

byte[] msg;

protected AbstractSCCCiphertext(byte[] msg) {
this.msg = msg;
}

public abstract byte[] toBytes();

@Override
public abstract String toString();

public abstract PlaintextContainerInterface decryptSymmetric(AbstractSCCKey key) 
    throws SCCException;

public abstract PlaintextContainerInterface decryptAsymmetric(AbstractSCCKey key) 
    throws SCCException;

public abstract AbstractSCCCiphertext reEncryptSymmetric(AbstractSCCKey key) 
    throws SCCException;


public abstract AbstractSCCCiphertext reEncryptAsymmetric(AbstractSCCKey key) 
    throws SCCException;
}

abstract class AbstractSCCKey {

KeyType type;
byte[] privateKey
byte[] publicKey;
String algorithm;

protected AbstractSCCKey(KeyType type, byte[] publicKey, byte[] privateKey, String algorithm) {
this.type = type;
this.publicKey = publicKey;
this.privateKey = privateKey;
this.algorithm = algorithm;
}

public abstract byte[] toBytes() 
    throws SCCException;

public abstract byte[] getPublicKeyBytes()
    throws SCCException;

public abstract byte[] getPrivateKeyBytes() 
    throws SCCException;

public abstract KeyType getKeyType();

public abstract String getAlgorithm();
}

abstract class AbstractSCCHash {

byte[] hashMsg;

protected AbstractSCCHash(byte[] hashMsg) {

this.hashMsg = hashMsg;
}

public abstract byte[] toBytes();

@Override
public abstract String toString();

public abstract boolean validateHash(PlaintextContainerInterface plaintext) 
    throws SCCException;

public abstract boolean validateHash(byte[] plaintext) 
    throws SCCException;

public abstract AbstractSCCHash updateHash(PlaintextContainerInterface plaintext) 
    throws SCCException;

public abstract AbstractSCCHash updateHash(byte[] plaintext)
    throws SCCException;
}


abstract class AbstractSCCPasswordHash {

byte[] hashMsg;

protected AbstractSCCPasswordHash(byte[] hashMsg) {
this.hashMsg = hashMsg;
}

public abstract byte[] toBytes();

@Override
public abstract String toString();

public abstract boolean validatePasswordHash(PlaintextContainerInterface password) 
    throws SCCException;

public abstract boolean validatePasswordHash(byte[] password) 
    throws SCCException;
}

abstract class AbstractSCCSignature {

byte[] signatureMsg;

protected AbstractSCCSignature(byte[] signatureMasg) {
this.signatureMsg = signatureMasg;
}

public abstract byte[] toBytes();

@Override
public abstract String toString();

public abstract boolean validateSignature(AbstractSCCKey key) 
	throws SCCException;

public abstract AbstractSCCSignature updateSignature(PlaintextContainerInterface plaintext, AbstractSCCKey key) 
    throws SCCException;
}


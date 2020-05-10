---
title: Secure Crypto Config
abbrev: SCC
docname: draft-kmindi-secure-crypto-config-latest
category: info

ipr: trust200902
area: General
workgroup: TODO Working Group
keyword: Internet-Draft

stand_alone: yes
pi: [toc, sortrefs, symrefs]

author:
 -
    ins: "K. Mindermann"
    name: "Kai Mindermann"
    organization: "iC Consult GmbH"
    email: kai.mindermann@ic-consult.com
 -
    ins: "L. Teis"
    name: "Lisa Teis"
    organization: ""
    email: lisateis102@gmail.com

normative:
  RFC2119:
  RFC4949: SecurityGlossary
  RFC8152: COSE
  RFC8259: JSON


informative:
  RFC5652: CMS
  RFC5698: DSSC
  RFC7228: ConstrainedDevices
  AESNI:
    target: https://www.intel.com/content/dam/doc/white-paper/advanced-encryption-standard-new-instructions-set-paper.pdf
    title: "Intel Advanced Encryption Standard (AES) Instruction Set White Paper"
    author:
      name: Shay Gueron
      org: Intel Corporation
    date: 2010
    format:
      PDF: https://www.intel.com/content/dam/doc/white-paper/advanced-encryption-standard-new-instructions-set-paper.pdf

  # REST:
  #   target: http://www.ics.uci.edu/~fielding/pubs/dissertation/fielding_dissertation.pdf
  #   title: Architectural Styles and the Design of Network-based Software Architectures
  #   author:
  #     ins: R. Fielding
  #     name: Roy Thomas Fielding
  #     org: University of California, Irvine
  #   date: 2000
  #   seriesinfo:      "Ph.D.": "Dissertation, University of California, Irvine"
  #   format:
  #     PDF: http://www.ics.uci.edu/~fielding/pubs/dissertation/fielding_dissertation.pdf



--- abstract

Cryptography providers, libraries and APIs usually define defaults for the offered cryptography primitives.
These defaults have to be kept to be backwards compatible with all users of the API that used the defaults.
Yet, these default choices become insecure at some later point. 
E.g. a key size of 128 bit may not be sufficient anymore.
To keep these defaults up-to-date three things are described in this document: 
(1) A process that is repeated every year, where the
CRFG publishes a new set of default configurations for standardized cryptography primitives, 
(2) a format based on *TODO* that specifies the default secure configuration of this (and previous) year(s) and 
(3) a format to derive the parameters from output of cryptography primitives, otherwise future changes of the default configuration would change existing applications behaviour.

--- middle

# TODO Remove at the end

- [ ] TODO https://www.ietf.org/standards/ids/guidelines/
- [ ] TODO References:
  - [ ] "Normative references specify documents that must be read to understand or implement the technology in the new RFC, or whose technology must be present for the technology in the new RFC to work." (https://www.ietf.org/about/groups/iesg/statements/normative-informative-references/)
  - [ ] "Handling Normative References to Standards-Track Documents" https://tools.ietf.org/html/rfc4897
- [ ] TODO https://www.ietf.org/media/documents/92kramdown-Bormann.pdf
- [ ] TODO Check if "Generic Security Service Application Program Interface Version 2, Update 1" {{?RFC2743}} is relevant.

[^TODO]: Do not remove the TODOS, but mark them as complete by adding an x between the brackets.

# Introduction

[^TODO]

- [ ] TODO Write Introduction
- [x] Mention Main Goals: (0) Prevent insecure cryptography use/implementation for the future. (1) Enable cryptography libraries and APIs to offer secure defaults with inherent future-proofnes; (2) Prevent non-expert programmers from misusing cryptography APIs; (3) Allow standardized definition of secure parameters for cryptography algorithms; (4) Standardized across all implementations; (5) Prevent outdated example code and documentation.
- [x] Mention: Yearly published secure configuration recommendations that can be used per default from cryptography libraries. This prevents aging/maturing libraries from offering insecure default implementations. 
- [x] Mention target group ((1) developers who are not experts but still need to implement cryptography functionality. (2) Cryptography library developers that should integrate SCC to provide secure defaults. (3) standardization institutions (like BSI or NIST) who can use the publication format for their own set of cryptography recommendations)
- [ ] TODO Mention/describe the gap between cryptography concept basics (like all properties of symmetric encryption can be known except the key) vs the standardization process vs the actual implementation. During this concretization much more complexity is added to cryptography for users of cryptography.
  - [ ] TODO even describe this better by describing the basic cryptographic primitives we are concerned about and then describe the current best practice for its standardization (e.g. an RFC) and then an actual often used implementation (e.g. Java SDK Crypto API).
- [x] TODO mention and describe [TLS Cipher suites](https://www.iana.org/assignments/tls-parameters/tls-parameters.xhtml#tls-parameters-4)
- [ ] TODO mention why the "Recommended" Column is not enough
- [ ] TODO Mention/Refer to (use as example for complexit)how many input parameters (5 to 8 in addition to the password itself) [The memory-hard Argon2 password hash and proof-of-work function](https://tools.ietf.org/html/draft-irtf-cfrg-argon2-10#section-3.1) has.
- [x] TODO integrate "Guidelines for Cryptographic Algorithm Agility and Selecting Mandatory-to-Implement Algorithms" BCP 201 {{?RFC7696}}.
- [x] TODO integrate "Algorithm Agility Procedure for the Resource Public Key Infrastructure (RPKI)" BCP 182 {{?RFC6916}}.
- [x] TODO Mention/Refer to "CBOR Object Signing and Encryption (COSE)" {{-COSE}}
  - see defined Algorithms/Parameters etc. at the [IANA registry for COSE](https://www.iana.org/assignments/cose/cose.xhtml)
  - e.g. There is an entry for the Advanced Encryption Standard with key size, mode and tag length: A256GCM;3;AES-GCM mode w/ 256-bit key, 128-bit tag;
- [x] TODO Mention/Refer to "The JavaScript Object Notation (JSON) Data Interchange Format" {{-JSON}}
- [x] TODO Mention/Refer to "Data Structure for the Security Suitability of Cryptographic Algorithms (DSSC)" {{-DSSC}}

## Motivation
The correct choice of secure parameters when implementing cryptographic primitives or algorithms is not always easy to ensure.
However, the security of the primitives to be used depends mainly on the choice of these parameters (e.g. the correct key length).
In order to be able to prevent insecure implementations and usage of cryptography, it is necessary to assure that the choice of parameters is made correctly.
Unfortunately, this is not always easy.
The correct choice of secure parameters is a big issue especially for software developers who are not primarily concerned with cryptography.
Often, they choose necessary parameters based on documentation which is outdated or shows an insecure parameter set.
Another point that can lead to an insecure choice are cryptographic libraries.
Normally they should provide secure default parameters, but often it is the case that they are insecure or outdated.
Therefore, we also need to prevent the misuse of cryptographic APIs.
Furthermore, it is necessary to find a way to enable cryptographic libraries and APIs to offer a secure choice of parameters.
To alleviate the mentioned problems, it is a good idea to generate and provide a standardized definition of secure parameters for cryptographic primitives.
Such a standard would also lead to a standardization across all implementations. A data structure to represent this standardized definition in JSON {{-JSON}} is given in the following {{dataStructures}}.
This standardized definition of a secure parameter set is referred to as Secure Crypto Config (SCC) in the following.

The publication of a SCC would be helpful especially for developers that are not specialized in cryptography.
Often the basic concept of cryptographic primitives like hashing, in which the plaintext simply gets hashed with the help of a hash algorithm, is easy to understand. But the actual usage can get rather difficult.
This can be seen on the [example of Argon2](https://tools.ietf.org/html/draft-irtf-cfrg-argon2-10#section-3.1) in which it is necessary to provide up to 8 additional parameters to be able to start the actual hashing.
There is a huge gap between the concept and the actual implementation.
SCC can close this gap and help the developers to make a secure choice by simply looking into the SCC.
Despite non-expert developers the SCC can also be used as a template for standardization institutions such as NIST or BSI to provide their parameter recommendations in a structured and machine-readable way.
Furthermore, cryptographic library developers can integrate the SCC to offer secure defaults.
But in practice, it is often not possible to simply change the interfaces of existing cryptographic libraries.
Therefore, an additional abstraction layer is needed here, which should provide an easy to use interface for the programmers to actually use the library functions with the secure parameter choice.
An example of such an additional abstraction layer can be seen in {{scc_java_api_example}}.

It already exists a Data Structure for the Security Suitability of Cryptographic Algorithms (DSSC) {{-DSSC}} in an XML-format that stores some of the necessary information’s, that are also required for the SCC.
DSSC represents a data structure that should support the automatic analysis of security suitability of given cryptographic algorithms.
In contrast to SCC, DSSC does not make suggestions for currently secure parameters that should be used, but is intended for the evaluation of algorithms.
Nevertheless, it can be regarded as a reference, as it contains a lot of information that will be needed in SCC.

(?) The SCC should also have the property of algorithm agility which includes that it should be easy to switch from one set of parameters and algorithms to another.
This is necessary to be able to adapt to the current state of security.
Also, it is important to use a common set of cryptographic algorithms and consider how many choices of parameters and algorithms to provide.
Procedures that contribute to the achievement of these properties are described in more detail in the "Guidelines for Cryptographic Algorithm Agility and Selecting Mandatory-to-Implement Algorithms" BCP 201 {{?RFC7696}} and the "Algorithm Agility Procedure for the Resource Public Key Infrastructure (RPKI)" BCP 182 {{?RFC6916}}.

One problem in the usage of the SCC is the fact that the defined parameters will change over time.
Therefore, it is necessary to check the SCC with its parameters regularly and adjust them if necessary.
A regular adjustment process once a year, for example, could guarantee this. However, adjusting the parameters is not always easy.
If for example a plaintext should be encrypted one has to keep the same parameters for both encryption and decryption.
Otherwise the ciphertext obtained by encryption cannot be decrypted correctly back again.
In contrast we can see that in the use of Transport Layer Security (TLS), parameter changes are comparatively less complicated. For the duration of a connection between client and server, it is possible to choose from different algorithms in advance.
A selection of possible algorithms for TLS with recommendation for their usage can be found for example at the [IANA Registry for TLS Cipher suites](https://www.iana.org/assignments/tls-parameters/tls-parameters.xhtml#tls-parameters-4).
This issue could be solved by developing different backwards compatible layers. But, it would be more advantageous to store the parameters used for the encryption in addition to the actual encrypted data.
This is also necessary to prevent applications from becoming incompatible with updated SCC over time.
To store this necessary information there already exists a standard named CBOR Object Signing and Encryption (COSE) {{-COSE}}.
The parameters and algorithms defined for COSE can be found at the [IANA registry for COSE](https://www.iana.org/assignments/cose/cose.xhtml).
COSE represents a data structure that contributes to the storage of the cryptographic output (e.g. ciphertext) as well as the used parameters in a structured way.


## Terminology

### Conventions and Definitions

The key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL NOT", "SHOULD",
"SHOULD NOT", "RECOMMENDED", "NOT RECOMMENDED", "MAY", and "OPTIONAL" in this
document are to be interpreted as described in BCP 14 {{RFC2119}} {{!RFC8174}}
when, and only when, they appear in all capitals, as shown here.

### Terms

The terms 
  "Alice and Bob",
  "API",
  "attack",
  "authenticity",
  "availability",
  "break",
  "brute force", 
  "cipher",
  "ciphertext",
  "classified", 
  "classification level", 
  "classification label", 
  "clear text",
  "confidentiality",
  "cryptographic hash",
  "encode",
  "encrypt",
  "encryption",
  "hybrid encryption",
  "initialization value (IV)",
  "integrity",
  "key", 
  "mode",
  "nonce",
  "password",
  "permanent storage",
  "plain text",
  "plaintext",
  "protocol",
  "risk",
  "salt",
  "security",
  "security level",
  "threat",
  "trust",
  "vulnerability",
in this document are to be interpreted as described in {{-SecurityGlossary}}.

The term "hash" is used as a synonym for "cryptographic hash".


## Use Cases

### Cryptographic primitives

#### Symmetric Encryption

Symmetric Encryption is an important cryptographic primitive especially

plaintext + key => ciphertext

AES

A256GCM;3;AES-GCM mode w/ 256-bit key, 128-bit tag;


#### Asymmetric Encryption

plaintext + publickey => ciphertext

ciphertext + privatekey => plaintext

RSA

#### Hashing

plaintext => hash

SHA-3

##### Password Derivation

plaintext,hashalgorithm => hash

argon2

#### Digital Signatures

plaintext,privatekey => signature

signature,publickey => vailid/not-valid

ECDSA

### Secure Crypto Config Use Cases

The Secure Crypto Config has three main use cases: 

- Centralized and regularly updated single source of truth for secure algorithm and (their) parameter choices for most common cryptography primitives and use cases.
- Machine readable and extensible format so that organisations (e.g. governmental or commercial) can define their own set of cryptography configuration to both allow and to verify the compliant usage of cryptography.
- Cryptography library/API interface description for cryptography API developers and for software developers who can then use that interface.

**Choice of secure parameter set:**
With the help of SCC it is possible especially for non-expert programmers to look up secure parameters for cryptographic primitives in the SCC.
In this way an insecure choice of parameters with the help of outdated example codes and documentations can be prevented.
The published SCC provides a simple and easy way for looking up secure parameter sets.

**Standard for recommendations of parameters:** SCC could be used as a data structure for standardization institutions such as NIST or BSI to provide their security recommendations in a structured and machine-readable way.

**Integration in cryptographic libraries and APIs:**
It is often the case that cryptographic libraries and APIs using insecure default parameters or deprecate over time as the library is updated frequently and security standards change over time.
With the integration of SCC, a constantly secure parameter set can be provided. In this way also the misuse of the libraries and APIs could be prevented.

### Misuse Cases

*"A Misuse Case [...] highlights something that should not happen (i.e. a Negative Scenario)"* [Misuse Case](https://en.wikipedia.org/w/index.php?title=Misuse_case&oldid=941745374)

# Requirements and Assumptions

## Requirements

- Security Level Requirements:
The Secure Crypto Config should define different security levels.
E.g. information has different classification levels and longevity. 
Additionally, cryptography operations could not or much slower perform on constrained devices, which should also be handled with the security levels.
For each security level the consensuns finding process and entities shall publish a distinct secure crypto config. 

- Consensuns Finding Process and entities:
  - The Secure Crypto Config must be renewed regularly.
  - The Secure Crypto Config must be renewable on-demand.
  - There must be a guideline on which entities must agree to publish a new Secure Crypto Config.
  - There must be a guideline on which entities may participate in the consensus finding process and how they may do so.
  - There must be a guideline on how to determine broad availability of both cryptography algorithms and chosen parameters.

- Publication Format and Distribution Requirements:
  - General:
    - Standardized unique and distinct names for (1) cryptography algorithms (2) their parameters and (3) the combination of the algorithm with set parameters. 
      Otherwise ambiguity would make it harder for developers and cryptography implementors to make correct and secure choices.
    - There must be a versioning that allows to distinguish between Secure Crypto Configurations and what is the latest Secure Crypto Config.
    - There must be a deprecation process that ensures usage of outdated/insecure Crypto Configurations ceases.
    - There must be an official source where this Secure Crypto Config is maintained and can be obtained from (e.g. via the WWW).
    - The official source should also provide information about the Secure Crypto Config Interface that should be utilized for the application of the Secure Crypto Config.
  - Human readable
    - Easy to find what is a secure parameter set for a given requirement (e.g. one table, two-dimensional)
    - Easy accessibility
    - Must be easy to update by the committee
  - Machine readable
    - Cryptography libraries, regardless of the programming language, should be able to directly map (without extensive parsing) the Secure Crypto Config to their implementation
    - Must be easy to verify which Secure Crypto Config is used / was used (e.g. in Continuous Integration platforms)
    - Must be easy to verify the authenticity of the Secure Crypto Config (e.g. is this really what the CFRG has published)
    - Must be easy to extend/alter by other organizations (e.g. maybe the BSI wants to publish its own secure crypto config that differs from the standardized one. Maybe a hiearchical approach with inheritance from the base SCC?)

- Cryptography library integration requirements:
  - Easy to integrate by cryptography libraries
  - Experts should still be able to use/access the unaltered output of cryptographic primitives
  - Recommendation what should be the default secure crypto config for a cryptography library (e.g. should it be the one for TOP Secret or *only* for Secret?)
  - Recommendation what should a cryptography library do if it can not support the parameters specified in the latest Secure Crypto Config. (E.g. key size for RSA would be n*2 and the library supports only n)
  - Recommendation how should a cryptography library integrate the Secure Crypto Config so that it should be up to date as soon as possible after a new SCC has been published?

- General Requirements:
  - Interoperability with other standards/formats (e.g. {{-COSE}})
  - The Secure Crypto Config should cover most common cryptography primitives and their currently broadly available and secure algorithms.
  - *Threat Model* / adversary powers: What kind of attacker should the secure crypto config protect against? (Almighty? Gouvernment? Company?). Presumably  different group of attackers.
  - The Secure Crypto Config should prevent non-experts to configure cryptography primitives in an insecure way.
  - The Secure Crypto Config should not prevent experts from using or changing all parameters of cryptogaphy primitives provided by a cryptography library/API.

## Assumptions

The Secure Crypto Config assumes that both the proposed algorithms and the implementations (cryptography libraries) for the cryptography primitives are secure.
This also means that side-channel attacks are not considered explicitly.

# Secure Crypto Config

## Security Levels

### Security Level Constraints

- [ ] TODO integrate
  - Information classification: Support different classifications (e.g. TOP SECRET, SECRET, CONFIDENTIAL)
    - Information classification is not always needed. E.g. many applications require one encryption / hashing algorithm and use only that one for all information encrypted/hashed. But for future proofing it would be good to be able to process information based on its classification.
  - Longevity: The time some information must be protected can differ from milliseconds to decades (related to Document classification)
    - Different SCC's based on longevity is not always needed. E.g. many applications require one encryption / hashing algorithm and use only that one for all information encrypted/hashed. But for future proofing it would be good to be able to process information based on its longevity.
  - Contrained Devices: Computing power is not always available for both the most advanced cryptography algorithms and their parameters.

#### Information classification

- [ ] TODO describe Why is document classification needed (refer to Requirements if possible).
- [ ] TODO Refer to [Classified Information](https://en.wikipedia.org/wiki/Classified_information) and [Traffic Light Protocol](https://www.first.org/tlp/docs/tlp-v1.pdf) or other material to find a globally valid definition for classifying information.

#### Longevity

#### Constrained Devices

For cryptography often complex computations have to be executed. 
Yet, not all environments have the same hardware resources available.
E.g. it is not always the case that the used processors have dedicated cryptography hardware or even specialized execution units or instruction sets like {{AESNI}}.
A detailed discussion and definitions can be found in {{-ConstrainedDevices}}.
Yet, their definitions are too concrete. 
Which is why we define constrained devices not based on concrete processing power (e.g. 100k instructions per second): 
For the Secure Crypto Config a device is constrained when it has multiple magnitudes less resources than a current (at the time of reading!) standard personal computer.
For example if a current standard personal computer can encrypt with 1 GiB/s, a constrained device would be all devices that can only perform the same cryptography operation with less than 10 MiB/s.
Resources can be everything important for cryptography like dedicated crytography hardware, instruction sets, memory, power consumption, storage space, communication bandwith, latency etc.


### Security Level 1

Unclassified information, constrained devices, short longevity

### Security Level 2

### Security Level 3

### Security Level 4

### Security Level 5

### Security Level Extensions/Extendability


# Consensuns Finding Process and entities

## Consensus Finding

### Guideline to choose cryptography algorithm and parameters

## Entities


# Publication

- [ ] TODO Describe used versioning concept for SCC. 
- [ ] TODO Refer to [Semantic Versioning](https://semver.org/)

# Data Structures {#dataStructures}

|              | More than 10 years   | More than 1 year               | More than 1 week | Less than one week |
| ------------ | -------------------- | ------------------------------ | ---------------- | ------------------ |
| TOP SECRET   | TOP_SECRET_gt10years | TOP_SECRET_gt1yearsEndFragment |                  |                    |
| SECRET       |                      |                                |                  |                    |
| CONFIDENTIAL |                      |                                |                  |                    |
{: #scc_secret_vs_longevity}


See {{scc_example}}.

- [ ] TODO is JSON a appropriate format?
- [ ] TODO How is COSE more approriate/in parts of JSON? or is a mapping (=> parsing needed) better between COSE<->JSON?

# Cryptography Algorithm Standards Recommendation

- [ ] TODO should there be a template for cryptogrpahy algorithm standards (in addition to COSE) for the Secure Crypto Config or is it enough that the Secure Crypto Config Consensus Finding defines the secure parameters for all cryptography algorithms?

# Implementation Specification

- [ ] TODO describe requirements for cryptography API implementors and designers
- [ ] TODO decide how the configuration should be made available to programmers
  - [ ] e.g. should there be constants like "SCC_TOP_SECRET_LATEST" and "SCC_TOP_SECRET_LATEST". 
  - [ ] And like "SCC_TOP_SECRET_LATEST.AES" which points always to the latest Secure Crypto Config definition for AES parameters.
- [ ] TODO how should cryptography implementations, that implement/support SCC, generate the parameters?
  - [ ] What kind of parameters can be chosen based on the Secure Crypto Config? => E.g. Should be all except the plaintext and the key for encryption algorithms. Also many parameters can be generated based on cryptographically secure random numbers.

# Security Considerations

## Consensus Finding

## Publication Format

## Cryptography library implementation

## General Security Considerations

### Special Use Cases and (Non-)Security Experts

The Secure Crypto Config does not apply to all use cases for cryptography and usage of cryptography primitives.
It is meant to provide secure defaults for the most common use cases and for non-expert programmers.
Additionally, non-experts may still implement vulnerable code by using the Secure Crypto Config. 
Yet, it should reduce the vulnerabilities from making the wrong choices about parameters for cryptography primitives.

## Security of Cryptography primitives and implementations

The Secure Crypto Config assumes that both the proposed algorithms and the implementations (cryptography libraries) for the cryptography primitives are secure.
- [ ] TODO is this redundant? because it is also described in the assumptions section.

### Security Guarantees

The Secure Crypto Config does not guarantee security for the proposed parameter configuration.
E.g. a new algorithm that can do brute-force attacks exponentially faster could be existing or published right after the publication of the most recent Secure Crypto Config was published itself.
Yet, the Secure Crypto Config makes a best effort to be as up-to-date with recent discoveries, research and developments in cryptography algorithms as possible.

## TODO

- [ ] TODO Security
- [ ] TODO are some of the listed common issues relevant?: [TypicalSECAreaIssues](https://trac.ietf.org/trac/sec/wiki/TypicalSECAreaIssues)
- [ ] TODO check if security considerations of TLS 1.2 are relevant, especially appendix [D, E and F](https://tools.ietf.org/html/rfc5246#appendix-D)

### Threat Model / Adversaries

- [ ] TODO describe adversaries / group of adversaries and their powers
- [ ] TODO describe other general threats for the Secure Crypto Config (maybe put that into the section Security Considerations):
  - [ ] Process
  - [ ] Publication
  - [ ] Content

There are different possibilities in which a potential adversary could intervene during the creation as well as after the publication of the SCC. These attack scenarios must be considered and prevented.

**Process:** During the creation process, it is necessary for selected institutions to agree on a secure parameter set.
It could be possible that one party wants to influence this process in a bad way.
As a result, it could be agreed on weaker parameter sets than originally intended.

**Publication:** After the publication of the SCC a potential attacker could gain access to the provided files on the corresponding platform and change the content to an insecure parameter set.
Depending on the distribution method of the SCC, it is also possible that an attacker could change the content of the SCC as man-in-the-middle.
Especially if a http connection is used to obtain the SCC, this will be a serious problem.




# IANA Considerations

- [] TODO Are there IANA Considerations?


--- back

# Example JSON Secure Crypto Config

~~~~
{::include src/scc_example.json}
~~~~
{: #scc_example title="Example for JSON format"}

# Example Java Interface using Secure Crypto Config

~~~~
{::include src/scc_java_api_example.java}
~~~~
{: #scc_java_api_example title="Example for a JAVA SCC API"}


# Acknowledgments
{:numbered="false"}

- [ ] TODO acknowledge.

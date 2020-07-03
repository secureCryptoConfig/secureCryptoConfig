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

- [x] TODO (2) a format based on *TODO* that specifies the default secure configuration

Cryptography providers, libraries and APIs usually define defaults for the offered cryptography primitives.
These defaults have to be kept to be backwards compatible with all users of the API that used the defaults.
Yet, these default choices become insecure at some later point. 
E.g. a key size of 128 bit may not be sufficient anymore.
To keep these defaults up-to-date three things are described in this document: 
(1) A process that is repeated every two years, where the
CRFG publishes a new set of default configurations for standardized cryptography primitives, 
(2) a format based on JSON that specifies the default secure configuration of this (and previous) year(s) and 
(3) a format to derive the parameters from output of cryptography primitives, otherwise future changes of the default configuration would change existing applications behavior.

--- middle



# Introduction


## TODO Remove at the end

[^TODO]: Do not remove the TODOS, but mark them as complete by adding an x between the brackets.
[^TODO]

### General TODOS

- [x] TODO Write Introduction
- [x] Mention Main Goals: (0) Prevent insecure cryptography use/implementation for the future. (1) Enable cryptography libraries and APIs to offer secure defaults with inherent future-proofnes; (2) Prevent non-expert programmers from misusing cryptography APIs; (3) Allow standardized definition of secure parameters for cryptography algorithms; (4) Standardized across all implementations; (5) Prevent outdated example code and documentation.
- [x] Mention: Yearly published secure configuration recommendations that can be used per default from cryptography libraries. This prevents aging/maturing libraries from offering insecure default implementations. 
- [x] Mention target group ((1) developers who are not experts but still need to implement cryptography functionality. (2) Cryptography library developers that should integrate Secure Crypto Config to provide secure defaults. (3) standardization institutions (like BSI or NIST) who can use the publication format for their own set of cryptography recommendations)
- [x] TODO Mention/describe the gap between cryptography concept basics (like all properties of symmetric encryption can be known except the key) vs the standardization process vs the actual implementation. During this concretization much more complexity is added to cryptography for users of cryptography.
  - [x] TODO even describe this better by describing the basic cryptographic primitives we are concerned about and then describe the current best practice for its standardization (e.g. an RFC) and then an actual often used implementation (e.g. Java SDK Crypto API).
- [x] TODO mention and describe [TLS Cipher suites](https://www.iana.org/assignments/tls-parameters/tls-parameters.xhtml#tls-parameters-4)
- [ ] TODO mention why the "Recommended" Column is not enough
- [x] TODO Mention/Refer to (use as example for complexity)how many input parameters (5 to 8 in addition to the password itself) [The memory-hard Argon2 password hash and proof-of-work function](https://tools.ietf.org/html/draft-irtf-cfrg-argon2-10#section-3.1) has.
- [x] TODO integrate "Guidelines for Cryptographic Algorithm Agility and Selecting Mandatory-to-Implement Algorithms" BCP 201 {{?RFC7696}}.
- [x] TODO integrate "Algorithm Agility Procedure for the Resource Public Key Infrastructure (RPKI)" BCP 182 {{?RFC6916}}.
- [x] TODO Mention/Refer to "CBOR Object Signing and Encryption (COSE)" {{-COSE}}
  - see defined Algorithms/Parameters etc. at the [IANA registry for COSE](https://www.iana.org/assignments/cose/cose.xhtml)
  - e.g. There is an entry for the Advanced Encryption Standard with key size, mode and tag length: A256GCM;3;AES-GCM mode w/ 256-bit key, 128-bit tag;
- [x] TODO Mention/Refer to "The JavaScript Object Notation (JSON) Data Interchange Format" {{-JSON}}
- [x] TODO Mention/Refer to "Data Structure for the Security Suitability of Cryptographic Algorithms (DSSC)" {{-DSSC}}
- [ ] TODO integrate Password Hashing Format Specification: [PHC String Format](https://github.com/P-H-C/phc-string-format/blob/master/phc-sf-spec.md) a successor to the deprecated Modular Crypt Format (MCF).

### Other TODOS
- [ ] TODO https://www.ietf.org/standards/ids/guidelines/
- [ ] TODO References:
  - [ ] "Normative references specify documents that must be read to understand or implement the technology in the new RFC, or whose technology must be present for the technology in the new RFC to work." (https://www.ietf.org/about/groups/iesg/statements/normative-informative-references/)
  - [ ] "Handling Normative References to Standards-Track Documents" https://tools.ietf.org/html/rfc4897
- [ ] TODO https://www.ietf.org/media/documents/92kramdown-Bormann.pdf
- [x] TODO Check if "Generic Security Service Application Program Interface Version 2, Update 1" {{?RFC2743}} is relevant.
- [x] TODO Check if "An Interface and Algorithms for Authenticated Encryption" {{?RFC5116}} is relevant.


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
This standardized definition of a secure parameter set is referred to as Secure Crypto Config in the following.

The publication of a Secure Crypto Config would be helpful especially for developers that are not specialized in cryptography.
Often the basic concept of cryptographic primitives like hashing, in which the plaintext simply gets hashed with the help of a hash algorithm, is easy to understand. But the actual usage can get rather difficult.
This can be seen on the [example of Argon2](https://tools.ietf.org/html/draft-irtf-cfrg-argon2-10#section-3.1) in which it is necessary to provide up to 8 additional parameters to be able to start the actual hashing.
There is a huge gap between the concept and the actual implementation.
Secure Crypto Config can close this gap and help the developers to make a secure choice by simply looking into the Secure Crypto Config.
Despite non-expert developers the Secure Crypto Config can also be used as a template for standardization institutions such as NIST or BSI to provide their parameter recommendations in a structured and machine-readable way.
Furthermore, cryptographic library developers can integrate the Secure Crypto Config to offer secure defaults.
But in practice, it is often not possible to simply change the interfaces of existing cryptographic libraries.
Therefore, an additional abstraction layer is needed here, which should provide an easy to use interface for the programmers to actually use the library functions with the secure parameter choice.
An example of such an additional abstraction layer can be seen in {{scc_java_api_example}}.

It already exists a Data Structure for the Security Suitability of Cryptographic Algorithms (DSSC) {{-DSSC}} in an XML-format that stores some of the necessary information’s, that are also required for the Secure Crypto Config.
DSSC represents a data structure that should support the automatic analysis of security suitability of given cryptographic algorithms.
In contrast to Secure Crypto Config, DSSC does not make suggestions for currently secure parameters that should be used, but is intended for the evaluation of algorithms.
Nevertheless, it can be regarded as a reference, as it contains a lot of information that will be needed in Secure Crypto Config.

The Secure Crypto Config should also have the property of algorithm agility which includes that it should be easy to switch from one set of parameters and algorithms to another.
This is necessary to be able to adapt to the current state of security.
Also, it is important to use a common set of cryptographic algorithms and consider how many choices of parameters and algorithms to provide.
Procedures that contribute to the achievement of these properties are described in more detail in the "Guidelines for Cryptographic Algorithm Agility and Selecting Mandatory-to-Implement Algorithms" BCP 201 {{?RFC7696}} and the "Algorithm Agility Procedure for the Resource Public Key Infrastructure (RPKI)" BCP 182 {{?RFC6916}}.

One problem in the usage of the Secure Crypto Config is the fact that the defined parameters will change over time.
Therefore, it is necessary to check the Secure Crypto Config with its parameters regularly and adjust them if necessary.
A regular adjustment process once a year, for example, could guarantee this. However, adjusting the parameters is not always easy.
If for example a plaintext should be encrypted one has to keep the same parameters for both encryption and decryption.
Otherwise the ciphertext obtained by encryption cannot be decrypted correctly back again.
In contrast we can see that in the use of Transport Layer Security (TLS), parameter changes are comparatively less complicated. For the duration of a connection between client and server, it is possible to choose from different algorithms in advance.
A selection of possible algorithms for TLS with recommendation for their usage can be found for example at the [IANA Registry for TLS Cipher suites](https://www.iana.org/assignments/tls-parameters/tls-parameters.xhtml#tls-parameters-4).
This issue could be solved by developing different backwards compatible layers. But, it would be more advantageous to store the parameters used for the encryption in addition to the actual encrypted data.
This is also necessary to prevent applications from becoming incompatible with updated Secure Crypto Config over time.
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

- [x] TODO remove not used in document: "Alice and Bob","hybrid encryption", "permanent storage", "risk",  "vulnerability",
- [x] TODO define cryptographic primitive (for usage in use cases and in document) https://crypto.stackexchange.com/questions/39735/whats-a-cryptographic-primitive-really

The terms 
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
  "confidentiality",
  "cryptographic hash",
  "encrypt",
  "encryption",
  "initialization vector (IV)",
  "integrity",
  "key", 
  "mode",
  "nonce",
  "password",
  "plain text",
  "plaintext",
  "protocol",
  "security",
  "security level",
  "threat",
  "trust",
in this document are to be interpreted as described in {{-SecurityGlossary}}.

The term "hash" is used as a synonym for "cryptographic hash".
The term "cryptographic primitive" is used in this document to describe a generic building block used in the field of cryptography e.g. Hashing, symmetric encryption.


## Use Cases

### Secure Crypto Config Use Cases

The Secure Crypto Config has three main use cases: 

- Centralized and regularly updated single source of truth for secure algorithm and (their) parameter choices for most common cryptography primitives and use cases.
- Machine readable and extensible format so that organizations (e.g. governmental or commercial) can define their own set of cryptography configuration to both allow and to verify the compliant usage of cryptography.
- Cryptography library/API interface description for cryptography API developers and for software developers who can then use that interface.


### Cryptography Use Cases {#cryptoCase}
- TODO [ ] should CSPRNG, Key generation, Password hashing be considered?

The Secure Crypto Config should cover cryptography algorithm and parameter configurations for the following widely used cryptography use cases.
These are mostly no security protocols but cryptography primitives.

#### Symmetric Encryption

Symmetric Encryption is an important cryptographic primitive especially as it is usually multiple magnitudes faster both for encryption and decryption than asymmetric cryptography.
Yet, the secret has to be shared with all participants.

Expected input parameters by cryptography users:

- plaintext
- secret key

Expected output: ciphertext.

Additional Parameters often required in practice:

- Algorithm
- Block-Mode
- IV
- Padding-Mode
- Authentication Tag size

Possible secure usage:

- A256GCM;3;AES-GCM mode w/ 256-bit key, 128-bit tag
- ChaCha20/Poly1305;24;ChaCha20/Poly1305 w/ 256-bit key, 128-bit tag

#### Asymmetric Encryption

Beside symmetric encryption is asymmetric encryption another important cryptographic primitive to considered.

Expected input parameters for encryption:

- plaintext 
- public key

Expected output: ciphertext.

Expected input parameters for decryption:

- ciphertext
- private key 

Possible secure usage:

- RSA
- RSAES-OAEP w/ SHA-512,-42,RSAES-OAEP w/ SHA-512

#### Hashing
Hashing is an important cryptographic primitives and is often needed as a part of many other cryptographic use cases e.g. password derivation.

Expected input parameters by cryptography users:

- plaintext

Expected output: hash.

Possible secure usage:

- SHA-512 (TEMPORARY - registered 2019-08-13, expires 2020-08-13);-44;SHA-2 512-bit Hash
- SHAKE256 (TEMPORARY - registered 2019-08-13, expires 2020-08-13);-45;256-bit SHAKE


#### Password Hashing

The secure storage of passwords requires hashing. 
Yet, password hashing requires that the hashing can not be performed very fast to prevent attackers from guessing/brute-forcing passwords from leaks or against the live system.
E.g. it is totally fine for users if the login takes 0.1 seconds instead of microseconds.
This results in special families of hash algorithms which offer additional tuning parameters.

Expected input parameters by cryptography users:

- plaintext
- hash-algorithm

Expected output: hash.

Possible secure usage:

- Argon2id

#### Key Generation

- [ ] TODO should key generation be considered? (Symmetric/Asymmetric)

A key is necessary for many cryptographic use cases e.g. symmetric and asymmetric encryption.
Therefore, key generation is an important part while implementing cryptographic code. 

Expected output: key.

Possible secure generation:

- Use of CSPRNG
- Keys derived via derivation function from passwords/other keys


#### Digital Signatures
Signing is an important and often needed cryptographic use case. It is based on the principle of asymmetrical encryption.

Expected input parameters for encryption:

- plaintext 
- private key

Expected output: signature.

Expected input parameters for decryption:

- signature
- public key 

Expected output: valid/not-valid.

Possible secure usage:

- ECDSA
- ES512;-36;ECDSA w/ SHA-512


# Requirements and Assumptions

## Requirements {#requirements}

- [ ] TODO in "Publication Format and Distribution Requirements"/Machine readable. Must be easy to extend/alter by other organizations (e.g. maybe the BSI wants to publish its own secure crypto config that differs from the standardized one. Maybe a hierarchical approach with inheritance from the base SCC?) added?
- [ ] TODO in "General requirements", *Threat Model* / adversary powers. What kind of attacker should the secure crypto config protect against? (Almighty? Government? Company?). Presumably  different group of attackers.


- Security Level Requirements:
The Secure Crypto Config should define different security levels.
E.g. information has different classification levels and longevity. 
Additionally, cryptography operations could not or much slower perform on constrained devices, which should also be handled with the security levels.
For each security level the consensus finding process and entities shall publish a distinct secure crypto config. 

- Consensus Finding Process and entities:
  - The Secure Crypto Config must be renewed regularly.
  - The Secure Crypto Config must be renewable on-demand.
  - There must be a guideline on which entities must agree to publish a new Secure Crypto Config.
  - There must be a guideline on which entities may participate in the consensus finding process and how they may do so.
  - There must be a guideline on how to determine broad availability of both cryptography algorithms and chosen parameters.

- Publication Format and Distribution Requirements:
  - General:
    - The Secure Crypto Config must be easily publishable by the committee.
    - Standardized unique and distinct names for (1) cryptography algorithms (2) their parameters and (3) the combination of the algorithm with set parameters. 
      Otherwise ambiguity would make it harder for developers and cryptography implementors to make correct and secure choices.
    - There must be a versioning that allows to distinguish between Secure Crypto Configurations and what is the latest Secure Crypto Config.
    - There must be a deprecation process that ensures usage of outdated/insecure Crypto Configurations ceases.
    - There must be an official source where this Secure Crypto Config is maintained and can be obtained from (e.g. via the WWW).
    - The official source format of the Secure Crypto Config must be cryptographically protected to ensure its integrity and authenticity.
    - Other published formats derived from the source format (e.g. for human readability on a webpage) do not have to be cryptographically protected but should be generated automatically from the source format.
    - The official source should also provide information about the Secure Crypto Config Interface that should be utilized for the application of the Secure Crypto Config.
    - The Secure Crypto Config must specify how it can be extended (e.g. more security levels) and how derivatives work.
  - Human readable
    - The Secure Crypto Config must have a human readable format. 
    - The Secure Crypto Config must allow non-experts to find secure cryptography algorithms and appropriate parameters for common cryptography use cases.
    - The Secure Crypto Config human readable publication format should only use easy to comprehend data structures like two-dimensional tables.
  - Machine readable
    - Cryptography libraries, regardless of the programming language, should be able to directly map (without extensive parsing) the Secure Crypto Config to their implementation
    - Must be easy to verify which Secure Crypto Config is used / was used (e.g. in Continuous Integration platforms)
    - Must be easy to verify the authenticity of the Secure Crypto Config (e.g. is this really what the CFRG has published)

- Cryptography library integration requirements:
  - Easy to integrate by cryptography libraries
  - Experts should still be able to use/access the unaltered output of cryptographic primitives
  - Recommendation what should be the default Secure Crypto Config for a cryptography library (e.g. should it be the one with the highest security level or *only* a weaker one?)
  - Recommendation what should a cryptography library do if it can not support the parameters specified in the latest Secure Crypto Config. (E.g. key size for RSA would be n*2 and the library supports only n)
  - Recommendation on how a cryptography library should integrate the Secure Crypto Config so that it is up to date as soon as possible after a new Secure Crypto Config has been published

- General Requirements:
  - Interoperability with other standards/formats (e.g. {{-COSE}})
  - The Secure Crypto Config should cover most common cryptography primitives and their currently broadly available and secure algorithms.
  - The Secure Crypto Config should be protected against attackers as defined in {{attacker}}
  - The Secure Crypto Config should prevent non-experts to configure cryptography primitives in an insecure way.
  - The Secure Crypto Config should not prevent experts from using or changing all parameters of cryptography primitives provided by a cryptography library/API.

## Assumptions

The Secure Crypto Config assumes that both the proposed algorithms and the implementations (cryptography libraries) for the cryptography primitives are secure.
This also means that side-channel attacks are not considered explicitly.
It is also assumed that programmers, software engineers and other humans are going to use cryptography.
They are going to make implementation choices without being able to consult cryptography and security experts and without understanding cryptography related documentation fully. 
This also means that it is not considered best practice to assume or propose that only cryptography experts (should) use cryptography (primitives/libraries).

# Security Levels {#securityLevels}

The Secure Crypto Config must be able to provide a secure parameter set for different security levels. 
These security levels depend on the following security constraints: **Information classification (Secret, Confidential), Longevity (less than one day, more than a day), Constrained devices (constrained, not constrained)**.
They are defined in {{constraints}} below.
The Secure Crypto Config provides 5 common security levels for which official algorithm/parameter choices are published.

## Security Level 1 - Low

Confidential information, regardless of the other two constraints

## Security Level 2

Secret information, less than one day longevity, constrained device

## Security Level 3

Secret information, less than one day longevity, non-constrained device

## Security Level 4

Secret information, more than a day longevity, constrained device

## Security Level 5 - High

Secret information, more than a day longevity, non-constrained device

## Security Level Constraints {#constraints}

### Information classification

Information classification here is about the confidentiality of the information.
Not all information is equally confidential, e.g. it can be classified into different classes of information.
For governmental institutions usually three classes are used: Confidential, Secret, or Top Secret.
The Secure Crypto Config considers only **Confidential** and **Secret** for its standardized security levels.
Further levels with other classifications can be added by other organizations.
Additionally, in common (non-governmental) use cases data is not labelled with an information class, so one class has to be chosen for the cryptography processing of all data.

The Secure Crypto Config does not endorse a definition of the information classes, yet **Secret** information is to be considered to have higher confidentiality requirements than **Confidential** information.


### Longevity

The time how long information has to be kept confidential can influence cryptography parameters a lot.
Usually what you talked about with your friends should be kept confidential for a life time.
Yet, a public trade transaction must only be confidential until the trade was executed which can happen in milliseconds.
It directly influences a very important attacker resource: The time an attacker has to try to gain access to the confidential information.
The Secure Crypto Config considers only two ranges of longevity for its standardized security levels: **short longevity** of less than one day and **long longevity** of a day or more than a day.
Further levels with other longevity levels can be added by other organizations.


### Constrained Devices

For cryptography often complex computations have to be executed. 
Yet, not all environments have the same hardware resources available.
E.g. it is not always the case that the used processors have dedicated cryptography hardware or even specialized execution units or instruction sets like {{AESNI}}.
Detailed discussion and definitions can be found in {{-ConstrainedDevices}}.
Yet, their definitions are too concrete to be used in the Secure Crypto Config's standardized security levels. 
Therefore, the Secure Crypto Config uses defines constraint devices not based on concrete processing power (e.g. 100k instructions per second):

A device is constrained when it has **multiple orders of magnitudes** less resources than a current (not a new one, but broadly in use at the time of publication of a Secure Crypto Config!) standard personal computer.

For example if a current standard personal computer can encrypt with 1 GiB/s, a constrained device would be all devices that can only perform the same cryptography operation with less than 10 MiB/s.
Resources can be everything important for cryptography like dedicated cryptography hardware, instruction sets, memory, power consumption, storage space, communication bandwidth, latency etc. 
The Secure Crypto Config considers only **constrained** and **non-constrained** for its standardized security levels.
Further levels with other constrained resource definitions can be added by other organizations.


### n-Bit-Security

- [ ] TODO add n-Bit-Security to Security Levels in time-independent way (e.g. referring to increases in bit instead of hardcoded values for the levels) ?

n-Bit Security Level:

> A cryptographic mechanism achieves a security level of n bits if costs which are equivalent to 2^n calculations of the encryption function of an efficient block cipher (e.g. AES) are tied to each attack against the mechanism which breaks the security objective of the mechanism with a high probability of success. BSI


### Attacker Resources and Capabilities {#attacker}

- [ ] TODO is a more formal definition useful, e.g. a list of [Attack Models](https://en.wikipedia.org/wiki/Attack_model)?* *TODO What about side-channels, especially if the attacker has access to the system?*

The Secure Crypto Config considers only the following same attacker resources and capabilities for all standardized security levels:

- The attacker knows all used algorithms and parameters except secrets according to Kerckhoffs's principle. 
- The attacker has access to the system used for cryptography operations and can utilize its cryptography operations apart from obtaining secrets. 
- The attacker can utilize very high-performance computing resources such as super computers and distributed computing (e.g. this includes very high memory, storage, processing and networking performance)

Further levels with other attacker definitions can be added by other organizations.

## Security Level Extensions/Extendability

- [ ] TODO adapt content? 

One requirement of the Secure Crypto Config is that it must be easy to extend/alter (see {{requirements}}).
It is still possible to change, add or remove some of the here proposed security levels.
E.g. entities that are participating in the consensus finding process could define additional levels.

# Consensus Finding Process and entities {#consensus}

To provide a Secure Crypto Config it is necessary to agree upon a secure and appropriate cryptographic parameter set for each security level (see {{securityLevels}}).
This should happen in a common consensus finding process which takes place at regular intervals e.g. every two yeasr. 
During this process the Crypto Forum Research Group (CFRG) decides in cooperation with other institutions like the Bundesamt für Sicherheit in der Informationstechnik (BSI) or the National Institute of Standards and Technology (NIST) for a set of secure parameters.
After the successful decision the agreed on parameters can be added in the proposed JSON data structure (see {{dataStructures}}) and provided on an appropriate platform.

## Consensus Finding

Consensus must be found two years after the last consensus was found.
This ensures that there is a new Secure Crypto Config every two years, even if the configuration itself has not changed.

### Regular Process {#process}

- [ ] TODO adapt format for Proposal phase submission?

The process has three phases that MUST be finalized within 2 years:

- (1) One year **Proposal phase** during which all participating entities must propose at least two cryptography algorithms and parameters per cryptography use case per security level.
- (2) Six months **Consensus finding phase** during which all participating entities must agree on a common Secure Crypto Config.
- (3) Six months **Publication phase** ensures the publication of the final Secure Crypto Config AND allows the Secure Crypto Config Interface and other cryptography implementations to integrate the necessary changes.

During the Proposal phase the proposed algorithms and all necessary parameters should be submitted in table form for each security level and defined cryptographic use case like proposed.
This table format is simply structured and is easy to read by human as the Consensus finding phase can only be done manually.
It is important that the parameters for each cryptographic use case depending on its security level can be found easily by the participants of the consensus finding process such that it is possible to get to an agreement faster.

### Emergency Process {#emergency}

- [ ] TODO in 3) would replacing also be an option? how?

In cases when a regularly still valid Secure Crypto Config would become insecure regarding either a proposed algorithm or a proposed parameter choice it must be revised with the following process:

1. Determine the insecure configuration.
2. Remove the insecure configuration.
3. Publish the revised Secure Crypto Config with a new patch version.
4. Mark the old (unrevised) Secure Crypto Config as deprecated.

Examples for emergency cases are drastically better brute force algorithms or brute force performance (e.g. quantum computers/algorithms), drastically discovered flaws in proposed algorithms and their configurations.

- [ ] TODO The working group itself can decide when to alter the Secure Crypto Config IANA registry. If e.g. an proposed algorithm gets insecure this can be marked inside the corresponding IANA registry entry.
- [ ] TODO Another possibility is to use the RFC Errata process. If a security gap was found it can be corrected and the improved version gets published. 

### Requirements for Selection of Cryptography Algorithm and Parameters

The Secure Crypto Config MUST only propose cryptography algorithms and parameters that fulfill the following requirements:

- Cryptography algorithms and parameters have stable implementations in at least two different programming languages.
- Cryptography algorithms and parameters have a defined standard to store the algorithm and parameter identification alongside the result (e.g. like {{-COSE}}). 
  This is required to ensure cryptography operation results can be processed even if the default parameters have been changed or the information has been processed with a previous version of the Secure Crypto Config.
- Cryptography algorithms that support parametrization to adapt to increased brute force performance and to allow longevity of the algorithm especially for hardware optimized implementations.

The Secure Crypto Config SHOULD only propose cryptography algorithms and parameters that fulfill the following requirements:

- Cryptography algorithms and parameters are defined in a globally accepted standard which was suspect to a standardization process.
- Cryptography algorithms and parameters are licensed under a license that allows free distribution.

## Entities

- [ ] TODO what is "significant cryptography expertise"?

Entities that participate in the proposal phase SHOULD have significant cryptography expertise.
Entities that participate in the consensus finding phase MUST have significant cryptography expertise.

Each execution of the regular process for the creation of a Secure Crypto Config is described in detail in a separate RFC. The working group itself decides on the participating entities.

# Publication Format and Distribution

## Versioning {#version}

The Secure Crypto Config Format is regularly published in a specific year. 
Therefore, the Secure Crypto Config format MUST use the following versioning format: **YYYY-PATCH**.
YYYY is a positive integer describing the year (using the Gregorian calendar, and considering the year that has not ended in all time zones, cf. Anywhere on Earth Time) this specific Secure Crypto Config was published.
PATCH is a positive integer starting at 0 and only increasing for emergency releases.

## Naming {#naming}

 - [ ] TODO propose naming for secure crypto configurations
 - [ ] TODO maybe merge with versioning section before
 - [ ] TODO keep in mind the secure crypto config interface, which should be able to use these naming conventions in multiple programming languages
 - [ ] TODO possibly: SCC_SecurityLevel_**SecurityLevelNumber**_**Version**" ? Put the corresponding number in **Security Level Number** depending for which security level the SCC is created for.

The Secure Crypto Config uses the following naming conventions to prevent ambiguity and remove implementation choices:
SCC_SecurityLevel_**Security Level Number**_**Version**.
Put the corresponding number in **Security Level Number** depending for which security level the SCC was created for.
To be able to differentiate SCCs with the same Security Levels but different versions the version according to {{version}} is added.

## Secure Crypto Config IANA Registry {#IANA}

- [ ] TODO Naming convention. Specification depending on crypto use case?
- [ ] TODO dash character "-" not possible in enum!

The Secure Crypto Config requires one IANA Registry with the following columns:

- Secure Crypto Config release version: **YYYY-PATCH**
- Distinct **Algorithm-Parameter-Identifier** that uniquely identifies a cryptography algorithm and the parameters
- Distinct and **constantly available reference** where all parameters are unambiguously defined
- (Optional) Short Description of the parameters

Algorithm-Parameter-Identifier: MUST only consist of uppercase alphanumeric characters and underscores.
Depending on the use case the Algorithm Parameter Identifier can be constructed differently.
We propose the following schemes:

- For symmetric encryption the name should look like **AlgorithmName_Mode_Padding_KeyLength_TagLength_NonceLength** (e.g. AES_GCM_NoPadding_256_128_128).
- For hashing as **HashAlgorithmName_KeyLength** (e.g. SHA3_256).
- For asymmetric encryption and digital signatures **AlgorithmName_AuxiliaryAlgorithm_Padding_KeyLength** (e.g. RSA_ECB_OAEP_4096).

### Example for Secure Crypto Config IANA Registry 

| SCC Version | AlgParam Identifier | Reference | Description                                       |
| ----------- | ------------------- | --------- | ------------------------------------------------- |
| 2020-01     | AES_GCM_256_128_128 | {{-COSE}} | AES 256 with GCM and 128 bit tag and random nonce |

### Other Algorithm Registries

The following list gives an overview and examples for the available registries at IANA for cryptography algorithm and their parameters.

- [AEAD Algorithms](https://www.iana.org/assignments/aead-parameters/aead-parameters.xhtml)
- [CBOR Object Signing and Encryption (COSE)](https://www.iana.org/assignments/cose/cose.xhtml)
- [Named Information Hash Registry](https://www.iana.org/assignments/named-information/named-information.xhtml#hash-alg)

But often there is no matching algorithm specification in existing IANA registries that support the appropriate selection of parameters.
E.g. in {{?RFC5116}} the definition for AEAD_AES_128_GCM proposes a nonce length of 96 bit.
If we want to use a length higher than this we have to look for another registry than [AEAD Algorithms](https://www.iana.org/assignments/aead-parameters/aead-parameters.xhtml), because there is no specification supporting a higher nonce length. 
However, for cryptographic use cases such as asymmetric encryption and digital signing appropriate algorithm specifications can be found in the [CBOR Object Signing and Encryption (COSE)](https://www.iana.org/assignments/cose/cose.xhtml) registry.
For the crypto use case hashing the [Named Information Hash Registry](https://www.iana.org/assignments/named-information/named-information.xhtml#hash-alg) defines appropriate specifications for hash algorithms that matches a possible set of parameters for the Secure Crypto Config.

It is difficult to find a specification of an algorithm inside existing IANA registries that exactly match all of the chosen parameters for a specific cryptographic use case, especially in the case of symmetric encryption.
It is also tedious to search for different specifications in different registries.
Therefore, it could be advantageous to create a IANA registry explicitly for the creation of Secure Crypto Config like described in {{IANA}}

## Data Structures {#dataStructures}

- [ ] TODO refer to IANA registry in Usage explanation
- [ ] TODO is JSON a appropriate format?
- [ ] TODO How is COSE more appropriate/in parts of JSON? or is a mapping (=> parsing needed) better between COSE<->JSON?

For each defined security level a distinct JSON-file must be provided. 
These files must follow a common schema and contain the suitable parameters depending on its security level. 
The general schema of the JSON files is shown in {{scc_general}}.

~~~~
{::include src/scc_general.json}
~~~~
{: #scc_general title="General JSON format"}

- SecurityLevel : Contains the number of the corresponding Security Level of the Secure Crypto Config 
- PolicyName: Contains the name of the corresponding Secure Crypto Config according to the naming schema defined in {{naming}}
- Publisher: Contains an array of all parties that were participating in the consensus finding process
  - name: Name of the participating party
  - URL: Put in the official URL of the named publisher
- Version: Contains version in the format defined in {{version}}
- PolicyIssueDate: Date at which the Secure Crypto Config was published in the format: YYYY-MM-DD
- Expiry: Date at which the Secure Crypto Config expires in the format: YYYY-MM-DD
- Usage: Contains an array of all cryptographic use cases defined in {{cryptoCase}}. 
- For each cryptographic use case, usually at least two as described in {{consensus}}, agreed upon algorithms with necessary parameters are included. Each of these algorithms with its parameters is specified with its unique identification name defined in an Secure Crypto Config IANA registry.

This format allows custom algorithm/parameter selections both by overwriting use cases completely or by adding only specific algorithm identifiers.

## Human readable format {#humanReadableFormat}

- [ ] TODO look through

There should also be a human readable format on a webpage derived from the source format. This format must not be cryptographically protected but should be generated automatically from the source format. For a good human readability the content of the source format is presented in form of a table showing the cryptographic use case and its proposed algorithms. There is one table for each defined security level. Such a table for one security level could be presented on a webpage like shown in {{example}}.

## Official SCC Repository

### Location of SCC Repository

The needed Secure Crypto Config files should be published at an official Github repository.
There all current versions will be provided during the interval of the Publication phase (see {{process}}).
Additionally all previously published files are still stored at this location even if new versions are published. 

### Format of SCC Repository

```
scc-repo
- configs
  - 2020
    - 00
      - 2020-00.SEC1.json
      - 2020-00.SEC1-signature1
      - 2020-00.SEC1-signature2
      - 2020-00.SEC2.json
      - 2020-00.SEC2-signature1
      - 2020-00.SEC2-signature2
      - 2020-00.SEC3.json
      - 2020-00.SEC3-signature1
      - 2020-00.SEC3-signature2
      - 2020-00.SEC4.json
      - 2020-00.SEC4-signature1
      - 2020-00.SEC4-signature2
      - 2020-00.SEC5.json
      - 2020-00.SEC5-signature1
      - 2020-00.SEC5-signature2
    - 01
    - 02
  - 2021
  - 2022
  - 2023
  - 2024
```
{: #scc-repo-example title="Example for SCC Repository content"}

```
scc-repo
- configs
  - a
      - b
      - 0c1
      - ReallySecure
  - 0x1111
    - 2020-00.SEC1.json
    - 2020-00.SEC1-signature1
    - 2020-00.SEC1-signature2
    - 2020-00.SEC2.json
    - 2020-00.SEC2-signature1
    - 2020-00.SEC2-signature2
    - 2020-00.SEC3.json
    - 2020-00.SEC3-signature1
    - 2020-00.SEC3-signature2
    - 2020-00.SEC4.json
    - 2020-00.SEC4-signature1
    - 2020-00.SEC4-signature2
    - 2020-00.SEC5.json
    - 2020-00.SEC5-signature1
    - 2020-00.SEC5-signature2
  - asdf
  - afd
  - af
```
{: #scc-repo-example2 title="Example for SCC Repository content with custom naming scheme"}

The SCC configuration files are expected to be in any folder hierarchy below the folder `configs`-folder.
The folder which contains the final SCC JSON files must contain the required signature files at the same level of the JSON files.
Each JSON file should be accompanied by a corresponding signature file that has the same filename without extension as the JSON file, suffixed by `-signatureX` where `X` is a counter starting at 1.

### Integrity/Signing process

Each JSON-file should be accompanied by at least two signatures.

- [ ] TODO what kind of signing process should be used? 
  - [ ] GPG?
  - [ ] openssl?
  - [ ] Git GPG signed commits?
  - [ ] Use an SCC recommended signing algorithm/format
- [ ] Can two signatures be put in one signature file? Should they be put in the same file?
- [ ] Public Key distribution?!

The signatures should be generated by entities defined by the Secure Crypto Config Working Group. 
They are responsible to publish and renew the used public keys.

# Secure Crypto Config Interface 

## Semantic Versioning

The implementation of the Secure Crypto Config Interface MUST follow [Semantic Versioning](https://semver.org/), which specifies a version format of **X.Y.Z** (Major.Minor.Patch) and semantics when to increase which version part. 

## Process to get and update the Secure Crypto Config 

- [ ] TODO Description where to get SCC and how to validate

### Integrity Check

- [ ] TODO which public keys should be used? (See above Integrity/Signing process Public Key distribution?!)


### Delivery of Secure Crypto Config with Interface

- [ ] Define used subfolder or similar

### Using a different Secure Crypto Config Repository/Folder

- [ ] TODO what method to set a different SCC config/repo path

## Application Programming Interface (API)

### Methods and Parameters

- [ ] TODO see/unify with Java implementation {{scc_java_api_example}}

Intended methods and parameters included in the Java interface are currently work in process. First suggestions can be seen in {{scc_java_api_example}}.

#### Supported Algorithm Parameter Types

- [ ] TODO What is with parameters that have to be chosen during runtime? (e.g. the length of the nonce can be specified but not its content?) Maybe refer to how the [PHC String Format](https://github.com/P-H-C/phc-string-format/blob/master/phc-sf-spec.md) describes how parameters must be defined and only allow constants and csprng generated content?

Cryptography algorithms require different parameters.
The Secure Crypto Config Interface considers the following types of parameters:

- Parameter Size (e.g. key length in bit)
- Parameter Counter Content (e.g. nonce)
- Parameter Secure Random Content (e.g. nonce)
- Parameter User Automatic Tunable Content (e.g. memory consumption for Argon2 password hashing algorithm)
- Parameter User Defined Content (e.g. plaintext and key for symmetric encryption)
- Parameter Compound Parameter Content (e.g. counter + random = nonce)

### Automatic Parameter Tuning

- [ ] TODO is it possible to define new algorithm/parameter combinations on the fly (in extensions/derivations) or are only SCC IANA registry identifiers allowed/usable?

It should be possible to have user specified parameters such as the key/nonce length explicitly given by the user, but also a performance mode which makes an evaluation for each configuration and give back a prioritized list for each configuration.
In this way it is possible to select parameters depending on systems properties.
Such a parameter choice would be beneficial e.g. in the case of [Argon2](https://tools.ietf.org/html/draft-irtf-cfrg-argon2-03) in which one parameter for the memory usage must be given.
This choice should be chosen on the base of the corresponding system.
That kind of parameter selection can be seen e.g. in  [Libpasta Tuning](https://libpasta.github.io/advanced/tuning/), which returns a secure parameter set depending on executed evaluations.

### Output of readable Secure Crypto Config

- [ ] TODO Output of current SCC integrated in crypto library. Method like "printUsedSCC()" with output that shows if it is a validate SCC
- [ ] TODO In case of own derived version e.g. from a company from the original SCC it could be the case that there is no signature to check

A possibility to check the used Secure Crypto Config for validity should be provided. This can be done by testing the signature of the provided file for validity. 
If it is a self-derived version from the original Secure Crypto Config than it could also be possible that there is no signature provided that could be checked for validity.

## TODOs

- [x] TODO Describe used versioning concept for SCC. 
  - [x] TODO Refer to [Semantic Versioning](https://semver.org/)
- [ ] TODO describe requirements for cryptography API implementors and designers
- [x] TODO decide how the configuration should be made available to programmers
- [x] TODO which platforms are suitable?
- The SCC could be provided on a suitable platform (?) and is accessible over the network (adversaries? e.g. http connection)
  - [ ] e.g. should there be constants like "SCC_TOP_SECRET_LATEST" and "SCC_TOP_SECRET_LATEST". 
  - [ ] And like "SCC_TOP_SECRET_LATEST.AES" which points always to the latest Secure Crypto Config definition for AES parameters.
- [ ] TODO how should cryptography implementations, that implement/support SCC, generate the parameters?
- [x] What kind of parameters can be chosen based on the Secure Crypto Config? => E.g. Should be all except the plaintext and the key for encryption algorithms. Also many parameters can be generated based on cryptographically secure random numbers.
- [x] TODO The Secure Crypto Config Interface should include a performance evaluation mode which evaluates the performance of each configuration and returns a prioritized list for each configuration. E.g. cf. [Libpasta Tuning](https://libpasta.github.io/advanced/tuning/)


# Cryptography Library Implementation Specification

Cryptography libraries should provide the above mentioned Secure Crypto Config Interface.
Until a common cryptography library provides the Secure Crypto Config Interface itself, a separate interface library should be used instead.

# Cryptography Algorithm Standards Recommendation

- [ ] TODO should there be a template for cryptography algorithm standards (in addition to COSE) for the Secure Crypto Config or is it enough that the Secure Crypto Config Consensus Finding defines the secure parameters for all cryptography algorithms?

# Security Considerations
- [x] TODO are some of the listed common issues relevant?: [TypicalSECAreaIssues](https://trac.ietf.org/trac/sec/wiki/TypicalSECAreaIssues)
- [x] TODO check if security considerations of TLS 1.2 are relevant, especially appendix [D, E and F](https://tools.ietf.org/html/rfc5246#appendix-D)
- [ ] TODO Are these appropriate security considerations?

## Consensus Finding

- Only trustworthy and cryptographic specialized institutions should participating otherwise a Secure Crypto Config with a weak and insecure parameter set could be provided as a result.


## Publication Format

- The operators of the Secure Crypto Config must ensure that potential unauthorized parties are not able to manipulate the parameters of the published Secure Crypto Config. 

## Cryptography library implementation

- Integrity must be ensured if potential users want to fetch the provided Secure Crypto Config from the corresponding platform over the network e.g. by using a signature.
- Users should only trust Secure Crypto Config issued from the original publisher with the associated signature. Users are responsible verify this signature.

## General Security Considerations

### Special Use Cases and (Non-)Security Experts

The Secure Crypto Config does not apply to all use cases for cryptography and usage of cryptography primitives.
It is meant to provide secure defaults for the most common use cases and for non-expert programmers.
Additionally, non-experts may still implement vulnerable code by using the Secure Crypto Config. 
Yet, it should reduce the vulnerabilities from making the wrong choices about parameters for cryptography primitives.

## Security of Cryptography primitives and implementations

- [ ] TODO is this redundant? because it is also described in the assumptions section.

- The Secure Crypto Config assumes that both the proposed algorithms and the implementations (cryptography libraries) for the cryptography primitives are secure.
- Since the validity of the parameters defined in the Secure Crypto Config can change infrequently (e.g. discovered flaws in proposed algorithms) the lifespan of the Secure Crypto Config and an emergency process (see {{emergency}}) should be considered carefully before the actual publication.
- Operators should keep in mind the validity of the provided parameters e.g. to be able to react fast in the case discovered flaws in algorithms.

### Security Guarantees

The Secure Crypto Config does not guarantee security for the proposed parameter configuration.
E.g. a new algorithm that can do brute-force attacks exponentially faster could be existing or published right after the publication of the most recent Secure Crypto Config was published itself.
Yet, the Secure Crypto Config makes a best effort to be as up-to-date with recent discoveries, research and developments in cryptography algorithms as possible.

## TODO

- [x] TODO Security
- [x] TODO are some of the listed common issues relevant?: [TypicalSECAreaIssues](https://trac.ietf.org/trac/sec/wiki/TypicalSECAreaIssues)
- [x] TODO check if security considerations of TLS 1.2 are relevant, especially appendix [D, E and F](https://tools.ietf.org/html/rfc5246#appendix-D)

### Threat Model / Adversaries {#threatModel}

- [x] TODO describe adversaries / group of adversaries and their powers -> Attacker Resources and Capability
- [x] TODO describe other general threats for the Secure Crypto Config (maybe put that into the section Security Considerations):
  - [x] Process
  - [x] Publication
  - [x] Content

There are different possibilities in which a potential adversary could intervene during the creation as well as after the publication of the Secure Crypto Config. These attack scenarios must be considered and prevented.

- **Process:** During the creation process, it is necessary for selected institutions to agree on a secure parameter set.
It could be possible that one party wants to influence this process in a bad way.
As a result, it could be agreed on weaker parameter sets than originally intended.

- **Publication:** After the publication of the Secure Crypto Config a potential attacker could gain access to the provided files on the corresponding platform and change the content to an insecure parameter set.

- **Content:** Depending on the distribution method of the Secure Crypto Config, it is also possible that an attacker could change the content of the Secure Crypto Config as man-in-the-middle.
Especially if a http connection is used to obtain the Secure Crypto Config, this will be a serious problem.


# IANA Considerations

- [] TODO Are there IANA Considerations?
- [] TODO May add reference to own registry

The data structure (see {{dataStructures}}) defined in this document uses the JSON format as defined in {{!RFC8259}}.

--- back

# Examples {#example}

In the following table possible algorithm and  parameter choices for the different cryptographic use cases (see {{cryptoCase}}) depending on the security levels (see {{securityLevels}}) are given. 
This is only a visualization format of the source format for which the corresponding example is shown in {{scc_example}}.
This format could also be used to show the content of the JSON source format in a human readable way on a webpage like described in {{humanReadableFormat}}.

- [x] TODO use algorithm/parameter identifiers

| UseCase\Level  | 5                                                                                   |
| -------------- | ----------------------------------------------------------------------------------- |
| **Symmetric**  | AEAD_AES_256_GCM<br> [RFC5116] <br> AEAD_CHACHA20_POLY1305 <br>{{?RFC8439}}         |
| **Asymmetric** | RSAES-OAEP w/ SHA-512<br> [RFC8230]                                                 |
| **Hashing**    | sha3-512<br> [[FIPS 202]](https://nvlpubs.nist.gov/nistpubs/FIPS/NIST.FIPS.202.pdf) |
| **Signing**    | ES512 <br> {{-COSE}}                                                                |
{:#scc_useCase_level}


## JSON Secure Crypto Config
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

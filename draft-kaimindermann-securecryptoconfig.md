---
title: Secure Crypto Config
abbrev: SCC
docname: draft-kaimindermann-securecryptoconfig-latest
category: info

ipr: trust200902
area: General
workgroup: cfrg
keyword: Internet-Draft

stand_alone: yes
smart_quotes: no
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

Choosing secure cryptography algorithms and their corresponding parameters is difficult.
Also, current cryptography APIs cannot change their default configuration which renders them inherently insecure. 
The Secure Crypto Config provides a method that allows cryptography libraries to change the default cryptography algorithms over time and at the same time stay compatible with previous cryptography operations. 
This is achieved by combining three things standardized by the Secure Crypto Config:
(1) A process that is repeated every two years, where a new set of default configurations for standardized cryptography primitives is published in a specific format.
(2) A Secure Crypto Config Interface that describes a common API to use cryptography primitives in software
(3) using COSE to derive the parameters from output of cryptography primitives, otherwise future changes of the default configuration would change existing applications behavior.

--- note_Note_to_Readers

Comments are solicited and should be addressed to the [GitHub repository issue tracker](https://github.com/secureCryptoConfig/secureCryptoConfig) and/or the author(s)

--- middle

# Introduction
<!--
### General TODOS

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
-->

## Motivation

Cryptography needs standardization to work across various domains and to be interoperable between different implementations.
One domain that is not covered sufficiently by cryptography standards is the selection and maintenance of cryptography algorithms and their parameters.
Choosing an appropriate and secure cryptography algorithm alone is difficult. 
Yet, even more difficult is the choice of the required and matching parameters for that algorithm (e.g. [Argon2 has 10 input parameters](https://tools.ietf.org/html/draft-irtf-cfrg-argon2-10#section-3.1)).
After the choice has been made, all involved parties need to use exactly this configuration.
There is no specification on how the chosen cryptography configuration should be stored, distributed and retrieved.
Furthermore, supporting more than one configuration or being able to add future configurations is not defined.
That reduces software compatibility and increases maintenance efforts.

Cryptography algorithm implementations, regardless of for one algorithm or multiple ones, offer some kind of Application Programming Interface for developers to use the algorithms.
Yet, in many cases these interfaces provide no abstraction from the underlying algorithm but expose much of the internal states and parameters.
Also the more abstracting interfaces, usually found in the standard libraries of programming languages, require users to have much cryptography experience to use them correctly and securely.
Moreover, even approaches that tried to increase usability by providing defaults, these defaults become quickly outdated but cannot be changed in the interface anymore as applications using these defaults rely on that functionality.

It sounds a lot like a problem for software engineering and not for cryptography standardization.
But software engineering alone cannot provide a programming interface for cryptography algorithms that also works for future algorithms and parameters and at the same time is able to change the default implementation easily.
Both the choice of the algorithm/parameters and the default behavior must be automated and standardized to remove this burden from developers and to make cryptography work by default and in the intended secure way.

The Secure Crypto Config approaches this problem first by providing a regularly updated list of secure cryptography algorithms and corresponding parameters for common cryptography use cases.
Second, it provides a standardized Application Programming Interface which provides the Secure Crypto Config in a misuse resistant way to developers.
Third, it leverages an already standardized format ({{-COSE}}) to store the used parameters alongside the results of cryptography operations. 
That ensures that future implementations can change their default cryptography algorithms but still parse the used configuration from existing data and perform the required cryptography operations on it.

Each of these approaches could be used on its own. 
Yet, the combination of them allows software to be easier to maintain, more compatible with other cryptography implementations and to future security developments, and most importantly more secure.

The Secure Crypto Config makes common assumptions that are not true for all possible scenarios.
In cases where security experts are indeed involved and more implementation choices have to be made, the Secure Crypto Config still allows the usage of predefined or even custom cryptography algorithms and parameters.

<!--
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
This can be seen in the [example of Argon2](https://tools.ietf.org/html/draft-irtf-cfrg-argon2-10#section-3.1) in which it is necessary to provide up to 8 additional parameters to be able to start the actual hashing.
There is a huge gap between the concept and the actual implementation.
Secure Crypto Config can close this gap and help the developers to make a secure choice by simply looking into the Secure Crypto Config.
Despite non-expert developers the Secure Crypto Config can also be used as a template for standardization institutions such as NIST or BSI to provide their parameter recommendations in a structured and machine readable way.
Furthermore, cryptographic library developers can integrate the Secure Crypto Config to offer secure defaults.
But in practice, it is often not possible to simply change the interfaces of existing cryptographic libraries.
Therefore, an additional abstraction layer is needed here, which should provide an easy to use interface for the programmers to actually use the library functions with the secure parameter choice.
An example of such an additional abstraction layer can be seen in {{scc_java_api_example}}.

It already exists a Data Structure for the Security Suitability of Cryptographic Algorithms (DSSC) {{-DSSC}} in an XML-format that stores some of the necessary information, that are also required for the Secure Crypto Config.
DSSC represents a data structure that should support the automatic analysis of security suitability of given cryptographic algorithms.
In contrast to Secure Crypto Config, DSSC does not make suggestions for currently secure parameters that should be used but is intended for the evaluation of algorithms.
Nevertheless, it can be regarded as a reference, as it contains a lot of information that will be needed in Secure Crypto Config.

The Secure Crypto Config should also have the property of algorithm agility which includes that it should be easy to switch from one set of parameters and algorithms to another.
This is necessary to be able to adapt to the current state of security.
Also, it is important to use a common set of cryptographic algorithms and consider how many choices of parameters and algorithms to provide.
Procedures that contribute to the achievement of these properties are described in more detail in the "Guidelines for Cryptographic Algorithm Agility and Selecting Mandatory-to-Implement Algorithms" BCP 201 {{?RFC7696}} and the "Algorithm Agility Procedure for the Resource Public Key Infrastructure (RPKI)" BCP 182 {{?RFC6916}}.

One problem in the usage of the Secure Crypto Config is the fact that the defined parameters will change over time.
Therefore, it is necessary to check the Secure Crypto Config with its parameters regularly and adjust them if necessary.
A regular adjustment process once a year, for example, could guarantee this. However, adjusting the parameters is not always easy.
If for example a plaintext should be encrypted one has to keep the same parameters for both encryption and decryption.
Otherwise, the ciphertext obtained by encryption cannot be decrypted correctly back again.
In contrast, we can see that in the use of Transport Layer Security (TLS), parameter changes are comparatively less complicated. For the duration of a connection between client and server, it is possible to choose from different algorithms in advance.
A selection of possible algorithms for TLS with recommendation for their usage can be found for example at the [IANA Registry for TLS Cipher suites](https://www.iana.org/assignments/tls-parameters/tls-parameters.xhtml#tls-parameters-4).
This issue could be solved by developing different backward compatible layers. But, it would be more advantageous to store the parameters used for the encryption in addition to the actual encrypted data.
This is also necessary to prevent applications from becoming incompatible with updated Secure Crypto Config over time.
To store this necessary information there already exists a standard named CBOR Object Signing and Encryption (COSE) {{-COSE}}.
The parameters and algorithms defined for COSE can be found at the [IANA registry for COSE](https://www.iana.org/assignments/cose/cose.xhtml).
COSE represents a data structure that contributes to the storage of the cryptographic output (e.g. ciphertext) as well as the used parameters in a structured way.
-->

## Terminology

### Conventions and Definitions

The key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL NOT", "SHOULD",
"SHOULD NOT", "RECOMMENDED", "NOT RECOMMENDED", "MAY", and "OPTIONAL" in this
document are to be interpreted as described in BCP 14 {{RFC2119}} {{!RFC8174}}
when, and only when, they appear in all capitals, as shown here.

### Terms

<!--- [x] TODO remove not used in document: "Alice and Bob","hybrid encryption", "permanent storage", "risk",  "vulnerability",-->

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

The Secure Crypto Config has the following main use cases: 

- Centralized and regularly updated single source of truth for secure algorithms and their parameters for most common cryptography primitives and use cases.
- Both machine and human readable format to specify the above mentioned cryptography algorithm/parameter configuration. 
  The format is also extensible to allow others (e.g. governmental or commercial organizations) to define their own set of cryptography configurations.
- Standardized cryptography API that not uses the Secure Crypto Config for the selection of the most recent cryptography configurations but also uses a standardized cryptography operation output format to enclose the chosen parameters.


### Cryptography Use Cases {#cryptoCase}
<!--- TODO [ ] should CSPRNG, Key generation, Password hashing be considered?-->

The Secure Crypto Config covers cryptography algorithm and parameter configurations for widely used cryptography use cases defined in the following sections.

#### Symmetric Encryption

Symmetric Encryption is an important cryptographic primitive especially as it is usually multiple magnitudes faster both for encryption and decryption than asymmetric cryptography.
Yet, the secret has to be shared with all participants.

The only expected input parameters by cryptography users:

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

Besides symmetric encryption is asymmetric encryption another important cryptographic primitive to considered.

The only expected input parameters for encryption:

- plaintext 
- public key

Expected output: ciphertext.

The only expected input parameters for decryption:

- ciphertext
- private key 

Possible secure usage:

- RSA
- RSAES-OAEP w/ SHA-512,-42,RSAES-OAEP w/ SHA-512

#### Hashing
Hashing is an important cryptographic primitive and is often needed as a part of many other cryptographic use cases e.g. password derivation.

The only expected input parameters by cryptography users:

- plaintext

Expected output: hash.

Possible secure usage:

- SHA-512 (TEMPORARY - registered 2019-08-13, expires 2020-08-13);-44;SHA-2 512-bit Hash
- SHAKE256 (TEMPORARY - registered 2019-08-13, expires 2020-08-13);-45;256-bit SHAKE


#### Password Hashing

The secure storage of passwords requires hashing. 
Yet, password hashing requires that the hashing can not be performed very fast to prevent attackers from guessing/brute-forcing passwords from leaks or against the live system.
E.g. it is totally fine for users if the login takes 0.1 seconds instead of microseconds.
This results in special families of hash algorithms that offer additional tuning parameters.

The only expected input parameters by cryptography users:

- plaintext
- hash-algorithm

Expected output: hash.

Possible secure usage:

- Argon2id

#### Key Generation

- [ ] TODO should key generation be considered? (Symmetric/Asymmetric)

A key is necessary for many cryptographic use cases e.g. symmetric and asymmetric encryption.
Therefore, key generation is an important part while implementing cryptographic code. 

The only expected input is the intended use case.

Expected output: key.

Possible secure generation:

- Use of CSPRNG
- Keys derived via derivation function from passwords/other keys

#### Digital Signatures
Signing is an important and often needed cryptographic use case. It is based on the principle of asymmetrical encryption.

The only expected input parameters for signing:

- plaintext 
- private key

Expected output: signature.

The only expected input parameters for verifying the signature:

- signature
- public key 

Expected output: valid/not-valid.

Possible secure usage:

- ECDSA
- ES512;-36;ECDSA w/ SHA-512

# Requirements and Assumptions

## Requirements {#requirements}

<!--- [ ] TODO in "Publication Format and Distribution Requirements"/Machine readable. Must be easy to extend/alter by other organizations (e.g. maybe the BSI wants to publish its own secure crypto config that differs from the standardized one. Maybe a hierarchical approach with inheritance from the base SCC?) added?-->
<!--- [ ] TODO in "General requirements", *Threat Model* / adversary powers. What kind of attacker should the secure crypto config protect against? (Almighty? Government? Company?). Presumably  different group of attackers.-->

In the following, all requirements are listed that regard the Secure Crypto Config or the Secure Crypto Config Interface.

- Security Level Requirements:
The Secure Crypto Config should define different security levels.
E.g. information has different classification levels and longevity. 
Additionally, cryptography operations could not or much slower perform on constrained devices, which should also be handled with the security levels.
For each security level, the consensus finding process and entities shall publish a distinct secure crypto config. 

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
      Otherwise, ambiguity would make it harder for developers and cryptography implementors to make correct and secure choices.
    - There must be a versioning that allows to distinguish between Secure Crypto Configurations and what is the latest Secure Crypto Config.
    - There must be a deprecation process that ensures usage of outdated/insecure Crypto Configurations cases.
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
    - Must be easy to verify the authenticity of the Secure Crypto Config (e.g. is this really what the CFRG has published?)

- Cryptography library integration requirements:
  - Easy to integrate by cryptography libraries
  - Experts should still be able to use/access the unaltered output of cryptographic primitives
  - Recommendation what should be the default Secure Crypto Config for a cryptography library (e.g. should it be the one with the highest security level or *only* a weaker one?)
  - Recommendation of what should a cryptography library do if it can not support the parameters specified in the latest Secure Crypto Config. (E.g. key size for RSA would be n*2 and the library supports only n)
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
These security levels depending on the following security constraints: **Information classification (Secret, Confidential), Longevity (less than one day, more than a day), Constrained devices (constrained, not constrained)**.
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

Information classification within this document is about the confidentiality of the information.
Not all information is equally confidential, e.g. it can be classified into different classes of information.
For governmental institutions usually three classes are used: Confidential, Secret, or Top Secret.
The Secure Crypto Config considers only **Confidential** and **Secret** for its standardized security levels.
Further levels with other classifications can be added by other organizations.
Additionally, in common (non-governmental) use cases data is not labeled with an information class.
Hence, often only one class is chosen for the cryptography processing of all data.

The Secure Crypto Config does not endorse a definition of the information classes, yet **Secret** information is to be considered to have higher confidentiality requirements than **Confidential** information.

### Longevity

The time how long information has to be kept confidential can influence cryptography parameters a lot.
Usually what you talked about with your friends should be kept confidential for a lifetime.
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
Therefore, the Secure Crypto Config defines constraint devices not based on concrete processing power (e.g. 100k instructions per second):

A device is constrained when it has **multiple orders of magnitudes** fewer resources than a current (not a new one, but broadly in use at the time of publication of a Secure Crypto Config!) standard personal computer.

For example, if a current standard personal computer can encrypt with 1 GiB/s, a constrained device would be all devices that can only perform the same cryptography operation with less than 10 MiB/s.
Resources can be everything important for cryptography like dedicated cryptography hardware, instruction sets, memory, power consumption, storage space, communication bandwidth, latency etc. 
The Secure Crypto Config considers only **constrained** and **non-constrained** for its standardized security levels.
Further levels with other constrained resource definitions can be added by other organizations.

### n-Bit-Security

<!--- [ ] TODO add n-Bit-Security to Security Levels in time-independent way (e.g. referring to increases in bit instead of hardcoded values for the levels) ?-->

n-Bit Security Level:

> A cryptographic mechanism achieves a security level of n bits if costs which are equivalent to 2^n calculations of the encryption function of an efficient block cipher (e.g. AES) are tied to each attack against the mechanism which breaks the security objective of the mechanism with a high probability of success. BSI

### Attacker Resources and Capabilities {#attacker}

<!-- - [ ] TODO is a more formal definition useful, e.g. a list of [Attack Models](https://en.wikipedia.org/wiki/Attack_model)?* *TODO What about side-channels, especially if the attacker has access to the system?-->

The Secure Crypto Config considers only the following same attacker resources and capabilities for all standardized security levels:

- The attacker knows all used algorithms and parameters except secrets according to Kerckhoffs's principle. 
- The attacker has access to the system used for cryptography operations and can utilize its cryptography operations apart from obtaining secrets. 
- The attacker can utilize very high-performance computing resources such as supercomputers and distributed computing (e.g. this includes very high memory, storage, processing and networking performance)

Further security levels with other attacker definitions can be added by other organizations.

# Consensus Finding Process and entities {#consensus}

To provide a Secure Crypto Config, it is necessary to agree upon a secure and appropriate cryptographic parameter set for each security level (see {{securityLevels}}).
This must happen in a common consensus finding process which takes place during regular intervals.
The consensus finding process is based on the established RFC process during which the Secure Crypto Config Working Group
decides in cooperation with the Crypto Forum Research Group (CFRG) and other institutions like the Bundesamt für Sicherheit in der Informationstechnik (BSI) or the National Institute of Standards and Technology (NIST) for a set of secure parameters.
After the successful decision, the agreed on parameters can be added in the defined publication data structures (see {{dataStructures}}) and provided on the repository platform.

## Consensus Finding

Consensus must be found two years after the last consensus was found.
This ensures that there is a new Secure Crypto Config every two years, even if the configuration itself has not changed.
There is a regular process and an emergency process to release Secure Crypto Configurations.

### Regular Process {#process}

<!--- [ ] TODO adapt format for Proposal phase submission?-->

The process has three phases that MUST be finalized within 2 years:

- (1) One year **Proposal phase** during which all participating entities must propose at least two cryptography algorithms and parameters per cryptography use case per security level.
- (2) Six months **Consensus finding phase** during which all participating entities must agree on a common Secure Crypto Config.
- (3) Six months **Publication phase** ensures the publication of the final Secure Crypto Config AND allows the Secure Crypto Config Interface and other cryptography implementations to integrate the necessary changes.

During the Proposal phase the proposed algorithms and all necessary parameters should be submitted in table form for each security level and defined cryptographic use case as proposed.
This table format is simply structured and is easy to read by humans as the Consensus finding phase can only be done manually.
It is important that the parameters for each cryptographic use case depending on its security level can be found easily by the participants of the consensus finding process such that it is possible to get to an agreement faster.

### Emergency Process {#emergency}

<!--- [ ] TODO in 3) would replacing also be an option? how?-->
- [ ] TODO How can the Working Group alter the Secure Crypto Config IANA registry / or use the RFC Errata process?

In cases when a regularly still valid Secure Crypto Config would become insecure regarding either a proposed algorithm or a proposed parameter choice it must be revised with the following process:

1. Determine the insecure configuration.
2. Remove the insecure configuration.
3. Publish the revised Secure Crypto Config with a new patch version.
4. Mark the old (unrevised) Secure Crypto Config as deprecated.

Examples for emergency cases are drastically better brute force algorithms or brute force performance (e.g. quantum computers/algorithms), drastically discovered flaws in proposed algorithms and their configurations.

An applied emergency process results in the problem that currently used Secure Crypto Config Interface versions are no longer up-to-date, because they are still supporting the no longer secure algorithms. Therefore, the corresponding algorithms need to be marked as insecure. If e.g. a proposed algorithm gets insecure this can be marked inside the corresponding Secure Crypto Config IANA registry entry as no longer proposed to make the users aware of its insecurity. The Working Group itself can decide when to alter the Secure Crypto Config IANA registry. 

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

<!--- [ ] TODO what is "significant cryptography expertise"?-->

Entities that participate in the proposal phase SHOULD have significant cryptography expertise.
Entities that participate in the consensus finding phase MUST have significant cryptography expertise.
Cryptographic expertise is defined by the Secure Crypto Config Working Group or the CFRG.

<!--Each execution of the regular process for the creation of a Secure Crypto Config is described in detail in a separate RFC. The Working Group itself decides on the participating entities.-->

# Publication Format and Distribution

In general the Secure Crypto Config is published via JSON files in an official repository. 
The Secure Crypto Config also utilizes IANA registries.

## Versioning {#version}

The Secure Crypto Config is regularly published in a specific year. 
Therefore, the Secure Crypto Config format MUST use the following versioning format: **YYYY-PATCH**.
YYYY is a positive integer describing the year (using the Gregorian calendar, and considering the year that has not ended in all time zones, cf. Anywhere on Earth Time) this specific Secure Crypto Config was published.
PATCH is a positive integer starting at 0 and only increasing for emergency releases.

## Naming {#naming}

<!-- - [ ] TODO keep in mind the secure crypto config interface, which should be able to use these naming conventions in multiple programming languages-->
<!-- - [ ] TODO possibly: SCC_SecurityLevel_**SecurityLevelNumber**_**Version**" ? Put the corresponding number in **Security Level Number** depending for which security level the SCC is created for.-->

The naming of official released SCCs must follow this format:

`SCC_**Version**_LEVEL_**Security Level Number**`

E.g. a Secure Crypto Config for Security Level 5 release in 2020 the first time (so no patch version) would be named: `SCC_2020-00_LEVEL_5`

The naming of files is not regulated, only the content is standard relevant.
Yet, the Secure Crypto Config Files should use the mentioned naming convention as well as adding a suffix (file type ending) `.json` to prevent ambiguity and remove implementation choices:

`SCC_**Version**_LEVEL_**Security Level Number.json**`

## Secure Crypto Config IANA Registry {#IANA}

**NOT NEEDED?, as the Secure Crypto Config uses other registries, e.g. COSE. No final decision, yet.**

- [ ] TODO Naming convention. Specification depending on crypto use case?
- [ ] TODO dash character "-" not possible in enum!

The Secure Crypto Config requires one IANA Registry with the following columns:

- Secure Crypto Config release version: **YYYY-PATCH**
- Distinct **Algorithm-Parameter-Identifier** that uniquely identifies a cryptography algorithm and the parameters
- Distinct and **constantly available reference** where all parameters are unambiguously defined
- (Optional) Short description of the parameters

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

### Utilized Algorithm Registries {#registries}

The Secure Crypto Config can only propose cryptography algorithms and parameters that have been standardized.
Therefore, it refers to the following IANA registries: 

- [CBOR Object Signing and Encryption (COSE)](https://www.iana.org/assignments/cose/cose.xhtml)
- [AEAD Algorithms](https://www.iana.org/assignments/aead-parameters/aead-parameters.xhtml)
- [Named Information Hash Registry](https://www.iana.org/assignments/named-information/named-information.xhtml#hash-alg)

Used registries must define all required parameters for an algorithm to implement it without ambiguity. 
E.g. implementations must not be able to choose other parameter values for a cryptography algorithm and parameter combination.

<!--But often there is no matching algorithm specification in existing IANA registries that support the appropriate selection of parameters.
E.g. in {{?RFC5116}} the definition for AEAD_AES_128_GCM proposes a nonce length of 96 bit.
If we want to use a length higher than this we have to look for another registry than [AEAD Algorithms](https://www.iana.org/assignments/aead-parameters/aead-parameters.xhtml), because there is no specification supporting a higher nonce length. 
However, for cryptographic use cases such as asymmetric encryption and digital signing appropriate algorithm specifications can be found in the [CBOR Object Signing and Encryption (COSE)](https://www.iana.org/assignments/cose/cose.xhtml) registry.
For the crypto use case hashing the [Named Information Hash Registry](https://www.iana.org/assignments/named-information/named-information.xhtml#hash-alg) defines appropriate specifications for hash algorithms that match a possible set of parameters for the Secure Crypto Config.

It is difficult to find a specification of an algorithm inside existing IANA registries that exactly match all of the chosen parameters for a specific cryptographic use case, especially in the case of symmetric encryption.
It is also tedious to search for different specifications in different registries.
Therefore, it could be advantageous to create an IANA registry explicitly for the creation of Secure Crypto Config like described in {{IANA}}
-->

## Data Structures {#dataStructures}

<!--- [ ] TODO How is COSE more appropriate/in parts of JSON? or is a mapping (=> parsing needed) better between COSE<->JSON?-->

For each defined security level, a distinct JSON file must be provided. 
These files must adhere to the common schema and shown in {{scc_general}} and described in the following.

~~~~
{::include src/scc_general.json}
~~~~
{: #scc_general title="General JSON format"}

- SecurityLevel: Contains the number of the corresponding Security Level of the Secure Crypto Config 
- PolicyName: Contains the name of the corresponding Secure Crypto Config according to the naming schema defined in {{naming}}
- Publisher: Contains an array of all parties that participated in the consensus finding process
  - name: Name of the participating party
  - URL: Put in the official URL of the named publisher
- Version: Contains version in the format defined in {{version}}
- PolicyIssueDate: Date at which the Secure Crypto Config was published in the format: YYYY-MM-DD
- Expiry: Date at which the Secure Crypto Config expires in the format: YYYY-MM-DD
- Usage: Contains an array of objects for each cryptographic use case defined in {{cryptoCase}}. 
  - For each cryptographic use case, at least two agreed upon algorithms (see {{consensus}}) with necessary parameters are included. 
    Each of these algorithms with its parameters is specified with its unique identification name defined in a IANA registry used by the Secure Crypto Config.

This format allows custom algorithm/parameter definitions both by overwriting use cases completely or by adding only specific algorithm identifiers via custom configurations.

## Human readable format {#humanReadableFormat}

The Secure Crypto Config can not only be used automatically but also provide the cryptography algorithms and parameters for humans.
The human readable format must be derived from the JSON files both to protect from copy-paste-errors and to validate the cryptographic signatures.
Yet, the human readable format or publication page itself must not be cryptographically protected.
There should be one accessible resource, e.g. a webpage, where the source format (JSON files) are automatically used for displaying them in appropriate ways (e.g. tables with various sorting and searching options).

## Official Secure Crypto Config Repository

### Location of Secure Crypto Config Repository

The needed Secure Crypto Config files should be published at an official GitHub repository.
There, all current versions will be provided during the interval of the Publication phase (see {{process}}).
Additionally, all previously published files are still stored at this location even if new versions are published. 

### Format of Secure Crypto Config Repository {#RepoFormat}

~~~~
scc-repo
- configs
  - 2020
    - 00
      - SCC_2020-00_LEVEL_1.json
      - SCC_2020-00_LEVEL_1.signature1
      - SCC_2020-00_LEVEL_1.signature2
      - SCC_2020-00_LEVEL_2.json
      - SCC_2020-00_LEVEL_2.signature1
      - SCC_2020-00_LEVEL_2.signature2
      - SCC_2020-00_LEVEL_3.json
      - SCC_2020-00_LEVEL_3.signature1
      - SCC_2020-00_LEVEL_3.signature2
      - SCC_2020-00_LEVEL_4.json
      - SCC_2020-00_LEVEL_4.signature1
      - SCC_2020-00_LEVEL_4.signature2
      - SCC_2020-00_LEVEL_5.json
      - SCC_2020-00_LEVEL_5.signature1
      - SCC_2020-00_LEVEL_5.signature2
    - 01
    - 02
  - 2021
  - 2022
  - 2023
  - 2024
~~~~
{: #scc_repo_example title="Example for Secure Crypto Config Repository content"}

~~~~
scc-repo
- configs
  - a
      - b
      - 0c1
      - ReallySecure
  - 0x1111
    - SCC_2020-00_LEVEL_1.json
    - SCC_2020-00_LEVEL_1.signature1
    - SCC_2020-00_LEVEL_1.signature2
    - SCC_2020-00_LEVEL_2.json
    - SCC_2020-00_LEVEL_2.signature1
    - SCC_2020-00_LEVEL_2.signature2
    - SCC_2020-00_LEVEL_3.json
    - SCC_2020-00_LEVEL_3.signature1
    - SCC_2020-00_LEVEL_3.signature2
    - SCC_2020-00_LEVEL_4.json
    - SCC_2020-00_LEVEL_4.signature1
    - SCC_2020-00_LEVEL_4.signature2
    - SCC_2020-00_LEVEL_5.json
    - SCC_2020-00_LEVEL_5.signature1
    - SCC_2020-00_LEVEL_5.signature2
  - asdf
  - afd
  - af
~~~~
{: #scc_repo_example2 title="Example for Secure Crypto Config Repository content with custom naming scheme"}

The Secure Crypto Config configuration files are expected to be in any folder hierarchy below the folder `configs`-folder.
Each JSON file should be accompanied by corresponding signature files that have the same filename without extension as the JSON file, suffixed by `-signatureX` where `X` is a counter starting at 1.

### Integrity/Signing process

- [ ] TODO what kind of signing process should be used? 
  - [ ] GPG?
  - [ ] openssl?
  - [ ] Git GPG signed commits?
  - [ ] Use an SCC recommended signing algorithm/format
- [ ] Can two signatures be put in one signature file? Should they be put in the same file?
- [ ] Public Key distribution?! (In GitHub repo?)

Each JSON file should be accompanied by at least two signatures.
Both signatures are stored in different files on the same level as their corresponding Secure Crypto Config file to reduce the parsing effort.
The signatures should be generated by entities defined by the Secure Crypto Config Working Group. 
They are responsible to publish and renew the used public keys.
For signing of the corresponding Secure Crypto Config JSON files *openssl* could be used. 
The public keys needed for validation are published in the official repository of the Secure Crypto Config.

# Secure Crypto Config Interface {#Interface}

This section describes the programming interface that provides the Secure Crypto Config.
The Secure Crypto Config Interface is generic and describes the API that should be used by each programming language.

## Semantic Versioning

The implementation of the Secure Crypto Config Interface MUST follow [Semantic Versioning](https://semver.org/), which specifies a version format of **X.Y.Z** (Major.Minor.Patch) and semantics when to increase which version part. 
It would be beneficial if the release of a new Interface version gets synchronized with the publication of a new Secure Crypto Config. 
It should be possible to support the newly defined parameters of Secure Crypto Config in the interface as soon as possible.

## Deployment of (custom) Secure Crypto Config with Interface

There are two different possibilities to work with the Secure Crypto Config: 
- The preferred option is to use the Secure Crypto Configs that will be delivered within the Interface. 
In each new Interface version the current Secure Crypto Configs will be added such that always the latest Secure Crypto Configs at the time of the Interface release will be supported. Previous Secure Crypto Configs will remain inside the Interface such that also older ones can still be used. 
- Another option is to define a specific path to your own/derived versions of the Secure Crypto Configs with the same structure of the files as described in {{dataStructures}} but with other values than in the official ones.

The Interface will process the Secure Crypto Configs as follows:

1. Check if the path to the Secure Crypto Configs is a valid one.

2. Check if the `configs` folder exists.

3. For each folder following `configs` in the hierarchy look inside that folder and check the existence of JSON files that need to be considered.
  This check will happen recursively for all folders inside the hierarchy.

4. For every JSON file found, look if there exists a signature. If one is given, check if the signature is valid for the corresponding file.

5. Every file with a valid signature will be parsed and further processed.

The parsing of each valid JSON file must be done as follows:

1. Read out all information of all JSON files that need to be considered. The information of each file is stored in a corresponding object. With this procedure all JSON files need to be read only once which will contribute to the performance.

2. Parsing of security level: Check if it is a positive integer. All files not containing an (positive) integer number as security level value will be discarded.

3. Parsing of algorithm identifiers: Only the algorithm identifiers that are supported by the Interface will be considered and stored inside the corresponding object. The supported algorithms are specified inside the interface (e.g. with an enmum).

4. Parsing of the version of all files:
  All files with values in the wrong format (see {{version}}) will be excluded from further processing. 
  Find the latest (according to version) Secure Crypto Config file with the highest appearing security level (determined in previous step). 
  The path to this file will be used as default path used for each cryptographic use case if nothing else is specified by the user. 
  If two or more files with identical levels and version number are found, only the first one will be used, others are discarded.

5. The unique algorithm identifiers for the execution of a specific cryptographic use case will be fetched from the corresponding object (representing the JSON file determined beforehand) at the time the users invoke a method for a specific cryptographic use case. The Interface will also provide a possibility to choose a specific algorithm (out of the supported ones) for executing the desired use case. In this case the specified algorithm is used.
  The identifiers will be compared with the supported ones in order of their occurrence inside the file. 
  If one matching identifier is found it will be used for execution. 
  If it is not a matching one the value will be skipped and the next one will be compared. 
  If none of the algorithms inside the selected Secure Crypto Config can be found an error will occur.


### Delivery of Secure Crypto Config with Interface

Each Secure Crypto Config Interface must be published in such a way that it uses (a copy of) the recent Secure Crypto Config repository.

The Secure Crypto Config will be stored inside the subfolder `scc-configs` which should be located in the Interface `src`-folder if existent.
The structure of the `scc-configs` folder will be the same as in the described hierarchy of the GitHub repository.
In any new version of the Interface the latest published Secure Crypto Config and its signatures must be used. 

If new Secure Crypto Configs will be published for which no published version of the Interface is available, the custom repository approach can be used as described in the following.

### Using a custom Secure Crypto Config Repository

It is also possible to use a different path to the Secure Crypto Configs. 
As also derived versions of the Secure Crypto Config for specific needs should be supported it will also be feasible to define a path to own or derived files that differentiate from the default `src/scc-configs/configs` folder. 
In this case, a method for setting and using a specific path must be provided by the Interface.

### Integrity Check

- [ ] TODO which public keys should be used? (See above Integrity/Signing process Public Key distribution?!)

The check for valid signature of the Secure Crypto Configs is always made before every actual usage of the Interface functionalities.
In this way, it is possible to guarantee that the entity using the Interface only works with valid Secure Crypto Configs and circumvents the risk of forged file contents. 
The public key needed for validity can be found in the official GitHub repository.
If own derived Secure Crypto Configs are created than it can be possible that no validation process is needed for these files.

## Application Programming Interface (API)

### Methods and Parameters

Intended methods and parameters included in the Java interface are described in {{scc_java_api_example}}.

#### Supported Algorithm Parameter Types

- [ ] TODO What is with parameters that have to be chosen during runtime? (e.g. the length of the nonce can be specified but not its content?) Maybe refer to how the [PHC String Format](https://GitHub.com/P-H-C/phc-string-format/blob/master/phc-sf-spec.md) describes how parameters must be defined and only allow constants and csprng generated content?

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

It should be possible to have user specified parameters such as the key/nonce length explicitly given by the user, but also a performance mode that evaluates for each configuration and gives back a prioritized list for each configuration.
In this way, it is possible to select parameters depending on systems properties.
Such a parameter choice would be beneficial e.g. in the case of [Argon2](https://tools.ietf.org/html/draft-irtf-cfrg-argon2-03) in which one parameter for the memory usage must be given.
This choice should be chosen based on the corresponding system.
That kind of parameter selection can be seen e.g. in  [Libpasta Tuning](https://libpasta.github.io/advanced/tuning/), which returns a secure parameter set depending on executed evaluations.

### Output of readable Secure Crypto Config

A Secure Crypto Config Interface must offer the following additional methods regarding the configuration
- A method that returns a human readable version of the currently used Secure Crypto Config 
- A method that returns the currently used cryptography algorithm and parameters for a given use case
- A method that validates the content of a Secure Crypto Config JSON file and one or more signatures

## TODOs

- The SCC could be provided on a suitable platform (?) and is accessible over the network (adversaries? e.g. http connection)
  - [ ] e.g. should there be constants like "SCC_TOP_SECRET_LATEST" and "SCC_TOP_SECRET_LATEST". 
  - [ ] And like "SCC_TOP_SECRET_LATEST.AES" which points always to the latest Secure Crypto Config definition for AES parameters.
- [ ] TODO how should cryptography implementations, that implement/support SCC, generate the parameters?
- [x] What kind of parameters can be chosen based on the Secure Crypto Config? => E.g. Should be all except the plaintext and the key for encryption algorithms. Also, many parameters can be generated based on cryptographically secure random numbers.
- [x] TODO The Secure Crypto Config Interface should include a performance evaluation mode which evaluates the performance of each configuration and returns a prioritized list for each configuration. E.g. cf. [Libpasta Tuning](https://libpasta.github.io/advanced/tuning/)

# Cryptography Library Implementation Specification

Cryptography libraries should provide the above mentioned Secure Crypto Config Interface.
Until a common cryptography library provides the Secure Crypto Config Interface itself, there should be wrapper implementations that provide the Secure Crypto Config Interface and make use of the programming languages' standard cryptography library.

# Cryptography Algorithm Standards Recommendation

When new cryptography algorithm and/or parameter/mode/etc standards are created, they should contain a section mentioning the creating of the proposed secure parameter sets in the above mentioned IANA registries.
This ensures that new cryptography algorithms and parameter sets are available faster for the Secure Crypto Config Interface implementations to use.

# Security Considerations

- [x] TODO are some of the listed common issues relevant?: [TypicalSECAreaIssues](https://trac.ietf.org/trac/sec/wiki/TypicalSECAreaIssues)
- [x] TODO check if security considerations of TLS 1.2 are relevant, especially appendix [D, E and F](https://tools.ietf.org/html/rfc5246#appendix-D)
- [ ] TODO Are these appropriate security considerations?

## Consensus Finding

- Only trustworthy and cryptographic specialized entities should participate in the publication process of the Secure Crypto Config.
  Otherwise a Secure Crypto Config with a weak and insecure parameter set could be provided.

## Publication Format

- The operators of the Secure Crypto Config must ensure that potential unauthorized parties are not able to manipulate the parameters of the published Secure Crypto Config. 
  Countermeasures to this are in place by utilizing gits gpg signatures and integrity as well as signatures for the published Secure Crypto Config files as well.

## Cryptography library implementation

- Integrity must be ensured if potential users want to fetch the provided Secure Crypto Config from the corresponding platform over the network e.g. by using a signatures.
- Users should only trust Secure Crypto Config issued from the original publisher with the associated signature. Users are responsible to verify the provided signatures.

## General Security Considerations

### Special Use Cases and (Non-)Security Experts

The Secure Crypto Config does not apply to all use cases for cryptography and usage of cryptography primitives.
It is meant to provide secure defaults for the most common use cases and non-expert programmers.
Additionally, non-experts may still implement vulnerable code by using the Secure Crypto Config. 
Yet, it should reduce the vulnerabilities from making the wrong choices about parameters for cryptography primitives.

## Security of Cryptography primitives and implementations

- The Secure Crypto Config assumes that both the proposed algorithms and the implementations (cryptography libraries) for the cryptography primitives are secure as long as they are used with the correct parameters, states and orders of function calls.

### Security Guarantees

The Secure Crypto Config makes the best effort to be as up-to-date with recent discoveries, research and developments in cryptography algorithms as possible.
Following this, it strives to publish cryptography algorithms and corresponding parameter choices for common use cases.

Yet, the Secure Crypto Config and the involved parties working on and publishing it do not guarantee security for the proposed parameter configurations or any entity making use of it.
E.g. a new algorithm that can do brute-force attacks exponentially faster could be existing or published right after the publication of the most recent Secure Crypto Config was published itself.

### Threat Model / Adversaries {#threatModel}

There are different possibilities in which a potential adversary could intervene during the creation as well as after the publication of the Secure Crypto Config. These attack scenarios must be considered and prevented.

- **Process:** During the creation process, it is necessary for selected institutions to agree on a secure parameter set.
It could be possible that one party wants to influence this process in a bad way.
As a result, it could be agreed on weaker parameter sets than originally intended.

- **Publication:** After the publication of the Secure Crypto Config a potential attacker could gain access to the provided files on the corresponding platform and change the content to an insecure parameter set.

- **Content:** Depending on the distribution method of the Secure Crypto Config, it is also possible that an attacker could change the content of the Secure Crypto Config as man-in-the-middle.
Especially if an http connection is used to obtain the Secure Crypto Config, this will be a serious problem.


# IANA Considerations

- [] TODO Are there IANA Considerations?
- [] TODO May add reference to own registry

The data structure (see {{dataStructures}}) defined in this document uses the JSON format as defined in {{!RFC8259}}.

--- back

# Examples {#example}

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

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
  RFC8152: COSE
  RFC4949: SecurityGlossary
  RFC8259: JSON

informative:
  RFC5652: CMS
  RFC5698: DSSC



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
  - [ ] Mention Main Goals: (1) Enable cryptography libraries and APIs to offer secure defaults with inherent future-proofnes; (2) Prevent non-expert programmers from misusing cryptography APIs; (3) Allow standardized definition of secure parameters for cryptography algorithms; (4) Standardized across all implementations; (5) Prevent outdated example code and documentation.
  - [ ] Mention: Yearly published secure configuration recommendations that can be used per default from cryptography libraries. This prevents aging/maturing libraries from offering insecure default implementations. 
  - [ ] Mention target group ((1) developers who are not experts but still need to implement cryptography functionality. (2) Cryptography library developers that should integrate SCC to provide secure defaults. (3) standardization institutions (like BSI or NIST) who can use the publication format for their own set of cryptography recommendations)
  - [ ] TODO mention and describe [TLS Cipher suites](https://www.iana.org/assignments/tls-parameters/tls-parameters.xhtml#tls-parameters-4)
  - [ ] TODO mention why the Recommended Column is not enough
- [ ] TODO Mention/Refer to how many input parameters (5 to 8 in addition to the password itself) [The memory-hard Argon2 password hash and proof-of-work function](https://tools.ietf.org/html/draft-irtf-cfrg-argon2-10#section-3.1) has.
- [ ] TODO integrate "Guidelines for Cryptographic Algorithm Agility and Selecting Mandatory-to-Implement Algorithms" BCP 201 {{?RFC7696}}.
- [ ] TODO integrate "Algorithm Agility Procedure for the Resource Public Key Infrastructure (RPKI)" BCP 182 {{?RFC6916}}.
- [ ] TODO Mention/Refer to "CBOR Object Signing and Encryption (COSE)" {{-COSE}}
  - see defined Algorithms/Parameters etc. at the [IANA registry for COSE](https://www.iana.org/assignments/cose/cose.xhtml)
  - e.g. There is an entry for the Advanced Encryption Standard with key size, mode and tag length: A256GCM;3;AES-GCM mode w/ 256-bit key, 128-bit tag;
- [ ] TODO Mention/Refer to "The JavaScript Object Notation (JSON) Data Interchange Format" {{-JSON}}
- [ ] TODO Mention/Refer to "Data Structure for the Security Suitability of Cryptographic Algorithms (DSSC)" {{-DSSC}}

## Motivation
The correct choice of secure parameters when implementing cryptographic primitives or algorithms is not always easy to ensure. 
However, the security of the primitives to be used depends mainly on the choice of these parameters (e.g. the correct key length). 
For this reason, it is necessary to assure that the choice of parameters is made correctly. 
Unfortunately, this is not always easy. 
This is a big issue especially for software developers who are not primarily concerned with cryptography, but nevertheless have to generate cryptographic secure code. 
In order to choose the right parameters, they often make use of documentations and tutorials from the Internet. 
But not every source provides a safe choice of parameters or even shows an outdated selection of parameters. 
Therefore, there is the danger of causing insecurity during the implementation of cryptographic code.

Another point that can lead to the wrong insecure parameter choice are cryptographic libraries. 
Normally they should provide secure default parameters, but these are not always secure or no longer up to the current standard of security. 
To alleviate these problems, it is a good idea to generate and provide a standardized definition of secure parameters for cryptographic primitives. 
This could be used to ensure correct parameter usage and thus provide more security, since standardization across all implementations could be guaranteed. 
A data structure to represent this standardized definition in JSON {{-JSON} is given in the following (/ref chapter of datastructure). 
This standardized definition of a secure parameter set is referred to as Secure Crypto Config (SCC) in the following.
(?) When creating such a data structure it is helpful to take a look at the Guidelines for Cryptographic Algorithm Agility and Selecting Mandatory-to-Implement Algorithms" BCP 201 {{?RFC7696}} and the "Algorithm Agility Procedure for the Resource Public Key Infrastructure (RPKI)" BCP 182 {{?RFC6916}} as a reference to ensure cryptographic algorithm agility.

There is the Data Structure for the Security Suitability of Cryptographic Algorithms (DSSC) {{-DSSC}} which stores some of the parameters, that are also required for the SCC, in an XML-format. 
DSSC was originally created for other purposes than the SCC and therefore also contains components that are not required for the SCC. 
Nevertheless, it can be regarded as a reference, as it contains a lot of information that is also necessary for the SCC.

The data structure created for SCC could also be useful as a template for standardisation institutions such as NIST or BSI to provide their parameter recommendations in a structured way. 
This would serve the purpose of offering a version that is also easy to read by machines, in contrast to their mostly textual and only human readable recommendations on their websites.

If the SCC is provided with a secure parameter set, it can also be used to update cryptographic libraries. 
With the help of this obsolete or insecure default parameters could be bypassed and the secure predefined parameters of the SCC could be used instead. 
In practice, it is often not possible to simply change the interfaces of existing cryptographic libraries. 
Therefore, an additional abstraction layer is needed here, which should provide an easy to use interface for the programmers to actually use the library functions with the secure parameter choice. 
An example of such an additional abstraction layer can be seen in {{scc_java_api_example}}.

(?) One occuring problem is the fact that the parameters defined in the SCC may change over time. 
Therefore, it is necessary to check the SCC with its parameters regularly and adjust them if necessary. 
A regular adjustment process once a year, for example, could guarantee this. 
However, adjusting the parameters is not always easy. 
In contrast we can see that in the use of Transport Layer Security (TLS), parameter changes are comparatively less complicated. 
For the duration of a connection between client and server, it is possible to choose from different algorithms in advance and thus to choose an algorithm that is secure from a cryptographic point of view. 
A selection of possible algorithms for TLS can be found for example at the [IANA Registry for TLS Cipher suites](https://www.iana.org/assignments/tls-parameters/tls-parameters.xhtml#tls-parameters-4). 
This is not that easy if for example file encryption should be performed, because in this case one has to keep the same parameters for both encryption and decryption. 
Otherwise the cipher obtained by encryption cannot be decrypted correctly back again. 
This could be solved by developing different backwards compatible layers. 
However, it would be more advantageous to store the parameters used for the encryption in addition to the actual encrypted data, which would considerably reduce the maintenance costs in contrast to the solution mentioned beforehand. 
This is also necessary to prevent applications from becoming incompatible with updated SCC over time. 
To store this necessary informations regarding the parameters there are already different standards like CMS {{-CMS} or COSE {{-COSE}. 
The parameters and algorithms defined for COSE can be found at the [IANA registry for COSE](https://www.iana.org/assignments/cose/cose.xhtml).


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
  "threat",
  "trust",
  "vulnerability",
in this document are to be interpreted as described in {{-SecurityGlossary}}.

The term "hash" is used as a synonym for "cryptographic hash".


## Use Cases

### Misuse Cases

*"A Misuse Case [...] highlights something that should not happen (i.e. a Negative Scenario)"* [Misuse Case](https://en.wikipedia.org/w/index.php?title=Misuse_case&oldid=941745374)

# Requirements and Assumptions

## Requirements

- Information classification: Support different classifications (e.g. TOP SECRET, SECRET, CONFIDENTIAL)
  - Information classification is not always needed. E.g. many applications require one encryption / hashing algorithm and use only that one for all information encrypted/hashed. But for future proofing it would be good to be able to process information based on its classification.
- Longevity: The time some information must be protected can differ from milliseconds to decades (related to Document classification)
  - Different SCC's based on longevity is not always needed. E.g. many applications require one encryption / hashing algorithm and use only that one for all information encrypted/hashed. But for future proofing it would be good to be able to process information based on its longevity.
- Contrained Devices: Computing power is not always available for both the most advanced cryptography algorithms and their parameters.
- *Threat Model* / adversary powers: What kind of attacker should the secure crypto config protect against? (Almighty? Gouvernment? Company?). Presumably  different group of attackers.
- Which primitives should be covered?
- Which algorithms (per primitive) should be covered?
- Easy to find what is a secure parameter set for a given requirement (e.g. one table, two-dimensional)
- Must be easy to verify which Secure Crypto Config is used / was used (e.g. in Continuous Integration platforms)
- Must be easy to verify the authenticity of the Secure Crypto Config (e.g. is this really what the CFRG has published)
- Easily accessible for library implementation
- Really easy mapping for various programming languages, without complicated/many additional logic/parsing
- Easy to integrate by cryptography libraries
- Experts should still be able to use/access the unaltered output of cryptographic primitives
- Regular updates (yearly?)
- Must be easy to extend/alter by other organizations (e.g. maybe the BSI wants to publish its own secure crypto config that differs from the standardized one. Maybe a hiearchical approach with inheritance from the base SCC?)
- Must be easy to update by the committee
- Interoperability with other standards/formats (e.g. {{-COSE}})
- Recommendation what should be the default secure crypto config for a cryptography library (e.g. should it be the one for TOP Secret or *only* for Secret?)
- Recommendation what should a cryptography library do if it can not support the parameters specified in the latest Secure Crypto Config. (E.g. key size for RSA would be n*2 and the library supports only n)
- Recommendation how should a cryptography library integrate the Secure Crypto Config so that it should be up to date as soon as possible after a new SCC has been published?

## Assumptions

- What does secure mean?
- Level of security? (key length?)
- [ ] Describe that it is assumed that experts can still use the non-default or customizable variants of cryptography algorithms

# Secure Crypto Config

## Threat Model / Adversaries

- [ ] TODO describe adversaries / group of adversaries and their powers
- [ ] TODO describe other general threats for the Secure Crypto Config (maybe put that into the section Security Considerations):
  - [ ] Process
  - [ ] Publication
  - [ ] Content

## Information classification

- [ ] TODO describe Why is document classification needed (refer to Requirements if possible).
- [ ] TODO Refer to [Classified Information](https://en.wikipedia.org/wiki/Classified_information) and [Traffic Light Protocol](https://www.first.org/tlp/docs/tlp-v1.pdf) or other material to find a globally valid definition for classifying information.

## Longevity



# Process Consensus Finding and Publication

## Consensus Finding



## Publication

- [ ] TODO Describe used versioning concept for SCC. 
- [ ] TODO Refer to [Semantic Versioning](https://semver.org/)

# Data Structures

|              | More than 10 years   | More than 1 year               | More than 1 week | Less than one week |
|--------------|----------------------|--------------------------------|------------------|--------------------|
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

- [ ] TODO Security
- [ ] TODO are some of the listed common issues relevant?: [TypicalSECAreaIssues](https://trac.ietf.org/trac/sec/wiki/TypicalSECAreaIssues)


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

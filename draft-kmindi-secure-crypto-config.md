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

informative:



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
- [ ] TODO Mention/Refer to "The JavaScript Object Notation (JSON) Data Interchange Format" {{!RFC8259}}
- [ ] TODO Mention/Refer to "Data Structure for the Security Suitability of Cryptographic Algorithms (DSSC)" {{?RFC5698}}

## Motivation

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

# Acknowledgments
{:numbered="false"}

- [ ] TODO acknowledge.

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
    email: lisa.teis@example.com

normative:
  RFC2119:

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

- TODO https://www.ietf.org/standards/ids/guidelines/
- TODO https://www.ietf.org/media/documents/92kramdown-Bormann.pdf

# Introduction

- [ ] TODO Introduction
- [ ] TODO integrate "Algorithm Agility Procedure for the Resource Public Key Infrastructure (RPKI)" BCP 182 {{RFC6916}}.

> Algorithm Suite A:  The "current" algorithm suite used for hashing
>                and signing; used in examples in this document.
>
>   Algorithm Suite B:  The "next" algorithm suite used for hashing and
>               signing; used in examples in this document.

- [ ] TODO integrate "Guidelines for Cryptographic Algorithm Agility and Selecting Mandatory-to-Implement Algorithms" BCP 201 {{RFC7696}}.

## Motivation

## Terminology

### Conventions and Definitions

The key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL NOT", "SHOULD",
"SHOULD NOT", "RECOMMENDED", "NOT RECOMMENDED", "MAY", and "OPTIONAL" in this
document are to be interpreted as described in BCP 14 {{RFC2119}} {{!RFC8174}}
when, and only when, they appear in all capitals, as shown here.

## Use Cases

### Misuse Cases

*"A Misuse Case [...] highlights something that should not happen (i.e. a Negative Scenario)" https://en.wikipedia.org/w/index.php?title=Misuse_case&oldid=941745374*

# Requirements and Assumptions

## Requirements

- Document classification: Support different classifications (e.g. TOP SECRET, SECRET, CONFIDENTIAL)
- Longevity: The time some information must be protected can differ from milliseconds to decades (related to Document classification)
- Contrained Devices: Computing power is not always available for both the most advanced cryptography algorithms and their parameters.
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

## Assumptions

- What does secure mean?
- Level of security? (key length?)

# Data Structures

*TODO is JSON a appropriate format?*

# Process Consensus Finding and Publication

# Security Considerations

*TODO Security*

*TODO are some of the listed common issues relevant?: https://trac.ietf.org/trac/sec/wiki/TypicalSECAreaIssues*


# IANA Considerations

This document has no IANA actions.

# References


--- back

# Example JSON Secure Crypto Config

# Example Java Interface using Secure Crypto Config

# Acknowledgments
{:numbered="false"}

TODO acknowledge.

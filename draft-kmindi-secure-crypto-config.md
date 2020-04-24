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

normative:
  RFC2119:

informative:



--- abstract

Cryptography providers, libraries and APIs usually define defaults for the offered cryptography primitives.
These defaults have to be kept to be backwards compatible with all users of the API that used the defaults.
Yet, these default choices become insecure at some later point. 
E.g. a key size of 128 bit may not be sufficient anymore.
To keep these defaults up-to-date this RFC describes three things: 
(1) A process that is repeated every year, where the
CRFG publishes a new set of default configurations for standardized cryptography primitives, 
(2) a format based on COSE that specifies the default secure configuration of this (and previous) year(s) and 
(3) a format to derive the parameters from output of cryptography primitives, otherwise future changes of the default configuration would change existing applications behaviour.

--- middle

# Introduction

TODO Introduction

## Motivation

## Terminology

### Conventions and Definitions

The key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL NOT", "SHOULD",
"SHOULD NOT", "RECOMMENDED", "NOT RECOMMENDED", "MAY", and "OPTIONAL" in this
document are to be interpreted as described in BCP 14 {{RFC2119}} {{!RFC8174}}
when, and only when, they appear in all capitals, as shown here.

## Use Cases

# Requirements and Assumptions

## Requirements

## Assumptions

# Data Structures

TODO is JSON a appropriate format?

# Process Consensus Finding and Publication

# Security Considerations

TODO Security

TODO are some of the listed common issues relevant?: https://trac.ietf.org/trac/sec/wiki/TypicalSECAreaIssues


# IANA Considerations

This document has no IANA actions.

# References


--- back

# Acknowledgments
{:numbered="false"}

TODO acknowledge.

# Appendix?

## Example JSON Secure Crypto Config

## Example Java Interface using Secure Crypto Config


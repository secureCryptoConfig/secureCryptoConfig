
## information classification
- [ ] TODO describe Why is document classification needed (refer to Requirements if possible).
- [ ] TODO Refer to [Classified Information](https://en.wikipedia.org/wiki/Classified_information) and [Traffic Light Protocol](https://www.first.org/tlp/docs/tlp-v1.pdf) or other material to find a globally valid definition for classifying information.
- [ ] TODO Support different classifications (e.g. TOP SECRET, SECRET, CONFIDENTIAL)
- [ ] TODO Integrate: Information classification is not always needed. E.g. many applications require one encryption / hashing algorithm and use only that one for all information encrypted/hashed. But for future proofing it would be good to be able to process information based on its classification.
- [ ] TODO Definitions here? Better formatting as bulleted points?
- [ ] TODO Up to now: Topsecret, secret, Confidential, unclassified - Confidential? Or is secret enough? Where exactly is the difference in encryption techniques
- [ ] TODO Unclassified at all? No protective measures -> no param set necessary

- **Secret**:
There exists sensitive information whose public availability would lead to large damage.
Special protection measures must be taken for this information since there is a high risk of misuse and damage to the organization privacy or reputation if the information is not published correctly.
- **Confidential**:
This classification contains information which is relevant and worthy of protection, but the risk of misuse is low even in the case of unintentional publication.

- [ ] TODO may remove later


The different supported classifications can be seen in the following.
There exists information whose public availability would lead to very large damage and is therefore classified as **TOPSECRET**.
Special protection measures must be taken for this information since there is a very high risk of misuse and damage to the organization privacy or reputation if the information is not published correctly. 
The **SECRET** classification would be a gradation of the former classification.
Here sensitive information in included that should not be disclosed to the public unintentionally.
In this case, making the information public would still cause damage and a high risk of misuse exists, but not to the same extent as in TOPSECRET.
In addition to the classifications of sensitive information, there are also classifications that require less protective measures. **Confidential** describes information which is relevant and worthy of protection, but the risk of misuse is low even in the case of unintentional publication. 
The lowest classification is **UNCLASSIFIED**.
The information of this classification is not of high relevance and does not need protection, since the risk of misuse is minimal. 
It is not necessary to consider all theses different classifications in all applications.
In some cases it may be sufficient to use the same algorithms for all types of information, but it would be good for future proofing to be able to process information according to its classification.


## longevity

The time information must be protected can be as low as milliseconds but also be more than a decade.
Different SCC's based on longevity is not always needed. 
E.g. many applications require one encryption / hashing algorithm and use only that one for all information encrypted/hashed. But for future proofing it would be good to be able to process information based on its longevity.

The duration of needed data secrecy can vary with different kinds of information and their respective applications.
While for some information it is enough to be encrypted for only a few seconds, for other information it is required to keep them secret for decades.
Therefore, it is necessary to consider the degree of longevity and provide different SCCs depending on it.
Such SCCs based on longevity are not needed for every application.
For many applications it can be sufficient to only have one specified algorithm which is used on all kinds of information.
But for future proofing it would be good to be able to process information based on its longevity.
The SCC only consideres **short** longevity which includes a duration of less than one day and **long** longevity requiring protection for more than one day.

- [ ] TODO logevity definition.
- [ ] TODO More classifications necessary?
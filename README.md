# asyncapi-parser
The purpose of this project is to provide a command line parser for asyncapi files.
The output of the operation is an extract of Solace Queues derived from Channels.
This extract can then be fed to a CI/CD process (Ansible in this case) for provisioning.

## Build

### Build Requirements
This project requires the [SolaceLabs/jasyncapi](https://github.com/SolaceLabs/jasyncapi) project reference.
As of today (October 25, 2022), this project must be built from the source on the **solace-updates** branch.
*It's a long story as to why this PR has not been merged... To summarize, I opened PR#6 to the maintainer for the changes in this branch.
However, the maintainer is a Russian from Moscow. He has since fled the country to avoid mobilization for the war in Ukraine.
I swear that I am not making this up.*

### Build the projects
* Clone the **jasyncapi** project and build from branch **solace-updates**.
* Build with ```mvn clean install```
* Reference to jasyncapi is included in the asyncapi-parser **pom.xml**:
```xml
	<dependency>
	    <groupId>com.asyncapi</groupId>
	    <artifactId>asyncapi-core</artifactId>
	    <version>1.0.1-EAP-1</version>
	</dependency>
```
* Build the *asyncapi-parser* project with ```mvn clean install```

## Execute
This **asyncapi-parser** project will create two jar files in the target folder:
A jar with dependencies and one without the dependencies. Usage is self-explanatory.

### Command line options:
- ```--asyncapi-in=[asyncapi source file path]``` - Fails if not provided.
- ```--output=[output file path]``` - Program will auto-generate an output file name in the working directory if not provided
- ```--target-server=[asyncapi server block ID] - Program will use first server in the ```servers``` block if not provided.
- ```--debug``` - Produces more output

### Examples

#### Example #1

**Execute:**
```bash
java -jar asyncapi-cicd-parser-0.1.1-jar-with-dependencies.jar --asyncapi-in=samples/acme-retail-customer-mobile.json --output=my-extract1.yaml --target-server=development
```
**Output file: my-extract1.yaml**
```yaml
---
title: acme-retail-customer-mobile
description: AsyncAPI for Customer Facing Mobile Application
apiVersion: 0.1.0
applicationDomainId: 9n3oj6yfxm3
applicationDomainName: acme-retail - Store Operations
applicationId: 28a372rrk0s
applicationName: Customer Facing Mobile Application
applicationVersionId: hxd36iqulyh
eventApiStateId: 1
eventApiStateName: DRAFT
environment: development
modelledEventMesh: acme-retail Store
modelledEventMeshId: 46h17klzg1n
eventPortalEnvironmentId: bvmn5likrk2
logicalBroker: logical_broker_b
queueDefinitions:
- queueName: acme-retail.mobileapp.retail-order.updates.receive
  queueSettings:
    accessType: exclusive
  topicSubscriptions:
  - acme-retail/store-ops/retail-order/online-fulfilled/v1/*/*/*
  - acme-retail/store-ops/retail-order/online-paid/v1/*/*/*
  - acme-retail/store-ops/retail-order/online-cancelled/v1/*/*/*
```

#### Example #2
**Execute:**
```bash
java -jar asyncapi-cicd-parser-0.1.1-jar-with-dependencies.jar --asyncapi-in=samples/acme-retail-customer-mobile.yaml
```

**Output file: cicd-output-c18d49c7-75c6-4abe-b59b-fcfef30e02e3.yaml**
```yaml
---
title: acme-retail-customer-mobile
description: AsyncAPI for Customer Facing Mobile Application
apiVersion: 0.1.0
applicationDomainId: 9n3oj6yfxm3
applicationDomainName: acme-retail - Store Operations
applicationId: 28a372rrk0s
applicationName: Customer Facing Mobile Application
applicationVersionId: hxd36iqulyh
eventApiStateId: 1
eventApiStateName: DRAFT
environment: integration
modelledEventMesh: acme-retail Store
modelledEventMeshId: nk29cfpn55c
eventPortalEnvironmentId: i6f2y4eviq3
logicalBroker: logical_broker_b
queueDefinitions:
- queueName: acme-retail.mobileapp.retail-order.updates.receive
  queueSettings:
    accessType: exclusive
  topicSubscriptions:
  - acme-retail/store-ops/retail-order/online-fulfilled/v1/*/*/*
  - acme-retail/store-ops/retail-order/online-paid/v1/*/*/*
  - acme-retail/store-ops/retail-order/online-cancelled/v1/*/*/*
```

Output file

## To-Do
1. Implement logging. Screen output is directed from System.out and System.err commands. This should be updated to use a standard logging mechanism.



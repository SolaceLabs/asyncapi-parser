

# To create queues + downstream operations, need:

- Pointer to Event Portal instance 
    - Used to map to token (stored)
- queueName
    - destinations-->destination
        - destinationType: queue
        - queue:
            name:
            topicSubscriptions: { List }
    - Verify that isQueue parameter is true? 
- subscriptionList
- appDomain
    - x-application-domain-id

```yaml
---
##### FROM INFO HEADER
title: myEventApi
description: myEventApi
apiVersion: 0.1.0
applicationDomainId: htkekrh37s0
applicationDomainName: myAppDomain
eventApiStateId: 1
eventApiStateName: DRAFT
##### FROM SERVERS
modeledEventMesh: NEED A POINTER THAT CAN BE RESOLVED WITH THE ASSOCIATED EP INSTANCE/CREDENTIALS
eventPortalInstanceId: NEED SOMETHING TO POINT TO CREDENTIALS ASSOCIATED WITH THIS API - how is this handled?
environment: CORRELATES TO AN INVENTORY                     -- USED TO LOOK UP MODELED EVENT MESH
brokerIdentifier: THIS IS MISSING, SHOULD BE LOGICAL BROKER -- GET FROM SERVERS BLOCK
msgVpn: THIS IS MISSING, CAN BE IN THE INVENTORY????        -- YES, AS IT SHOULD BE)
#####
queueDefinitions:
  - queueName: theQueueName
    queueSettings:
      accessType: exclusive
    topicSubscriptions:
      - topic/subscription/1
      - topic/subscription/two
      - topic/subscription/*/3
  - queueName: theOtherQueueName
    queueSettings:
      accessType: non-exclusive
    topicSubscriptions:
      - other/subscription/1
      - other/subscription/two
      - other/subscription/*/3
```

```json
{
  "x-event-api-state-name": "DRAFT",
  "description": "myEventApi",
  "x-application-domain-id": "htkekrh37s0",
  "title": "myEventApi",
  "x-application-domain-name": "myAppDomain",
  "version": "0.1.0",
  "x-event-api-state-id": "1"
}
```

Modeled event mesh
x-ep-modeled-event-mesh
State from AsyncAPI spec? (need state name or state-id)
x-event-api-state-id
x-event-api-state-name

channel name/id
get all solace bindings
destinationType
deliveryMode
queue.name
queue.accessType
queue.subscription (List)






```yaml
channels:
  'person/{personId}/{eventType}':
    publish:
      bindings:
        solace:
          bindingVersion: 0.2.0
          destinations:
            - destinationType: queue
              queue:
                name: CreatedHREvents
                topicSubscriptions:
                - person/*/created
            - destinationType: queue
              queue:
                name: UpdatedHREvents
                topicSubscriptions:
                - person/*/updated
```

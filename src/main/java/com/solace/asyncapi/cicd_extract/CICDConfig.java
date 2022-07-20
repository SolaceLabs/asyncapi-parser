package com.solace.asyncapi.cicd_extract;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckForNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CICDConfig {

    @CheckForNull
    @JsonProperty
    protected String        title;

    @CheckForNull
    @JsonProperty
    protected String        description;

    @CheckForNull
    @JsonProperty
    protected String        apiVersion;

    @CheckForNull
    @JsonProperty
    protected String        applicationDomainId;

    @CheckForNull
    @JsonProperty
    protected String        applicationDomainName;

    @CheckForNull
    @JsonProperty
    protected String        eventApiState;

    @CheckForNull
    @JsonProperty
    protected String        eventApiStateName;

    @CheckForNull
    @JsonProperty
    protected String        modeledEventMesh;

    /**
     * Removed - Not using for current project iteration
    @CheckForNull
    @JsonProperty
    protected String        eventPortalInstanceId;

    @CheckForNull
    @JsonProperty
    protected String        environment;
    */

    @CheckForNull
    @JsonProperty
    protected String        logicalBroker;

    @JsonProperty
    @Builder.Default
    protected List<QueueDefinition> queueDefinitions = new ArrayList<QueueDefinition>();

}

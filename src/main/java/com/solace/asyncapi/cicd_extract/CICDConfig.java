package com.solace.asyncapi.cicd_extract;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckForNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
    @JsonInclude(Include.NON_NULL)
    protected String        applicationDomainId;

    @CheckForNull
    @JsonProperty
    @JsonInclude(Include.NON_NULL)
    protected String        applicationDomainName;

    @CheckForNull
    @JsonProperty
    @JsonInclude(Include.NON_NULL)
    protected String        applicationId;

    @CheckForNull
    @JsonProperty
    @JsonInclude(Include.NON_NULL)
    protected String        applicationName;

    @CheckForNull
    @JsonProperty
    @JsonInclude(Include.NON_NULL)
    protected String        applicationVersionId;

    @CheckForNull
    @JsonProperty
    @JsonInclude(Include.NON_NULL)
    protected String        eventApiStateId;

    @CheckForNull
    @JsonProperty
    @JsonInclude(Include.NON_NULL)
    protected String        eventApiStateName;

    @CheckForNull
    @JsonProperty
    protected String        environment;

    @CheckForNull
    @JsonProperty
    protected String        modelledEventMesh;

    @CheckForNull
    @JsonProperty
    protected String        modelledEventMeshId;

    @CheckForNull
    @JsonProperty
    protected String        eventPortalEnvironmentId;

    @CheckForNull
    @JsonProperty
    protected String        logicalBroker;

    @JsonProperty
    @Builder.Default
    protected List<QueueDefinition> queueDefinitions = new ArrayList<QueueDefinition>();

}

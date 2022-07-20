package com.solace.asyncapi.cicd_extract;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckForNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QueueDefinition contains all properties of a queue
 * To be used with Ansible or other CI/CD tool
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueDefinition {
    
    @JsonProperty
    @CheckForNull
    protected String        queueName;

    @JsonProperty
    @Builder.Default
    protected QueueSettings queueSettings = new QueueSettings();

    @JsonProperty
    @Builder.Default
    protected List<String>  topicSubscriptions = new ArrayList<String>();
}

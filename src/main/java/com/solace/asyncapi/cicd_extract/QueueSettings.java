package com.solace.asyncapi.cicd_extract;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueSettings {

    @JsonAnySetter
    @Builder.Default
    protected Map<String, String> properties = new HashMap<String, String>();

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return properties;
    }
}

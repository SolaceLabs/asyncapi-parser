package com.solace.asyncapi.asyncapi_parser;

import org.junit.Before;
import org.junit.Test;

import com.asyncapi.v2.binding.solace.SolaceOperationBinding;
import com.asyncapi.v2.binding.solace.SolaceOperationBinding.SolaceDestinationType;
import com.asyncapi.v2.model.AsyncAPI;
import com.asyncapi.v2.model.channel.message.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import static org.junit.Assert.*;

import java.io.File;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private ObjectMapper mapper = new ObjectMapper( new YAMLFactory().enable(YAMLGenerator.Feature.MINIMIZE_QUOTES) );
    private static final String yamlFile = "src/test/resources/samples/acme-retail-customer-mobile.yaml";
    private static final String jsonFile = "src/test/resources/samples/acme-retail-customer-mobile.json";

    AsyncAPI yamlAsyncApi = null;
    AsyncAPI jsonAsyncApi = null;

    @Before
    public void readParseYaml()
    {
        try {
            yamlAsyncApi = mapper.readValue(new File(yamlFile), AsyncAPI.class);
        } catch (Exception exc) {
            fail("Failed to parse yaml input file: " + yamlFile);
        }
    }

    @Before
    public void readParseJson()
    {
        try {
            jsonAsyncApi = mapper.readValue(new File(jsonFile), AsyncAPI.class);
        } catch (Exception exc) {
            fail("Failed to parse json input file: " + jsonFile);
        }
    }
    /**
     * Rigourous Test :-)
     */
    @Test
    public void testYamlFindServerIntegration()
    {
        assertTrue( yamlAsyncApi.getServers().containsKey("integration") );
    }

    @Test
    public void testYamlCheckServerDescription() {
        assertTrue( yamlAsyncApi.getServers().get("development").getDescription().contentEquals("Development Environment") );
    }

    @Test
    public void testYamlCheckServerExtension() {
        assertTrue( yamlAsyncApi.getServers().get("production").getExtensionFields().get("x-ep-logical-broker").contentEquals("logical_broker_b"));
    }

    @Test
    public void testYamlCheckChannelSolaceBinding() {
        assertTrue( yamlAsyncApi.getChannels().get("acme-retail/store-ops/retail-order/{retail_order_verb}/v1/{store_location}/{customer_id}/{order_id}")
                                        .getPublish()
                                        .getBindings()
                                        .containsKey("solace"));
    }

    @Test
    public void testYamlQueueName() {
        Object op = yamlAsyncApi.getChannels().get("acme-retail/store-ops/retail-order/{retail_order_verb}/v1/{store_location}/{customer_id}/{order_id}")
                                        .getPublish()
                                        .getBindings()
                                        .get("solace");
        assertTrue( op.getClass() == SolaceOperationBinding.class);
        
        SolaceOperationBinding so = ( SolaceOperationBinding )op;
        assertTrue(so.getDestinations().get(0).getDestinationType() == SolaceDestinationType.QUEUE);
        assertTrue(so.getDestinations().get(0).getQueue().getTopicSubscriptions().size() > 0);
    }

    @Test
    public void testJsonFindServerProduction()
    {
        assertTrue( jsonAsyncApi.getServers().containsKey("production") );
    }

    @Test
    public void testJsonCheckServerProtocol() {
        assertTrue( jsonAsyncApi.getServers().get("production").getProtocol().contentEquals("solace") );
    }

    @Test
    public void testJsonCheckServerExtension() {
        assertTrue( jsonAsyncApi.getServers().get("production").getExtensionFields().get("x-ep-modelled-event-mesh-id").contentEquals("nb0b6yoojr2"));
    }

    @Test
    public void testJsonCheckMessageExtension() {
        Object msg = jsonAsyncApi.getComponents().getMessages().get("RegistrationEvent_0_1_0");
        assertTrue( msg.getClass() == Message.class );
        
        Message theMsg = ( Message )msg;
        assertTrue( theMsg.getExtensionFields().get("x-event-name").contentEquals("RegistrationEvent"));
    }

    @Test
    public void testJsonQueueName() {

        assertTrue( jsonAsyncApi.getInfo().getExtensionFields().get("x-application-name").contentEquals("Customer Facing Mobile Application") );
    }
}

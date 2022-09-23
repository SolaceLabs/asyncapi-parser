package com.solace.asyncapi.asyncapi_parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.asyncapi.v2.binding.solace.SolaceOperationBinding;
import com.asyncapi.v2.model.AsyncAPI;
import com.asyncapi.v2.model.channel.ChannelItem;
import com.asyncapi.v2.model.channel.operation.Operation;
import com.asyncapi.v2.model.info.Info;
import com.asyncapi.v2.model.server.Server;
import com.solace.asyncapi.cicd_extract.CICDConfig;
import com.solace.asyncapi.cicd_extract.QueueDefinition;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncAPIToCICDMapper {

    public static CICDConfig mapAsyncAPIToCICD(AsyncAPI asyncApi, String targetServer) 
            throws AsyncAPIToCICDException
    {
		Info apiInfo = asyncApi.getInfo();

		if (apiInfo == null) {
			final String msg = "AsyncAPI 'info' block not found in input";
			log.warn(msg);
			throw new AsyncAPIToCICDException(100, msg);
		}
		log.debug("++ Found asyncApi.info block");

		if (asyncApi.getServers() == null || asyncApi.getServers().size() < 1) {
			final String msg = "AsyncAPI 'servers' block not found in input";
			log.warn(msg);
            throw new AsyncAPIToCICDException(101, msg);
		}
		log.debug("++ Found asyncApi.servers block");

        Server serverRecord = null;
        String serverId = "";
        // Lookup [server] if targetServer is provided, else use first entry
        if ( targetServer != null && targetServer.length() > 0 ) {
            serverRecord = asyncApi.getServers().get(targetServer);
            serverId = targetServer;
            if (serverRecord == null) {
				String msg = String.format("Could not find server using targetServer=%s", targetServer);
				log.warn(msg);
                throw new AsyncAPIToCICDException(102, msg);
            }
        } else {
            Iterator<Map.Entry<String, Server>> serverIterator = asyncApi.getServers().entrySet().iterator();
            if ( serverIterator.hasNext() ) {
                Map.Entry<String, Server> serverEntry = serverIterator.next();
                serverRecord = serverEntry.getValue();
                serverId = serverEntry.getKey();
            } else {
				final String msg = "No server in servers block";
				log.warn(msg);
                throw new AsyncAPIToCICDException(103, msg);
            }
        }
        log.debug("++ Found server == [%s]\n", serverId);

        if ( asyncApi.getChannels() == null || asyncApi.getChannels().size() < 1 ) {
			String msg = "No channels found in input";
			throw new AsyncAPIToCICDException(104, msg);
		}
        log.debug("++ Found asyncApi.channels block");
		
        CICDConfig config = new CICDConfig();

		config.setTitle(apiInfo.getTitle());
		config.setDescription(apiInfo.getDescription());
		config.setApiVersion(apiInfo.getVersion());
		config.setApplicationDomainId(apiInfo.getExtensionFields().get("x-application-domain-id"));
		config.setApplicationDomainName(apiInfo.getExtensionFields().get("x-application-domain-name"));
		config.setApplicationId(apiInfo.getExtensionFields().get("x-application-id"));
		config.setApplicationName(apiInfo.getExtensionFields().get("x-application-name"));
		config.setApplicationVersionId(apiInfo.getExtensionFields().get("x-application-version-id"));
		config.setEventApiStateId(apiInfo.getExtensionFields().get("x-event-api-state-id"));
		config.setEventApiStateName(apiInfo.getExtensionFields().get("x-event-api-state-name"));
		config.setEnvironment( serverId );
		config.setModelledEventMesh(serverRecord.getExtensionFields().get("x-ep-modelled-event-mesh"));
		config.setModelledEventMeshId(serverRecord.getExtensionFields().get("x-ep-modelled-event-mesh-id"));
		config.setEventPortalEnvironmentId(serverRecord.getExtensionFields().get("x-ep-environment-id"));
		config.setLogicalBroker(serverRecord.getExtensionFields().get("x-ep-logical-broker"));

		ArrayList<QueueDefinition> queueDefinitions = new ArrayList<QueueDefinition>();

		for (Map.Entry<String, ChannelItem> channelItem : asyncApi.getChannels().entrySet() ) {

			mapQueueDestinations( queueDefinitions, channelItem.getValue().getPublish());

			mapQueueDestinations( queueDefinitions, channelItem.getValue().getSubscribe() );

		}

        if (queueDefinitions.size() == 0 ) {
            log.warn("No Solace Queue Definitions found in input AsyncAPI");
        }

		log.info("Successfull mapped AsyncApi into CICDExtract format");
		config.setQueueDefinitions(queueDefinitions);

        return config;
    }

	private static void mapQueueDestinations( List<QueueDefinition> queueDefinitions, Operation op ) {
		if ( op == null || op.getBindings() == null || op.getBindings().size() == 0 ) return;
		for (Map.Entry<String, ?> opsBindingEntry : op.getBindings().entrySet()) {
			if (opsBindingEntry.getValue().getClass() == SolaceOperationBinding.class) {
				SolaceOperationBinding solOpsBinding = ( SolaceOperationBinding )opsBindingEntry.getValue();
				if ( solOpsBinding.getDestinations() == null ) continue;
				for ( SolaceOperationBinding.SolaceDestination dest : solOpsBinding.getDestinations() ) {
					if (
						dest.getDestinationType() == SolaceOperationBinding.SolaceDestinationType.QUEUE 
						&& dest.getQueue() != null ) {
							QueueDefinition qd = new QueueDefinition();
							qd.setQueueName(dest.getQueue().getName());
							if ( dest.getQueue().getAccessType() != null ) {
								qd.getQueueSettings().getProperties().put("accessType", dest.getQueue().getAccessType().toString() );
							}
							if ( dest.getQueue().getTopicSubscriptions() != null ) {
								qd.setTopicSubscriptions(dest.getQueue().getTopicSubscriptions());
							}
							queueDefinitions.add(qd);
					}
				}
			}
		}
	}
}

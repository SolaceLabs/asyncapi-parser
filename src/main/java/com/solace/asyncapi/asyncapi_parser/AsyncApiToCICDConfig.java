package com.solace.asyncapi.asyncapi_parser;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.asyncapi.v2.model.AsyncAPI;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.solace.asyncapi.cicd_extract.CICDConfig;

/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsyncApiToCICDConfig
{
	static final String		ARG_ASYNCAPI_IN = "--asyncapi-in=";
	static final String		ARG_OUT     	= "--output=";
	static final String		ARG_SERVER		= "--target-server=";
	static final String		ARG_DEBUG		= "--debug";
    public static void main( String[] args )
    {
		String inputFile = "";
		String outputFile = "";
		String targetServer = "";
		boolean debug = false;

    	for (String arg : args) {
			if (arg.startsWith(ARG_ASYNCAPI_IN) && arg.length() > ARG_ASYNCAPI_IN.length()) {
				inputFile = arg.replace(ARG_ASYNCAPI_IN, "");
			}
			if (arg.startsWith(ARG_OUT) && arg.length() > ARG_OUT.length()) {
				outputFile = arg.replace(ARG_OUT, "");
			}
			if (arg.startsWith(ARG_SERVER) && arg.length() > ARG_SERVER.length()) {
				targetServer = arg.replace(ARG_SERVER, "");
			}
			if (arg.contentEquals(ARG_DEBUG)) {
				debug = true;
			}
		}

		if (inputFile == null || inputFile.contentEquals("")) {
			System.err.println("\nERROR - Input File is required");
			System.out.println("USAGE:");
			System.out.format("\tcicd_extract %sINPUTFILE [ %sOUTPUTFILE ] [ %sTARGET_SERVER ] [ %s ]\n",
									ARG_ASYNCAPI_IN,
									ARG_OUT,
									ARG_SERVER,
									ARG_DEBUG);
			System.out.println();
			System.out.println("\tIf OUTPUTFILE is not specified, a filename will be generated and output to the working directory");
			System.out.println("\tIf TARGET_SERVER is not specified, the first entry in the [servers] block will be used");
			System.out.println("\t--debug will cause verbose output to stdout");
			System.exit(-1);
		}

		if (outputFile == null || outputFile.contentEquals("")) {
			outputFile = "cicd-output-" + UUID.randomUUID().toString() + ".yaml";
		}

		if ( debug && ( targetServer == null || targetServer.contentEquals("") ) ) {
			System.out.format("%s[SERVER] not specified, will use first [server] in AsyncAPI file\n",
									ARG_SERVER);
		}

		// Show options if debug
		if (debug) {
			System.out.println();
			System.out.println("DEBUG option is ON");
			System.out.println("Input File:  " + inputFile);
			System.out.println("Output File: " + outputFile);
			System.out.println("READY TO PARSE INPUT");
			System.out.println();
		}

		// Parse input AsyncAPI spec
    	AsyncAPI asyncApi = null;
    	ObjectMapper mapper = new ObjectMapper( new YAMLFactory().enable(YAMLGenerator.Feature.MINIMIZE_QUOTES) );
    	
        try {
        	asyncApi = mapper.readValue(new File(inputFile), AsyncAPI.class);
        } catch (DatabindException dbexc) {
        	System.err.println("Failed to parse the input file\n" + dbexc.getMessage());
        	System.exit(-10);
		} catch (StreamReadException srexc ) {
        	System.err.println("Failed to parse the input file\n" + srexc.getMessage());
        	System.exit(-11);
		} catch (IOException ioexc) {
			System.err.println("There was an error reading the input file: " + inputFile);
			System.err.println(ioexc.getMessage());
        	System.exit(-12);
		}
		if (debug) 	System.out.println("COMPLETED PARSING");

		// Map output format
		CICDConfig config = null;
		try {
			config = AsyncAPIToCICDMapper.mapAsyncAPIToCICD(asyncApi, targetServer, debug);
		} catch ( AsyncAPIToCICDException aexc ) {
			System.exit(aexc.getErrorCode());
		} catch ( Exception exc ) {
			System.err.println("An error occurred parsing the AsyncAPI input file");
			System.err.println(exc);
			System.exit(-1);
		}
		if (debug) System.out.println("COMPLETED MAPPING");

		// Create output file
		try {
			mapper.writeValue(new File( outputFile ), config);
        } catch (DatabindException dbexc) {
        	System.err.println("Failed to create the output file format\n" + dbexc.getMessage());
        	System.exit(-20);
		} catch (StreamWriteException swexc ) {
        	System.err.println("Failed to serialize the output file\n" + swexc.getMessage());
        	System.exit(-21);
		} catch (IOException ioexc) {
			System.err.println("There was an error writing the output file: " + outputFile);
			System.err.println(ioexc.getMessage());
        	System.exit(-22);
		}
		if (debug) System.out.println( "Success! - Serialized output written to: " + outputFile );

		System.exit(0);
	}

}

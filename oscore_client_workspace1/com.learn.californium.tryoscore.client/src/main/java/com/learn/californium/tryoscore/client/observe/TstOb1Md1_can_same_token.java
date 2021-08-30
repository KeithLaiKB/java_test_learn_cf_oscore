package com.learn.californium.tryoscore.client.observe;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.coap.Token;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.RandomTokenGenerator;
import org.eclipse.californium.core.network.TokenGenerator;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.cose.AlgorithmID;
import org.eclipse.californium.elements.util.Bytes;
import org.eclipse.californium.oscore.HashMapCtxDB;
import org.eclipse.californium.oscore.OSCoreCoapStackFactory;
import org.eclipse.californium.oscore.OSCoreCtx;
import org.eclipse.californium.oscore.OSCoreResource;
import org.eclipse.californium.oscore.OSException;

public class TstOb1Md1_can_same_token {

	private final static HashMapCtxDB db = new HashMapCtxDB();
	//
	//
	//
	private static String uri_addr1 = "127.0.0.1";
	private static String uri_addr3 = "192.168.239.137";		

	//
	//private final static String uriLocal 			= "coap://localhost";
	private final static String uriLocal1 			= "coap://"+uri_addr1;
	private final static String uriLocal3 			= "coap://"+uri_addr3;
	//
	//
	private final static AlgorithmID alg = AlgorithmID.AES_CCM_16_64_128;
	private final static AlgorithmID kdf = AlgorithmID.HKDF_HMAC_SHA_256;
	private final static byte[] master_secret = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B,
			0x0C, 0x0D, 0x0E, 0x0F, 0x10 };
	private final static byte[] master_salt = { (byte) 0x9e, (byte) 0x7c, (byte) 0xa9, (byte) 0x22, (byte) 0x23,
			(byte) 0x78, (byte) 0x63, (byte) 0x40 };
	//
	//


	public static void main(String[] args) throws OSException {

		byte[] sid = new byte[0];
		byte[] rid = new byte[] { 0x01 };
		//
		//
		//
		EndpointManager.clear();
		OSCoreCoapStackFactory.useAsDefault(db);
		//
		try {
			OSCoreCtx ctx = new OSCoreCtx(master_secret, true, alg, sid, rid, kdf, 32, master_salt, null);

			db.addContext(uriLocal1, ctx);


		}
		catch(OSException e) {
			System.err.println("Failed to set client OSCORE Context information!");
		}


		CoapClient client = new CoapClient();

		CoapHandler myObserveHandler 			= null;
		// Handler for Observe responses
        try {
			//
        	// set handler for observer method, because observe method needs asynchronous operation
			myObserveHandler = new CoapHandler() {

	            @Override
	            public void onLoad(CoapResponse response) {
	            	System.out.println("on load: " + response.getResponseText());
	            	System.out.println("get code: " + response.getCode().name());
	            }

	            @Override
	            public void onError() {
	            }
	        };
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//Create request and initiate Observe relationship
		Request r1 = new Request(Code.GET);
		r1.setConfirmable(true);
		r1.setURI("coap://"+uri_addr1+":5656"+"/oscore/observe2");
		r1.getOptions().setOscore(Bytes.EMPTY);



		//TokenGenerator tokenGenerator = new RandomTokenGenerator(NetworkConfig.getStandard());
		//Token tokengen1 = tokenGenerator.createToken(TokenGenerator.Scope.LONG_TERM);

		byte[] bys_token1 = {-123, -120, 3, -22, -36, 63, -50, -118};
		Token token1 = new Token(bys_token1);
		
		r1.setToken(token1);
		r1.setObserve();
		CoapObserveRelation relation = client.observe(r1,myObserveHandler);


        //---------------------------------------------
        // wait for the notifications
        long startObserveTime=System.nanoTime();   		 
		//
		//
		boolean judge_timeout = false;
		while (judge_timeout==false) {
			long nowTime_tmp=System.nanoTime();
			long timelimit_tmp=20*1000000000L;
			if(nowTime_tmp-startObserveTime>timelimit_tmp) {
				judge_timeout=true;
			}
		}



		//---------------------------------------------
		// cancel observe
		//relation.reactiveCancel();
		relation.proactiveCancel();
        //---------------------------------------------
        // wait for cancel
        startObserveTime=System.nanoTime();   			 
		//
		//
		judge_timeout = false;
		while (judge_timeout==false) {
			long nowTime_tmp=System.nanoTime();
			long timelimit_tmp=10*1000000000L;
			if(nowTime_tmp-startObserveTime>timelimit_tmp) {
				judge_timeout=true;
			}
		}
		
		
		client.shutdown();
		
		
		
		
	}
}
package com.learn.californium.tryoscore.client.observe;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionNumberRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.cose.AlgorithmID;
import org.eclipse.californium.elements.util.Bytes;
import org.eclipse.californium.oscore.ContextRederivation.PHASE;
import org.eclipse.californium.oscore.HashMapCtxDB;
import org.eclipse.californium.oscore.OSCoreCoapStackFactory;
import org.eclipse.californium.oscore.OSCoreCtx;
import org.eclipse.californium.oscore.OSCoreResource;
import org.eclipse.californium.oscore.OSException;
/**
 * 
 * 
 * <p>
 * 							description:																			</br>	
 * &emsp;						client to observe																	</br>
 * &emsp;						enable rederivation and set param to be PHASE.CLIENT_INITIATE						</br>
 * 
 * 							ref:																					</br>	
 * &emsp;						californium/cf-oscore/src/test/java/org/eclipse/californium/oscore/ContextRederivationTest.java  	</br>	
 *  																												</br>
 *  
 *
 * @author laipl
 *
 */
public class TstOb2_Rederivation_CliInit_can2 {

	private final static HashMapCtxDB db = new HashMapCtxDB();
	//
	//
	
	
	//
	private static String uri_addr1 = "127.0.0.1";
	private static String uri_addr2 = "135.0.237.84";			//因为你的树莓派已经端口映射到它的公共IP上了, 用这个就可以了
	private static String uri_addr3 = "192.168.239.137";		
	private static String uri_addr4 = "192.168.50.178";			//因为你是访问者, 你不需要知道树莓派在它的局域网中的内部IP,所以这个不需要
	//
	//private final static String uriLocal 			= "coap://localhost";
	private final static String uriLocal1 			= "coap://"+uri_addr1+":5656";
	private final static String uriLocal2 			= "coap://"+uri_addr2;
	private final static String uriLocal3 			= "coap://"+uri_addr3;
	private final static String uriLocal4 			= "coap://"+uri_addr4;
	private final static String uriLocal9 			= "myranduri";
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


		//californium/cf-oscore/src/main/java/org/eclipse/californium/oscore/ResponseDecryptor.java
		//INFO org.eclipse.californium.oscore.OptionJuggle - Removing inner only E options from the outer options




		byte[] sid = new byte[0];
		byte[] rid = new byte[] { 0x01 };
		//
		byte[] myContextId1 = { 0x74, 0x65, 0x73, 0x74, 0x74, 0x65, 0x73, 0x74 };
		byte[] myContextId2 = { 0x74, 0x65, 0x73, 0x74, 0x74, 0x65, 0x73, 0x75 };
		byte[] myContextId3 = { 0x74, 0x65, 0x73, 0x74, 0x74, 0x65, 0x73, 0x76 };
		byte[] myContextId4 = { 0x75 };
		//
		//
		EndpointManager.clear();
		OSCoreCoapStackFactory.useAsDefault(db);
		//
		try {
			//OSCoreCtx ctx = new OSCoreCtx(master_secret, true, alg, sid, rid, kdf, 32, master_salt, null);
			//
			//OSCoreCtx ctx = new OSCoreCtx(master_secret, true, alg, sid, rid, kdf, 0, master_salt, null);
			//OSCoreCtx ctx = new OSCoreCtx(master_secret, true);
			OSCoreCtx ctx = new OSCoreCtx(master_secret, true, alg, sid, rid, kdf, 32, master_salt, null, 4096);
			//db.addContext("coap://" + "127.0.0.1", ctx)
			//db.addContext("coap://" + uri_addr2, ctx);
			//db.addContext(uriLocal, ctx);
			//db.addContext(uriLocal9, ctx);
			//db.addContext(inner_server_uri, ctx);
			db.addContext(uriLocal1, ctx);
			//
			ctx.setContextRederivationEnabled(true);
			// Explicitly initiate the context re-derivation procedure
			ctx.setContextRederivationPhase(PHASE.CLIENT_INITIATE);
			//
			//
			//
			//
			//
			//OSCoreCtx newCtx = rederiveWithContextID(ctx, myContextId);
			//newCtx.setIncludeContextId(ctx,encodeToCborBstrBytes(myContextId));
			//ctx.setContextRederivationPhase(PHASE.CLIENT_INITIATE);
			//ctx.setContextRederivationPhase(PHASE.CLIENT_PHASE_1);
			//ctx.setContextRederivationPhase(PHASE.CLIENT_PHASE_3);

		}
		catch(OSException e) {
			System.err.println("Failed to set client OSCORE Context information!");
		}






		String resourceUri = "/oscore/observe2";
		//CoapClient client = new CoapClient("coap://"+uri_addr2+":5656");
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

		//ObserveHandler handler = new ObserveHandler();

		//Create request and initiate Observe relationship
		//byte[] token1 = Bytes.createBytes(new Random(), 8);
		//byte[] token1 = {0x0F, 0x1F, 0x2F, 0x3F, 0x4F, 0x5F, 0x6F, 0x7F};
		// token 可以 粗略的 类似于 mqtt中的 clientId
		// 但californium 的 setToken表明 token的大小为  0--8 bytes
		String str_token1 = "client1";
		byte[] token1 = str_token1.getBytes();
        byte[] token2 = {0x0F, 0x1F, 0x2F, 0x3F, 0x4F, 0x5F, 0x6F, 0x71};
		System.out.println(token1);

		Request r1 = new Request(Code.GET);
		r1.setConfirmable(true);
		r1.setURI("coap://"+uri_addr1+":5656"+"/hello_observer");
		//r1.setURI("coap://"+uri_addr2+":5656"+"/oscore/observe2");
		//r1.setURI("coap://127.0.0.1:5656/oscore/observe2");
		//r1.setURI("coap://135.0.237.84:5656/oscore/observe2");
		//r1.getOptions().setOscore(Bytes.EMPTY);
		
		//byte[] bys_temps=ByteBuffer.allocate(4).putInt(OptionNumberRegistry.NO_RESPONSE).array();
		//System.out.println(bys_temps);
		//System.out.println(bys_temps[0]);
		//r1.getOptions().setOscore(ByteBuffer.allocate(4).putInt(OptionNumberRegistry.OSCORE).array());
		r1.getOptions().setOscore(Bytes.EMPTY);
		//
		//
		//Request r = createClientRequest(Code.GET, resourceUri);
		r1.setToken(token1);
		r1.setObserve();
		CoapObserveRelation relation = client.observe(r1,myObserveHandler);

		//
		//
		//
		//Wait until 2 messages have been received
		//assertTrue(handler.waitOnLoadCalls(2, 2000, TimeUnit.MILLISECONDS));
        //---------------------------------------------
        // wait for the notifications
        long startObserveTime=System.nanoTime();   			//获取开始时间  
		//
		//
		boolean judge_timeout = false;
		while (judge_timeout==false) {
			long nowTime_tmp=System.nanoTime();
			long timelimit_tmp=10*1000000000L;
			if(nowTime_tmp-startObserveTime>timelimit_tmp) {
				judge_timeout=true;
			}
		}





		/*

		//Now cancel the Observe and wait for the final response
		Request r2 = new Request(Code.GET);
		r2.setConfirmable(true);
		r2.setURI("coap://"+uri_addr3+":5656"+"/hello_observer");
		//r2.setURI("coap://"+uri_addr2+":5656"+"/oscore/observe2");
		//r2.setURI("coap://127.0.0.1:5656/oscore/observe2");
		//r1.setURI("coap://135.0.237.84:5656/oscore/observe2");
		r2.getOptions().setOscore(Bytes.EMPTY);
		//
		//
		//r = createClientRequest(Code.GET, resourceUri);
		r2.setToken(token);
		//
		// http://sisinflab.poliba.it/swottools/ldp-coap/docs/javadoc/v1_0/org/eclipse/californium/core/coap/Request.html#setObserve--
		// setObserveCancel() 和 getOptions().setObserve(1) 作用应该是一样的, 因为getOptions().setObserve(1); 用到了 setObserveCancel
		r2.getOptions().setObserve(1); //Deregister Observe
		//r2.setObserveCancel();
		//
		//
		r2.send();

		
		
		
		try {
			Response resp = r2.waitForResponse(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
		
		
		
		relation.proactiveCancel();
		
		
        //---------------------------------------------
        // wait for the notifications
        startObserveTime=System.nanoTime();   			//获取开始时间  
		//
		//
		judge_timeout = false;
		while (judge_timeout==false) {
			long nowTime_tmp=System.nanoTime();
			long timelimit_tmp=5*1000000000L;
			if(nowTime_tmp-startObserveTime>timelimit_tmp) {
				judge_timeout=true;
			}
		}
		/*
		assertEquals( ResponseCode.CONTENT, resp.getCode());
		assertEquals(MediaTypeRegistry.TEXT_PLAIN, resp.getOptions().getContentFormat());
		assertFalse(resp.getOptions().hasObserve());
		assertEquals("two", resp.getPayloadString());
		assertEquals("two", relation.getCurrent().getResponseText());
		*/
		client.shutdown();
	}

	/*
	private Request createClientRequest(Code c, String resourceUri) {
		String serverUri = "coap://localhost";
		Request r = new Request(c);
		r.setConfirmable(true);
		r.setURI(serverUri);
		if(withOSCORE) {
			r.getOptions().setOscore(Bytes.EMPTY); //Use OSCORE
		}
		return r;
	}*/
}
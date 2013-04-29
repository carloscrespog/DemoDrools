package com.sample;

import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import org.drools.KnowledgeBase;
import org.drools.WorkingMemoryEntryPoint;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.json.JSONException;
import org.json.JSONObject;


public class NodeToExpert {

	private String ipDest;
	private SocketIO socket;
	private static WorkingMemoryEntryPoint entryPoint;
	private static KnowledgeBuilder kbuilder;
	private static KnowledgeBase kbase;
	private static StatefulKnowledgeSession ksession;
	
	
	public NodeToExpert(String ipDest,
			WorkingMemoryEntryPoint entryPoint,KnowledgeBuilder kbuilder,KnowledgeBase kbase, StatefulKnowledgeSession ksession) {
		super();
		this.ipDest = ipDest;
		
		NodeToExpert.entryPoint = entryPoint;
		NodeToExpert.kbuilder=kbuilder;
		NodeToExpert.kbase=kbase;
		NodeToExpert.ksession=ksession;
	}


	public void init(){
		
		try {
			socket = new SocketIO("http://localhost:3000/");

			socket.connect(new IOCallback() {
	        public void onMessage(JSONObject json, IOAcknowledge ack) {
	            try {
	                System.out.println("Server said:" + json.toString(2));
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	        }

	        
	        public void onMessage(String data, IOAcknowledge ack) {
	            System.out.println("Server said: " + data);
	        }

	        
	        public void onError(SocketIOException socketIOException) {
	            System.out.println("an Error occured");
	            socketIOException.printStackTrace();
	        }

	        
	        public void onDisconnect() {
	            System.out.println("Connection terminated.");
	        }

	        
	        public void onConnect() {
	            System.out.println("Connection established");
	        }

	        
	        public void on(String event, IOAcknowledge ack, Object... args) {
	        	if(event.equals("Rule")){
	        		System.out.println("Rule received: "+args[0]);
	        		Resource myResource = ResourceFactory.newReaderResource((Reader) new StringReader((String) args[0]));

	        		kbuilder.add(myResource, ResourceType.DRL);
	                if ( kbuilder.hasErrors() ) {
	                    System.out.println( kbuilder.getErrors().toString() );
	                    throw new RuntimeException( "Unable to compile drl\"." );
	                }
	                kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
	                

	        	}else{
	        		System.out.println("Server triggered event '" + event + "'");
		            Event ev=new Event(event,"hello Drools");
		            entryPoint.insert(ev);
	        	}
	            

	        }
	    });

	    // This line is cached until the connection is establisched.
	    socket.send("Hello Server!");
	    socket.emit("hola","Hello Server!");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}

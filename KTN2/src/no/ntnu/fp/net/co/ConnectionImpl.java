/*
 * Created on Oct 27, 2004
 */
package no.ntnu.fp.net.co;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import javax.sound.sampled.ReverbType;

//import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.cl.ClException;
import no.ntnu.fp.net.cl.ClSocket;
import no.ntnu.fp.net.cl.KtnDatagram;
import no.ntnu.fp.net.cl.KtnDatagram.Flag;
import no.ntnu.fp.net.co.AbstractConnection.State;

/**
 * Implementation of the Connection-interface. <br>
 * <br>
 * This class implements the behaviour in the methods specified in the interface
 * {@link Connection} over the unreliable, connectionless network realised in
 * {@link ClSocket}. The base class, {@link AbstractConnection} implements some
 * of the functionality, leaving message passing and error handling to this
 * implementation.
 * 
 * @author Sebj¿rn Birkeland and Stein Jakob Nordb¿
 * @see no.ntnu.fp.net.co.Connection
 * @see no.ntnu.fp.net.cl.ClSocket
 */
public class ConnectionImpl extends AbstractConnection {

	/** Keeps track of the used ports for each server port. */
	private static Map<Integer, Boolean> usedPorts = Collections.synchronizedMap(new HashMap<Integer, Boolean>());
	private static final int MAXPORT = 48900;
	private static final int STARTPORT = 5555;

	private int sendTries = 0; //keeps track of how many times we have tried to send a packet and not received answer
	private static final int MAXSENDTRIES = 2;
	private int receives = 0; //keeps track of how many times we have tried to receive a packet and not received answer
	private static final int MAXRECEIVES = 2;

	private KtnDatagram lastPacket = null;
	/**
	 * Initialise initial sequence number and setup state machine.
	 * 
	 * @param myPort
	 *            - the local port to associate with this connection
	 */
	public ConnectionImpl(int myPort) {
		this.myPort = myPort;
		this.myAddress = getIPv4Address();
		//throw new NotImplementedException();
	}

	private String getIPv4Address(){
		try {
			return InetAddress.getLocalHost().getHostAddress();
		}
		catch (UnknownHostException e) {
			return "127.0.0.1";
		}
	}

	/**
	 * Establish a connection to a remote location.
	 * 	
	 * @param remoteAddress
	 *            - the remote IP-address to connect to
	 * @param remotePort
	 *            - the remote portnumber to connect to
	 * @throws IOException
	 *             If there's an I/O error.
	 * @throws java.net.SocketTimeoutException
	 *             If timeout expires before connection is completed.
	 * @see Connection#connect(InetAddress, int)
	 * @see AbstractConnection#receiveAck()
	 */
	public void connect(InetAddress remoteAddress, int remotePort) throws IOException, SocketTimeoutException{
		KtnDatagram ack;
		this.remoteAddress = remoteAddress.getHostAddress();
		this.remotePort = remotePort;
		KtnDatagram IPacket = constructInternalPacket(Flag.SYN);
		// uses a self made method similar to sendDataPacketWithRetransmit() because we need to send a packet even though the state is set to CLOSED
		//ack = sendPacketWithRetransmitConnect(IPacket);
		try{
			simplySendPacket(IPacket);
		} catch (ClException e) {

			Log.writeToLog("SimplySendFailed", "ConnectionImpl");
			e.printStackTrace();
		}
		state = State.SYN_SENT;

		ack = receiveAck();
		if(ack != null)
			this.remotePort = ack.getSrc_port();
		else
			throw new SocketTimeoutException();

		if(ack.getFlag() == Flag.SYN_ACK){
			//If we received a syn_ack from the right server the connection is established
			state = State.ESTABLISHED;
			//System.out.println("Client Established!");
		}
		//System.out.println(ack.getFlag());
		sendAck(ack,false);
	}
	/**
	 * Listen for, and accept, incoming connections.
	 * 
	 * @return A new ConnectionImpl-object representing the new connection.
	 * @see Connection#accept()
	 */
	public Connection accept() throws IOException, SocketTimeoutException {
		KtnDatagram ack;
		state = State.LISTEN;
		KtnDatagram packet;
		do{
			packet = receivePacket(true);
		}
		while(packet == null || packet.getFlag() != Flag.SYN);
		state = State.SYN_RCVD;
		ConnectionImpl c = new ConnectionImpl(findFreePort());//method to find a free port
		c.remotePort = packet.getSrc_port();
		c.remoteAddress = packet.getSrc_addr();
		try{
			c.sendAck(packet,true);
		} catch (IOException e) {
			Log.writeToLog("sendAck failed", "ConnectionImpl");
			e.printStackTrace();
		}
		ack = c.receiveAck();
		if(!ack.getSrc_addr().equals(c.remoteAddress)){
			System.out.println("ACK SRC not Equal to remoteAdress");
			System.out.println("Ack.src: "+ ack.getSrc_addr());
			System.out.println("Remoteadress: " + c.remoteAddress);
			
			return null;
		}
		if(ack == null || ack.getFlag() != Flag.ACK)
			throw new SocketTimeoutException();
		
		c.state = State.ESTABLISHED;
		//System.out.println("Server connection up");
		Log.writeToLog("Connection established", "Client");
		state = State.LISTEN;
		return (Connection)c;
	}
	/**
	 * Finds a free port for the accept method 
	 * Alltough originaly it was based on the idea of usedPorts holding available ports (which would be far more elegant), 
	 * the method now generates more input for the usedPorts and eventually throws an exception if the port number get to high.
	 * 
	 * @return
	 * @throws IOException
	 */
	public int findFreePort() throws IOException{//method to find a free port
		int port = STARTPORT;
		usedPorts.put(port, false);
		boolean foundPort = false;
		while(!foundPort){
			port = (int) (Math.random()*MAXPORT-STARTPORT)+STARTPORT;
			try{
				if(!usedPorts.get(port)){ //assuming usedport is stored as false, this if should hit when we find a freeport
					foundPort = true;
					usedPorts.put(port, false);
				}
			}
			catch(NullPointerException e){//There is no previously used port that is free in usedPorts, so we need to add a new one 
				if(port < MAXPORT){
					usedPorts.put(port, false);
					port = STARTPORT;
				}
			}
		}
		return port;
	}

	/**
	 * Send a message from the application.
	 * 
	 * @param msg
	 *            - the String to be sent.
	 * @throws ConnectException
	 *             If no connection exists
	 * @throws IOException
	 *             If no ACK was received.
	 * @see AbstractConnection#sendDataPacketWithRetransmit(KtnDatagram)
	 * @see no.ntnu.fp.net.co.Connection#send(String)
	 */
    public void send(String msg) throws ConnectException, IOException {
    	
    	KtnDatagram dataGram  = constructDataPacket(msg);
    	sendDataPacketWithRetransmit(dataGram);
    	
    }

	/**
	 * Wait for incoming data.
	 * 
	 * @return The received data's payload as a String.
	 * @see Connection#receive()
	 * @see AbstractConnection#receivePacket(boolean)
	 * @see AbstractConnection#sendAck(KtnDatagram, boolean)
	 */
    public String receive() throws ConnectException, IOException {
    	KtnDatagram pkt;
    	do{
    		pkt = receivePacket(false);
    		
    	}while(!isValid(pkt));
    	sendAck(pkt, false);
    	return (String) pkt.getPayload();
    }

	/**
	 * Close the connection.
	 * @see Connection#close()
	 */
	public void close() throws IOException {
		System.out.println("System in state : " + state);
		KtnDatagram ack = null;
		KtnDatagram packet = null;
		KtnDatagram finack = null;
		if(state == State.CLOSE_WAIT){
			System.out.println("Last Packet: "+lastPacket);
			sendAck(lastPacket, false);
			packet = constructInternalPacket(Flag.FIN);

			try {
				Thread.currentThread().sleep(100);//Wait for client to be ready to recieve FIN
				simplySendPacket(packet);
			} catch (ClException e) {
				e.printStackTrace();
			}
			catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			ack = receiveAck();
			if(ack == null)
				//How to handle this? Answer: Do nothing!
			state = State.CLOSED;
		}
		else if(state == State.ESTABLISHED){
			packet = constructInternalPacket(Flag.FIN);
			try {
				simplySendPacket(packet);
			} catch (ClException e) {
				e.printStackTrace();
			}
			state = State.FIN_WAIT_1;
			ack = receiveAck();
			if(ack == null){
				if(sendTries < MAXSENDTRIES){
					state = State.ESTABLISHED;
					close();
					return;					
				}
				else{
					state = State.CLOSED;
				}
			}
			state = State.FIN_WAIT_2;
			
			System.out.println("Waiting for Fin2");
			finack = receiveAck();
			if(finack == null)
				finack = receiveAck();
			System.out.println("Done waiting for Fin2");
			if(finack != null)
				sendAck(finack, false);
				state = State.TIME_WAIT;
			
			state = State.CLOSED;
			
		}
		else{
			System.out.println("Impressive you managed to call close in the state: " + state);
			throw new IOException();
		}
		
		
	}


	protected boolean isValid(KtnDatagram packet) {
		if(packet.getChecksum() == packet.calculateChecksum())
			return true;
		return false;
	}
}
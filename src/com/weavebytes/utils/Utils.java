/*
 * -----------------------------------
 * Lan Chat Lite
 * -----------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * 
 * Author: Roohi Verma <roohivermapb@gmail.com>
 * 
 */


package com.weavebytes.utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Utils {

	public static String getSystemIP() {
		
		
		return "";
	}
	
	public static String getHost() {
		
		try {
            InetAddress ipAddr = InetAddress.getLocalHost();
            return ipAddr.getHostName();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
		return "Unknown-host";
	}
	
	public static String getIP(){
	    String ipAddress = null;
	    Enumeration<NetworkInterface> net = null;
	    try {
	        net = NetworkInterface.getNetworkInterfaces();
	    } catch (SocketException e) {
	        throw new RuntimeException(e);
	    }

	    while(net.hasMoreElements()){
	        NetworkInterface element = net.nextElement();
	        Enumeration<InetAddress> addresses = element.getInetAddresses();
	        while (addresses.hasMoreElements()){
	            InetAddress ip = addresses.nextElement();
	            if (ip instanceof Inet4Address){
	            	
	                if (ip.isSiteLocalAddress()){

	                    ipAddress = ip.getHostAddress();
	                }
	            }
	        }
	    }
	    return ipAddress;
	}
	
	public static String getIpPrefix(String ip) {
		
		String l[] = ip.split("\\.");
		return l[0] + "." + l[1] + "." + l[2] + ".";
		
	}
	
	public static void sendUdpBroadcast(String msg, int port) {
		for(int i=2; i<255; i++) {	
			String ip = getIpPrefix(getIP()) + i;
			
			// dont send my msg to me !!!
			if(ip.equals(getIP())) continue;
			
			sendUdpMsg(msg, ip, port);
		}
	}
	
	public static void sendUdpMsg(String msg, String ip, int port) {
	    try {

	      InetAddress address = InetAddress.getByName(ip);
	    	
	      // Initialize a datagram packet with data and address
	      DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), address, port);

	      // Create a datagram socket, send the packet through it, close it.
	      DatagramSocket dsocket = new DatagramSocket();
	      dsocket.send(packet);
	      dsocket.close();
	    } catch (Exception e) {
	      System.err.println(e);
	    }
	  }
	
	
}//Utils

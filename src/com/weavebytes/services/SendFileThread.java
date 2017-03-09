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


package com.weavebytes.services;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.weavebytes.config.Config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class SendFileThread extends Thread {

	private String filePath;
	private String otherIP;
	private int port;
   
	/**
	 * constructor
	 * 
	 * @param filePath
	 */
	public SendFileThread(String filePath, String otherIP, int port) {
		
		System.out.println("[SendFileThread] ()");
		
		this.filePath = filePath;
		this.otherIP = otherIP;
		this.port = port;
	
	}
	/**
	 * thread run method
	 */
	public void run() {
		
		System.out.println("[SendFileThread] :: started at port " + port);
		int progress = 0;
		try {
					
			Socket socket = new Socket(InetAddress.getByName(otherIP), port);
			
			//The InetAddress specification
			InetAddress IA = InetAddress.getByName("localhost"); 

			//Specify the file
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis); 

			//Get socket's output stream
			OutputStream os = socket.getOutputStream();

			//Read File Contents into contents array 
			byte[] contents;
			long fileLength = file.length(); 
			long current = 0;
			
			//Frame to display the progress bar
			JFrame progFrame =  new JFrame("Progress frame");
		  	progFrame.setBounds(500, 300, 350, 200);
		  	
		  	JPanel panel = new JPanel();
		  	panel.setLayout(new GridLayout(10, 1));
		  	
		  	//Progress bar
		  	JProgressBar progressbar = new JProgressBar(0, 100); 	
		  	progressbar.setSize(new Dimension(100, 15));
		    progressbar.setBackground(Color.white);
		    progressbar.setForeground(Color.green);		    
		   
		    //adding blank frames
		    for(int i = 0; i <= 2; i++){
			    panel.add(new JPanel());
			}
			JPanel progressPanel = new JPanel(new BorderLayout(5, 5));
			JLabel progressLabel = new JLabel();
		
			progressPanel.add(progressLabel);	    
			panel.add(progressPanel, BorderLayout.EAST);
			panel.add(progressbar);
		 
		    progFrame.add(panel);
		    progFrame.setVisible(true);		
		  			
			long start = System.nanoTime();
			
		   //Going to receive data
			while(current!=fileLength){ 
				int size = 10000;
				if(fileLength - current >= size)
					current += size;    
				else{ 
					size = (int)(fileLength - current); 
					current = fileLength;
				} 
				contents = new byte[size]; 
				bis.read(contents, 0, size); 
				os.write(contents);
			   	
				System.out.print("Sending file ... "+ (progress = (int) ((current*100)/fileLength)) +"% complete!");
				progressLabel.setText("Sending file...." + progress + "%");
				progressbar.setValue(progress);
			}   
           progFrame.setVisible(false);
			os.flush(); 
			//File transfer done. Close the socket connection!
			socket.close();
			//showing the success message
		  JOptionPane.showMessageDialog(null, "File sent successfully !");		 
		  System.out.println("[SendFileThread] :: File sent succesfully!");  		
		}
		catch(Exception e)
		{
			System.out.println("[SendFileThread] :: exception: " + e);
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Failed to send file. Please send again");
			System.out.println(" File not sent !");  
		}		         
	}

}//SendFileThread

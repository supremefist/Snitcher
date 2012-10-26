package com.s3.snitcher.arduino;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

import java.security.KeyStore.Builder;
import java.util.Enumeration;


public class ArduinoSnitcher implements SerialPortEventListener {
	
	
	SerialPort serialPort;
	/** Buffered input stream from the port */
	private InputStream input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public void initialize(String portName) {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
			}
		}

		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = serialPort.getInputStream();
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				int available = input.available();
				byte chunk[] = new byte[available];
				input.read(chunk, 0, available);

				// Displayed results are codepage dependent
				System.out.print(new String(chunk));
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

	private synchronized void writeData(byte[] data)
	{
		try {
			output.write(data);
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}
	
	
	private void sendCommand(byte command, int channel)
	{
		
		ByteBuffer b = ByteBuffer.allocate(6);
		b.put((byte)0x7);
		b.put((byte)0xE);
		b.put(command);
		b.put((byte)channel);
		b.put((byte)0x7);
		b.put((byte)0xE);
		
		
		writeData(b.array());
	}

	
	private class Command {
		public final static byte BUILDING = 0;
		public final static byte BUILD_INTERRUPTED = 1;
		public final static byte BUILD_FAILED = 2;
		public final static byte BUILD_SUCCEEDED = 3;
	}

	
	
	public void setChannelToState(int channel, ProjectState projectState ) throws Exception
	{
		if (channel > 5 )
		{
			throw new Exception("Only channels 0 -> 5 exist");
		}
		
		switch (projectState)
		{
		case BUILD_FAILED:
			sendCommand(Command.BUILD_FAILED, channel);
			break;
		case BUILD_SUCCEEDED:
			sendCommand(Command.BUILD_SUCCEEDED, channel);
			break;
		case BUILD_INTERRUPTED:
			sendCommand(Command.BUILD_INTERRUPTED, channel);
			break;
		case BUILDING:
			sendCommand(Command.BUILDING, channel);
			break;
		}
	}
}

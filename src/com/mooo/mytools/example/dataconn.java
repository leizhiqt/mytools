package com.mooo.mytools.example;

// By Rowland http://rowland.blcss.com

import java.io.*;
import java.net.*;
import java.util.*;

/** dataconn encapsulates a Socket with some ease of use features **/
public class dataconn {
	public boolean debug = false;
	public String debugname = null;
	public boolean error = false;

	protected Socket sconn;
	protected InputStream is;
	protected OutputStream os;

	public dataconn() {
	}

	public dataconn(String szhost, int port) {
		try {
			connect(new Socket(InetAddress.getByName(szhost), port));
		} catch (Throwable T) {
			System.err.println("new dataconn ERR: " + T.getMessage());
			error = true;
		}
	}

	public dataconn(InetAddress rhost, int port) {
		try {
			connect(new Socket(rhost, port));
		} catch (Throwable T) {
			System.err.println("new dataconn ERR: " + T.getMessage());
			error = true;
		}
	}

	public dataconn(Socket _s) {
		connect(_s);
	}

	public void setDebug(boolean _debug) {
		debug = _debug;
	}

	protected void finalize() {
		close();
	}

	protected void connect(Socket _s) {
		sconn = _s;
		connect();
	}

	protected void connect() {
		error = false;
		try {
			is = sconn.getInputStream();
			os = sconn.getOutputStream();
		} catch (Throwable T) {
			exception(T);
			error = true;
		}
	}

	protected void close() {
		if (sconn == null)
			return;
		try {
			os = null;
			is = null;
			sconn.close();
			sconn = null;
		} catch (Throwable E) {
			System.err.println("dataconn.close ERR: " + E.getMessage());
			error = true;
		}
	}

	protected final void write(String d) {
		write(d.getBytes());
	}

	protected final void write(byte[] d) {
		if (error)
			return;
		try {
			os.write(d);
			os.flush();
			debuglog(false, d);
			log(false, d);
		} catch (Throwable T) {
			exception(T);
			error = true;
		}
	}

	protected final byte[] read() {
		if (error)
			return null;
		try {
			Thread.sleep(50);
		} catch (InterruptedException IE) {
			return null;
		}
		try {
			int iavail = is.available();
			if (iavail > 0) {
				byte[] d = new byte[iavail];
				is.read(d);
				debuglog(true, d);
				log(true, d);
				return d;
			}
		} catch (Throwable T) {
			exception(T);
			error = true;
		}
		return null;
	}

	// exception and log handling...

	protected final void debuglog(boolean isread, byte[] d) {
		if (!debug)
			return;
		if (debugname != null)
			System.err.print(debugname + " ");
		if (isread)
			System.err.print("R:");
		else
			System.err.print("W:");
		System.err.println(" " + d.length + " bytes");
	}

	protected void log(boolean isread, byte[] d) {
		// override in derived class
	}

	protected void exception(Throwable T) {
		String m = "EXCEPTION: " + T.getMessage();
		System.err.println(m);
		if (!(T instanceof SocketException))
			T.printStackTrace();
	}

}

// =====================================================================================

/**
 * proxyconn is glue between two Sockets wrapped in dataconn, to pss traffic
 * between them.
 **/
class proxyconn implements Runnable {
	public boolean debug = false;
	protected Thread t;
	protected dataconn c1, c2;

	public proxyconn() {
	}

	public proxyconn(dataconn _c1, dataconn _c2) {
		c1 = _c2;
		c2 = _c2;
	}

	public proxyconn(Socket s1, Socket s2) {
		c1 = new dataconn(s1);
		c2 = new dataconn(s2);
	}

	public proxyconn(Socket s1, String thost, int tport) {
		c1 = new dataconn(s1);
		c2 = new dataconn(thost, tport);
	}

	public void go() {
		c1.debug = debug;
		c1.debugname = "c1";
		c2.debug = debug;
		c2.debugname = "c2";
		t = new Thread(this);
		t.start();
	}

	protected void log(boolean fromc1, byte[] d) {
	}

	public void run() {
		while (dopolling()) {
		}
	}

	/*
	 * dopolling is called in run() loop. return false to close proxy connection
	 * *
	 */
	protected boolean dopolling() {
		try {
			byte[] d;
			d = c1.read();
			if (d != null) {
				log(true, d);
				c2.write(d);
			}
			d = c2.read();
			if (d != null) {
				log(false, d);
				c1.write(d);
			}
			if (c1.error || c2.error)
				return false;
		} catch (Throwable T) {
			exception(T);
			return false;
		}
		return true;
	}

	protected void exception(Throwable T) {
		System.err.println("proxyconn ERR" + T.getMessage());
	}
}

// =====================================================================================

/** proxyserver listens on given lport, forwards traffic to tport on thost **/
class proxyserver implements Runnable {
	public boolean debug = false;

	protected int tport, lport; // tport is port on target host, lport is listen
								// port
	protected String thost;
	protected Thread thread;

	public proxyserver() {
	}

	public proxyserver(String _thost, int _port) {
		thost = _thost;
		tport = lport = _port;
	}

	public proxyserver(String _thost, int _tport, int _lport) {
		thost = _thost;
		tport = _tport;
		lport = _lport;
	}

	public void go() {
		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		try {
			ServerSocket ss = new ServerSocket(lport);
			if (debug)
				System.err.println("proxyserver: " + lport + " listening");
			while (true) {
				Socket sconn = ss.accept();
				if (debug)
					System.err.print(" gotConn: " + lport + " ");
				gotconn(sconn);
			}
		} catch (Throwable T) {
			if (debug)
				System.err
						.println("proxyserver: " + lport + " " + T.toString());
			T.printStackTrace();
		}
	}

	protected void gotconn(Socket sconn) throws Exception {
		proxyconn pc = new proxyconn(sconn, thost, tport);
		pc.go();
	}

	public static void main(String args[]) {
		Integer iport = new Integer(args[1]);
		proxyserver us = new proxyserver(args[0], iport.intValue());
		us.go();
	}
}

// =====================================================================================

/**
 * proxylogconn is a logging version of proxyconn. Stores log files in "log'
 * subdirectory
 **/
class proxylogconn extends proxyconn {
	protected PrintWriter lout;
	public String logfspec = "?";

	public proxylogconn(Socket s1, String thost, int tport) {
		c1 = new dataconn(s1);
		c2 = new dataconn(thost, tport);

		try {
			InetAddress rhost = s1.getInetAddress();
			String rhostname = rhost.getHostName();
			logfspec = "log" + File.separatorChar + tport + "." + rhostname
					+ ".log";
			FileOutputStream outraw = new FileOutputStream(logfspec);
			lout = new PrintWriter(outraw);
			lout.println("// CLIENT: " + rhostname);
			lout.println("// TARGET: " + thost);
		} catch (Throwable T) {
			System.err.println("Can't open log file: " + logfspec);
			System.err.println("proxylogserver ERR: " + T.getMessage());
		}
	}

	public void run() {
		super.run();
		if (lout != null)
			lout.close();
	}

	protected void finalize() {
		try {
			if (lout != null)
				lout.close();
		} catch (Throwable T) {
		}
	}

	protected void exception(Throwable T) {
		try {
			System.err.println("EXCEPTION: " + T.getMessage());
			lout.println("EXCEPTION: " + T.getMessage());
		} catch (Throwable T1) {
		}
	}

	protected void log(boolean fromc1, byte[] d) {
		try {
			if (fromc1)
				lout.print("c(\"");
			else
				lout.print("s(\"");
			lout.print(printableBytes(d));
			lout.println("\");");
			lout.flush();
		} catch (Throwable T) {
			exception(T);
		}

	}

	static final String printableBytes(byte[] bytes) {
		if (bytes == null)
			return "*NONE*";
		StringBuffer s = new StringBuffer();
		int i;
		for (i = 0; i < bytes.length;) {
			int b = bytes[i];
			if (b < 0)
				b = 256 + b; // byte is signed type!
			if (b < ' ' || b > 0x7f) {
				int d1 = (int) (b >> 6) & 7;
				b = b & 0x3f;
				int d2 = (int) (b >> 3) & 7;
				int d3 = (int) b & 7;
				s.append("\\" + d1);
				s.append(d2);
				s.append(d3);
			} else if ('\\' == (char) b)
				s.append("\\\\");
			else
				s.append((char) b);
			i++;
			if (0 == (i - (40 * (i / 40))))
				s.append("\"+\n   \"");
		}
		String ss = s.toString();
		System.out.println(ss);
		return ss;
	}

}

// ========================================================================================

/**
 * proxylogserver is a logging version of proxyserver. Stores log files in "log'
 * subdirectory
 **/
class proxylogserver extends proxyserver {
	int tport;

	public proxylogserver() {
	}

	public proxylogserver(String _thost, int _port) {
		super(_thost, _port);
		tport = _port;
	}

	public void go() {
		thread = new Thread(this);
		thread.start();
	}

	protected void gotconn(Socket sconn) throws Exception {
		proxylogconn pc = new proxylogconn(sconn, thost, tport);
		pc.go();
	}

	public static void main(String args[]) {
		Integer iport = new Integer(args[1]);
		proxylogserver us = new proxylogserver(args[0], iport.intValue());
		us.go();
	}
}

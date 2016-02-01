package com.sombrainc.p2p.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerActions implements Runnable {

	private Socket socket;

	public ServerActions(final Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());) {
			final String action = ois.readUTF();
			if ("download".equals(action)) {
				downloadAction(ois);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private String getAbsolutePath(final String path) {
		return System.getProperty("user.home") + "/share/" + path;
	}

	private void downloadAction(final ObjectInputStream ois) throws IOException {
		System.out.println("download started");
		final String fileName = ois.readUTF();
		final File file = new File(getAbsolutePath(fileName));
		System.out.println(file.getName());
		final InputStream fis = new FileInputStream(file);
		try (final OutputStream sout = socket.getOutputStream(); final BufferedInputStream bis = new BufferedInputStream(fis);) {
			final ObjectOutputStream out = new ObjectOutputStream(sout);
			out.writeInt((int) file.length());
			out.flush();
			final byte[] mybytearray = new byte[(int) file.length()];
			bis.read(mybytearray, 0, mybytearray.length);
			sout.write(mybytearray, 0, mybytearray.length);
			sout.flush();
		}
	}
}

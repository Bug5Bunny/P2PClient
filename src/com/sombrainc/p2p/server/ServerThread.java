package com.sombrainc.p2p.server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerThread implements Runnable {

	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(6667);) {
			while (true) {
				new Thread(new ServerActions(serverSocket.accept())).start();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}

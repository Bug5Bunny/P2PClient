package com.sombrainc.p2p.server;

import java.io.IOException;
import java.net.ServerSocket;

import com.sombrainc.p2p.util.Constant;

public class PeerThread implements Runnable {

	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(Constant.CLIENTPORT);) {
			while (true) {
				new Thread(new PeerActions(serverSocket.accept())).start();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}

package com.sombrainc.p2p.service.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.sombrainc.p2p.service.Service;
import com.sombrainc.p2p.util.Constant;

public class ConnectService extends Service {

	@Override
	public void execute() throws IOException {
		try (Socket socket = new Socket(Constant.SERVERIP, Constant.SERVERPORT)) {
			System.out.println("Connecting to server...");
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			output.writeUTF(Constant.CONNECT);
			output.flush();
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			String line = input.readUTF();
			System.out.println(line);
		}
	}
}
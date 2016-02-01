package com.sombrainc.p2p.command.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.sombrainc.p2p.Client;
import com.sombrainc.p2p.command.Command;

public class ConnectCommand extends Command {

	@Override
	public void execute() throws IOException {
		String reason = "connect";
		try (Socket socket = new Socket(Client.SERVERIP, Client.PORT)) {
			System.out.println("Connecting to server...");
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeUTF(reason);
			out.flush();
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			String line = in.readUTF();
			System.out.println(line);
		}
	}

}
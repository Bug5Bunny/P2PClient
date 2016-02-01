package com.sombrainc.p2p.service.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.sombrainc.p2p.Client;
import com.sombrainc.p2p.service.Service;
import com.sombrainc.p2p.util.Constant;

public class AddService extends Service {

	@Override
	public void execute() throws IOException {

		List<String> files = new ArrayList<>();
		System.out.println("Input file names:");
		System.out.println("Input 'done' to complete file names input");
		while (true) {
			String line = Client.scanner.nextLine();
			if (line.equals("done")) {
				break;
			}
			files.add(line);
		}
		try (Socket socket = new Socket(Constant.SERVERIP, Constant.SERVERPORT)) {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			out.writeUTF(Constant.ADD);
			out.flush();
			out.writeUTF(InetAddress.getLocalHost().getHostAddress().toString());
			out.writeObject(files.toArray(new String[0]));
			out.flush();
			System.out.println(input.readUTF());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

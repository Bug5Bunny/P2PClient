package com.sombrainc.p2p.command.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.sombrainc.p2p.Client;
import com.sombrainc.p2p.command.Command;
import com.sombrainc.p2p.util.CustomScanner;

public class AddCommand extends Command {

	@Override
	public void execute() throws IOException {
		String reason = "add";

		String line;
		List<String> files = new ArrayList<>();
		int i = 0;
		System.out.println("Input file names:");
		System.out.println("Input 'done' to complete file names input");
		while (true) {
			line = CustomScanner.scanner.nextLine();
			if (line.equals("done")) {
				break;
			}
			files.add(line);
			i++;
		}
		try (Socket socket = new Socket(Client.SERVERIP, Client.PORT)) {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			out.writeUTF(reason);
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

package com.sombrainc.p2p.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.sombrainc.p2p.Client;
import com.sombrainc.p2p.service.Service;
import com.sombrainc.p2p.service.impl.AddService;
import com.sombrainc.p2p.service.impl.ConnectService;
import com.sombrainc.p2p.service.impl.DownloadFileService;
import com.sombrainc.p2p.util.Constant;

public class Controller {
	private static final Map<String, Service> COMMANDS;

	static {
		COMMANDS = new HashMap<>();
		COMMANDS.put(Constant.CONNECT, new ConnectService());
		COMMANDS.put(Constant.ADD, new AddService());
		COMMANDS.put(Constant.FINDFILE, new DownloadFileService());
	}

	public void menu() throws IOException {

		while (true) {
			System.out.print("Command:");
			String reason = Client.scanner.nextLine();
			if (COMMANDS.get(reason) != null) {
				COMMANDS.get(reason).execute();
			} else {
				System.out.println("Wrong command! Try again.");
			}
		}
	}
	public void downloadMenu() {
		try {
			String[] ipAddresses = new String[0];
			String fileName = Client.scanner.nextLine();
			try (final Socket socket = new Socket(Constant.SERVERIP, Constant.SERVERPORT);) {
				final ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
				output.writeUTF(Constant.FINDFILE);
				output.flush();
				output.writeUTF(fileName);
				output.flush();
				final ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
				ipAddresses = (String[]) inputStream.readObject();
			}
			if (ipAddresses.length == 0) {
				System.out.println("no such file");
				return;
			}

			for (int i = 0; i < ipAddresses.length; i++) {
				System.out.println("Coumputer IP | Computer number");
				System.out.println(ipAddresses[i] + "	#" + (i + 1));
			}
			System.out.println("Type computer number for file downloading:");
			int index;
			Scanner scanner = Client.scanner;
			while (true) {
				if (scanner.hasNextInt()) {
					index = scanner.nextInt() - 1;
					if (index < 0 || index >= ipAddresses.length) {
						System.out.println("Wrong input value, please try again");
						continue;
					}
					break;
				}
				System.out.println("Wrong input value, please try again");
			}

			DownloadFileService.downloadFile(ipAddresses[index], fileName);
		} catch (final IOException | ClassNotFoundException e) {
			System.out.println("There is some problem with file downloading");
		}
	}
	
	public List<String> inputFiles() {
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
		return files;
	}
}
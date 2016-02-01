package com.sombrainc.p2p;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sombrainc.p2p.command.Command;
import com.sombrainc.p2p.command.impl.AddCommand;
import com.sombrainc.p2p.command.impl.ConnectCommand;
import com.sombrainc.p2p.command.impl.DownloadFileCommand;
import com.sombrainc.p2p.server.ServerThread;
import com.sombrainc.p2p.util.CustomScanner;

public class Client {
	public static final int PORT = 6666;
	public static final String SERVERIP = "127.0.0.1";
	private static final Map<String, Command> COMMANDS;

	static {
		COMMANDS = new HashMap<>();
		COMMANDS.put("connect", new ConnectCommand());
		COMMANDS.put("add", new AddCommand());
		COMMANDS.put("findFile", new DownloadFileCommand());
	}

	public static void main(String[] args) throws IOException {
		new Thread(new ServerThread()).start();
		COMMANDS.get("connect").execute();
		String reason;
		while (true) {
			System.out.print("Command:");
			reason = CustomScanner.scanner.nextLine();
			if (COMMANDS.containsKey(reason)) {
				COMMANDS.get(reason).execute();
			} else {
				System.out.println("Wrong command! Try again.");
			}
		}
	}
}
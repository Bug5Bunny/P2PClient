package com.sombrainc.p2p.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sombrainc.p2p.Client;
import com.sombrainc.p2p.service.Service;
import com.sombrainc.p2p.service.impl.AddService;
import com.sombrainc.p2p.service.impl.ConnectService;
import com.sombrainc.p2p.service.impl.DownloadFileService;
import com.sombrainc.p2p.util.Constant;

public class Controller {
	private static final Map<String, Service> COMMANDS;
	private static final DownloadFileService downloadFileService = new DownloadFileService();

	static {
		COMMANDS = new HashMap<>();
		COMMANDS.put(Constant.CONNECT, new ConnectService());
		COMMANDS.put(Constant.ADD, new AddService());
		COMMANDS.put(Constant.FINDFILE, downloadFileService);
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
}
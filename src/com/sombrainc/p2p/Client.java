package com.sombrainc.p2p;

import java.io.IOException;
import java.util.Scanner;

import com.sombrainc.p2p.controller.Controller;
import com.sombrainc.p2p.server.ServerThread;
import com.sombrainc.p2p.service.impl.ConnectService;

public class Client {

	public static final Scanner scanner = new Scanner(System.in);
	private static final ConnectService connectCommand = new ConnectService();

	public static void main(String[] args) throws IOException {
		new Thread(new ServerThread()).start();
		connectCommand.execute();
		new Controller().menu();
	}
}
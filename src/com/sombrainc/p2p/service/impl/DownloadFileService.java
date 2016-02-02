package com.sombrainc.p2p.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;

import com.sombrainc.p2p.Client;
import com.sombrainc.p2p.service.Service;
import com.sombrainc.p2p.util.Constant;

public class DownloadFileService implements Service {

	@Override
	public void execute() {
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

			downloadFile(ipAddresses[index], fileName);
		} catch (final IOException | ClassNotFoundException e) {
			System.out.println("There is some problem with file downloading");
		}
	}

	private void downloadFile(final String ip, String fileName) throws IOException {
		try (Socket socket = new Socket(ip, Constant.CLIENTPORT);) {
			ObjectOutputStream socketOutput = new ObjectOutputStream(socket.getOutputStream());
			socketOutput.writeUTF(Constant.DOWNLOAD);
			socketOutput.writeUTF(fileName);
			socketOutput.flush();
			final InputStream is = socket.getInputStream();
			final ObjectInputStream in = new ObjectInputStream(is);
			final String msg = in.readUTF();
			System.out.println(msg);
			final OutputStream fos = new FileOutputStream(fileName);
			IOUtils.copy(is, fos);
			fos.flush();

		}
	}
}

package com.sombrainc.p2p.command.impl;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.sombrainc.p2p.command.Command;
import com.sombrainc.p2p.util.CustomScanner;

public class DownloadFileCommand extends Command {
	private static final int PORT = 6666;

	@Override
	public void execute() {

		try {
			String[] ipAddresses = new String[0];
			String fileName = CustomScanner.scanner.nextLine();
			try (final Socket socket = new Socket("127.0.0.1", PORT);) {

				final ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());
				dos.writeUTF("findFile");
				dos.flush();
				dos.writeUTF(fileName);
				dos.flush();
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
			Scanner scanner = CustomScanner.scanner;
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

	private static void downloadFile(final String ip, String fileName) throws IOException {
		try (Socket socket = new Socket(ip, 6667);) {
			ObjectOutputStream socketOutput = new ObjectOutputStream(socket.getOutputStream());
			socketOutput.writeUTF("download");
			socketOutput.writeUTF(fileName);
			socketOutput.flush();
			final InputStream is = socket.getInputStream();
			final ObjectInputStream in = new ObjectInputStream(is);
			final int fileSize = in.readInt();

			final byte[] mybytearray = new byte[fileSize];

			final OutputStream fos = new FileOutputStream(fileName);
			try (final BufferedOutputStream bos = new BufferedOutputStream(fos);) {
				int bytesRead = is.read(mybytearray, 0, mybytearray.length);
				int current = bytesRead;

				do {
					bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
					if (bytesRead > 0) {
						current += bytesRead;
					}
				} while (bytesRead > 0);

				bos.write(mybytearray, 0, current);
				bos.flush();
			}
		}
	}

}

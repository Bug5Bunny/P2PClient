package com.sombrainc.p2p.service.impl;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.sombrainc.p2p.controller.Controller;
import com.sombrainc.p2p.service.Service;
import com.sombrainc.p2p.util.Constant;

public class DownloadFileService implements Service {

	@Override
	public void execute() {
		new Controller().downloadMenu();
	}

	public static void downloadFile(final String ip, String fileName) throws IOException {
		try (Socket socket = new Socket(ip, Constant.CLIENTPORT);) {
			ObjectOutputStream socketOutput = new ObjectOutputStream(socket.getOutputStream());
			socketOutput.writeUTF(Constant.DOWNLOAD);
			socketOutput.writeUTF(fileName);
			socketOutput.flush();
			final InputStream is = socket.getInputStream();
			final ObjectInputStream in = new ObjectInputStream(is);
			final String msg = in.readUTF();
			System.out.println(msg);
			if (Constant.DOWNLOAD.equals(msg)) {
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
}

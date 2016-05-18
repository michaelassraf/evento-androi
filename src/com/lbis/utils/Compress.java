package com.lbis.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class Compress {
	private static final int BUFFER = 80000;
	final Logger log = Logger.getLogger(getClass().getSimpleName());

	public void zip(String[] files, String zipFile) {
		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(zipFile);

			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

			byte data[] = new byte[BUFFER];

			for (int i = 0; i < files.length; i++) {
				log.info("Goind to compress file " + files[i]);
				FileInputStream fi = new FileInputStream(files[i]);
				origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
			}

			out.close();
			log.info("Successfully compressed files");
		} catch (Exception e) {
			log.error("File compression failed", e);
			e.printStackTrace();
		}

	}

	public void zip(String fileAsAString, String zipFile) {
		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(zipFile);

			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

			byte data[] = new byte[BUFFER];
			log.info("Goind to compress file " + fileAsAString);
			InputStream fi = convertStringToInputStream(fileAsAString);
			origin = new BufferedInputStream(fi, BUFFER);
			ZipEntry entry = new ZipEntry(fileAsAString.substring(fileAsAString.lastIndexOf("/") + 1));
			out.putNextEntry(entry);
			int count;
			while ((count = origin.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private InputStream convertStringToInputStream(String string) {
		try {
			return new ByteArrayInputStream(string.getBytes());
		} catch (Exception e) {
			return null;
		}
	}

	public static void compress(File input, File output) throws IOException {
		FileInputStream fis = new FileInputStream(input);
		FileOutputStream fos = new FileOutputStream(output);
		GZIPOutputStream gzipStream = new GZIPOutputStream(fos);
		IOUtils.copy(fis, gzipStream);
		gzipStream.close();
		fis.close();
		fos.close();
	}

	public static void decompress(File input, File output) throws IOException {
		FileInputStream fis = new FileInputStream(input);
		FileOutputStream fos = new FileOutputStream(output);
		GZIPInputStream gzipStream = new GZIPInputStream(fis);
		IOUtils.copy(gzipStream, fos);
		gzipStream.close();
		fis.close();
		fos.close();
	}
}
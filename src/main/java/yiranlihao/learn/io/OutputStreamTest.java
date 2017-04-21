package yiranlihao.learn.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

public class OutputStreamTest {

	// ���ļ���д���ַ���
	@Test
	public void test1() throws IOException {
		String fileName = "D:" + File.separator + "hello.txt";
		File f = new File(fileName);
		OutputStream out = new FileOutputStream(f);
		String str = "Hello World";
		byte[] b = str.getBytes();
		out.write(b);
		out.close();
	}

	// ���ļ���һ���ֽ�һ���ֽڵ�д���ַ���
	@Test
	public void test2() throws IOException {
		String fileName = "D:" + File.separator + "hello.txt";
		File f = new File(fileName);
		OutputStream out = new FileOutputStream(f);
		String str = "Hello World����";
		byte[] b = str.getBytes();
		for (int i = 0; i < b.length; i++) {
			out.write(b[i]);
		}
		out.close();
	}

	@Test
	public void test3() throws IOException {
		String fileName = "D:" + File.separator + "hello.txt";
		File f = new File(fileName);
		OutputStream out = new FileOutputStream(f, true);// true��ʾ׷��ģʽ������Ϊ����
		String str = "Rollen";
		// String str="\r\nRollen"; ���Ի���
		byte[] b = str.getBytes();
		for (int i = 0; i < b.length; i++) {
			out.write(b[i]);
		}
		out.close();
	}

}

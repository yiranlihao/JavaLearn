package yiranlihao.learn.io;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import org.junit.Test;

public class InputStreamTest {

	@Test
	public void test1() throws IOException {

		String fileName = "D:" + File.separator + "hello.txt";
		File f = new File(fileName);
		InputStream in = new FileInputStream(f);
		byte[] b = new byte[1024];
		in.read(b);
		in.close();
		System.out.println(new String(b));
	}

	@Test
	public void test2() throws IOException {
		String fileName = "D:" + File.separator + "hello.txt";
		File f = new File(fileName);
		InputStream in = new FileInputStream(f);
		byte[] b = new byte[1024];
		int len = in.read(b);
		in.close();
		System.out.println("读入长度为：" + len);
		System.out.println(new String(b));// test1
		System.out.println(new String(b, 0, len));
	}

	@Test
	public void test3() throws IOException {
		String fileName = "D:" + File.separator + "hello.txt";
		File f = new File(fileName);
		InputStream in = new FileInputStream(f);
		byte[] b = new byte[(int) f.length()];
		in.read(b);
		System.out.println("文件长度为：" + f.length());
		in.close();
		System.out.println(new String(b));
	}

	@Test
	public void test4() throws IOException {
		String fileName = "D:" + File.separator + "hello.txt";
		File f = new File(fileName);
		InputStream in = new FileInputStream(f);
		byte[] b = new byte[(int) f.length()];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) in.read();
		}
		in.close();
		System.out.println(new String(b));
	}

	@Test
	public void test5() throws IOException {
		String fileName = "D:" + File.separator + "hello.txt";
		File f = new File(fileName);
		InputStream in = new FileInputStream(f);
		byte[] b = new byte[1024];
		int count = 0;
		int temp = 0;
		while ((temp = in.read()) != (-1)) {
			b[count++] = (byte) temp;
		}
		in.close();
		System.out.println(new String(b));
	}
	@SuppressWarnings("resource")
	@Test
	public void test6() throws IOException {
		File file = new File("d:" + File.separator + "hello.txt");
		DataInputStream input = new DataInputStream(new FileInputStream(file));
		char[] ch = new char[10];
		int count = 0;
		char temp;
		while ((temp = input.readChar()) != 'C') {
			ch[count++] = temp;
		}
		System.out.println(ch);
	}

	@Test
	public void test7() throws IOException {
		String str = "hello,rollenholt";
		PushbackInputStream push = null;
		ByteArrayInputStream bat = null;
		bat = new ByteArrayInputStream(str.getBytes());
		push = new PushbackInputStream(bat);
		int temp = 0;
		while ((temp = push.read()) != -1) {
			if (temp == ',') {
				push.unread(temp);
				temp = push.read();
				System.out.print("(回退" + (char) temp + ") ");
			} else {
				System.out.print((char) temp);
			}
		}
	}
}

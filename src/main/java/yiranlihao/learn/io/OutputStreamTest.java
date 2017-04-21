package yiranlihao.learn.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

public class OutputStreamTest {

	// 向文件中写入字符串
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

	// 向文件中一个字节一个字节的写入字符串
	@Test
	public void test2() throws IOException {
		String fileName = "D:" + File.separator + "hello.txt";
		File f = new File(fileName);
		OutputStream out = new FileOutputStream(f);
		String str = "Hello World！！";
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
		OutputStream out = new FileOutputStream(f, true);// true表示追加模式，否则为覆盖
		String str = "Rollen";
		// String str="\r\nRollen"; 可以换行
		byte[] b = str.getBytes();
		for (int i = 0; i < b.length; i++) {
			out.write(b[i]);
		}
		out.close();
	}

}

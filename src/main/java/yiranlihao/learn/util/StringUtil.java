package yiranlihao.learn.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ���������������ַ�����һЩʵ�ò���
 * 
 * @author Lihao
 * @Date 2017��4��25��
 * @Time 10:27:06
 * @version 1.0
 */
public class StringUtil {

	/**
	 * �����������ָ��ַ���
	 * 
	 * @param str
	 *            String ԭʼ�ַ���
	 * @param splitsign
	 *            String �ָ���
	 * @return String[] �ָ����ַ�������
	 */
	public static String[] split(String str, String splitsign) {
		int index;
		if (str == null || splitsign == null) {
			return null;
		}
		ArrayList<String> al = new ArrayList<String>();
		while ((index = str.indexOf(splitsign)) != -1) {
			al.add(str.substring(0, index));
			str = str.substring(index + splitsign.length());
		}
		al.add(str);
		return (String[]) al.toArray(new String[0]);
	}

	/**
	 * �����������滻�ַ���
	 * 
	 * @param from
	 *            String ԭʼ�ַ���
	 * @param to
	 *            String Ŀ���ַ���
	 * @param source
	 *            String ĸ�ַ���
	 * @return String �滻����ַ���
	 */
	public static String replace(String from, String to, String source) {
		if (source == null || from == null || to == null)
			return null;
		StringBuffer str = new StringBuffer("");
		int index = -1;
		while ((index = source.indexOf(from)) != -1) {
			str.append(source.substring(0, index) + to);
			source = source.substring(index + from.length());
			index = source.indexOf(from);
		}
		str.append(source);
		return str.toString();
	}

	/**
	 * �滻�ַ��������ܹ���HTMLҳ����ֱ����ʾ(�滻˫���ź�С�ں�)
	 * 
	 * @param str
	 *            String ԭʼ�ַ���
	 * @return String �滻����ַ���
	 */
	public static String htmlencode(String str) {
		if (str == null) {
			return null;
		}
		return replace("\"", "&quot;", replace("<", "&lt;", str));
	}

	/**
	 * �滻�ַ��������������ת����ԭʼ�루�滻��˫���ź�С�ںţ�
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String htmldecode(String str) {
		if (str == null) {
			return null;
		}
		return replace("&quot;", "\"", replace("&lt;", "<", str));
	}

	private static final String _BR = "<br/>";

	/**
	 * ������������ҳ����ֱ����ʾ�ı����ݣ��滻С�ںţ��ո񣬻س���TAB
	 * 
	 * @param str
	 *            String ԭʼ�ַ���
	 * @return String �滻����ַ���
	 */
	public static String htmlshow(String str) {
		if (str == null) {
			return null;
		}

		str = replace("<", "&lt;", str);
		str = replace(" ", "&nbsp;", str);
		str = replace("\r\n", _BR, str);
		str = replace("\n", _BR, str);
		str = replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;", str);
		return str;
	}

	/**
	 * ��������������ָ���ֽڳ��ȵ��ַ���
	 * 
	 * @param str
	 *            String �ַ���
	 * @param length
	 *            int ָ������
	 * @return String ���ص��ַ���
	 */
	public static String toLength(String str, int length) {
		if (str == null) {
			return null;
		}
		if (length <= 0) {
			return "";
		}
		try {
			if (str.getBytes("GBK").length <= length) {
				return str;
			}
		} catch (Exception e) {
		}
		StringBuffer buff = new StringBuffer();

		int index = 0;
		char c;
		length -= 3;
		while (length > 0) {
			c = str.charAt(index);
			if (c < 128) {
				length--;
			} else {
				length--;
				length--;
			}
			buff.append(c);
			index++;
		}
		buff.append("...");
		return buff.toString();
	}

	/**
	 * �����������ж��Ƿ�Ϊ����
	 * @param str ������ַ���
	 * @return ����������true,���򷵻�false
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
		return pattern.matcher(str).matches();
	}

	/**
	 * �ж��Ƿ�Ϊ������������double��float
	 * 
	 * @param str
	 *            ������ַ���
	 * @return �Ǹ���������true,���򷵻�false
	 */
	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?\\d+\\.\\d+$");
		return pattern.matcher(str).matches();
	}

	/**
	 * �ж��ǲ��ǺϷ��ַ� c Ҫ�жϵ��ַ�
	 */
	public static boolean isLetter(String str) {
		if (str == null || str.length() < 0) {
			return false;
		}
		Pattern pattern = Pattern.compile("[\\w\\.-_]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * ��ָ�����ַ�������ȡEmail content ָ�����ַ���
	 * @param content
	 * @return
	 */
	public static String parse(String content) {
		String email = null;
		if (content == null || content.length() < 1) {
			return email;
		}
		// �ҳ�����@
		int beginPos;
		int i;
		String token = "@";
		String preHalf = "";
		String sufHalf = "";

		beginPos = content.indexOf(token);
		if (beginPos > -1) {
			// ǰ��ɨ��
			String s = null;
			i = beginPos;
			while (i > 0) {
				s = content.substring(i - 1, i);
				if (isLetter(s))
					preHalf = s + preHalf;
				else
					break;
				i--;
			}
			// ����ɨ��
			i = beginPos + 1;
			while (i < content.length()) {
				s = content.substring(i, i + 1);
				if (isLetter(s))
					sufHalf = sufHalf + s;
				else
					break;
				i++;
			}
			// �жϺϷ���
			email = preHalf + "@" + sufHalf;
			if (isEmail(email)) {
				return email;
			}
		}
		return null;
	}

	/**
	 * �����������ж�������ַ����Ƿ����Email��ʽ.
	 * @param str ������ַ���
	 * @return ��Email��ʽ����true,���򷵻�false
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.length() < 1 || email.length() > 256) {
			return false;
		}
		Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		return pattern.matcher(email).matches();
	}

	/**
	 * �����������ж�������ַ����Ƿ�Ϊ������
	 * @param str ������ַ���
	 * @return ����Ǵ����ַ���true,���򷵻�false
	 */
	public static boolean isChinese(String str) {
		Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
		return pattern.matcher(str).matches();
	}

	/**
	 * �����������Ƿ�Ϊ�հ�,����null��""
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * �����������ж��Ƿ�Ϊ����
	 * @param x
	 * @return
	 */
	public static boolean isPrime(int x) {
		if (x <= 7) {
			if (x == 2 || x == 3 || x == 5 || x == 7)
				return true;
		}
		int c = 7;
		if (x % 2 == 0)
			return false;
		if (x % 3 == 0)
			return false;
		if (x % 5 == 0)
			return false;
		int end = (int) Math.sqrt(x);
		while (c <= end) {
			if (x % c == 0) {
				return false;
			}
			c += 4;
			if (x % c == 0) {
				return false;
			}
			c += 2;
			if (x % c == 0) {
				return false;
			}
			c += 4;
			if (x % c == 0) {
				return false;
			}
			c += 2;
			if (x % c == 0) {
				return false;
			}
			c += 4;
			if (x % c == 0) {
				return false;
			}
			c += 6;
			if (x % c == 0) {
				return false;
			}
			c += 2;
			if (x % c == 0) {
				return false;
			}
			c += 6;
		}
		return true;
	}

	/**
	 * ���������������ת�ɴ�д
	 * @param str �����ַ���
	 * @return String �����ת���ɴ�д����ַ���
	 */
	public static String hangeToBig(String str) {
		double value;
		try {
			value = Double.parseDouble(str.trim());
		} catch (Exception e) {
			return null;
		}
		char[] hunit = { 'ʰ', '��', 'Ǫ' }; // ����λ�ñ�ʾ
		char[] vunit = { '��', '��' }; // ������ʾ
		char[] digit = { '��', 'Ҽ', '��', '��', '��', '��', '½', '��', '��', '��' }; // ���ֱ�ʾ
		long midVal = (long) (value * 100); // ת��������
		String valStr = String.valueOf(midVal); // ת�����ַ���

		String head = valStr.substring(0, valStr.length() - 2); // ȡ��������
		String rail = valStr.substring(valStr.length() - 2); // ȡС������

		String prefix = ""; // ��������ת���Ľ��
		String suffix = ""; // С������ת���Ľ��
		// ����С����������
		if (rail.equals("00")) { // ���С������Ϊ0
			suffix = "��";
		} else {
			suffix = digit[rail.charAt(0) - '0'] + "��" + digit[rail.charAt(1) - '0'] + "��"; // ����ѽǷ�ת������
		}
		// ����С����ǰ�����
		char[] chDig = head.toCharArray(); // ����������ת�����ַ�����
		char zero = '0'; // ��־'0'��ʾ���ֹ�0
		byte zeroSerNum = 0; // ��������0�Ĵ���
		for (int i = 0; i < chDig.length; i++) { // ѭ������ÿ������
			int idx = (chDig.length - i - 1) % 4; // ȡ����λ��
			int vidx = (chDig.length - i - 1) / 4; // ȡ��λ��
			if (chDig[i] == '0') { // �����ǰ�ַ���0
				zeroSerNum++; // ����0��������
				if (zero == '0') { // ��־
					zero = digit[0];
				} else if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
					prefix += vunit[vidx - 1];
					zero = '0';
				}
				continue;
			}
			zeroSerNum = 0; // ����0��������
			if (zero != '0') { // �����־��Ϊ0,�����,������,��ʲô��
				prefix += zero;
				zero = '0';
			}
			prefix += digit[chDig[i] - '0']; // ת�������ֱ�ʾ
			if (idx > 0)
				prefix += hunit[idx - 1];
			if (idx == 0 && vidx > 0) {
				prefix += vunit[vidx - 1]; // �ν���λ��Ӧ�ü��϶�������,��
			}
		}

		if (prefix.length() > 0)
			prefix += 'Բ'; // ����������ִ���,����Բ������
		return prefix + suffix; // ������ȷ��ʾ
	}

	/**
	 * ����������ȥ���ַ������ظ������ַ���
	 * @param str ԭ�ַ�������������ַ������ÿո�����Ա�ʾ���ַ���
	 * @return String ����ȥ���ظ����ַ�������ַ���
	 */
	private static String removeSameString(String str) {
		Set<String> mLinkedSet = new LinkedHashSet<String>();// set���ϵ����������Ӽ��������ظ�
		String[] strArray = str.split(" ");// ���ݿո�(������ʽ)�ָ��ַ���
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < strArray.length; i++) {
			if (!mLinkedSet.contains(strArray[i])) {
				mLinkedSet.add(strArray[i]);
				sb.append(strArray[i] + " ");
			}
		}
		System.out.println(mLinkedSet);
		return sb.toString();
	}

	/**
	 * �������������������ַ�
	 * @param src
	 * @return
	 */
	public static String encoding(String src) {
		if (src == null)
			return "";
		StringBuilder result = new StringBuilder();
		if (src != null) {
			src = src.trim();
			for (int pos = 0; pos < src.length(); pos++) {
				switch (src.charAt(pos)) {
				case '\"':
					result.append("&quot;");
					break;
				case '<':
					result.append("&lt;");
					break;
				case '>':
					result.append("&gt;");
					break;
				case '\'':
					result.append("&apos;");
					break;
				case '&':
					result.append("&amp;");
					break;
				case '%':
					result.append("&pc;");
					break;
				case '_':
					result.append("&ul;");
					break;
				case '#':
					result.append("&shap;");
					break;
				case '?':
					result.append("&ques;");
					break;
				default:
					result.append(src.charAt(pos));
					break;
				}
			}
		}
		return result.toString();
	}

	/**
	 * �����������ж��ǲ��ǺϷ����ֻ�����
	 * @param handset
	 * @return boolean
	 */
	public static boolean isHandset(String handset) {
		try {
			String regex = "^1[\\d]{10}$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(handset);
			return matcher.matches();

		} catch (RuntimeException e) {
			return false;
		}
	}

	/**
	 * ���������������������ַ�
	 * @param src
	 * @return
	 */
	public static String decoding(String src) {
		if (src == null)
			return "";
		String result = src;
		result = result.replace("&quot;", "\"").replace("&apos;", "\'");
		result = result.replace("&lt;", "<").replace("&gt;", ">");
		result = result.replace("&amp;", "&");
		result = result.replace("&pc;", "%").replace("&ul", "_");
		result = result.replace("&shap;", "#").replace("&ques", "?");
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// String source = "abcdefgabcdefgabcdefgabcdefgabcdefgabcdefg";
		// String from = "efg";
		// String to = "������";
		// System.out.println("���ַ���source�У���to�滻from���滻���Ϊ��"
		// + replace(from, to, source));
		// System.out.println("����ָ���ֽڳ��ȵ��ַ�����"
		// + toLength("abcdefgabcdefgabcdefgabcdefgabcdefgabcdefg", 9));
		// System.out.println("�ж��Ƿ�Ϊ������" + isInteger("+0"));
		// System.out.println("�ж��Ƿ�Ϊ������������double��float��" + isDouble("+0.36"));
		// System.out.println("�ж�������ַ����Ƿ����Email��ʽ��" +
		// isEmail("fhwbj@163.com"));
		// System.out.println("�ж�������ַ����Ƿ�Ϊ�����֣�" + isChinese("��ã�"));
		// System.out.println("�ж�����������Ƿ���������" + isPrime(12));
//		System.out.println("�����ת���ɴ�д��" + hangeToBig("10019658"));
		System.out.println("ȥ���ַ������ظ������ַ�����" + removeSameString("100 100 9658"));
		// System.out.println("���������ַ���" + encoding("100\"s<>fdsd100 9658"));
		// System.out.println("�ж��ǲ��ǺϷ����ֻ����룺" + isHandset("15981807340"));

		// System.out.println("���ַ�����ȡֵEmail��" + parse("159818 fwhbj@163.com
		// 07340"));
	}
}

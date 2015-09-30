package j.wu.charset;


public class StringSize {
	public static void main(String str[]) {

		getStringSizeTest();
	}

	/**
	 * java默认编码下显示中英文字符个数
	 * 
	 * @param str
	 * @return
	 */
	public static int getStringSize(String str) {
		System.out.println(str + " = " + str.getBytes().length);
		return str.getBytes().length;
	}

	public static void getStringSizeTest() {
		getStringSize("123456");
		getStringSize(",./<>?';:");
		getStringSize("中国");
		getStringSize("中国123");
		getStringSize("中国@.");
	}
}

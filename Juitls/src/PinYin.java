import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYin {

	public static void main(String[]args){
		PinYin py=new PinYin();
		String s="王俊续abcABC";
		System.out.println(getPinYin(s));
	}

	/** * 将字符串中的中文转化为拼音,其他字符不变 * @param inputString * @return */
	public static String getPinYin(String inputString) {
		if (inputString == null && inputString.trim().equalsIgnoreCase("")) {
			return null;
		}

		// 设置汉字拼音输出的格式
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 大小写设置，默认小写
		format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);// 音标方式
		// 不知道这个是什么意思 写完查api看看
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		//
		char[] input = inputString.trim().toCharArray();
		String output = "";
		try {
			char c;
			for (int i = 0; i < input.length; i++) {
				c=input[i];
				// 是中文或者a-z或者A-Z转换拼音(我的需求，是保留中文或者a-z或者A-Z)
				if (java.lang.Character.toString(c).matches(
						"[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							c, format);//有可能是多音字
					output += temp[0];//使用第一个
				} 
//				else if (((int) c >= 65 && (int) c <= 90)
//						|| ((int) c >= 97 && (int) c <= 122)) {
//					output += new String[] { String.valueOf(c) };
//				} 
				else {
					output += c;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	/** * 获取汉字串拼音首字母，英文字符不变 * @param chinese 汉字串 * @return 汉语拼音首字母 */
	public static String getFirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							arr[i], defaultFormat);
					if (temp != null) {
						pybf.append(temp[0].charAt(0));
					} else {
						pybf.append(arr[i]);
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}

	/**
	 * * 获取汉字串拼音，英文字符不变 * @param chinese 汉字串 * @return 汉语拼音
	 * */
	public static String getFullSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		try {
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] > 128) {
					pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i],
							defaultFormat)[0]);
				} else {
					pybf.append(arr[i]);
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return pybf.toString();
	}
}

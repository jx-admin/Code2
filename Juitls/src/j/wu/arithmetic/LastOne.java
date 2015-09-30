package j.wu.arithmetic;
import java.util.ArrayList;
import java.util.List;

/**
 * @author junxu.wang
 *
 * @date : 2015年3月25日 下午12:42:09
 * 
 * 数七数列
 * 
 */
public class LastOne {
	public static void main(String[] args) {
		lastOneTest();
	}

	public static void lastOneTest() {
		for (int i = 1; i <= 10; i++) {
			System.out.println(getLastOne(1, 3, getData(i)));
		}
	}

	/**
	 * <pre>
	 * 序列数组生成
	 * @param count
	 * @return
	 * </pre>
	 */
	public static List getData(int count) {
		List data = new ArrayList(count);
		for (int i = 0; i < count;) {
			data.add(++i);
		}
		return data;
	}

	/**
	 * <pre>
	 * 数N(7)算法
	 * @param start 起始数
	 * @param key N（7）遇到次数或遇到次数的倍数条件成立
	 * @param data 被数数的一组数据
	 * @return 最后剩下的一个数
	 * </pre>
	 */
	public static int getLastOne(int start, int key, List data) {
		int result = -1;
		int current = start;
		int pos = 0;
		if (data != null && data.size() > 0) {
			System.out.println(data.toString());
			while (data.size() > 1) {
				if (current != 0
						&& (current % key == 0 || isContent(current, key))) {
					pos = pos % data.size();
					System.out.print((int) data.get(pos) + ",");
					data.remove(pos);
				} else {
					++pos;
				}
				++current;
			}
			result = (int) data.get(0);
		}
		return result;
	}

	/**
	 * <pre>
	 * 数字包含
	 * @param src 一串数字
	 * @param num 被包含的数
	 * @return 包含 true 否则false
	 * </pre>
	 */
	public static boolean isContent(int src, int num) {
		boolean result = Integer.toString(src).contains(Integer.toString(num));
		return result;
	}
}

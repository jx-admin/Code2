
import java.util.Stack;
//http://www.cnblogs.com/dolphin0520/p/3708602.html
/**表达式求值是程序设计语言编译中的一个基本问题。它的实现就是对“栈”的典型应用。本文针对表达式求值使用的是最简单直观的算法“算符优先法”。

我们都知道算术四则运算的运算规则是：
先乘除，后加减。

从左到右计算

先算括号内，再算括号外

表达式组成
任何一个表达式都有操作数、运算符和界定符组成。

操作数即可以是常量，也可以是被说明为变量或常量的标识符。

运算符可以分为算术运算，关系运算和逻辑运算符。

界定符有左右括号和结束符等。

本文为了方便演示只使用算术运算。

运算符优先级
对于连个相继出现的操作符θ1和θ2 有三种关系：大于、等于和小于。由此可以列出“+,-,*,/”之间的优先级。如下表：

 	+	-	*	/	(	)	#
+	>	>	<	<	<	>	>
-	>	>	<	<	<	>	>
*	>	>	>	>	<	>	>
/	>	>	>	>	<	>	>
(	<	<	<	<	<	=	 
)	>	>	>	>	 	>	>
#	<	<	<	<	<	 	=
 

加减乘除优先性都低于“（”但是高于“）”，由运算从左到右可知，当θ1=θ2 ，令θ1>θ2

为了算法简洁，在表达式的左边和右边虚设一个“#”，这一对“#”表示一个表达式求值完成。

“（”=“）”当一对括号相遇时表示括号内已运算完成。

“）”和“（”、“#”和“(”、“(”和“#”无法相继出现如果出现则表达式出现语法错误。

为实现优先算法，可以使用两个工作栈，一个是OPTR，用于寄存运算符，一个是OPND，用于寄存运算数和运算结果。

算法基本思路。
首先置操作数栈为空栈，表达式起始符为“#”为栈底元素。

依次读入表达式中的每个字符，若是操作数则进OPND栈，若是运算符则和OPTR栈的栈顶运算符比较优先权作相应操作，直至整个表达式求值完毕（OPTR栈顶元素和当前读入的字符均为“#”）
*/
/*经典算法-算术表达式求值
析用堆栈解析算术表达式的基本方法。给出的示例代码能解析任何包括+，-，*，/，()和0到9数字组成的算术表达式。

2 中缀表达式和后缀表达式

中缀表达式就是通常所说的算术表达式，比如(1+2)*3-4。

后缀表达式是指通过解析后，运算符在运算数之后的表达式，比如上式解析成后缀表达式就是12+3*4-。这种表达式可以直接利用栈来求解。

3 运算符的优先级

优先级 运算符 
1 括号() 
2 负号- 
3 乘方** 
4 乘*，除/，求余% 
5 加+，减- 
6 小于<，小于等于<=，大于>，大于等于>= 
7 等于==，不等于!= 
8 逻辑与&& 
9 逻辑或|| 
大致的规律是，一元运算符 > 二元运算符 > 多元运算符。
*/
/*
中缀表达式翻译成后缀表达式的方法如下：
（1）从右向左依次取得数据ch。
（2）如果ch是操作数，直接输出。
（3）如果ch是运算符（含左右括号），则：
      a：如果ch = '('，放入堆栈。
      b：如果ch = ')'，依次输出堆栈中的运算符，直到遇到'('为止。
      c：如果ch不是')'或者'('，那么就和堆栈顶点位置的运算符top做优先级比较。
          1：如果ch优先级比top高，那么将ch放入堆栈。
          2：如果ch优先级低于或者等于top，那么输出top，然后将ch放入堆栈。
（4）如果表达式已经读取完成，而堆栈中还有运算符时，依次由顶端输出。
如果我们有表达式(A-B)*C+D-E/F，要翻译成后缀表达式，并且把后缀表达式存储在一个名叫output的字符串中，可以用下面的步骤。

（1）读取'('，压入堆栈，output为空
（2）读取A，是运算数，直接输出到output字符串，output = A
（3）读取'-'，此时栈里面只有一个'('，因此将'-'压入栈，output = A
（4）读取B，是运算数，直接输出到output字符串，output = AB
（5）读取')'，这时候依次输出栈里面的运算符'-'，然后就是'('，直接弹出，output = AB-
（6）读取'*'，是运算符，由于此时栈为空，因此直接压入栈，output = AB-
（7）读取C，是运算数，直接输出到output字符串，output = AB-C
（8）读取'+'，是运算符，它的优先级比'*'低，那么弹出'*'，压入'+"，output = AB-C*
（9）读取D，是运算数，直接输出到output字符串，output = AB-C*D
（10）读取'-'，是运算符，和'+'的优先级一样，因此弹出'+'，然后压入'-'，output = AB-C*D+
（11）读取E，是运算数，直接输出到output字符串，output = AB-C*D+E
（12）读取'/'，是运算符，比'-'的优先级高，因此压入栈，output = AB-C*D+E
（13）读取F，是运算数，直接输出到output字符串，output = AB-C*D+EF
（14）原始字符串已经读取完毕，将栈里面剩余的运算符依次弹出，output = AB-C*D+EF/-
*/
public class Calc {
	private static int OPSET[] = new int[] { '+', '-', '*', '/', '(', ')', '#', '^' };
	private static int Prior[][] = new int[][] { // 运算符优先级�?
			                  // '+'  '-'  '*'  '/'  '('  ')'  '#'  '^'
			/* '+' */new int[] { '>', '>', '<', '<', '<', '>', '>', '<' },
			/* '-' */new int[] { '>', '>', '<', '<', '<', '>', '>', '<' },
			/* '*' */new int[] { '>', '>', '>', '>', '<', '>', '>', '<' },
			/* '/' */new int[] { '>', '>', '>', '>', '<', '>', '>', '<' },
			/* '(' */new int[] { '<', '<', '<', '<', '<', '=', ' ', '<' },
			/* ')' */new int[] { '>', '>', '>', '>', ' ', '>', '>', '>' },
			/* '#' */new int[] { '<', '<', '<', '<', '<', ' ', '=', '<' },
			/* '^' */new int[] { '>', '>', '>', '>', '<', '>', '>', '>' } };
	// StackChar类型的结点SC
	Stack<Integer> charList = new Stack<Integer>();
	// StackFloat类型的结点SF
	Stack<Float> floatList = new Stack<Float>();

	float operate(float a, int theta, float b) // 计算函数Operate
	{
		switch (theta) {
		case '+':
			return a + b;
		case '-':
			return a - b;
		case '*':
			return a * b;
		case '/':
			return a / b;
		case '^':
			return (float) Math.pow(a, b);
		default:
			return 0;
		}
	}

	boolean in(int test, int[] chars) {
		for (int c : chars) {
			if (test == c) {
				return true;
			}
		}
		return false;
	}

	int returnOpOrd(int op, int[] chars) {
		int size = chars.length;
		for (int i = 0; i < size; i++) {
			if (op == chars[i]) {
				return i;
			}
		}
		return 0;
	}

	int precede(int Aop, int Bop) {
		return Prior[returnOpOrd(Aop, OPSET)][returnOpOrd(Bop, OPSET)];
	}

	/**
	 * <pre>
	 * @param MyExpression
	 * @return
	 * </pre>
	 */
	float EvaluateExpression(String MyExpression) {
		// 算术表达式求值的算符优先算法
		// 设OPTR和OPND分别为运算符栈和运算数栈，OP为运算符集合
		// SC *OPTR = NULL; // 运算符栈，字符元�?
		// SF *OPND = NULL; // 运算数栈，实数元�?
		int theta;
		float a, b;
		MyExpression+="#";
		int size = MyExpression.length();
		int i = 0;
		StringBuilder dataSb = new StringBuilder();
		int c ;
		charList.clear();
		charList.push((int) '#');
		while (i < size || !charList.isEmpty()) {
			c=MyExpression.charAt(i);
			print("c= "+(char)c);
			if (!in(c, OPSET)) {
				dataSb.append((char)c); // 字符串连接函�?
				i++;
				c = MyExpression.charAt(i);
				if (in(c, OPSET)) {
					try {
						float d = Float.parseFloat(dataSb.toString()); // 字符串转换函�?double)
						// OPND = Push(OPND, Data);
						floatList.push(d);
						print("push= "+d);
					} catch (Exception e) {
						print("parse data exception");
					}
					dataSb.delete(0, dataSb.length());
				}
			} else // 不是运算符则进栈
			{
				int lastC = charList.peek();
				print("peed lastC="+(char)lastC);
				int pn = (char) precede((char) lastC, (char) c);
				print("pro = "+(char)pn);
				switch (pn) {
				case '<': // 栈顶元素优先级低
					// OPTR = Push(OPTR, *c);
					charList.push(c);
					print("push c = "+(char)c);
					// c++;
					i++;
					break;
				case '=': // 脱括号并接收下一字符
					// OPTR = Pop(OPTR);
					int cp=charList.pop();
					print("pop c = "+cp);
					// c++;
					i++;
					break;
				case '>': // �?��并将运算结果入栈
					// theta = OPTR->c;
					// OPTR = Pop(OPTR);
					theta = charList.pop();
					print("pop theta = "+(char)theta);
					// b = OPND->f;
					// OPND = Pop(OPND);
					b = floatList.pop();
					print("pop b = "+b);
					// a = OPND->f;
					// OPND = Pop(OPND);
					a = floatList.pop();
					print("pop a = "+a);
					// OPND = Push(OPND, Operate(a, theta, b));
					float r = operate(a, theta, b);
					floatList.push(r);
					print("push r = "+r);
					break;
				} // switch
			}
		} // while
		return floatList.peek();
	} // EvaluateExpression

	public static void main(String[] args) {
//		String expression = "3+4";
		//(A-B)*C+D-E/F
//		String expression = "(1+2)*3+4-10/2";
//		String expression = "(1/22+33*4+(3+4)*4/2)*3+4-100/2";
		String expression = "((3+5*2)+3)/5+(-6)/4*2+3";
//		print("请输入表达式:");
		float r=new Calc().EvaluateExpression(expression);
//		print("该表达式的结果");
		System.out.printf("%s=%g\n", expression,
				r);
	}

	public static void print(String msg) {
		System.out.println(msg);
	}
}

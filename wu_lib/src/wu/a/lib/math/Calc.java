package wu.a.lib.math;

import java.util.Stack;

public class Calc {

private static int Prior[][] =new int[][] { // 运算符优先级表
							  // '+' '-' '*' '/' '(' ')' '#' '^'
		/*'+'*/new int[]{'>', '>', '<', '<', '<', '>', '>', '<'},
		/*'-'*/new int[]{'>', '>', '<', '<', '<', '>', '>', '<'},
		/*'*'*/new int[]{'>', '>', '>', '>', '<', '>', '>', '<'},
		/*'/'*/new int[]{'>', '>', '>', '>', '<', '>', '>', '<'},
		/*'('*/new int[]{'<', '<', '<', '<', '<', '=', ' ', '<'},
		/*')'*/new int[]{'>', '>', '>', '>', ' ', '>', '>', '>'},
		/*'#'*/new int[]{'<', '<', '<', '<', '<', ' ', '=', '<'},
		/*'^'*/new int[]{'>', '>', '>', '>', '<', '>', '>', '>'} };
//StackChar类型的结点SC
Stack<Integer> charList=new Stack<Integer>();
//StackFloat类型的结点SF
Stack<Float> floatList=new Stack<Float>();

float Operate(float a, int theta, float b)      //计算函数Operate
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
int OPSET[] =new int[] { '+', '-', '*', '/', '(', ')', '#', '^' };
boolean in(int test,int[]chars){
	for(int c:chars){
		if(test==c){
			return true;
		}
	}
	return false;
}
int returnOpOrd(int op,int[]chars){
	int size=chars.length;
	for(int i=0;i<size;i++){
		if(op==chars[i]){
			return i;
		}
	}
	return 0;
}
int precede(int Aop, int Bop) {
	return Prior[returnOpOrd(Aop, OPSET)][returnOpOrd(Bop, OPSET)];
}
float EvaluateExpression(String MyExpression) {
	// 算术表达式求值的算符优先算法
	// 设OPTR和OPND分别为运算符栈和运算数栈，OP为运算符集合
//	SC *OPTR = NULL;       // 运算符栈，字符元素
//	SF *OPND = NULL;       // 运算数栈，实数元素
//	char TempData[20];
//	float Data, a, b;
//	char theta, *c, Dr[] = { '#', '\0' };
//	OPTR = Push(OPTR, '#');
//	c = strcat(MyExpression, Dr);
//	strcpy(TempData, "\0");       //字符串拷贝函数
	int  theta;
	float a,b;
	int size=MyExpression.length();
	int i=0;
	StringBuilder dataSb=new StringBuilder();
	int c=MyExpression.charAt(i);
	while (i<size || !charList.isEmpty()) {
		if (!in(c, OPSET)) {
			dataSb.append(c);          //字符串连接函数
			i++;
			c=MyExpression.charAt(i);
			if (in(c, OPSET)) {
				try{
				float d=Float.parseFloat(dataSb.toString());       //字符串转换函数(double)
//				OPND = Push(OPND, Data);
				floatList.push(d);}catch(Exception e){
					print("parse data exception");
				}
				dataSb.delete(0, dataSb.length()-1);
//				strcpy(TempData, "\0");
			}
		} else    // 不是运算符则进栈
		{
			int lastC=charList.peek();
			int pn=(char) precede((char)lastC, (char)c);
			switch (pn){
			case '<': // 栈顶元素优先级低
//				OPTR = Push(OPTR, *c);
				charList.push(c);
//				c++;
				i++;
				break;
			case '=': // 脱括号并接收下一字符
//				OPTR = Pop(OPTR);
				charList.pop();
//				c++;
				i++;
				break;
			case '>': // 退栈并将运算结果入栈
//				theta = OPTR->c;
//				OPTR = Pop(OPTR);
				theta=charList.pop();
//				b = OPND->f;
//				OPND = Pop(OPND);
				b=floatList.pop();
//				a = OPND->f;
//				OPND = Pop(OPND);
				a=floatList.pop();
//				OPND = Push(OPND, Operate(a, theta, b));
				float r=Operate(a, theta, b);
				floatList.push(r);
				break;
			} //switch
		}
	} //while
	return floatList.peek();
} //EvaluateExpression
public static void main(String[]args) {
	String expression="3+4";
	print("请输入表达式:");
	print("该表达式的值为:");
	System.out.printf("%s\b=%g\n", expression, new Calc().EvaluateExpression(expression));
}
public static void print(String msg){
	System.out.println(msg);
}
}

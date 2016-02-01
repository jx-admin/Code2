///*2014.5.6 测试环境: mingw*/
//#include <iostream>
//#include <vector>
//#include <stack>
//#include <string>
//#include <cstdlib>
//#include <cctype>
//#include <cstring>
//using namespace std;
//
//vector<string> preParse(char *str)   //对中缀表达式进行预处理，分离出每个token
//		{
//	vector<string> tokens;
//	int len = strlen(str);
//	char *p = (char *) malloc((len + 1) * sizeof(char)); //注意不要用 char *p = (char *)malloc(sizeof(str))来申请空间
//	int i = 0, j = 0;
//	while (i < len)          //去除表达式中的空格
//	{
//		if (str[i] == ' ') {
//			i++;
//			continue;
//		}
//		p[j++] = str[i++];
//	}
//	p[j] = '\0';
//	j = 0;
//	len = strlen(p);
//	while (j < len) {
//		char temp[2];
//		string token;
//		switch (p[j]) {
//		case '+':
//		case '*':
//		case '/':
//		case '(':
//		case ')': {
//			temp[0] = p[j];
//			temp[1] = '\0';
//			token = temp;
//			tokens.push_back(token);
//			break;
//		}
//		case '-': {
//			if (p[j - 1] == ')' || isdigit(p[j - 1]))  //作为减号使用
//					{
//				temp[0] = p[j];
//				temp[1] = '\0';
//				token = temp;
//				tokens.push_back(token);
//			} else    //作为负号使用
//			{
//				temp[0] = '#';
//				temp[1] = '\0';
//				token = temp;
//				tokens.push_back(token);
//			}
//			break;
//		}
//		default:     //是数字
//		{
//			i = j;
//			while (isdigit(p[i]) && i < len) {
//				i++;
//			}
//			char *opd = (char *) malloc(i - j + 1);
//			strncpy(opd, p + j, i - j);
//			opd[i - j] = '\0';
//			token = opd;
//			tokens.push_back(token);
//			j = i - 1;
//			free(opd);
//			break;
//		}
//		}
//		j++;
//	}
//	free(p);
//	return tokens;
//}
//
//int getPriority(string opt) {
//	int priority;
//	if (opt == "#")
//		priority = 3;
//	else if (opt == "*" || opt == "/")
//		priority = 2;
//	else if (opt == "+" || opt == "-")
//		priority = 1;
//	else if (opt == "(")
//		priority = 0;
//	return priority;
//}
//
//vector<string> toSuffix(char *str)  //转变为后缀形式
//		{
//	vector<string> tokens = preParse(str);
//	int i = 0;
//	int size = tokens.size();
//
//	vector<string> suffix;     //存储后缀表达式
//	stack<string> optStack;   //存储操作符
//
//	for (i = 0; i < size; i++) {
//		string token = tokens[i];
//		if (token == "#" || token == "+" || token == "-" || token == "*"
//				|| token == "/") {
//			if (optStack.size() == 0)   //如果操作符栈为空
//					{
//				optStack.push(token);
//			} else {
//				int tokenPriority = getPriority(token);
//				string topOpt = optStack.top();
//				int topOptPriority = getPriority(topOpt);
//				if (tokenPriority > topOptPriority) {
//					optStack.push(token);
//				} else {
//					while (tokenPriority <= topOptPriority) {
//						optStack.pop();
//						suffix.push_back(topOpt);
//						if (optStack.size() > 0) {
//							topOpt = optStack.top();
//							topOptPriority = getPriority(topOpt);
//						} else
//							break;
//
//					}
//					optStack.push(token);
//				}
//			}
//		} else if (token == "(") {
//			optStack.push(token);
//		} else if (token == ")") {
//			while (optStack.top() != "(") {
//				string topOpt = optStack.top();
//				suffix.push_back(topOpt);
//				optStack.pop();
//			}
//			optStack.pop();
//		} else   //如果是操作数，直接入操作数栈
//		{
//			suffix.push_back(token);
//		}
//	}
//	while (optStack.size() != 0) {
//		string topOpt = optStack.top();
//		suffix.push_back(topOpt);
//		optStack.pop();
//	}
//	return suffix;
//}
//
//int evalRPN(vector<string> &tokens) {
//
//        int result = 0;
//        int i;
//        stack<int> opd;         //存储操作数
//        int size = tokens.size();
//        for(i=0;i<size;i++)
//        {
//            if(tokens[i]=="*")
//            {
//                int rOpd = opd.top();   //右操作数
//                opd.pop();
//                int lOpd = opd.top();  //左操作数
//                opd.pop();
//                result = lOpd*rOpd;
//                opd.push(result);
//            }
//            else if(tokens[i]=="/")
//            {
//                int rOpd = opd.top();
//                opd.pop();
//                int lOpd = opd.top();
//                opd.pop();
//                result = lOpd/rOpd;
//                opd.push(result);
//            }
//            else if(tokens[i]=="+")
//            {
//                int rOpd = opd.top();
//                opd.pop();
//                int lOpd = opd.top();
//                opd.pop();
//                result = lOpd+rOpd;
//                opd.push(result);
//            }
//            else if(tokens[i]=="-")
//            {
//                int rOpd = opd.top();
//                opd.pop();
//                int lOpd = opd.top();
//                opd.pop();
//                result = lOpd-rOpd;
//                opd.push(result);
//            }
//            else
//            {
//                opd.push(atoi(tokens[i].c_str()));
//            }
//        }
//        return opd.top();
//    }
//
//int main(int argc, char *argv[]) {
//	char *str = "((3+5*2)+3)/5+(-6)/4*2+3";
//	vector<string> suffix = toSuffix(str);
//	int size = suffix.size();
//	for (int i = 0; i < size; i++)
//		cout << suffix[i] << " ";
//	cout << endl;
//	cout <<evalRPN(suffix);
//	return 0;
//}

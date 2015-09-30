//============================================================================
// Name        : n.cpp
// Author      : wu
// Version     :
// Copyright   : 2014 copyright
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <iostream>
using namespace std;

//int main() {
//	cout << "Hello World!!!" << endl; // prints Hello World!!!
//	return 0;
//}
//int main(){
//	int c=33;
//	int *x;
//
////	x=&c;
////	cout << x <<" x"<< endl; // prints Hello World!!!
//	*x=1;
//	cout << *x <<" *x"<< endl; // prints Hello World!!!
//	return 0;
//}
char * foobar(char *s){
//	*s='\0';
	s[1]='1';
	s[2]='2';
	s[3]='3';
//	printf(s);
	cout << s<< endl;
}

int main(){
	char s4[]="abcdef";
	foobar(s4);
}


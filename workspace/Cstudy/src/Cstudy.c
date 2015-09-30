/*
 ============================================================================
 Name        : Cstudy.c
 Author      : junxu
 Version     :
 Copyright   : Copyright 2014 by junxu
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>

int main(void) {
	setbuf(stdout, NULL); /*取消，默认输出流存在buffer机制*/
	char cstudyMenu();
	void SortMain();
	char type=0;
		while ((type=cstudyMenu())!='q') {
			switch (type) {
			case '0':
				return EXIT_SUCCESS;
			case '1':
				StudentManagerSystemMain();
				break;
			case '2':
				SortMain();
				break;
			}
		}
	return EXIT_SUCCESS;
}

/*菜单*/
char cstudyMenu() {
	char type[10];
	printf("C langauge study !!\n");
	printf("Student Manager System demo(1)\n");
	printf("Sort(2)\n");

	printf("exit(q)\n");
	gets(type);
	return type[0];
}

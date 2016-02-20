///*
// * Midlle2L.c
// *中缀转前缀
// *  Created on: 2016年2月20日
// *      Author: junxu.wang
// */
//#include <iostream>
//#include <string>
//#include <cstdlib>
//#include <cctype>
//#include <cstring>
//#include <stdio.h>
//#include <stdlib.h>
//#include <string.h>
//#define N 100
//using namespace std;
//
//typedef struct Node {
//	char key;
//	struct Node * left;
//	struct Node * right;
//} Node;
////search for the operator with the highest grade in a
//int search(char a[], int begin, int end) {
//	int tag = -1;
//	int isInBrackets = 0;
//	if (a[begin] == '(' && a[end] == ')') {
//		begin += 1;
//		end -= 1;
//	}
//	int amExist = 0;
//	int bmExist = 0;
//	int i;
//	for (i = begin; i <= end; i++) {
//		if (a[i] == '(')
//			isInBrackets++;
//		else if (a[i] == ')')
//			isInBrackets--;
//		else if ((a[i] == '+' || a[i] == '-') && isInBrackets == 0) {
//			tag = i;
//			amExist = 1;
//		} else if ((a[i] == '*' || a[i] == '/') && isInBrackets == 0
//				&& amExist == 0) {
//			tag = i;
//			bmExist = 1;
//		} else if (isInBrackets == 0 && amExist == 0 && bmExist == 0) {
//			tag = i;
//		}
//	}
//	return tag;
//}
////build the binary tree
//Node * buildBinaryTree(char a[], int begin, int end) {
//	if (a[begin] == '(' && a[end] == ')') {
//		begin += 1;
//		end -= 1;
//	}
//	cout << "begin " << begin << " end " << end << endl;
//	Node * p = NULL;
//	if (begin < 0 || end < 0) {
//	} else if (begin == end) {
//		p = (Node *) malloc(sizeof(Node));
//		p->key = a[begin];
//		p->left = NULL;
//		p->right = NULL;
//	} else {
//		int tag;
//		tag = search(a, begin, end);
//		if (tag != -1) {
//			p = (Node *) malloc(sizeof(Node));
//			p->key = a[tag];
//			p->left = buildBinaryTree(a, begin, tag - 1);
//			p->right = buildBinaryTree(a, tag + 1, end);
//		}
//	}
//	return p;
//}
//void outputBinaryTree(Node * head) {
//	if (head) {
//		printf("%c", head->key);
//		outputBinaryTree(head->left);
//		outputBinaryTree(head->right);
//	}
//}
//void freeBinaryTree(Node * head) {
//	if (head) {
//		Node *left = head->left;
//		Node *right = head->right;
//		free(head);
//		head = NULL;
//		freeBinaryTree(left);
//		freeBinaryTree(right);
//	}
//}
//int main() {
//	printf("please input a expression:");
////    char input[N];
////    while(scanf("%s", input))
////    {
//	char input[N] = { '1', '+', '2', '*', '3', '+', '(', '5', '+', '7', ')',
//			'/', '6', '+', '3' };
////        if(strcmp(input, "exit") == 0)
////        {
////            break;
////        }
//	Node * head = NULL;
//	int length = strlen(input);
//	head = buildBinaryTree(input, 0, length - 1);
//	outputBinaryTree(head);
//	printf("/n");
//	freeBinaryTree(head);
//	printf("please input a expression:");
////    }
//	return 0;
//}

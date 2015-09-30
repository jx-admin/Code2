/*
 ============================================================================
 Name        : Cstudy.c
 Author      : junxu
 Version     :
 Copyright   : Copyright 2014 by junxu
 Description : StudentManagerSystem in C, Ansi-style
 练习创建包含学生学号和姓名信息的单链表。其结构点数任意，以学号升序排列，以输入姓名为空结束。
 程序功能点：删除一个给定姓名的节点；插入一个给定学号和姓名的节点。
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>

/*链表节点的数据结构*/
struct student {
	int num; /*学号*/
	char name[20]; /*姓名*/
	struct student *next; /*指针域*/
};

typedef struct student STUDENT;

/*主函数*/
int StudentManagerSystemMain() {
	setbuf(stdout, NULL); /*取消，默认输出流存在buffer机制*/
	/*函数声明*/
	char menu(); /*菜单*/
	struct student *create(); /*创建链表*/
	struct student *insert(); /*插入节点*/
	struct student *delet(); /*删除节点*/
	void printStudent(); /*输出链表*/
	STUDENT *find();

	/*数据定义*/
	struct student *head; /**/
	char name[20];
	int n;
	head = NULL; /*作空链表*/

	char type=0;
	while ((type=menu())!='q') {
		switch (type) {
		case '0':
			return EXIT_SUCCESS;
		case '1':
			head = create(head); /*调用函数创建以head为头的链表*/
			break;
		case '2':
			printf("\nInput the inserted num and name:\n");
			gets(name); /*输入学号*/
			n = atoi(name); /**/
			gets(name); /*输入姓名*/
			head = insert(head, name, n); /*将结点插入链表*/
			break;
		case '3':
			printf("\nInput the deleted name:\n");
			gets(name); /*输入被删除姓名*/
			head = delet(head, name); /*调用函数删除结点*/
			break;
		case '4':
			printStudent(head); /*调用函数输出节点*/
			printf("press any key back...");
			scanf("%c", &type);
			scanf("%c", &type);
			break;
		case '5':
			printf("\nInput the find name:\n");
			gets(name);
			STUDENT *stu=find(head,name);
			break;
		}
	}
//	head=create(head);		/*调用函数创建以head为头的链表*/
//	print(head);			/*调用函数输出节点*/
//
//	printf("\nInput the inserted num and name:\n");
//	gets(name);				/*输入学号*/
//	n=atoi(name);			/**/
//	gets(name);				/*输入姓名*/
//	head=insert(head,name,n);	/*将结点插入链表*/
//	print(head);
//
//	printf("\nInput the deleted name:\n");
//	gets(name);				/*输入被删除姓名*/
//	head=delet(head,name);	/*调用函数删除结点*/
//	print(head);
	return EXIT_SUCCESS;
}

/*菜单*/
char menu() {
	char type[10];
	printf("Student Manager System demo\n");
	printf("create student(1)\n");
	printf("insert student(2)\n");
	printf("delet student(3)\n");
	printf("print nod(4)\n");
	printf("find student(5)\n");
	printf("exit(q)\n");
//	scanf("%c", &type);
	gets(type);
	return type[0];
}
/*创建链表*/
struct student *create(struct student *head) {
	char temp[30];
	struct student *newstudent, *curstudent;

	newstudent = curstudent = (STUDENT*) malloc(sizeof(STUDENT));
	printf("Input the num and name:\n");
	printf("Exit:double times Enter!\n");
	gets(temp);
	newstudent->num = atoi(temp);
	printf("num:%d\n",newstudent->num);
	gets(newstudent->name);
	printf("%s\n",newstudent->name);
	newstudent->next = NULL;
	while (strlen(newstudent->name) > 0) {
		if (head == NULL)
			head = newstudent;
		else
			curstudent->next = newstudent;
		curstudent = newstudent;
		newstudent = (struct student *) malloc(sizeof(struct student));
		printf("Input the num and name:\n");
		printf("Exit:double times Enter!\n");
			gets(temp);
			newstudent->num = atoi(temp);
//			scanf("%d",&(newstudent->num));
			printf("num:%d\n",newstudent->num);
			gets(newstudent->name);
			printf("%s\n",newstudent->name);
			newstudent->next = NULL;
	}
	return head;
}

/*插入结点*/
struct student *insert(struct student *head, char *name, int n) {
	struct student *newstudent, *curentstudent, *laststudent;
	newstudent = (struct student *) malloc(sizeof(struct student));
	strcpy(newstudent->name, name);
	newstudent->num = n;
	curentstudent = head;
	if (head == NULL) {
		head = newstudent;
		newstudent->next = NULL;
	} else {
		while (n > curentstudent->num && curentstudent->next != NULL) {
			laststudent = curentstudent;
			curentstudent = curentstudent->next;
		}
		if (n < curentstudent->num) {
			if (head == curentstudent) { /*插入表头*/
				head = newstudent;
				newstudent->next = curentstudent;
			} else { /*插入中间*/
				laststudent->next = newstudent;
				newstudent->next = curentstudent;
			}
		} else { /*插入表尾*/
			curentstudent->next = newstudent;
			newstudent->next = NULL;
		}
	}
	return (head);
}

/*删除结点*/
struct student *delet(struct student *head, char *name) {
	struct student *temp, *p;

	temp = head;
	if (temp == NULL) {
		printf("\nList is null!\n");
	} else {
		while (strcmp(temp->name, name) != 0 && temp->next != NULL) {/*查找*/
			p = temp;
			temp = temp->next;
		}
		if (strcmp(temp->name, name) == 0) {/*已找到*/
			if (temp == head) {/*表头*/
				head = head->next;
				printf("Delete the string(head):%s\n", temp->name);
				free(temp);
			} else {/*表中或表尾*/
				p->next = temp->next;
				printf("Delete the string:%s\n", temp->name);
				free(temp);
			}
		} else {
			printf("No find string!\n");
		}
	}
	return (head);
}

STUDENT *find(STUDENT *head,char *name){
	STUDENT *temp;
	temp=head;
	if(temp==NULL){
		printf("\nList is null\n");
		return NULL;
	}else{
		while(strcmp(temp->name,name)!=0&&temp->next!=NULL){
			temp=temp->next;
		}
		if(strcmp(temp->name,name)==0){
			printf("\nFind num:%d\n",temp->num);
			return temp;
		}
	}
	return NULL;
}

/*输出链表*/
void printStudent(STUDENT *head) {
	struct student *temp;
	temp = head;
	printf("\nOutput strings:\n");
	while (temp != NULL) {
		printf("\n%d------%s\n", temp->num, temp->name);
		temp = temp->next;
	}
	return;
}

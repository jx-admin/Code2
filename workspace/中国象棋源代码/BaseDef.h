#if !defined CCT_CHINESECHESS_DEF
#define CCT_CHINESECHESS_DEF

#include "BaseClasses.h"

const int MW=32,SW=1;				//MW-棋子宽度;SW-棋子间隔的一半
const int BWA=MW+SW*2;				//BWA-棋格宽度

const int XC[2]={BWA/2,BWA/2-1};	//XC,YC-棋子宽的一半
const int YC[2]={BWA/2,BWA/2-2};	//[0].[1]分别为阴影和明线的偏移量

const int XBW=BWA*9,YBW=BWA*10;		//棋盘的长宽
const int XOFFSET=15,YOFFSET=10;	//棋盘左上角相对窗口用户区的偏移

#define MAN 0			//人
#define COM 1			//计算机

#define RED 0			//红方
#define BLACK 1			//黑方

#define RED_K 0			//红帅
#define RED_S 1			//仕
#define RED_X 2			//相
#define RED_M 3			//马
#define RED_J 4			//车
#define RED_P 5			//炮
#define RED_B 6			//兵

#define BLACK_K 7		//黑将
#define BLACK_S 8		//士
#define BLACK_X 9		//象
#define BLACK_M 10		//马
#define BLACK_J 11		//车
#define BLACK_P 12		//炮
#define BLACK_B 13		//卒


//以下是全局函数定义:

//把棋子序号转换为对应图标的序号
const int  ManToIcon[33]=	{0,1,1,2,2,3,3,4,4,5,5,6,6,6,6,6
,7,8,8,9,9,10,10,11,11,12,12,13,13,13,13,13,-1};							

//棋子类型与图标的序号相同
#define ManToType  ManToIcon

const int ManToType7[33]=	{0,1,1,2,2,3,3,4,4,5,5,6,6,6,6,6
	,0,1,1,2,2,3,3,4,4,5,5,6,6,6,6,6,-1};


//随即函数,返回小于n的随机整数
int  rnd(const int& n);

//给出棋子序号!!,判断是红是黑
const int SideOfMan[33]=	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
	,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-1};

const int _defaultmap[11][12]=	
{
//	[0][1][2][3][4][5][6][7][8][9][10][11]
	{32,32,32,32,32,32,32,32,32,32,32,32},//[0]
	{32,32,32,32,32,32,32,32,32,32,32,32},//[1]
	{32,32,32,32,32,32,32,32,32,32,32,32},//[2]
	{32,32,32,32,32,32,32,32,32,32,32,32},//[3]
	{32,32,32,32,32,32,32,32,32,32,32,32},//[4]
	{32,32,32,32,32,32,32,32,32,32,32,32},//[5]
	{32,32,32,32,32,32,32,32,32,32,32,32},//[6]
	{32,32,32,32,32,32,32,32,32,32,32,32},//[7]
	{32,32,32,32,32,32,32,32,32,32,32,32},//[8]
	{32,32,32,32,32,32,32,32,32,32,32,32},//[9]
	{32,32,32,32,32,32,32,32,32,32,32,32}//[10]
};

//
const int FistOfSide[2]={0,16};
const int LastOfSide[2]={15,31};

//给出棋子排列数组和走法,判断是否能走
BOOL CanGo(int manmap[11][12],
		   const int & man,
		   const int & xfrom,
		   const int & yfrom,
		   const int & xto,
		   const int & yto);

//判断某种棋子能否放在某点
BOOL  IsNormal(const int & mantype,const int & x,const int & y);

void FixManMap(CFace & face, int map[11][12]);


#endif
// BaseClasses.h: interface for the CFace class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_BASECLASSES_H__23936A45_A884_11D4_9A6C_973E5A1E3F59__INCLUDED_)
#define AFX_BASECLASSES_H__23936A45_A884_11D4_9A6C_973E5A1E3F59__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class CXY 	//X,Y各占一字节的点类
{
public:
	CXY();
	CXY(int xx,int yy);
	CXY(CXY& xy);

	BOOL operator==(CXY& xy)const;
	BOOL operator!=(CXY& xy)const;
	CXY& operator=(CXY& xy);

	char x;
	char y;
};

class CMove	//走法
{
public:
	CMove(CMove& move);
	CMove();
	CMove& operator=(CMove& move);
	char man;				//所走的子
	char x;
	char y;					//走到的位置
};

class CStep	//棋步
{
public:
	CStep();

	char man;				//所走的子
	char eaten;				//被吃的子
	CXY from;				//从 from 点
	CXY to;					//走到 to 点
};

class CFace 			//棋局
{
public:
	CFace();
	CFace(CFace& face);

	BOOL operator==(CFace& face)const;
	BOOL operator!=(CFace& face)const;
	CFace& operator=(CFace& face);

	BOOL Open(LPCSTR filename);			//从文件读取
	BOOL Save(LPCSTR filename);			//保存到文件
	BOOL IsNormal();					//是否合法
	void Reset();						//设为标准棋局

	CXY man [32];						//32棋子的位置(man[*].x==0则棋子已死)
	int side;							//走的一方 RED|0-红;BLACK|1-黑
};

class CSetting
{
public:
	BOOL IsNormal();
	void Reset();
	CSetting();
	BOOL Load();
	BOOL Save();
	UINT m_nCOrM[2];			//给出[player]得到是否电脑
	UINT m_nPlayer[2];			//给出[color/side]得到棋手号
	UINT m_nLevel;				//计算机的等级
	UINT m_nMode;				//轮换方式(谁执什么棋)
	
};

#endif // !defined(AFX_BASECLASSES_H__23936A45_A884_11D4_9A6C_973E5A1E3F59__INCLUDED_)

// BaseClasses.cpp: implementation of the CFace class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "chess.h"
#include "BaseClasses.h"
#include "BaseDef.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CFace::CFace()
{
	Reset();
}

CFace::CFace(CFace& face)
{
	for(int i=0;i<32;i++)man[i]=face.man[i];

	side=face.side;
}

BOOL CFace::operator==(CFace& face)const
{
	for(int i=0;i<32;i++)
		if(man[i]!=face.man[i])return FALSE;

	if (side!=face.side)return FALSE;

	return TRUE;
}

BOOL CFace::operator!=(CFace& face)const
{
	for(int i=0;i<32;i++)
		if(man[i]!=face.man[i])return TRUE;

	if (side!=face.side)return TRUE;

	return FALSE;
}

CFace& CFace::operator=(CFace& face)
{
	for(int i=0;i<32;i++)man[i]=face.man[i];
	side=face.side;

	return *this;
}

void CFace::Reset()
{
	//红
	man[0].x=5;man[0].y=10;	//帅
	man[1].x=4;man[1].y=10;	//士
	man[2].x=6;man[2].y=10;	
	man[3].x=3;man[3].y=10;	//相
	man[4].x=7;man[4].y=10;
	man[5].x=2;man[5].y=10;	//马
	man[6].x=8;man[6].y=10;
	man[7].x=1;man[7].y=10;	//车
	man[8].x=9;man[8].y=10;
	man[9].x=2;man[9].y=8;	//炮
	man[10].x=8;man[10].y=8;
	man[11].x=1;man[11].y=7;//兵
	man[12].x=3;man[12].y=7;
	man[13].x=5;man[13].y=7;
	man[14].x=7;man[14].y=7;
	man[15].x=9;man[15].y=7;
	//黑
	man[16].x=5;man[16].y=1;//将
	man[17].x=4;man[17].y=1;//士
	man[18].x=6;man[18].y=1;
	man[19].x=3;man[19].y=1;//相
	man[20].x=7;man[20].y=1;
	man[21].x=2;man[21].y=1;//马
	man[22].x=8;man[22].y=1;
	man[23].x=1;man[23].y=1;//车
	man[24].x=9;man[24].y=1;
	man[25].x=2;man[25].y=3;//炮
	man[26].x=8;man[26].y=3;
	man[27].x=1;man[27].y=4;//卒
	man[28].x=3;man[28].y=4;
	man[29].x=5;man[29].y=4;
	man[30].x=7;man[30].y=4;
	man[31].x=9;man[31].y=4;

	side=RED;
}

BOOL CFace::IsNormal()
{
	if(side!=RED && side !=BLACK)return FALSE;
	
	int map[10][11];//map数组用来判断是否有两个没死的棋子放在同一点

	for(int i=1;i<10;i++)		
		for(int j=1;j<11;j++)map[i][j]=0;	//初始化

	for(int i=0;i<32;i++)
	{
		if(man[i].x!=0)						//没死
		{
			if	( 
				man[i].x<1	||
				man[i].x>9	||
				man[i].y<1	||
				man[i].y>10
				)	return FALSE;			//不在棋盘内
					
			if(map[man[i].x][man[i].y]!=0)	//这一点已有子
				return FALSE;

			map[man[i].x][man[i].y]=1;		//记者一点已有棋子

			//棋子放的位置不对:
			if( !::IsNormal(ManToType[i],man[i].x,man[i].y) )return FALSE;
		}
	}

	return TRUE;
}

BOOL CFace::Save(LPCSTR filename)
{
	CFile file;
	if(file.Open(filename,CFile::modeWrite|CFile::modeCreate))
	{
		file.SeekToBegin();
		file.Write(this,sizeof(CFace));
		file.Close();
		return TRUE;
	}
	else return FALSE;	
}

BOOL CFace::Open(LPCSTR filename)
{
	CFile file;
	if(file.Open( filename,CFile::modeRead))
	{
		file.SeekToBegin();
		file.Read(this,sizeof(CFace));
		file.Close();
		return TRUE;
	}
	return FALSE;
}

//////////////////////////////////////////////////////////////////////
// CMove Class
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CMove::CMove()
{
	man=0;
	x=0;
	y=0;
}

CMove::CMove(CMove &move)
{
	man=move.man;
	x=move.x;
	y=move.y;
}

CMove& CMove::operator =(CMove & move)
{
	man=move.man;
	x=move.x;
	y=move.y;
	return *this;
}


//////////////////////////////////////////////////////////////////////
// CStep Class
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CStep::CStep()
{
	man=32;
	eaten=32;
	from.x=0;
	from.y=0;
	to.x=0;
	to.y=0;
}


//////////////////////////////////////////////////////////////////////
// CXY Class
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CXY::CXY()
{
	x=0;
	y=0;
}

CXY::CXY(int xx,int yy)
{
	x=(BYTE)xx;
	y=(BYTE)yy;
}

CXY::CXY(CXY& xy)
{
	x=xy.x;
	y=xy.y;
}

CXY& CXY::operator=(CXY& xy)
{
	x=xy.x;
	y=xy.y;

	return *this;
}

BOOL CXY::operator==(CXY& xy)const
{
	if(xy.x != x)return FALSE;
	if(xy.y != y)return FALSE;

	return TRUE;
}

BOOL CXY::operator!=(CXY& xy)const
{
	if(xy.x != x)return TRUE;
	if(xy.y != y)return TRUE;

	return FALSE;
}

CSetting::CSetting()
{
	Reset();
}

BOOL CSetting::Save()
{
	CFile file;
	if(file.Open("Setting.set",CFile::modeWrite|CFile::modeCreate))
	{
		file.SeekToBegin();
		file.Write(this,sizeof(CSetting));
		file.Close();
		return TRUE;
	}
	else return FALSE;	
}


BOOL CSetting::Load()
{
	CFile file;
	if(file.Open( "Setting.set",CFile::modeRead))
	{
		file.SeekToBegin();
		file.Read(this,sizeof(CSetting));
		file.Close();

		if(IsNormal())return TRUE;
		else
		{
			Reset();
			Save();
			return FALSE;
		}		
	}
	MessageBox(NULL, "没找到 Setting.set 文件\n\n这个文件并不是必需的,但它记录了你的设置内容,\n\n请不要删掉\n\n你现在可以通过菜单 \"文件\" -> \"设置\" 重新设定.","提醒",MB_OK|MB_ICONINFORMATION);
	Reset();
	Save();
	return FALSE;
}


void CSetting::Reset()
{
	m_nMode=1;
	m_nLevel=3;
	m_nCOrM[0]=0;
	m_nCOrM[1]=1;
	m_nPlayer[0]=RED;
	m_nPlayer[1]=BLACK;
}

BOOL CSetting::IsNormal()
{
		if(m_nCOrM[0]>1 || m_nCOrM[1]>1)	return FALSE;
		if(m_nPlayer[0]>1 || m_nPlayer[1]>1)return FALSE;
		if(m_nPlayer[1]	== m_nPlayer[0])	return FALSE;
		if(m_nLevel<1 || m_nLevel>4)		return FALSE;
		if(m_nMode>2)						return FALSE;

		return TRUE;
}

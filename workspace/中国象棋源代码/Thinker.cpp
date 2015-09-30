// Thinker.cpp: implementation of the CThinker class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "BaseDef.h"
#include "Thinker.h"
#include "MoveList.h"
#include "ThinkDef.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CThinker::~CThinker()
{
	if(!m_bExited)Exit();
}

UINT CThinker::ThinkProc()		//思考线程生存期与类对象生存期相同
{
	int i,cur,maxvalue[3],curvalue;
	char *bman,*btox,*btoy;
	int *pcount;
	CMove maxmove[3];

	const char strman[14][3]=
	{"帅","仕","相","马","车","炮","兵","将","士","象","马","车","炮","卒"};

	TRACE("进入思考线程\n");

	while(1)
	{
		cs.Lock();
		if(m_bWaitForExit)
		{
			cs.Unlock();
			goto _EXIT;
		}
		cs.Unlock();
			
		cs.Lock();
		if(m_bWaitForCut)
		{
			cs.Unlock();
			goto _CUT;
		}
		cs.Unlock();

		cs.Lock();
		if(!m_bThinking)
		{
			cs.Unlock();
			continue;
		}
		cs.Unlock();
		
		curvalue=-10000;
		maxvalue[0]=-10000;
		maxvalue[1]=-10001;
		maxvalue[2]=-10002;

		cs.Lock();
		tlevel = m_nLevel;
		cs.Unlock();

		tdeep  =0;
		bman=tman[0];
		btox=ttox[0];
		btoy=ttoy[0];
		pcount=& tcount[0];
		*pcount=0;

		for(i=0;i<32;i++)
		{
			tmanx[i]=m_FaceToThink.man[i].x;
			tmany[i]=m_FaceToThink.man[i].y;
		}
		tside=m_FaceToThink.side;

		FixManMap(m_FaceToThink,tmap);

		cur=0;
		EnumList();

		i=*pcount;

		while(cur<i)
		{
			curvalue=SubThink(bman[cur],btox[cur],btoy[cur]);
//---------------------------
			bman=tman[0];
			btox=ttox[0];
			btoy=ttoy[0];
//----------------------------
			//防止兑子过快:
			if(tmap[btox[cur]][btoy[cur]]!=32) curvalue-=BV1[4]/18;

			TRACE("%2d: %s(%2d,%2d) - (%2d,%2d) =%4d\n"
				,cur
				,strman[ManToType[bman[cur]]]
				,tmanx[bman[cur]]
				,tmany[bman[cur]]
				,btox[cur]
				,btoy[cur]
				,curvalue);
			if(curvalue >maxvalue[0])
			{
				maxmove[2]=maxmove[1];	maxvalue[2]=maxvalue[1];
				maxmove[1]=maxmove[0];	maxvalue[1]=maxvalue[0];
				maxmove[0].man=bman[cur];
				maxmove[0].x=btox[cur];
				maxmove[0].y=btoy[cur];
				maxvalue[0]=curvalue;
			}
			else if(curvalue >maxvalue[1])
			{
				maxmove[2]=maxmove[1];	maxvalue[2]=maxvalue[1];
				maxmove[1].man=bman[cur];
				maxmove[1].x=btox[cur];
				maxmove[1].y=btoy[cur];
				maxvalue[1]=curvalue;
			}
			else if(curvalue >maxvalue[2])
			{
				maxmove[2].man=bman[cur];
				maxmove[2].x=btox[cur];
				maxmove[2].y=btoy[cur];
				maxvalue[2]=curvalue;
			}

			cur ++;
			cs.Lock();
			m_nPercent=((cur+1)*100)/i;
			cs.Unlock();

//----------这一段要保证随时能调用
			cs.Lock();
			if(m_bWaitForExit)
			{
				cs.Unlock();
				goto _EXIT;
			}
			cs.Unlock();

			cs.Lock();
			if(m_bWaitForCut)
			{
				cs.Unlock();
				goto _CUT;
			}
			cs.Unlock();
//	--------------------------------
		}
		

//结束一次计算:
		cs.Lock();
		float f;
		f=(float(maxvalue[0]-maxvalue[2])/(float(maxvalue[0]+maxvalue[1]+maxvalue[2])/3));
		if(f<0.1 && f>-0.1)
		m_moveResult=maxmove[rnd(3)];
		else
		{
			f=(float(maxvalue[0]-maxvalue[1])/(float(maxvalue[0]+maxvalue[1])/2));
			if(f<0.1 && f>-0.1)	m_moveResult=maxmove[rnd(2)];
			else		m_moveResult=maxmove[0];
		}
		TRACE("结果: %s(%2d,%2d) - (%2d,%2d)\n"
			,strman[ManToType[m_moveResult.man]]
			,m_FaceToThink.man[m_moveResult.man].x
			,m_FaceToThink.man[m_moveResult.man].y
			,m_moveResult.x
			,m_moveResult.y);

		m_bThinkOver=TRUE;
//-------------------------
		for(i=0;i<32;i++)
		{
			m_FaceToThink.man[i].x=tmanx[i];
			m_FaceToThink.man[i].y=tmany[i];
		}
		m_FaceToThink.side=tside;
//-------------------------------

		cs.Unlock();
		goto _DDD;

_CUT:	cs.Lock();
		m_Cut.SetEvent();
		if(m_bWaitForCut)m_bWaitForCut=FALSE;
		cs.Unlock();
		
_DDD:	cs.Lock();
		m_bThinking=FALSE;
		cs.Unlock();
	}
	
_EXIT:	TRACE("退出思考线程\n");

	cs.Lock();
	m_Stoped.SetEvent();
	cs.Unlock();
	return 0;
}

UINT CThinker::_bogusthreadfunc(LPVOID lpparam)//线程函数不能是普通的类成员函数
{													//所以通过这个static类型的函数启动线程
	CThinker* This=(CThinker*)(lpparam);	//lpparam为构造器中传过来的"this"指针
	return This->ThinkProc();
}

CThinker::CThinker()
{
	ResetBV();
	m_nLevel		=	1;
 	m_nPercent		=	0;
	m_bExited		=	FALSE;
	m_bWaitForExit	=	FALSE;
	m_bWaitForCut	=	FALSE;
	m_bThinking		=	FALSE;
	m_bThinkOver	=	FALSE;

	m_pThinkThread=AfxBeginThread(_bogusthreadfunc,this,THREAD_PRIORITY_NORMAL);	//构造器中启动线程
}

BOOL CThinker::GetMove(CMove& move,CFace facetothink)
{
	//TRACE("Enter CThinker::GetMove()\n");

	if(!IsThinkOver())return FALSE;
	cs.Lock();
	if(facetothink==m_FaceToThink)
	{
		move=m_moveResult;

		cs.Unlock();
		return TRUE;
	}
	cs.Unlock();

	TRACE("CThinker::GetMove() 发现两次的棋局不同\n");

	return FALSE;
}


void CThinker::SetLevel(int level)
{
	cs.Lock();
	m_nLevel=level;
	TRACE("设置电脑等级为 %d\n",level);
	cs.Unlock();
}


void CThinker::Exit()
{
	cs.Lock();
	m_bWaitForExit=TRUE;
	cs.Unlock();
	::WaitForSingleObject(m_Stoped,INFINITE);
	m_bExited=TRUE;
	Sleep(300);
}

void CThinker::Think(CFace face)
{
	//TRACE("Enter CThinker::Think()\n");

	if(m_bThinking)Cut();

	cs.Lock();
	m_nPercent		=	0;
	m_FaceToThink	=	face;
	m_bThinking		=	TRUE;
	m_bThinkOver	=	FALSE;
	TRACE("计算 side = %d\n",face.side);
	cs.Unlock();

	//TRACE("Leave CThinker::Think()\n");
}

BOOL CThinker::IsThinkOver()
{
	//TRACE("Enter CThinker::IsThinkOver()\n");

	BOOL flag;
	cs.Lock();
	flag=m_bThinkOver;
	cs.Unlock();

	//TRACE("Leave CThinker::IsThinkOver()\n");

	return flag;
}







void CThinker::Cut()
{
	cs.Lock();
	m_bWaitForCut=TRUE;
	cs.Unlock();
	::WaitForSingleObject(m_Cut,INFINITE);
	cs.Lock();
	m_bThinkOver=FALSE;
	m_nPercent=0;
	cs.Unlock();
	TRACE("中止计算\n");
}

UINT CThinker::GetPercent()
{
	int val;
	cs.Lock();
	val=m_nPercent;
	cs.Unlock();
	return val;
}

#define ADD(man,tx,ty) {*lman=man;*ltox=tx;*ltoy=ty;lman++;ltox++;ltoy++;(*pcount)++;if(tmap[tx][ty]==FistOfSide[!tside])goto _NOKING;}

BOOL CThinker::EnumList()
{
	static int i,j,n,x,y,* pcount;
	static BOOL	flag;
	lman=tman[tdeep];
	ltox=ttox[tdeep];
	ltoy=ttoy[tdeep];
	pcount=&tcount[tdeep];

	for(n=FistOfSide[tside];n<=LastOfSide[tside];n++)
	{
		x=tmanx[n];
		if(!x)continue;
		y=tmany[n];
		switch(n)
		{
		case 0:
			if(tmanx[0]==tmanx[16])		//将帅在同一列
			{
				flag=FALSE;
				for(j=tmany[16]+1;j<tmany[0];j++)
				{
					if(tmap[x][j]!=32)
					{
						flag=TRUE;
						break;
					}
				}
				if (!flag)	
				{
					ADD(0,x,tmany[16]);
				}
			}
			j=y+1;if(j<=10 && NORED(x,j))	ADD(0,x,j)
			j=y-1;if(j>=8  && NORED(x,j))	ADD(0,x,j)
			i=x+1;if(i<=6  && NORED(i,y))	ADD(0,i,y)
			i=x-1;if(i>=4  && NORED(i,y))	ADD(0,i,y)
			break;
		case 16:
			if(tmanx[0]==tmanx[16])		//将帅在同一列
			{
				flag=FALSE;
				for(j=tmany[16]+1;j<tmany[0];j++)
				{
					if(tmap[x][j]!=32)
					{
						flag=TRUE;
						break;
					}
				}
				if (!flag)	
				{
					ADD(16,x,tmany[0]);
				}
			}
			j=y+1;if(j<=3 && NOBLACK(x,j))	ADD(16,x,j)
			j=y-1;if(j>=1  && NOBLACK(x,j))	ADD(16,x,j)
			i=x+1;if(i<=6  && NOBLACK(i,y))	ADD(16,i,y)
			i=x-1;if(i>=4  && NOBLACK(i,y))	ADD(16,i,y)
			break;
		case 1:
		case 2:
			i=x+1;j=y+1;if(i<=6 && j<=10 && NORED(i,j))	ADD(n,i,j)
			i=x+1;j=y-1;if(i<=6 && j>=8  && NORED(i,j))	ADD(n,i,j)
			i=x-1;j=y+1;if(i>=4 && j<=10 && NORED(i,j))	ADD(n,i,j)
			i=x-1;j=y-1;if(i>=4 && j>=8  && NORED(i,j))	ADD(n,i,j)
			break;
		case 17:
		case 18:
			i=x+1;j=y+1;if(i<=6 && j<=3 && NOBLACK(i,j))	ADD(n,i,j)
			i=x+1;j=y-1;if(i<=6 && j>=1 && NOBLACK(i,j))	ADD(n,i,j)
			i=x-1;j=y+1;if(i>=4 && j<=3	&& NOBLACK(i,j))	ADD(n,i,j)
			i=x-1;j=y-1;if(i>=4 && j>=1 && NOBLACK(i,j))	ADD(n,i,j)
			break;
		case 3:
		case 4:
			i=x+2;j=y+2;if(i<=9 && j<=10   && NORED(i,j))	if(NOMAN(x+1,y+1))	ADD(n,i,j)
			i=x+2;j=y-2;if(i<=9 && j>=6    && NORED(i,j))	if(NOMAN(x+1,y-1))	ADD(n,i,j)
			i=x-2;j=y+2;if(i>=1 && j<=10   && NORED(i,j))	if(NOMAN(x-1,y+1))	ADD(n,i,j)
			i=x-2;j=y-2;if(i>=1 && j>=6    && NORED(i,j))	if(NOMAN(x-1,y-1))	ADD(n,i,j)
			break;
		case 19:
		case 20:
			i=x+2;j=y+2;if(i<=9 && j<=5  && NOBLACK(i,j))	if(NOMAN(x+1,y+1))	ADD(n,i,j)
			i=x+2;j=y-2;if(i<=9 && j>=1  && NOBLACK(i,j))	if(NOMAN(x+1,y-1))	ADD(n,i,j)
			i=x-2;j=y+2;if(i>=1 && j<=5  && NOBLACK(i,j))	if(NOMAN(x-1,y+1))	ADD(n,i,j)
			i=x-2;j=y-2;if(i>=1 && j>=1  && NOBLACK(i,j))	if(NOMAN(x-1,y-1))	ADD(n,i,j)
			break;
		case 5:
		case 6:
			i=x+1;
			if(NOMAN(i,y))
			{
				i=x+2;j=y+1;if(i<=9 && j<=10 && NORED(i,j))	ADD(n,i,j)
				i=x+2;j=y-1;if(i<=9 && j>=1  && NORED(i,j))	ADD(n,i,j)
			}
			i=x-1;
			if(NOMAN(i,y))
			{
				i=x-2;j=y+1;if(i>=1 && j<=10 && NORED(i,j))	ADD(n,i,j)
				i=x-2;j=y-1;if(i>=1 && j>=1  && NORED(i,j))	ADD(n,i,j)
			}
			j=y+1;
			if(NOMAN(x,j))
			{
				i=x+1;j=y+2;if(i<=9 && j<=10 && NORED(i,j))	ADD(n,i,j)
				i=x-1;j=y+2;if(i>=1 && j<=10 && NORED(i,j))	ADD(n,i,j)
			}
			j=y-1;
			if(NOMAN(x,j))
			{
				i=x+1;j=y-2;if(i<=9 && j>=1 && NORED(i,j))	ADD(n,i,j)
				i=x-1;j=y-2;if(i>=1 && j>=1 && NORED(i,j))	ADD(n,i,j)
			}
			break;
		case 21:
		case 22:
			i=x+1;
			if(NOMAN(i,y))
			{
				i=x+2;j=y+1;if(i<=9 && j<=10 && NOBLACK(i,j))	ADD(n,i,j)
				i=x+2;j=y-1;if(i<=9 && j>=1  && NOBLACK(i,j))	ADD(n,i,j)
			}
			i=x-1;
			if(NOMAN(i,y))
			{
				i=x-2;j=y+1;if(i>=1 && j<=10 && NOBLACK(i,j))	ADD(n,i,j)
				i=x-2;j=y-1;if(i>=1 && j>=1  && NOBLACK(i,j))	ADD(n,i,j)
			}
			j=y+1;
			if(NOMAN(x,j))
			{
				i=x+1;j=y+2;if(i<=9 && j<=10 && NOBLACK(i,j))	ADD(n,i,j)
				i=x-1;j=y+2;if(i>=1 && j<=10 && NOBLACK(i,j))	ADD(n,i,j)
			}
			j=y-1;
			if(NOMAN(x,j))
			{
				i=x+1;j=y-2;if(i<=9 && j>=1 && NOBLACK(i,j))	ADD(n,i,j)
				i=x-1;j=y-2;if(i>=1 && j>=1 && NOBLACK(i,j))	ADD(n,i,j)
			}
			break;

		case 7:
		case 8:
				i=x+1;
				while(i<=9)
				{
					if (NOMAN(i,y))	ADD(n,i,y)
					else
					{
						if(NORED(i,y))	ADD(n,i,y)
						break;
					}
					i++;
				}
				i=x-1;
				while(i>=1)
				{
					if (NOMAN(i,y))	ADD(n,i,y)
					else
					{
						if(NORED(i,y))	ADD(n,i,y)
						break;
					}
					i--;
				}
				j=y+1;
				while(j<=10)
				{
					if (NOMAN(x,j))	ADD(n,x,j)
					else
					{
						if(NORED(x,j))	ADD(n,x,j)
						break;
					}
					j++;
				}
				j=y-1;
				while(j>=1)
				{
					if (NOMAN(x,j))	ADD(n,x,j)
					else
					{
						if(NORED(x,j))	ADD(n,x,j)
						break;
					}
					j--;
				}
				break;
		case 23:
		case 24:
				i=x+1;
				while(i<=9)
				{
					if (NOMAN(i,y))	ADD(n,i,y)
					else
					{
						if(NOBLACK(i,y))	ADD(n,i,y)
						break;
					}
					i++;
				}
				i=x-1;
				while(i>=1)
				{
					if (NOMAN(i,y))	ADD(n,i,y)
					else
					{
						if(NOBLACK(i,y))	ADD(n,i,y)
						break;
					}
					i--;
				}
				j=y+1;
				while(j<=10)
				{
					if (NOMAN(x,j))	ADD(n,x,j)					
					else
					{
						if(NOBLACK(x,j))	ADD(n,x,j)
						break;
					}
					j++;
				}
				j=y-1;
				while(j>=1)
				{
					if (NOMAN(x,j))	ADD(n,x,j)
					else
					{
						if(NOBLACK(x,j))	ADD(n,x,j)
						break;
					}
					j--;
				}
				break;
		case 9:
		case 10:
			i=x+1;flag=FALSE;
			while(i<=9)
			{
				if(NOMAN(i,y))
				{
					if(!flag)	ADD(n,i,y)
				}
				else
				{
					if(!flag)flag=TRUE;
					else 
					{
						if(NORED(i,y))	ADD(n,i,y)
						break;
					}
				}
				i++;
			}

			i=x-1;flag=FALSE;
			while(i>=1)
			{
				if(NOMAN(i,y)) 
				{
					if(!flag)	ADD(n,i,y)
				}
				else
				{
					if(!flag)flag=TRUE;
					else 
					{
						if(NORED(i,y))	ADD(n,i,y)
						break;
					}
				}
				i--;
			}

			j=y+1;flag=FALSE;
			while(j<=10)
			{
				if(NOMAN(x,j)) 
				{
					if(!flag)	ADD(n,x,j)
				}
				else
				{
					if(!flag)flag=TRUE;
					else 
					{
						if(NORED(x,j))	ADD(n,x,j)
						break;
					}
				}
				j++;
			}

			j=y-1;flag=FALSE;
			while(j>=1)
			{
				if(NOMAN(x,j)) 
				{
					if(!flag)	ADD(n,x,j)
				}
				else
				{
					if(!flag)flag=TRUE;
					else 
					{
						if(NORED(x,j))	ADD(n,x,j)
						break;
					}
				}
				j--;
			}
			break;

		case 25:
		case 26:
			i=x+1;flag=FALSE;
			while(i<=9)
			{
				if(NOMAN(i,y))
				{
					if(!flag)	ADD(n,i,y)
				}
				else
				{
					if(!flag)flag=TRUE;
					else
					{
						if(NOBLACK(i,y))	ADD(n,i,y)
						break;
					}
				}
				i++;
			}

			i=x-1;flag=FALSE;
			while(i>=1)
			{
				if(NOMAN(i,y)) 
				{
					if(!flag)	ADD(n,i,y)
				}
				else
				{
					if(!flag)flag=TRUE;
					else 
					{
						if(NOBLACK(i,y))	ADD(n,i,y)
						break;
					}
				}
				i--;
			}

			j=y+1;flag=FALSE;
			while(j<=10)
			{
				if(NOMAN(x,j))
				{
					if(!flag)	ADD(n,x,j)
				}
				else
				{
					if(!flag)flag=TRUE;
					else 
					{
						if(NOBLACK(x,j))	ADD(n,x,j)
						break;
					}
				}
				j++;
			}

			j=y-1;flag=FALSE;
			while(j>=1)
			{
				if(NOMAN(x,j))
				{
					if(!flag)	ADD(n,x,j)
				}
				else
				{
					if(!flag)flag=TRUE;
					else 
					{
						if(NOBLACK(x,j))	ADD(n,x,j)
						break;
					}
				}
				j--;
			}
			break;
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
			j=y-1;
			if(j>=1 && NORED(x,j))	ADD(n,x,j)
			if(y<=5)
			{
				i=x+1;if(i<=9 && NORED(i,y))	ADD(n,i,y)
				i=x-1;if(i>=1 && NORED(i,y))	ADD(n,i,y)
			}
			break;

		case 27:
		case 28:
		case 29:
		case 30:
		case 31:
			j=y+1;
			if(j<=10 && NOBLACK(x,j))	ADD(n,x,j)
			if(y>=6)
			{
				i=x+1;if(i<=9 && NOBLACK(i,y))	ADD(n,i,y)
				i=x-1;if(i>=1 && NOBLACK(i,y))	ADD(n,i,y)
			}
			break;
		default :
			break;
		}
	}
	return TRUE;
_NOKING:
	return FALSE;
}



#define CV(man,tx,ty) {k=tmap[tx][ty];v1[man]+=1;if(k!=32)v2[man][k]=1;}


void CThinker::ContactV()
{
	static int i,j,n,x,y;
	static BOOL	flag;

	for(n=0;n<=31;n++)
	{
		x=tmanx[n];
		if(!x)continue;
		y=tmany[n];
		switch(n)
		{
		case 0:
			if(tmanx[0]==tmanx[16])		//将帅在同一列
			{
				flag=FALSE;
				for(j=tmany[16]+1;j<tmany[0];j++)
				{
					if(tmap[x][j]!=32)
					{
						flag=TRUE;
						break;
					}
				}
				if (!flag)	
				{
					CV(0,x,tmany[16]);
				}
			}
			j=y+1;if(j<=10 )	CV(0,x,j)
			j=y-1;if(j>=8  )	CV(0,x,j)
			i=x+1;if(i<=6  )	CV(0,i,y)
			i=x-1;if(i>=4  )	CV(0,i,y)
			break;
		case 16:
			if(tmanx[0]==tmanx[16])		//将帅在同一列
			{
				flag=FALSE;
				for(j=tmany[16]+1;j<tmany[0];j++)
				{
					if(tmap[x][j]!=32)
					{
						flag=TRUE;
						break;
					}
				}
				if (!flag)	
				{
					CV(16,x,tmany[0]);
				}
			}
			j=y+1;if(j<=3 )	CV(16,x,j)
			j=y-1;if(j>=1 )	CV(16,x,j)
			i=x+1;if(i<=6 )	CV(16,i,y)
			i=x-1;if(i>=4 )	CV(16,i,y)
			break;
		case 1:
		case 2:
			i=x+1;j=y+1;if(i<=6 && j<=10 )	CV(n,i,j)
			i=x+1;j=y-1;if(i<=6 && j>=8  )	CV(n,i,j)
			i=x-1;j=y+1;if(i>=4 && j<=10 )	CV(n,i,j)
			i=x-1;j=y-1;if(i>=4 && j>=8  )	CV(n,i,j)
			break;
		case 17:
		case 18:
			i=x+1;j=y+1;if(i<=6 && j<=3 )	CV(n,i,j)
			i=x+1;j=y-1;if(i<=6 && j>=1 )	CV(n,i,j)
			i=x-1;j=y+1;if(i>=4 && j<=3	)	CV(n,i,j)
			i=x-1;j=y-1;if(i>=4 && j>=1 )	CV(n,i,j)
			break;
		case 3:
		case 4:
			i=x+2;j=y+2;if(i<=9 && j<=10  )	if(NOMAN(x+1,y+1))	CV(n,i,j)
			i=x+2;j=y-2;if(i<=9 && j>=6   )	if(NOMAN(x+1,y-1))	CV(n,i,j)
			i=x-2;j=y+2;if(i>=1 && j<=10  )	if(NOMAN(x-1,y+1))	CV(n,i,j)
			i=x-2;j=y-2;if(i>=1 && j>=6   )	if(NOMAN(x-1,y-1))	CV(n,i,j)
			break;
		case 19:
		case 20:
			i=x+2;j=y+2;if(i<=9 && j<=5 )	if(NOMAN(x+1,y+1))	CV(n,i,j)
			i=x+2;j=y-2;if(i<=9 && j>=1 )	if(NOMAN(x+1,y-1))	CV(n,i,j)
			i=x-2;j=y+2;if(i>=1 && j<=5 )	if(NOMAN(x-1,y+1))	CV(n,i,j)
			i=x-2;j=y-2;if(i>=1 && j>=1 )	if(NOMAN(x-1,y-1))	CV(n,i,j)
			break;
		case 5:
		case 6:
			i=x+1;
			if(NOMAN(i,y))
			{
				i=x+2;j=y+1;if(i<=9 && j<=10 )	CV(n,i,j)
				i=x+2;j=y-1;if(i<=9 && j>=1  )	CV(n,i,j)
			}
			i=x-1;
			if(NOMAN(i,y))
			{
				i=x-2;j=y+1;if(i>=1 && j<=10 )	CV(n,i,j)
				i=x-2;j=y-1;if(i>=1 && j>=1  )	CV(n,i,j)
			}
			j=y+1;
			if(NOMAN(x,j))
			{
				i=x+1;j=y+2;if(i<=9 && j<=10 )	CV(n,i,j)
				i=x-1;j=y+2;if(i>=1 && j<=10 )	CV(n,i,j)
			}
			j=y-1;
			if(NOMAN(x,j))
			{
				i=x+1;j=y-2;if(i<=9 && j>=1 )	CV(n,i,j)
				i=x-1;j=y-2;if(i>=1 && j>=1 )	CV(n,i,j)
			}
			break;
		case 21:
		case 22:
			i=x+1;
			if(NOMAN(i,y))
			{
				i=x+2;j=y+1;if(i<=9 && j<=10 )	CV(n,i,j)
				i=x+2;j=y-1;if(i<=9 && j>=1  )	CV(n,i,j)
			}
			i=x-1;
			if(NOMAN(i,y))
			{
				i=x-2;j=y+1;if(i>=1 && j<=10 )	CV(n,i,j)
				i=x-2;j=y-1;if(i>=1 && j>=1  )	CV(n,i,j)
			}
			j=y+1;
			if(NOMAN(x,j))
			{
				i=x+1;j=y+2;if(i<=9 && j<=10 )	CV(n,i,j)
				i=x-1;j=y+2;if(i>=1 && j<=10 )	CV(n,i,j)
			}
			j=y-1;
			if(NOMAN(x,j))
			{
				i=x+1;j=y-2;if(i<=9 && j>=1 )	CV(n,i,j)
				i=x-1;j=y-2;if(i>=1 && j>=1 )	CV(n,i,j)
			}
			break;

		case 7:
		case 8:
				i=x+1;
				while(i<=9)
				{
					if (NOMAN(i,y))	CV(n,i,y)
					else
					{
						CV(n,i,y)
						break;
					}
					i++;
				}
				i=x-1;
				while(i>=1)
				{
					if (NOMAN(i,y))	CV(n,i,y)
					else
					{
						CV(n,i,y)
						break;
					}
					i--;
				}
				j=y+1;
				while(j<=10)
				{
					if (NOMAN(x,j))	CV(n,x,j)
					else
					{
						CV(n,x,j)
						break;
					}
					j++;
				}
				j=y-1;
				while(j>=1)
				{
					if (NOMAN(x,j))	CV(n,x,j)
					else
					{
						CV(n,x,j)
						break;
					}
					j--;
				}
				break;
		case 23:
		case 24:
				i=x+1;
				while(i<=9)
				{
					if (NOMAN(i,y))	CV(n,i,y)
					else
					{
						CV(n,i,y)
						break;
					}
					i++;
				}
				i=x-1;
				while(i>=1)
				{
					if (NOMAN(i,y))	CV(n,i,y)
					else
					{
						CV(n,i,y)
						break;
					}
					i--;
				}
				j=y+1;
				while(j<=10)
				{
					if (NOMAN(x,j))	CV(n,x,j)					
					else
					{
						CV(n,x,j)
						break;
					}
					j++;
				}
				j=y-1;
				while(j>=1)
				{
					if (NOMAN(x,j))	CV(n,x,j)
					else
					{
						CV(n,x,j)
						break;
					}
					j--;
				}
				break;
		case 9:
		case 10:
			i=x+1;flag=FALSE;
			while(i<=9)
			{
				if(NOMAN(i,y))
				{
					if(!flag)	CV(n,i,y)
				}
				else
				{
					if(!flag)flag=TRUE;
					else 
					{
						CV(n,i,y)
						break;
					}
				}
				i++;
			}

			i=x-1;flag=FALSE;
			while(i>=1)
			{
				if(NOMAN(i,y)) 
				{
					if(!flag)	CV(n,i,y)
				}
				else
				{
					if(!flag)flag=TRUE;
					else 
					{
						CV(n,i,y)
						break;
					}
				}
				i--;
			}

			j=y+1;flag=FALSE;
			while(j<=10)
			{
				if(NOMAN(x,j)) 
				{
					if(!flag)	CV(n,x,j)
				}
				else
				{
					if(!flag)flag=TRUE;
					else 
					{
						CV(n,x,j)
						break;
					}
				}
				j++;
			}

			j=y-1;flag=FALSE;
			while(j>=1)
			{
				if(NOMAN(x,j)) 
				{
					if(!flag)	CV(n,x,j)
				}
				else
				{
					if(!flag)flag=TRUE;
					else 
					{
						CV(n,x,j)
						break;
					}
				}
				j--;
			}
			break;

		case 25:
		case 26:
			i=x+1;flag=FALSE;
			while(i<=9)
			{
				if(NOMAN(i,y))
				{
					if(!flag)	CV(n,i,y)
				}
				else
				{
					if(!flag)flag=TRUE;
					else
					{
						CV(n,i,y)
						break;
					}
				}
				i++;
			}

			i=x-1;flag=FALSE;
			while(i>=1)
			{
				if(NOMAN(i,y)) 
				{
					if(!flag)	CV(n,i,y)
				}
				else
				{
					if(!flag)flag=TRUE;
					else 
					{
						CV(n,i,y)
						break;
					}
				}
				i--;
			}

			j=y+1;flag=FALSE;
			while(j<=10)
			{
				if(NOMAN(x,j))
				{
					if(!flag)	CV(n,x,j)
				}
				else
				{
					if(!flag)flag=TRUE;
					else 
					{
						CV(n,x,j)
						break;
					}
				}
				j++;
			}

			j=y-1;flag=FALSE;
			while(j>=1)
			{
				if(NOMAN(x,j))
				{
					if(!flag)	CV(n,x,j)
				}
				else
				{
					if(!flag)flag=TRUE;
					else 
					{
						CV(n,x,j)
						break;
					}
				}
				j--;
			}
			break;
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
			j=y-1;
			if(j>=1 )	CV(n,x,j)
			if(y<=5)
			{
				i=x+1;if(i<=9 )	CV(n,i,y)
				i=x-1;if(i>=1 )	CV(n,i,y)
			}
			break;

		case 27:
		case 28:
		case 29:
		case 30:
		case 31:
			j=y+1;
			if(j<=10 )	CV(n,x,j)
			if(y>=6)
			{
				i=x+1;if(i<=9 )	CV(n,i,y)
				i=x-1;if(i>=1 )	CV(n,i,y)
			}
			break;
		default :
			break;
		}
	}
}

int CThinker::SubThink(int man, int tx, int ty)
{
	int ate,i,j,cur,maxvalue,curvalue,xs,ys;
	char *bman,*btox,*btoy;
	int *pcount;

	ate=32;
	tdeep++;
	//
	//移动棋子:
	xs=tmanx[man];ys=tmany[man];				//原坐标
	if (SideOfMan[tmap[tx][ty]]==!tside)	//目标点有对方的棋子
	{
		ate=tmap[tx][ty];					//记录下被吃掉的棋子
		if(ate==0 || ate==16)
		{
			tdeep--;
			return 9999;
		}
		tmanx[ate]=0;						//目标点的棋子被吃掉
	}
	tmap[tx][ty]=man;						//这两行是:
	tmap[xs][ys]=32;							//在map上的移动
	tmanx[man]=tx;							//这两行是:
	tmany[man]=ty;							//在face上的移动
	tside=!tside;

	if(tdeep != tlevel)
	{

		//初始化:
		bman=tman[tdeep];
		btox=ttox[tdeep];
		btoy=ttoy[tdeep];
		pcount=& tcount[tdeep];
		*pcount=0;

		cur=0;

		if(EnumList())
		{
			maxvalue=-10000;
			i=*pcount;
			while(cur< i)
			{
				curvalue=SubThink(bman[cur],btox[cur],btoy[cur]);
				if(curvalue>maxvalue)maxvalue=curvalue;
				cur ++;
			}
		}
		else maxvalue=9800;
	}
	else
	{
		memset(v2,0,sizeof(int)<<10);
		memset(v1,0,sizeof(int)<<5);
		memset(v3,0,sizeof(int)<<5);
		memset(v4,0,sizeof(int)<<5);

		maxvalue=0;
		ContactV();
//己方将军			
		for(i=FistOfSide[tside];i<=LastOfSide[tside];i++)
		{
			if(v2[i][FistOfSide[!tside]])
			{
				maxvalue=9700;
				goto _ENDSUB;
			}
		}

		for(i=0;i<32;i++)
		{
			k=ManToType7[i];
			v1[i]=BV1[k]+v1[i]*BV2[k];
			if(k==6)		v1[i]+=BV3[ BA[SideOfMan[i]][tmany[i]][tmanx[i]] ];
		}
		for(i=0;i<32;i++)
		{
			for(j=0;j<32;j++)
			{
				if(v2[i][j])
				{
					if(SideOfMan[i]==SideOfMan[j])
					{
						v3[i]+=v1[j]>>5;//己方
						v4[j]++;
					}
					else
					{
						v3[i]+=v1[j]>>3;//对方
						v4[j]--;
					}
				}
			}
		}
		for(i=FistOfSide[tside];i<=LastOfSide[tside];i++)
		{
			if(tmanx[i])maxvalue+=v1[i]+v3[i];			
		}
		static BOOL flag;
		flag=FALSE;k=32;		
		for(i=FistOfSide[!tside];i<=LastOfSide[!tside];i++)
		{
			if(tmanx[i])maxvalue-=v1[i]+v3[i];			
//对方将军
			if(v2[i][FistOfSide[tside]])
			{
				flag=TRUE;
				k=i;
				break;
			}
		}

		if(flag)//被将
		{
			if(v4[k]>=0)//所将军的棋子不能被吃掉
			{
				j=0;
				for(i=FistOfSide[tside];i<=LastOfSide[tside];i++)
				{
					if(v4[i]<0 && v1[i]>j)	j=v1[i];
				}
				maxvalue -=j;
			}
		}
		else
		{
			j=0;
			for(i=FistOfSide[!tside];i<=LastOfSide[!tside];i++)
			{
				if(v4[i]<0 && v1[i]>j)
					j=v1[i];
			}
			maxvalue +=j;
		}
	}


_ENDSUB:	tmanx[man]=xs;							//这两行是:
	tmany[man]=ys;							//在face上的恢复
	tmap[xs][ys]=man;						//在map上的恢复
	if(ate!=32)
	{
		tmanx[ate]=tx;
		tmany[ate]=ty;
		tmap[tx][ty]=ate;
	}
	else tmap[tx][ty]=32;

	tside=!tside;

	tdeep--;
	return -maxvalue;
}

BOOL CThinker::LoadThinkSetting()
{
	CFile file;
	if(file.Open( "Thinker.set",CFile::modeRead))
	{
		file.SeekToBegin();
		file.Read(BV1,7*sizeof(int));
		file.Read(BV2,7*sizeof(int));
		file.Read(BV3,5*sizeof(int));
		file.Close();

		if(BVIsNormal())return TRUE;
		else
		{
			ResetBV();
			SaveThinkSetting();
			return FALSE;
		}		
	}
	MessageBox(NULL, "没找到 Thinker.set 文件\n\n这个文件并不是必需的,但它记录了你的设置内容,\n\n请不要删掉\n\n你现在可以通过菜单 \"文件\" -> \"参数\" 重新设定.","提醒",MB_OK|MB_ICONINFORMATION);
	ResetBV();
	SaveThinkSetting();
	return FALSE;

}

void CThinker::ResetBV()
{
	BV1[0]=0;	BV2[0]=0;
	BV1[1]=250;	BV2[1]=1;
	BV1[2]=250;	BV2[2]=1;
	BV1[3]=300;	BV2[3]=12;
	BV1[4]=400;	BV2[4]=6;
	BV1[5]=300;	BV2[5]=6;
	BV1[6]=100;	BV2[6]=15;
	BV3[0]=0;
	BV3[1]=70;
	BV3[2]=90;
	BV3[3]=110;
	BV3[4]=120;
}

BOOL CThinker::BVIsNormal()
{
	int i;
	for(i=0;i<=6;i++)
	{
		if(BV1[i]<0||BV1[i]>1000||BV2[i]<0||BV2[i]>1000)return FALSE;
	}
	for(i=0;i<=4;i++)
	{
		if(BV3[i]<0||BV3[i]>1000)return FALSE;
	}
	return TRUE;
}

BOOL CThinker::SaveThinkSetting()
{
	CFile file;
	if(file.Open("Thinker.set",CFile::modeWrite|CFile::modeCreate))
	{
		file.SeekToBegin();
		file.Write(BV1,7*sizeof(int));
		file.Write(BV2,7*sizeof(int));
		file.Write(BV3,5*sizeof(int));
		file.Close();
		return TRUE;
	}
	else return FALSE;	

}

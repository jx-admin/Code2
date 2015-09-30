// MoveList.cpp: implementation of the CMoveList class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "chess.h"
#include "MoveList.h"
#include "BaseDef.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CMoveList::CMoveList()
{
	now=NULL;
	head=NULL;
	end=NULL;
	count =0;
}

CMoveList::~CMoveList()
{
	now=head;
	while (now!=NULL)
	{
		now=now->next;
		delete head;
		head=now;
	}
}

void CMoveList::EnumMove(CFace &face)
{
	static CXY man;
	static int n,i,j,nb,nk;
	FixManMap(face,m_Map);

	if(end!=head)DelAll();

	 nb=face.side*16;
	 nk=face.side*16+15;
		for (n=nb;n<=nk;n++)
		{
			man=face.man[n];
			switch(n)
			{
			case 0:
				if(!man.x)break;
				if(CanGo(m_Map,0,man.x,man.y,man.x,face.man[16].y))	Add(0,man.x,face.man[16].y);			
				if(CanGo(m_Map,0,man.x,man.y,man.x+1,man.y))	Add(0,man.x+1,man.y);
				if(CanGo(m_Map,0,man.x,man.y,man.x-1,man.y))	Add(0,man.x-1,man.y);
				if(CanGo(m_Map,0,man.x,man.y,man.x,man.y+1))	Add(0,man.x,man.y+1);
				if(CanGo(m_Map,0,man.x,man.y,man.x,man.y-1))	Add(0,man.x,man.y-1);
				break;
			case 16:
				if(!man.x)break;
				if(CanGo(m_Map,16,man.x,man.y,man.x,face.man[0].y))	Add(n,man.x,face.man[0].y);			
				if(CanGo(m_Map,16,man.x,man.y,man.x+1,man.y))	Add(16,man.x+1,man.y);
				if(CanGo(m_Map,16,man.x,man.y,man.x-1,man.y))	Add(16,man.x-1,man.y);
				if(CanGo(m_Map,16,man.x,man.y,man.x,man.y+1))	Add(16,man.x,man.y+1);
				if(CanGo(m_Map,16,man.x,man.y,man.x,man.y-1))	Add(16,man.x,man.y-1);
				break;

			case 1:
			case 2:
			case 17:
			case 18:
				if(!man.x)break;
				if(CanGo(m_Map,n,man.x,man.y,man.x-1,man.y-1))	Add(n,man.x-1,man.y-1);
				if(CanGo(m_Map,n,man.x,man.y,man.x+1,man.y+1))	Add(n,man.x+1,man.y+1);
				if(CanGo(m_Map,n,man.x,man.y,man.x+1,man.y-1))	Add(n,man.x+1,man.y-1);
				if(CanGo(m_Map,n,man.x,man.y,man.x-1,man.y+1))	Add(n,man.x-1,man.y+1);
				break;

			case 3:
			case 4:
			case 19:
			case 20:
				if(!man.x)break;				
				if(CanGo(m_Map,n,man.x,man.y,man.x-2,man.y-2))	Add(n,man.x-2,man.y-2);
				if(CanGo(m_Map,n,man.x,man.y,man.x+2,man.y+2))	Add(n,man.x+2,man.y+2);
				if(CanGo(m_Map,n,man.x,man.y,man.x+2,man.y-2))	Add(n,man.x+2,man.y-2);
				if(CanGo(m_Map,n,man.x,man.y,man.x-2,man.y+2))	Add(n,man.x-2,man.y+2);
				break;

			case 5:
			case 6:
			case 21:
			case 22:
				if(!man.x)break;
				if(CanGo(m_Map,n,man.x,man.y,man.x-2,man.y-1))	Add(n,man.x-2,man.y-1);
				if(CanGo(m_Map,n,man.x,man.y,man.x+2,man.y+1))	Add(n,man.x+2,man.y+1);
				if(CanGo(m_Map,n,man.x,man.y,man.x+2,man.y-1))	Add(n,man.x+2,man.y-1);
				if(CanGo(m_Map,n,man.x,man.y,man.x-2,man.y+1))	Add(n,man.x-2,man.y+1);
				if(CanGo(m_Map,n,man.x,man.y,man.x-1,man.y-2))	Add(n,man.x-1,man.y-2);
				if(CanGo(m_Map,n,man.x,man.y,man.x+1,man.y+2))	Add(n,man.x+1,man.y+2);
				if(CanGo(m_Map,n,man.x,man.y,man.x+1,man.y-2))	Add(n,man.x+1,man.y-2);
				if(CanGo(m_Map,n,man.x,man.y,man.x-1,man.y+2))	Add(n,man.x-1,man.y+2);
				break;

			case 7:
			case 8:
			case 9:
			case 10:
			case 23:
			case 24:
			case 25:
			case 26:
				if(!man.x)break;
				for(i=1;i<=9;i++)	if(CanGo(m_Map,n,man.x,man.y,i,man.y))	Add(n,i,man.y);
				for(j=1;j<=10;j++)	if(CanGo(m_Map,n,man.x,man.y,man.x,j))	Add(n,man.x,j);
				break;

			case 27:
			case 28:
			case 29:
			case 30:
			case 31:
				if(!man.x)break;
				if(man.y>5)
				{
					if(CanGo(m_Map,n,man.x,man.y,man.x+1,man.y))	Add(n,man.x+1,man.y);
					if(CanGo(m_Map,n,man.x,man.y,man.x-1,man.y))	Add(n,man.x-1,man.y);
				}
				if(CanGo(m_Map,n,man.x,man.y,man.x,man.y+1))	Add(n,man.x,man.y+1);
				break;
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				if(!man.x)break;
				if(man.y<6)
				{
					if(CanGo(m_Map,n,man.x,man.y,man.x+1,man.y))	Add(n,man.x+1,man.y);
					if(CanGo(m_Map,n,man.x,man.y,man.x-1,man.y))	Add(n,man.x-1,man.y);
				}
				if(CanGo(m_Map,n,man.x,man.y,man.x,man.y-1))	Add(n,man.x,man.y-1);
				break;
			
			}
		}
}

void CMoveList::Add(int man, int x, int y)
{
	count ++;
	MOVENODE * p=new MOVENODE;
	p->move.man=man;
	p->move.x=x;
	p->move.y=y;

	if(end)
	{
		end->next=p;
		end=p;
	}
	else 
	{
		end=p;
		head=p;
		now=p;
	}
	end->next=NULL;
}

void CMoveList::DelAll()
{
	now=head;
	while (now!=NULL)
	{
		head=now;
		now=now->next;
		delete head;
	}

	head=NULL;
	now=NULL;
	end=NULL;
	count =0;
}

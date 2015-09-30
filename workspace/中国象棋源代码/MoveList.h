// MoveList.h: interface for the CMoveList class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_MOVELIST_H__069DD2ED_ADD6_11D4_9A6C_A237F78F8253__INCLUDED_)
#define AFX_MOVELIST_H__069DD2ED_ADD6_11D4_9A6C_A237F78F8253__INCLUDED_

#include "BaseClasses.h"	// Added by ClassView
#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

struct MOVENODE
{
	MOVENODE* next;
	CMove move;
};


class CMoveList  
{
public:
	void DelAll(void);
	void Add(int man,int x,int y);
	void EnumMove(CFace & face);
	MOVENODE* head;
	MOVENODE* end;
	MOVENODE* now;
	int m_Map[11][12];
	UINT count;
	CMoveList();
	virtual ~CMoveList();

};

#endif // !defined(AFX_MOVELIST_H__069DD2ED_ADD6_11D4_9A6C_A237F78F8253__INCLUDED_)

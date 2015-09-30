// StepList.cpp: implementation of the CStepList class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "Chess.h"
#include "BaseDef.h"
#include "BaseClasses.h"
#include "StepList.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CStepList::CStepList()
{
	m_pHead=new STEPNODE;
	m_pHead->left=NULL;
	m_pHead->right=NULL;
	m_pHead->Data=NULL;
	m_pCur=m_pHead;
}

CStepList::~CStepList()
{
	m_pCur=m_pHead;
	Remove();
	delete m_pHead;

}

void CStepList::Go(CStep *step)
{
	STEPNODE* node=new STEPNODE;
	
	node->left=m_pCur;
	node->Data=step;
	node->right=NULL;

	Remove();

	m_pCur->right=node;
	m_pCur=node;
}

void CStepList::Remove()
{
	STEPNODE * p;
	p=m_pCur;
	for(;p->right!=NULL;p=p->right);
	while(p!=m_pCur)
	{
		delete p->Data;
		p=p->left;
		delete p->right;
	}
	p->right=NULL;
}

CStep* CStepList::Undo()
{
	if(m_pCur==m_pHead)return NULL;

	m_pCur=m_pCur->left;
	return m_pCur->right->Data;
}

CStep* CStepList::Redo()
{
	if(m_pCur->right==NULL)return NULL;

	m_pCur=m_pCur->right;
	return m_pCur->Data;
}

void CStepList::RemoveAll()
{
	m_pCur=m_pHead;
	Remove();
}

BOOL CStepList::IsHead()
{
	if(m_pCur==m_pHead)return TRUE;

	return FALSE;
}

BOOL CStepList::IsEnd()
{
	if(m_pCur->right==NULL)return TRUE;

	return FALSE;
}


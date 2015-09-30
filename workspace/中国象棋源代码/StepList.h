// StepList.h: interface for the CStepList class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_STEPLIST_H__0142BEA0_2A62_11D4_9933_938D792DE10C__INCLUDED_)
#define AFX_STEPLIST_H__0142BEA0_2A62_11D4_9933_938D792DE10C__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

struct STEPNODE				//CStepList的结点
{
	STEPNODE* left;
	STEPNODE* right;
	CStep* Data;
};

class CStepList 
{
public:
	CStepList();	
	virtual ~CStepList();	//收回内存
	void Remove();			//删除m_pCur 后的所有结点(不包括*m_pCur)
	void Go(CStep*step);	//先Remove();然后增加一结点
	void RemoveAll();		//删除所有结点(不包括*m_pHead)
	BOOL IsEnd();			//m_pCur是否最后一结点
	BOOL IsHead();			//m_pCur是否等于m_pHead
	CStep* Redo();			//m_pCur后退一结点
	CStep* Undo();			//m_pCur前进一结点

protected:
	STEPNODE* m_pHead;		//栈底
	STEPNODE* m_pCur;		//栈尾(插入|删除 端)
};

#endif // !defined(AFX_STEPLIST_H__0142BEA0_2A62_11D4_9933_938D792DE10C__INCLUDED_)

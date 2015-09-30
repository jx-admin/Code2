// Thinker.h: interface for the CThinker class.
//
//////////////////////////////////////////////////////////////////////

#include "BaseClasses.h"

#if !defined(AFX_THINKER_H__994E259B_1A23_11D4_9933_BB99EA787221__INCLUDED_)
#define AFX_THINKER_H__994E259B_1A23_11D4_9933_BB99EA787221__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class CThinker  
{
public:
	BOOL SaveThinkSetting();
	BOOL LoadThinkSetting();
	int BV1[7];
	int BV2[7];
	int BV3[5];
	UINT GetPercent(void);
	void Cut(void);
	CThinker();
	void SetLevel(int level);
	virtual ~CThinker();
	void Think(CFace face);
	void Exit();
	BOOL IsThinkOver();
	BOOL GetMove(CMove& move,CFace facetothink);
	static UINT _bogusthreadfunc(LPVOID lpparam);

private:
	void ContactV(void);
	int SubThink(int man,int tx,int ty);
	BOOL EnumList(void);
	UINT ThinkProc(void);
protected:
	BOOL BVIsNormal();
	void ResetBV();
	CMove m_moveResult;
	BOOL m_bThinking;
	BOOL m_bThinkOver;
	BOOL m_bWaitForExit;
	BOOL m_bWaitForCut;
	BOOL m_bExited;
	UINT m_nLevel;
	UINT m_nPercent;
	CFace m_FaceToThink;
	CCriticalSection cs;
	CEvent m_Stoped;
	CEvent m_Cut;
	CWinThread* m_pThinkThread;
};

#endif // !defined(AFX_THINKER_H__994E259B_1A23_11D4_9933_BB99EA787221__INCLUDED_)

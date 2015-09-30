// MapFace.h: interface for the CMapFace class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_MAPFACE_H__069DD2EE_ADD6_11D4_9A6C_A237F78F8253__INCLUDED_)
#define AFX_MAPFACE_H__069DD2EE_ADD6_11D4_9A6C_A237F78F8253__INCLUDED_

#include "BaseClasses.h"	// Added by ClassView
#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class CMapFace  
{
public:
	int m_nManMap[11][12];		//纪录棋盘各棋位的状态(有无棋子/棋子序号)
	void FixManMap();		//每次更新m_Face时调用,更新m_nManMap
	CFace m_Face;
	CMapFace();
	virtual ~CMapFace();

};

#endif // !defined(AFX_MAPFACE_H__069DD2EE_ADD6_11D4_9A6C_A237F78F8253__INCLUDED_)

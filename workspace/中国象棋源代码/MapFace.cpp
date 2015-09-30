// MapFace.cpp: implementation of the CMapFace class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "chess.h"
#include "MapFace.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CMapFace::CMapFace()
{

}

CMapFace::~CMapFace()
{

}

void CMapFace::FixManMap()
{
	for(int i=0;i<11;i++)
		for(int j=0;j<12;j++)m_nManMap[i][j]=32;

	for(i=0;i<32;i++)
	{
		if(m_Face.man[i].x)
			m_nManMap[m_Face.man[i].x][m_Face.man[i].y]=i;
	}
		
}


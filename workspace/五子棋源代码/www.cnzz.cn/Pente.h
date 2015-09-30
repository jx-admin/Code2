// Pente.h : main header file for the PENTE application
//

#if !defined(AFX_PENTE_H__9A01CC66_D589_11D1_92B0_000021003EA5__INCLUDED_)
#define AFX_PENTE_H__9A01CC66_D589_11D1_92B0_000021003EA5__INCLUDED_

#if _MSC_VER >= 1000
#pragma once
#endif // _MSC_VER >= 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"		// main symbols

#define MODE_WITH_COMPUTER	0
#define MODE_2PLAYER		1
#define MODE_2PLAYER_NET	2
#define MODE_2PLAYER_MODEM	3

#define ID_TIMER_NET		100

#define MESSAGE_SIZE		512

/////////////////////////////////////////////////////////////////////////////
// CPenteApp:
// See Pente.cpp for the implementation of this class
//

class CPenteApp : public CWinApp
{
public:
	CPenteApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CPenteApp)
	public:
	virtual BOOL InitInstance();
	//}}AFX_VIRTUAL

// Implementation

	//{{AFX_MSG(CPenteApp)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Developer Studio will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_PENTE_H__9A01CC66_D589_11D1_92B0_000021003EA5__INCLUDED_)

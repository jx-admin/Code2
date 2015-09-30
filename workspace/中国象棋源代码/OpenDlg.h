#if !defined(AFX_OPENDLG_H__DCEB7D21_B144_11D4_9A6C_AD5D64042E55__INCLUDED_)
#define AFX_OPENDLG_H__DCEB7D21_B144_11D4_9A6C_AD5D64042E55__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// OpenDlg.h : header file
//
#include "CoolButton.h"

/////////////////////////////////////////////////////////////////////////////
// COpenDlg dialog

class COpenDlg : public CDialog
{
// Construction
public:
	COpenDlg(CWnd* pParent = NULL);   // standard constructor

// Dialog Data
	//{{AFX_DATA(COpenDlg)
	enum { IDD = IDD_OPEN };
	CCoolButton	m_btOK;
	CCoolButton	m_btCancel;
	CListBox	m_btList;
	CCoolButton	m_btDir;
	//}}AFX_DATA


// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(COpenDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	HICON m_hIconDir;

	// Generated message map functions
	//{{AFX_MSG(COpenDlg)
	virtual BOOL OnInitDialog();
	afx_msg void OnBtDir();
	afx_msg void OnPaint();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_OPENDLG_H__DCEB7D21_B144_11D4_9A6C_AD5D64042E55__INCLUDED_)

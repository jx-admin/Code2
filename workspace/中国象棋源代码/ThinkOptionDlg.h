#if !defined(AFX_THINKOPTIONDLG_H__A8780381_B129_11D4_9A6C_80DD079DDE4E__INCLUDED_)
#define AFX_THINKOPTIONDLG_H__A8780381_B129_11D4_9A6C_80DD079DDE4E__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// ThinkOptionDlg.h : header file
//

#include "CoolButton.h"

/////////////////////////////////////////////////////////////////////////////
// CThinkOptionDlg dialog

class CThinkOptionDlg : public CDialog
{
// Construction
public:
	CThinkOptionDlg(CWnd* pParent = NULL);   // standard constructor

// Dialog Data
	//{{AFX_DATA(CThinkOptionDlg)
	enum { IDD = IDD_THINKOPTION };
	CSpinButtonCtrl	m_spin17;
	CSpinButtonCtrl	m_spin16;
	CSpinButtonCtrl	m_spin15;
	CSpinButtonCtrl	m_spin14;
	CSpinButtonCtrl	m_spin13;
	CSpinButtonCtrl	m_spin9;
	CSpinButtonCtrl	m_spin8;
	CSpinButtonCtrl	m_spin7;
	CSpinButtonCtrl	m_spin6;
	CSpinButtonCtrl	m_spin5;
	CSpinButtonCtrl	m_spin4;
	CSpinButtonCtrl	m_spin3;
	CSpinButtonCtrl	m_spin2;
	CSpinButtonCtrl	m_spin12;
	CSpinButtonCtrl	m_spin11;
	CSpinButtonCtrl	m_spin10;
	CSpinButtonCtrl	m_spin1;
	CCoolButton	m_btCancel;
	CCoolButton	m_btOk;
	int		m_editb1;
	int		m_editb2;
	int		m_editj1;
	int		m_editj2;
	int		m_editm1;
	int		m_editm2;
	int		m_editp1;
	int		m_editp2;
	int		m_edits1;
	int		m_edits2;
	int		m_editx1;
	int		m_editx2;
	int		m_edit30;
	int		m_edit31;
	int		m_edit32;
	int		m_edit33;
	int		m_edit34;
	//}}AFX_DATA


// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CThinkOptionDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:

	// Generated message map functions
	//{{AFX_MSG(CThinkOptionDlg)
	virtual BOOL OnInitDialog();
	afx_msg void OnMyok();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_THINKOPTIONDLG_H__A8780381_B129_11D4_9A6C_80DD079DDE4E__INCLUDED_)

#if !defined(AFX_SETUPDLG_H__360B7442_D6B5_11D1_92B0_000021003EA5__INCLUDED_)
#define AFX_SETUPDLG_H__360B7442_D6B5_11D1_92B0_000021003EA5__INCLUDED_

#if _MSC_VER >= 1000
#pragma once
#endif // _MSC_VER >= 1000
// SetupDlg.h : header file
//

/////////////////////////////////////////////////////////////////////////////
// CSetupDlg dialog

class CSetupDlg : public CDialog
{
// Construction
public:
	CSetupDlg(CWnd* pParent = NULL);   // standard constructor

// Dialog Data
	//{{AFX_DATA(CSetupDlg)
	enum { IDD = IDD_SETUPDLG };
	CButton	m_RadioWithComputer;
	CButton	m_Radio2Player;
	CButton	m_RadioYouFirst;
	CButton	m_RadioComputerFirst;
	CButton	m_CheckSound;
	//}}AFX_DATA

	HICON m_hIcon;

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CSetupDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:

	// Generated message map functions
	//{{AFX_MSG(CSetupDlg)
	afx_msg void OnCheckSound();
	afx_msg void OnRadio2player();
	afx_msg void OnRadioComputerFirst();
	afx_msg void OnRadioWithcomputer();
	afx_msg void OnRadioYouFirst();
	virtual BOOL OnInitDialog();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Developer Studio will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_SETUPDLG_H__360B7442_D6B5_11D1_92B0_000021003EA5__INCLUDED_)

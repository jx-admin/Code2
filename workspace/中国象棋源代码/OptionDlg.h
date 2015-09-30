#if !defined(AFX_OPTIONDLG_H__778CB660_250D_11D4_9933_8EC28E61C20D__INCLUDED_)
#define AFX_OPTIONDLG_H__778CB660_250D_11D4_9933_8EC28E61C20D__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// OptionDlg.h : header file
//

/////////////////////////////////////////////////////////////////////////////
// COptionDlg dialog

class COptionDlg : public CDialog
{
// Construction
public:
	COptionDlg(CWnd* pParent = NULL);   // standard constructor
// Dialog Data
	//{{AFX_DATA(COptionDlg)
	enum { IDD = IDD_OPTION };
	CCoolButton	m_btOK;//确认
	CCoolButton	m_btReset;//回复默认
	CCoolButton	m_btCancel;//取消
	CSpinButtonCtrl	m_spinLevel;//
	int		m_nMode;
	int		m_CORM0;
	int		m_CORM1;
	int		m_nLevel;//棋手等级
	//}}AFX_DATA


// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(COptionDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:

	// Generated message map functions
	//{{AFX_MSG(COptionDlg)
	virtual BOOL OnInitDialog();
	afx_msg void OnReset();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_OPTIONDLG_H__778CB660_250D_11D4_9933_8EC28E61C20D__INCLUDED_)

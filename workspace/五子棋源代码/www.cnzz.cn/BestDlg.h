#if !defined(AFX_BESTDLG_H__0740A355_D3AB_11D1_A77A_444553540000__INCLUDED_)
#define AFX_BESTDLG_H__0740A355_D3AB_11D1_A77A_444553540000__INCLUDED_

#if _MSC_VER >= 1000
#pragma once
#endif // _MSC_VER >= 1000
// BestDlg.h : header file
//

/////////////////////////////////////////////////////////////////////////////
// CBestDlg dialog

class CBestDlg : public CDialog
{
// Construction
public:
	CBestDlg(CWnd* pParent = NULL);   // standard constructor

// Dialog Data
	//{{AFX_DATA(CBestDlg)
	enum { IDD = IDD_BEST };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA

	HICON m_hIcon;

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CBestDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	CEdit&     	Name1Edit() 
				{ return *(CEdit*)GetDlgItem(IDC_EDIT1); }
	CEdit&     	Name2Edit() 
				{ return *(CEdit*)GetDlgItem(IDC_EDIT2); }
	CEdit&     	Name3Edit() 
				{ return *(CEdit*)GetDlgItem(IDC_EDIT3); }
	CEdit&     	Time1Edit() 
				{ return *(CEdit*)GetDlgItem(IDC_EDIT4); }
	CEdit&     	Time2Edit() 
				{ return *(CEdit*)GetDlgItem(IDC_EDIT5); }
	CEdit&     	Time3Edit() 
				{ return *(CEdit*)GetDlgItem(IDC_EDIT6); }

	// Generated message map functions
	//{{AFX_MSG(CBestDlg)
	virtual BOOL OnInitDialog();
	afx_msg void OnRestartcount();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Developer Studio will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_BESTDLG_H__0740A355_D3AB_11D1_A77A_444553540000__INCLUDED_)

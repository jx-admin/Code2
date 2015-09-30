#if !defined(AFX_RECORDDLG_H__0740A356_D3AB_11D1_A77A_444553540000__INCLUDED_)
#define AFX_RECORDDLG_H__0740A356_D3AB_11D1_A77A_444553540000__INCLUDED_

#if _MSC_VER >= 1000
#pragma once
#endif // _MSC_VER >= 1000
// RecordDlg.h : header file
//

/////////////////////////////////////////////////////////////////////////////
// CRecordDlg dialog

class CRecordDlg : public CDialog
{
// Construction
public:
	CRecordDlg(CWnd* pParent = NULL);   // standard constructor

// Dialog Data
	//{{AFX_DATA(CRecordDlg)
	enum { IDD = IDD_RECORD };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA

	HICON m_hIcon;

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CRecordDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	CEdit&     	NameEdit() 
				{ return *(CEdit*)GetDlgItem(IDC_EDIT1); }
	CEdit&     	SkillEdit() 
				{ return *(CEdit*)GetDlgItem(IDC_EDIT2); }

	// Generated message map functions
	//{{AFX_MSG(CRecordDlg)
	virtual BOOL OnInitDialog();
	virtual void OnOK();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Developer Studio will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_RECORDDLG_H__0740A356_D3AB_11D1_A77A_444553540000__INCLUDED_)

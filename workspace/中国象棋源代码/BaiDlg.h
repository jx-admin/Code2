#if !defined(AFX_BAIDLG_H__59AF5EC0_29E2_11D4_9933_F061814DFD0D__INCLUDED_)
#define AFX_BAIDLG_H__59AF5EC0_29E2_11D4_9933_F061814DFD0D__INCLUDED_

#include "ChessDlg.h"

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// BaiDlg.h : header file
//

/////////////////////////////////////////////////////////////////////////////
// CBaiDlg dialog

class CBaiDlg : public CDialog
{
// Construction
public:
	CBaiDlg(CWnd* pParent = NULL);   // standard constructor

// Dialog Data
	//{{AFX_DATA(CBaiDlg)
	enum { IDD = IDD_BAI };
	CCoolButton	m_btSave;
	CCoolButton	m_btOpen;
	CCoolButton	m_btNormal;
	CCoolButton	m_btClear;
	CCoolButton	m_btCancel;
	CCoolButton	m_btOK;
	int		m_nGoSide;
	//}}AFX_DATA


// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CBaiDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	void New(const CFace& face);
	void Reset();
	void UpdateFace();
	BOOL GetFace(CFace& face);
	BOOL FaceToPoint(CPoint& point);

	int m_IconMap[10][11];
	CPoint m_Point;
	CDC* m_pdcBack;
	CDC* m_pdcBoard;
	CChessDlg* m_pParent;

	// Generated message map functions
	//{{AFX_MSG(CBaiDlg)
	virtual BOOL OnInitDialog();
	afx_msg void OnBaiok();
	afx_msg void OnPaint();
	afx_msg void OnRButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnBtClear();
	afx_msg void OnBtNormal();
	afx_msg void OnBlackB();
	afx_msg void OnBlackJ();
	afx_msg void OnBlackK();
	afx_msg void OnBlackM();
	afx_msg void OnBlackP();
	afx_msg void OnBlackS();
	afx_msg void OnBlackX();
	afx_msg void OnRedB();
	afx_msg void OnRedJ();
	afx_msg void OnRedK();
	afx_msg void OnRedM();
	afx_msg void OnRedP();
	afx_msg void OnRedS();
	afx_msg void OnRedX();
	afx_msg void OnBaiDelete();
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnBtOpen();
	afx_msg void OnBtSave();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_BAIDLG_H__59AF5EC0_29E2_11D4_9933_F061814DFD0D__INCLUDED_)

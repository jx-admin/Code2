// PenteDlg.h : header file
//

#if !defined(AFX_PENTEDLG_H__9A01CC68_D589_11D1_92B0_000021003EA5__INCLUDED_)
#define AFX_PENTEDLG_H__9A01CC68_D589_11D1_92B0_000021003EA5__INCLUDED_

#if _MSC_VER >= 1000
#pragma once
#endif // _MSC_VER >= 1000

/////////////////////////////////////////////////////////////////////////////
// CPenteDlg dialog

#include "Dib.h"

#define BOARD_LINE_NUMBER		15

class CPenteDlg : public CDialog
{
// Construction
public:
	CPenteDlg(CWnd* pParent = NULL);	// standard constructor
	~CPenteDlg();

// Dialog Data
	//{{AFX_DATA(CPenteDlg)
	enum { IDD = IDD_PENTE_DIALOG };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CPenteDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	HICON	m_hIcon;
	char	m_Board[BOARD_LINE_NUMBER][BOARD_LINE_NUMBER];	
	WORD	m_wXNull;
	WORD	m_wYNull;
	int		m_nPlayer;
	BYTE	m_byColor;
	WORD	m_cxGrid;
	WORD	m_cyGrid;    
	BOOL	m_bGameOver;       
	BOOL	m_bSoundOn;
	int		m_nType;
	WORD	m_wXCur1, m_wYCur1, m_wXCur2, m_wYCur2;
	CRect	m_rcCur1, m_rcCur2;
	WORD	m_wWhiteUndo, m_wBlackUndo;    
	BOOL	m_bCanUndo;	
	BOOL	m_bCanHint;
	CRect	m_rcHint;

	CMenu * m_pMenu;
	CDib  * m_pDibBoard;
	CDib  * m_pDibMask;
	CDib  * m_pDibBlack;
	CDib  * m_pDibWhite;
	WORD	m_wStoneWidth;
	WORD	m_wStoneHeight;

	void	PutStone(BYTE, CPoint, CDC *);
	void	Ring();
	int		Judge(int, int, char);
	CPoint	UrgentPoint(BYTE);
	CRect	GetCurStoneArea(CPoint);
	int		Distance(CPoint pt1, CPoint pt2);
	BOOL	IsStonePoint(CPoint& ptStone);
	BOOL	PointToStonePos(CPoint pt, CPoint& ptStone);
	void	InitParams();
	void	ReponseNew();

	// Generated message map functions
	//{{AFX_MSG(CPenteDlg)
	virtual BOOL OnInitDialog();
	virtual void OnCancel();
	virtual void OnOK(); 
	afx_msg void OnPaint();
	afx_msg void OnLButtonDown(UINT, CPoint);	
	afx_msg void OnNew();
	afx_msg void OnSound();	
	afx_msg void OnBest();	
	afx_msg void OnSkill1();
	afx_msg void OnSkill2();
	afx_msg void OnSkill3();	
	afx_msg void OnExit();
	afx_msg void OnAbout();
	afx_msg void OnHint();
	afx_msg void OnUndo();	
	afx_msg void OnLanguage();
	afx_msg BOOL OnSetCursor(CWnd* pWnd, UINT nHitTest, UINT message);
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg void OnPaletteChanged(CWnd* pFocusWnd);
	afx_msg BOOL OnQueryNewPalette();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnContextMenu(CWnd* pWnd, CPoint point);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Developer Studio will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_PENTEDLG_H__9A01CC68_D589_11D1_92B0_000021003EA5__INCLUDED_)

/*
Module : HLINKCTRL.H
Purpose: Interface for a MFC class for a static text control class with hyperlink support
Created: PJN / HLINK/1 / 16-06-1997
History: PJN           / 11-09-1997 Added support for a highlight color

Copyright (c) 1997 by PJ Naughter.  
All rights reserved.

*/


/////////////////////////// Macros ///////////////////////////

#ifndef __HLINKCTRL_H__
#define __HLINKCTRL_H__



/////////////////////////// Classes /////////////////////////////////


class CHLinkCtrl : public CStatic
{
public:
	//Constructors / Destructors
	CHLinkCtrl();
	virtual ~CHLinkCtrl();

	//Set or get the hyperlink to use
	void SetHyperLink(const CString& sActualLink);
	CString GetActualHyperLink() const { return m_sActualLink; };

  //Set or get the hyperlink description (really just the static's window text)
  void SetHyperLinkDescription(const CString& sDescription);
  CString GetHyperLinkDescription() const;

	//Set or get the hyperlink color 
	void SetLinkColor(const COLORREF& color);
	COLORREF GetLinkColor() { return m_Color; };

	//Set or get the hyperlink color for visited links
	void SetVisitedLinkColor(const COLORREF& color);
	COLORREF GetVisitedLinkColor() { return m_VisitedColor; };

	//Set or get the hyperlink color for highlighted links
	void SetHighlightLinkColor(const COLORREF& color);
	COLORREF GetHighlightLinkColor() { return m_HighlightColor; };
  void SetUseHighlightColor(BOOL bUseHighlight) { m_bUseHighlight = bUseHighlight; };

	//Gets whether the hyperlink has been visited
	BOOL GetVisited() { return m_State == ST_VISITED; };

  //Set or get whether the control should display a context menu
  void SetAllowContextMenu(BOOL bAllowContextMenu) { m_bAllowContextMenu = bAllowContextMenu; };
  BOOL GetAllowContextMenu() const { return m_bAllowContextMenu; };


  //Gets whether the window is automatically adjusted to 
  //the size of the description text displayed
	BOOL GetShrinkToFit()	{ return m_bShrinkToFit; }

  //Saves the hyperlink to an actual shortcut on file
#ifndef HLINK_NOOLE
  BOOL Save(const CString& sFilename) const;
#endif

#ifndef HLINK_NOOLE
  //Saves the hyperlink to the favorites folder
  BOOL AddToFavorites() const;
#endif

#ifndef HLINK_NOOLE
  //Saves the hyperlink to the desktop folder
  BOOL AddToDesktop() const;
#endif

  //Displays the properties dialog for this URL
  void ShowProperties() const;

  //Connects to the URL
  BOOL Open() const;

protected:
  enum State
  {
    ST_NOT_VISITED,
    ST_VISITED,
    ST_HIGHLIGHTED
  };

	//{{AFX_VIRTUAL(CHLinkCtrl)
	//}}AFX_VIRTUAL

	//{{AFX_MSG(CHLinkCtrl)
	afx_msg BOOL OnSetCursor(CWnd* pWnd, UINT nHitTest, UINT message);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnCopyShortcut();
	afx_msg void OnProperties();
  afx_msg void OnOpen();
  afx_msg void OnContextMenu(CWnd*, CPoint point);
	//}}AFX_MSG
	afx_msg HBRUSH CtlColor(CDC* pDC, UINT nCtlColor);
#ifndef HLINK_NOOLE
	afx_msg void OnAddToFavorites();
  afx_msg void OnAddToDesktop();
#endif

	void SetActualHyperLink(const CString& sActualLink);
	void HighLight(BOOL state);
#ifndef HLINK_NOOLE
  BOOL AddToSpecialFolder(int nFolder) const;
  BOOL OpenUsingCom() const;
#endif
  BOOL OpenUsingShellExecute() const;

	CString  m_sLinkDescription;
	CString  m_sActualLink;
	HCURSOR  m_hLinkCursor;
  HCURSOR  m_hArrowCursor;
	COLORREF m_Color;
	COLORREF m_VisitedColor;
  COLORREF m_HighlightColor;
	BOOL     m_bShrinkToFit;
  BOOL     m_bUseHighlight;
  State    m_State;
  State    m_OldState;
  BOOL     m_bShowingContext;
  CFont    m_font;
  BOOL     m_bAllowContextMenu;

	DECLARE_MESSAGE_MAP()
};


/*class CHLinkPage : public CPropertyPage
{
public:
	CHLinkPage();
	~CHLinkPage();
  void SetBuddy(const CHLinkCtrl* pBuddy) { m_pBuddy = pBuddy; };

	//{{AFX_DATA(CHLinkPage)
	enum { IDD = IDD_HLINK_PROPERTIES };
	//}}AFX_DATA

protected:
	//{{AFX_VIRTUAL(CHLinkPage)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

	DECLARE_DYNCREATE(CHLinkPage)

	//{{AFX_MSG(CHLinkPage)
	virtual BOOL OnInitDialog();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
  const CHLinkCtrl* m_pBuddy;
};


class CHLinkSheet : public CPropertySheet
{
public:
	CHLinkSheet(UINT nIDCaption, CWnd* pParentWnd = NULL, UINT iSelectPage = 0);
	CHLinkSheet(LPCTSTR pszCaption, CWnd* pParentWnd = NULL, UINT iSelectPage = 0);
	virtual ~CHLinkSheet();
  void SetBuddy(const CHLinkCtrl* pBuddy) { m_page1.SetBuddy(pBuddy); };


protected:
	//{{AFX_VIRTUAL(CHLinkSheet)
	//}}AFX_VIRTUAL

	DECLARE_DYNAMIC(CHLinkSheet)

	//{{AFX_MSG(CHLinkSheet)
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()

  CHLinkPage  m_page1;
};

*/









#endif //__HLINKCTRL_H__


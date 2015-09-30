/*
Module : HLINKCTRL.CPP
Purpose: Implementation for a MFC class for a static text control class with hyperlink support
Created: PJN / HLINK/1 / 16-06-1997
History: PJN /         / 24-06-1997, hyperlinks are now coloured ala IE
         PJN /         / 15-07-1997, Now supports ShrinkToFit
         PJN           / 11-09-1997, Added support for a highlight look
                                     which includes a highlight color and
                                     a shadowed look. Also fixed a bug 
                                     relating to the Edit controls context
                                     menu.
                                     Also included is a new context menu ala IE3
         PJN           / 06-11-1997, 1) Improved drawing of control by handling WM_CTLCOLOR through MFC message reflection
                                     2) class now derived from CStatic as it should have been from the begining
                                     3) Dropped support for shrink to fit as now no need
                                     4) Description is now taken from controls window text
                                     5) Now using more standard visited and unvisted colors
                                     6) Dropped support for optional underlying and drop shadow effects
                                     7) streamlined some of the functions declarations
                                     8) Wait cursor is now shown when a shortcut is being saved
                                     9) Context menu for control can now be turned off if so desired
         PJN           / 08-12-1997, 1) Removed ON_COMMAND(ID_POPUP_OPEN, OnOpen) from #define which was causing 
                                        to the hyperlink correctly
                                     2) Removed a level 4 warning which was being generated when building in release mode  
         PJN           / 09-01-1998  1) Removed duplicate OnAddToFavorites and OnAddToDesktop functions


Copyright (c) 1997 by PJ Naughter.  
All rights reserved.

*/

/////////////////////////////////  Includes  //////////////////////////////////

#include "stdafx.h"

#ifndef HLINK_NOOLE
#define INITGUID
#endif
#include "resource.h"
#include "hlinkctl.h"
#ifndef HLINK_NOOLE
#include <initguid.h>
#endif
#include <winnetwk.h>
#include <winnls.h>
#include <shlobj.h>
#ifndef HLINK_NOOLE
#include <intshcut.h>
#endif



/////////////////////////////////  Macros & Statics ///////////////////////////

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif


///////////////////////////////// Implementation //////////////////////////////


BEGIN_MESSAGE_MAP(CHLinkCtrl, CStatic)
	ON_WM_CONTEXTMENU()
	//{{AFX_MSG_MAP(CHLinkCtrl)
  ON_WM_SETCURSOR()
  ON_WM_LBUTTONDOWN()
	ON_WM_MOUSEMOVE()
	ON_COMMAND(ID_POPUP_COPYSHORTCUT, OnCopyShortcut)
	ON_COMMAND(ID_POPUP_PROPERTIES, OnProperties)
	//}}AFX_MSG_MAP
	ON_WM_CTLCOLOR_REFLECT()
  ON_COMMAND(ID_POPUP_OPEN, OnOpen)
#ifndef HLINK_NOOLE
	ON_COMMAND(ID_POPUP_ADDTOFAVORITES, OnAddToFavorites)
  ON_COMMAND(ID_POPUP_ADDTODESKTOP, OnAddToDesktop)
#endif
END_MESSAGE_MAP()


CHLinkCtrl::CHLinkCtrl()
{
	m_Color = RGB(0, 0, 255);
	m_VisitedColor = RGB(128, 0, 128);
	m_HighlightColor = RGB(255, 0, 0);

	m_bShrinkToFit = TRUE;
  m_bUseHighlight = TRUE;
	m_State = ST_NOT_VISITED;
  m_OldState = ST_NOT_VISITED;
  m_bShowingContext = FALSE;
  m_bAllowContextMenu = TRUE;

	//Load up the cursors
	m_hLinkCursor = AfxGetApp()->LoadCursor(IDC_HLINK);
  m_hArrowCursor = AfxGetApp()->LoadStandardCursor(IDC_ARROW);
}


CHLinkCtrl::~CHLinkCtrl()
{
}


void CHLinkCtrl::SetHyperLink(const CString& sActualLink)
{
	SetActualHyperLink(sActualLink);
}


void CHLinkCtrl::SetHyperLinkDescription(const CString& sDescription)
{
  SetWindowText(sDescription);
}


CString CHLinkCtrl::GetHyperLinkDescription() const
{
  CString sDescription;
  GetWindowText(sDescription);
  return sDescription;
}


void CHLinkCtrl::SetActualHyperLink(const CString& sActualLink)
{
	m_sActualLink = sActualLink;
}


BOOL CHLinkCtrl::OnSetCursor(CWnd* /*pWnd*/, UINT /*nHitTest*/, UINT /*message*/) 
{
  if (m_bShowingContext)
    ::SetCursor(m_hArrowCursor);
  else
  	::SetCursor(m_hLinkCursor);
	return TRUE;
}


void CHLinkCtrl::OnLButtonDown(UINT /*nFlags*/, CPoint /*point*/) 
{
  PostMessage(WM_COMMAND, ID_POPUP_OPEN);
}


void CHLinkCtrl::OnOpen()
{
  if (Open())
	  m_State = ST_VISITED;
}


void CHLinkCtrl::SetLinkColor(const COLORREF& color) 
{ 
	m_Color = color; 
	UpdateWindow(); 
}


void CHLinkCtrl::SetVisitedLinkColor(const COLORREF& color) 
{ 
	m_VisitedColor = color; 
	UpdateWindow(); 
}


void CHLinkCtrl::SetHighlightLinkColor(const COLORREF& color) 
{ 
	m_HighlightColor = color; 
	UpdateWindow(); 
}


void CHLinkCtrl::OnMouseMove(UINT nFlags, CPoint point) 
{
  if (!m_bUseHighlight)
    return;

	CRect rc;
	GetClientRect(rc);
	if (rc.PtInRect(point))
	{
		if (m_State != ST_HIGHLIGHTED)
		{
			SetCapture();
			HighLight(TRUE);
		} 
	}
	else
	{
		if (m_State == ST_HIGHLIGHTED)
		{
			HighLight(FALSE);
			ReleaseCapture();
		}
	}
	
	CStatic::OnMouseMove(nFlags, point);
}


void CHLinkCtrl::HighLight(BOOL state)
{
	if (state)
	{
		if (m_State != ST_HIGHLIGHTED)
		{
			m_OldState = m_State;
			m_State = ST_HIGHLIGHTED;
			Invalidate();
		}
	}
	else
	{
		if (m_State == ST_HIGHLIGHTED)
		{
			m_State = m_OldState;
			Invalidate();
		}
	}
}


void CHLinkCtrl::OnContextMenu(CWnd*, CPoint point)
{
  if (!m_bAllowContextMenu)
    return;

  HighLight(FALSE);
	ReleaseCapture();
  
	if (point.x == -1 && point.y == -1)
  {
		//keystroke invocation
		CRect rect;
		GetClientRect(rect);
		ClientToScreen(rect);

		point = rect.TopLeft();
		point.Offset(5, 5);
	}

	CMenu menu;
	VERIFY(menu.LoadMenu(IDR_HLINK_POPUP));

	CMenu* pPopup = menu.GetSubMenu(0);
	ASSERT(pPopup != NULL);
	CWnd* pWndPopupOwner = this;

  m_bShowingContext = TRUE;
	pPopup->TrackPopupMenu(TPM_LEFTALIGN | TPM_RIGHTBUTTON, point.x, point.y,
		pWndPopupOwner);
  m_bShowingContext = FALSE;
}


void CHLinkCtrl::OnCopyShortcut() 
{
  if (OpenClipboard())
  {
    int nBytes = sizeof(TCHAR) * (m_sActualLink.GetLength() + 1);
    HANDLE hMem = GlobalAlloc(GMEM_MOVEABLE | GMEM_DDESHARE, nBytes);
    TCHAR* pData = (TCHAR*) GlobalLock(hMem);
    _tcscpy(pData, (LPCTSTR) m_sActualLink);
    GlobalUnlock(hMem);
  	SetClipboardData(CF_TEXT, hMem);
    CloseClipboard();
  }
}


void CHLinkCtrl::OnProperties() 
{
	ShowProperties();
}


void CHLinkCtrl::ShowProperties() const
{
	CHLinkSheet propSheet(IDS_HLINK_PROPERTIES, this->GetParent());
  propSheet.m_psh.dwFlags |= PSH_NOAPPLYNOW;
  propSheet.SetBuddy(this);
  propSheet.DoModal();
}


BOOL CHLinkCtrl::Open() const
{
  CWaitCursor cursor;

#ifndef HLINK_NOOLE
  //First try to open using IUniformResourceLocator
  BOOL bSuccess = OpenUsingCom();
  
  //As a last resort try ShellExecuting the URL, may
  //even work on Navigator!
  if (!bSuccess)
    bSuccess = OpenUsingShellExecute();
#else
  BOOL bSuccess = OpenUsingShellExecute();
#endif

  return bSuccess;
}


#ifndef HLINK_NOOLE
BOOL CHLinkCtrl::OpenUsingCom() const
{
  //Get the URL Com interface
  IUniformResourceLocator* pURL;
  HRESULT hres = CoCreateInstance(CLSID_InternetShortcut, NULL, CLSCTX_INPROC_SERVER, IID_IUniformResourceLocator, (void**) &pURL);
  if (!SUCCEEDED(hres))
  {
    TRACE("Failed to get the IUniformResourceLocator interface\n");
    return FALSE;
  }

  hres = pURL->SetURL(m_sActualLink, IURL_SETURL_FL_GUESS_PROTOCOL);
  if (!SUCCEEDED(hres))
  {
    TRACE("Failed in call to SetURL\n");
    pURL->Release();
    return FALSE;
  }

  //Open the URL by calling InvokeCommand
  URLINVOKECOMMANDINFO ivci;
  ivci.dwcbSize = sizeof(URLINVOKECOMMANDINFO);
  ivci.dwFlags = IURL_INVOKECOMMAND_FL_ALLOW_UI;
  ivci.hwndParent = GetParent()->GetSafeHwnd();
  ivci.pcszVerb = "open";
  hres = pURL->InvokeCommand(&ivci);
  if (!SUCCEEDED(hres))
  {
    TRACE("Failed to invoke URL using InvokeCommand\n");
    pURL->Release();
    return FALSE;
  }

  // Release the pointer to IUniformResourceLocator.
  pURL->Release();

  return TRUE;
}
#endif


BOOL CHLinkCtrl::OpenUsingShellExecute() const
{
	HINSTANCE hRun = ShellExecute(GetParent()->GetSafeHwnd(), _T("open"), m_sActualLink, NULL, NULL, SW_SHOW);
  if ((int) hRun <= 32)
  {
    TRACE("Failed to invoke URL using ShellExecute\n");
    return FALSE;
  }

  return TRUE;
}


#ifndef HLINK_NOOLE
BOOL CHLinkCtrl::AddToSpecialFolder(int nFolder) const
{
  //Give the user some feedback
  CWaitCursor cursor;

  // Get the shell's allocator. 
  IMalloc* pMalloc;
  if (!SUCCEEDED(SHGetMalloc(&pMalloc))) 
  {
    TRACE("Failed to get the shell's IMalloc interface\n");
    return FALSE;
  }

  //Get the location of the special Folder required
  LPITEMIDLIST pidlFolder;
  HRESULT hres = SHGetSpecialFolderLocation(NULL, nFolder, &pidlFolder); 
  if (!SUCCEEDED(hres))
  {
    TRACE("Failed in call to SHGetSpecialFolderLocation\n");
    pMalloc->Release();
    return FALSE;
  }

  //convert the PIDL to a file system name and
  //add an extension of URL to create an Internet 
  //Shortcut file
  TCHAR sFolder[_MAX_PATH];
  if (!SHGetPathFromIDList(pidlFolder, sFolder))
  {
    TRACE("Failed in call to SHGetPathFromIDList");
    pMalloc->Release();
    return FALSE;
  }
  TCHAR sShortcutFile[_MAX_PATH];  

  CString sLinkDescription;
  GetWindowText(sLinkDescription);
  _tmakepath(sShortcutFile, NULL, sFolder, sLinkDescription, _T("URL"));

  //Free the pidl
  pMalloc->Free(pidlFolder);

  //Do the actual saving
  BOOL bSuccess = Save(sShortcutFile);

  // Release the pointer to IMalloc
  pMalloc->Release(); 

  return bSuccess;
}
#endif


#ifndef HLINK_NOOLE
void CHLinkCtrl::OnAddToFavorites() 
{
  AddToSpecialFolder(CSIDL_FAVORITES);
} 
#endif


#ifndef HLINK_NOOLE
void CHLinkCtrl::OnAddToDesktop() 
{
  AddToSpecialFolder(CSIDL_DESKTOP);
} 
#endif


#ifndef HLINK_NOOLE
BOOL CHLinkCtrl::Save(const CString& sFilename) const
{
  //Get the URL Com interface
  IUniformResourceLocator* pURL;
  HRESULT hres = CoCreateInstance(CLSID_InternetShortcut, NULL, CLSCTX_INPROC_SERVER, IID_IUniformResourceLocator, (void**) &pURL);
  if (!SUCCEEDED(hres))
  {
    TRACE("Failed to get the IUniformResourceLocator interface\n");
    return FALSE;
  }

  hres = pURL->SetURL(m_sActualLink, IURL_SETURL_FL_GUESS_PROTOCOL);
  if (!SUCCEEDED(hres))
  {
    TRACE("Failed in call to SetURL\n");
    pURL->Release();
    return FALSE;
  }

  // Get the IPersistFile interface for 
  // saving the shortcut in persistent storage.
  IPersistFile* ppf;
  hres = pURL->QueryInterface(IID_IPersistFile, (void **)&ppf);
  if (!SUCCEEDED(hres))
  {
    TRACE("Failed to get the IPersistFile interface\n");
    pURL->Release();
    return FALSE;
  }

  // Save the shortcut via the IPersistFile::Save member function.
  #ifndef _UNICODE
  WORD wsz[_MAX_PATH];
  MultiByteToWideChar(CP_ACP, 0, sFilename, -1, wsz, _MAX_PATH);
  hres = ppf->Save(wsz, TRUE);
  #else
  hres = ppf->Save(sFilename, TRUE);
  #endif
  if (!SUCCEEDED(hres))
  {
    TRACE("IPersistFile::Save failed!\n");
    ppf->Release();
    pURL->Release();
    return FALSE;
  }

  // Release the pointer to IPersistFile.
  ppf->Release();

  // Release the pointer to IUniformResourceLocator.
  pURL->Release();

  return TRUE;
} 
#endif


#ifdef _DEBUG
HBRUSH CHLinkCtrl::CtlColor(CDC* pDC, UINT nCtlColor) 
#else
HBRUSH CHLinkCtrl::CtlColor(CDC* pDC, UINT /*nCtlColor*/)
#endif
{
	ASSERT(nCtlColor == CTLCOLOR_STATIC);

	DWORD dwStyle = GetStyle();
	if (!(dwStyle & SS_NOTIFY)) 
  {
		// Turn on notify flag to get mouse messages and STN_CLICKED.
		// Otherwise, I'll never get any mouse clicks!
		::SetWindowLong(m_hWnd, GWL_STYLE, dwStyle | SS_NOTIFY);
	}
	

	HBRUSH hbr = NULL;
	if ((dwStyle & 0xFF) <= SS_RIGHT) 
  {
		// Modify the font to be underline
    if (!((HFONT) m_font))
    {
		  LOGFONT lf;
		  GetFont()->GetObject(sizeof(lf), &lf);

		  lf.lfUnderline = TRUE;
		  m_font.CreateFontIndirect(&lf);
    }
		pDC->SelectObject(&m_font);


    //set the text colors
    switch (m_State)
    {
		  case ST_NOT_VISITED:	pDC->SetTextColor(m_Color); break;
		  case ST_VISITED:	  	pDC->SetTextColor(m_VisitedColor); break;
		  case ST_HIGHLIGHTED:	pDC->SetTextColor(m_HighlightColor); break;
      default: ASSERT(FALSE);
    }
		pDC->SetBkMode(TRANSPARENT);

		// return hollow brush to preserve parent background color
		hbr = (HBRUSH)::GetStockObject(HOLLOW_BRUSH);
	}

	return hbr;
}






/*IMPLEMENT_DYNCREATE(CHLinkPage, CPropertyPage)


CHLinkPage::CHLinkPage() : CPropertyPage(CHLinkPage::IDD)
{
	//{{AFX_DATA_INIT(CHLinkPage)
	//}}AFX_DATA_INIT
  m_pBuddy = NULL;
}


CHLinkPage::~CHLinkPage()
{
}

void CHLinkPage::DoDataExchange(CDataExchange* pDX)
{
	CPropertyPage::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CHLinkPage)
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(CHLinkPage, CPropertyPage)
	//{{AFX_MSG_MAP(CHLinkPage)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()




IMPLEMENT_DYNAMIC(CHLinkSheet, CPropertySheet)

CHLinkSheet::CHLinkSheet(UINT nIDCaption, CWnd* pParentWnd, UINT iSelectPage)
	:CPropertySheet(nIDCaption, pParentWnd, iSelectPage)
{
  AddPage(&m_page1);
}


CHLinkSheet::CHLinkSheet(LPCTSTR pszCaption, CWnd* pParentWnd, UINT iSelectPage)
	:CPropertySheet(pszCaption, pParentWnd, iSelectPage)
{
  AddPage(&m_page1);
}


CHLinkSheet::~CHLinkSheet()
{
}


BEGIN_MESSAGE_MAP(CHLinkSheet, CPropertySheet)
	//{{AFX_MSG_MAP(CHLinkSheet)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()


BOOL CHLinkPage::OnInitDialog() 
{
	CPropertyPage::OnInitDialog();
	
	ASSERT(m_pBuddy);
  GetDlgItem(IDC_NAME)->SetWindowText(m_pBuddy->GetHyperLinkDescription());
  GetDlgItem(IDC_URL)->SetWindowText(m_pBuddy->GetActualHyperLink());

	return TRUE;
}
*/

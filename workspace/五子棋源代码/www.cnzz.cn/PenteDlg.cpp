// PenteDlg.cpp : implementation file
//

#include "stdafx.h"
#include <math.h>
#include <mmsystem.h>
#include "Pente.h"
#include "dib.h"
#include "setupdlg.h"
#include "AboutDlg.h"
#include "RecordDlg.h"
#include "BestDlg.h"
#include "Globalvar0.h"
#include "HelperAPI.h"
#include "PenteDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CPenteDlg dialog

CPenteDlg::CPenteDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CPenteDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(CPenteDlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
	// Note that LoadIcon does not require a subsequent DestroyIcon in Win32

	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);

	m_pMenu = new CMenu();
	m_pDibBoard = new CDib;
	m_pDibMask	= new CDib;
	m_pDibBlack = new CDib;
	m_pDibWhite = new CDib;

	InitParams();

	//////////////////////////////////////////       
	char   szVer[512];
	///int nVer = GetWindowsVersion((LPSTR)szVer);
	///if (stricmp(szVer, "Simplified Chinese")&&stricmp(szVer, "Chinese (PRC)")&&
	///	stricmp(szVer, "中文")&&stricmp(szVer, "简体中文"))
	///    g_bChinese = FALSE;
	//////////////////////////////////////////

	g_nSkill = AfxGetApp()->GetProfileInt("Settings", "Skill", 1);
	g_bSoundOn = AfxGetApp()->GetProfileInt("Settings", "SoundOn", 1);
    g_bUserBlack = AfxGetApp()->GetProfileInt("Settings", "UserUseBlackStone", 1);
	g_nRunMode =  AfxGetApp()->GetProfileInt("Settings", "Player", MODE_WITH_COMPUTER);    
	g_nTime1 = AfxGetApp()->GetProfileInt("Settings", "Time1", 225);    
	g_nTime2 = AfxGetApp()->GetProfileInt("Settings", "Time2", 225);    
	g_nTime3 = AfxGetApp()->GetProfileInt("Settings", "Time3", 225);    
	g_strName1 = AfxGetApp()->GetProfileString("Settings", "Name1", "Anonymous");    
	g_strName2 = AfxGetApp()->GetProfileString("Settings", "Name2", "Anonymous");    
	g_strName3 = AfxGetApp()->GetProfileString("Settings", "Name3", "Anonymous");    
}

CPenteDlg::~CPenteDlg()
{
	delete m_pMenu;
	delete m_pDibBoard;
	delete m_pDibMask;
	delete m_pDibBlack;
	delete m_pDibWhite;
	AfxGetApp()->WriteProfileInt("Settings", "Skill", g_nSkill);
	AfxGetApp()->WriteProfileInt("Settings", "SoundOn", g_bSoundOn);
    AfxGetApp()->WriteProfileInt("Settings", "UserUseBlackStone", g_bUserBlack);
	AfxGetApp()->WriteProfileInt("Settings", "Player", g_nRunMode);    
	AfxGetApp()->WriteProfileInt("Settings", "Time1", g_nTime1);    
	AfxGetApp()->WriteProfileInt("Settings", "Time2", g_nTime2);    
	AfxGetApp()->WriteProfileInt("Settings", "Time3", g_nTime3);    
	AfxGetApp()->WriteProfileString("Settings", "Name1", g_strName1);    
	AfxGetApp()->WriteProfileString("Settings", "Name2", g_strName2);    
	AfxGetApp()->WriteProfileString("Settings", "Name3", g_strName3);    
}

void CPenteDlg::InitParams()
{
	for (int i=0; i<BOARD_LINE_NUMBER;++i)
	    for (int j=0; j<BOARD_LINE_NUMBER; j++)
	    	m_Board[i][j] = 0;	

	m_bGameOver = FALSE;	              
	m_byColor = 0;            
	m_bCanUndo = FALSE;
	m_bCanHint = TRUE;
	m_wWhiteUndo = 0;
	m_wBlackUndo = 0;
	g_nStoneNum = 0;
	m_rcCur1.SetRectEmpty();
	m_rcCur2.SetRectEmpty();	
	m_rcHint.SetRectEmpty();
}

void CPenteDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CPenteDlg)
		// NOTE: the ClassWizard will add DDX and DDV calls here
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CPenteDlg, CDialog)
	//{{AFX_MSG_MAP(CPenteDlg)
	ON_WM_PAINT()
	ON_WM_LBUTTONDOWN()                           	
	ON_COMMAND(IDM_NEW, OnNew)			
	ON_COMMAND(IDM_SOUND, OnSound)			
	ON_COMMAND(IDM_BEST, OnBest)				
	ON_COMMAND(IDM_GRADE1, OnSkill1)					
	ON_COMMAND(IDM_GRADE2, OnSkill2)					
	ON_COMMAND(IDM_GRADE3, OnSkill3)							
	ON_COMMAND(IDM_EXIT, OnExit)			
	ON_COMMAND(IDM_ABOUT, OnAbout)			
	ON_COMMAND(IDM_HINT, OnHint)					
	ON_COMMAND(IDM_UNDO, OnUndo)
	ON_COMMAND(IDM_LANGUAGE, OnLanguage)
	ON_WM_SETCURSOR()
	ON_WM_QUERYDRAGICON()
	ON_WM_PALETTECHANGED()
	ON_WM_QUERYNEWPALETTE()
	ON_WM_SYSCOMMAND()
	ON_WM_CONTEXTMENU()
	ON_COMMAND(IDM_HELP_CONTEXT, OnHelp)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CPenteDlg message handlers

BOOL CPenteDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon
	
	// Check system display ability
	CDC *pDC = GetDC();
	int nSystemColors = pDC->GetDeviceCaps(BITSPIXEL);
	if (nSystemColors<8)	// 2**8 == 256
	{
		CString str1, str2;
		str1.LoadString(g_bChinese ? IDS_COLOR_LESS_CHINESE:IDS_COLOR_LESS_ENGLISH);
		str2.LoadString(g_bChinese ? IDS_TITLE_CHINESE:IDS_TITLE_ENGLISH);
    	if (MessageBox(str1, str2, MB_ICONQUESTION|MB_YESNO) != IDYES)
    	{
			CDialog::EndDialog(0);
			return TRUE;  					// return from InitDialog
		}
    }

	// Load Dibs
	m_pDibBoard->LoadDib(IDDIB_BOARD);
    m_pDibMask->LoadDib(IDDIB_MASK);
	m_pDibBlack->LoadDib(IDDIB_BLACK);
	m_pDibWhite->LoadDib(IDDIB_WHITE);
	m_pDibBoard->MakeBitmap(pDC);
    m_pDibMask->MakeBitmap(pDC);
	m_pDibBlack->MakeBitmap(pDC);
	m_pDibWhite->MakeBitmap(pDC);
	m_pDibBoard->GetPalette();
    m_pDibMask->GetPalette();
	m_pDibBlack->GetPalette();
	m_pDibWhite->GetPalette();
	ReleaseDC(pDC);
	////////////////////////////////////////

    CSetupDlg setupDlg(this);
    if (setupDlg.DoModal()==IDCANCEL)
	{
		CDialog::EndDialog(0);
		return TRUE;  					// return from InitDialog
	}

	CString str;
	if (g_nRunMode==MODE_WITH_COMPUTER && g_bUserBlack)
		str.LoadString(g_bChinese ? IDS_SINGLE_USER_CHINESE:IDS_SINGLE_USER_ENGLISH);
	else if (g_nRunMode==MODE_WITH_COMPUTER && !g_bUserBlack)
		str.LoadString(g_bChinese ? IDS_SINGLE_COMPUTER_CHINESE:IDS_SINGLE_COMPUTER_ENGLISH);
	else if	(g_nRunMode==MODE_2PLAYER)
    	str.LoadString(g_bChinese ? IDS_DOUBLE_CHINESE:IDS_DOUBLE_ENGLISH);
	SetWindowText(str);

	m_pMenu->DestroyMenu();
	m_pMenu->LoadMenu(g_bChinese ? IDR_MENU_CHINESE : IDR_MENU_ENGLISH);
	SetMenu(m_pMenu);
	m_pMenu->CheckMenuItem(IDM_SOUND, g_bSoundOn ? MF_CHECKED : MF_UNCHECKED);
	m_pMenu->EnableMenuItem(IDM_UNDO, m_bCanUndo ? MF_ENABLED : MF_GRAYED);   
	m_pMenu->EnableMenuItem(IDM_HINT, m_bCanHint&&(g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	m_pMenu->EnableMenuItem(IDM_GRADE1, (g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	m_pMenu->EnableMenuItem(IDM_GRADE2, (g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	m_pMenu->EnableMenuItem(IDM_GRADE3, (g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	if (g_nSkill == 1)
	{
		m_pMenu->CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_CHECKED);  
		m_pMenu->CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_UNCHECKED);
		m_pMenu->CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_UNCHECKED);
	}
	else if (g_nSkill == 2)
	{
		m_pMenu->CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_CHECKED);  
		m_pMenu->CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_UNCHECKED);
		m_pMenu->CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_UNCHECKED);
	}
	else if (g_nSkill == 3)
	{          
		m_pMenu->CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_CHECKED);  
		m_pMenu->CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_UNCHECKED);
		m_pMenu->CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_UNCHECKED);
	}
			
	// Adjust the 4 values to suit board
	m_wXNull = 13;	           
	m_wYNull = 13;
	m_cxGrid = 23;
	m_cyGrid = 23;

	// Stone size
	m_wStoneWidth  = m_pDibMask->GetWidth();
	m_wStoneHeight = m_pDibMask->GetHeight();

	int cxScreen = ::GetSystemMetrics(SM_CXSCREEN);
	int cyScreen = ::GetSystemMetrics(SM_CYSCREEN);
	int cxDlgFrame = ::GetSystemMetrics(SM_CXDLGFRAME);
	int cyDlgFrame = ::GetSystemMetrics(SM_CYDLGFRAME);
	int cxCaption = ::GetSystemMetrics(SM_CYCAPTION); 
	int cyMenu = ::GetSystemMetrics(SM_CYMENU);
	int nWidth  = m_pDibBoard->GetWidth() + 2*cxDlgFrame;
	int nHeight = m_pDibBoard->GetHeight() + cxCaption + 2*cyDlgFrame + cyMenu;
	MoveWindow((cxScreen-nWidth)/2, (cyScreen-nHeight)/2,
			   nWidth, nHeight);
	
	PlaySound(IDSOUND_WELCOME);
	
	if (g_nRunMode==MODE_WITH_COMPUTER && !g_bUserBlack)
	{
		CPoint pt;            			// Computer first
		m_byColor = !m_byColor;			// 1-Black  0-White
		m_Board[7][7]=m_byColor+1;
		pt.x = 7*m_cxGrid + m_wXNull;
		pt.y = 7*m_cyGrid + m_wYNull;
		InvalidateRect(GetCurStoneArea(pt), FALSE);
		UpdateWindow();
		ClientToScreen(&pt);
		SetCursorPos(pt.x, pt.y);
		PlaySound(IDSOUND_PUTSTONE);
		Judge(7, 7, m_Board[7][7]);
		Ring();
	}   
	
	return TRUE;  // return TRUE  unless you set the focus to a control
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CPenteDlg::OnPaint() 
{
	CPaintDC dc(this); // device context for painting
	if (IsIconic())
	{
		SendMessage(WM_ICONERASEBKGND, (WPARAM) dc.GetSafeHdc(), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		m_pDibBoard->Display(&dc, CPoint(0,0));

		for (int i=0; i<BOARD_LINE_NUMBER; ++i)
			for (int j=0; j<BOARD_LINE_NUMBER; ++j)
		    	if (m_Board[i][j])             
					PutStone(m_Board[i][j]-1, CPoint(i,j), &dc);

		CDialog::OnPaint();
	}
}

// The system calls this to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CPenteDlg::OnQueryDragIcon()
{
	return (HCURSOR) m_hIcon;
}

void CPenteDlg::OnLButtonDown(UINT nFlag, CPoint point)
{       
	CPoint pt;
	if (PointToStonePos(point, pt))
	{
		int nX = pt.x;
		int nY = pt.y;
		if (m_Board[nX][nY]==0 && !m_bGameOver)      
		{
			if (g_nRunMode == MODE_WITH_COMPUTER)
			{
		 		if ((g_bUserBlack && m_byColor==0) || 
	 				(!g_bUserBlack && m_byColor==1)) 
				{                                    
					// User
					m_byColor = !m_byColor;			// 1-Black  0-White
					m_Board[nX][nY]=m_byColor+1;
					m_wXCur1 = nX;
					m_wYCur1 = nY;
					CPoint pt(nX*m_cxGrid + m_wXNull, nY*m_cyGrid + m_wYNull);
					m_rcCur1 = GetCurStoneArea(pt);
					InvalidateRect(m_rcCur1, FALSE);
					InvalidateRect(m_rcHint, FALSE);
					UpdateWindow();
					PlaySound(IDSOUND_PUTSTONE);    
					Judge(nX, nY, m_Board[nX][nY]);
					Ring();
                       
					// Computer                       
					if (!m_bGameOver)
					{
						m_byColor = !m_byColor;			// 1-Black  0-White
						CPoint ptBest = UrgentPoint(m_byColor);
						m_Board[ptBest.x][ptBest.y]=m_byColor+1;
						m_wXCur2 = (WORD)ptBest.x;
						m_wYCur2 = (WORD)ptBest.y;
						pt.x = ptBest.x*m_cxGrid + m_wXNull;
						pt.y = ptBest.y*m_cyGrid + m_wYNull;
						m_rcCur2 = GetCurStoneArea(pt);
						InvalidateRect(m_rcCur2, FALSE);
						UpdateWindow();
						ClientToScreen(&pt);
						SetCursorPos(pt.x, pt.y);
						PlaySound(IDSOUND_PUTSTONE);
						m_bCanUndo = TRUE;
						m_pMenu->EnableMenuItem(IDM_UNDO, m_bCanUndo ? MF_ENABLED : MF_GRAYED);   
						Judge(ptBest.x, ptBest.y, m_Board[ptBest.x][ptBest.y]);
						Ring();
					}
				}
			}
			else if (g_nRunMode == MODE_2PLAYER)			// Users
			{
				m_byColor = !m_byColor;			// 1-Black  0-White
				m_Board[nX][nY]=m_byColor+1;
				m_wXCur1 = m_wXCur2;
				m_wYCur1 = m_wYCur2;
				m_wXCur2 = nX;
				m_wYCur2 = nY;
				CPoint pt(nX*m_cxGrid + m_wXNull, nY*m_cyGrid + m_wYNull);
				m_rcCur1 = m_rcCur2;
				m_rcCur2 = GetCurStoneArea(pt);
				InvalidateRect(m_rcCur2, FALSE);
				UpdateWindow();
				PlaySound(IDSOUND_PUTSTONE);                 
				if (!m_rcCur1.IsRectEmpty())
				{       
					m_bCanUndo = TRUE;
					m_pMenu->EnableMenuItem(IDM_UNDO, m_bCanUndo ? MF_ENABLED : MF_GRAYED);   
				}
				Judge(nX, nY, m_Board[nX][nY]);
				Ring();
			}
		}	            
		else
			PlaySound(IDSOUND_ERROR);
	}
	else
		PlaySound(IDSOUND_ERROR);

	CDialog::OnLButtonDown(nFlag, point);
}             
          
int CPenteDlg::Distance(CPoint pt1, CPoint pt2)
{
	return (int)sqrt((double)(pt1.x-pt2.x)*(pt1.x-pt2.x) + (double)(pt1.y-pt2.y)*(pt1.y-pt2.y));
}

BOOL CPenteDlg::IsStonePoint(CPoint& ptStone)
{
	if (ptStone.x<0 || ptStone.x>=BOARD_LINE_NUMBER || ptStone.y<0 || ptStone.y>=BOARD_LINE_NUMBER)
	{
		if (ptStone.x<0)
			ptStone.x = 0;
		else if (ptStone.x>=BOARD_LINE_NUMBER)
			ptStone.x = BOARD_LINE_NUMBER-1;
		if (ptStone.y<0)
			ptStone.y = 0;
		else if (ptStone.y>=BOARD_LINE_NUMBER)
			ptStone.y = BOARD_LINE_NUMBER-1;
		
		return FALSE;
	}

	return TRUE;
}

BOOL CPenteDlg::PointToStonePos(CPoint pt, CPoint& ptStone)
{
	int nPosX = (pt.x - m_wXNull)/m_cxGrid;
	int nPosY = (pt.y - m_wYNull)/m_cyGrid;
	CPoint pt0(nPosX*m_cxGrid+m_wXNull, nPosY*m_cyGrid+m_wYNull);
	CPoint pt1((nPosX+1)*m_cxGrid+m_wXNull, nPosY*m_cyGrid+m_wYNull);
	CPoint pt2((nPosX+1)*m_cxGrid+m_wXNull, (nPosY+1)*m_cyGrid+m_wYNull);
	CPoint pt3(nPosX*m_cxGrid+m_wXNull, (nPosY+1)*m_cyGrid+m_wYNull);
	
	int nDis0 = Distance(pt, pt0);
	int nDis1 = Distance(pt, pt1);
	int nDis2 = Distance(pt, pt2);
	int nDis3 = Distance(pt, pt3);
	int nLimit = m_wStoneWidth/2-4;
	if (nDis0 <= nLimit)
		ptStone = CPoint(nPosX, nPosY);
	else if (nDis1 <= nLimit)
		ptStone = CPoint(nPosX+1, nPosY);
	else if (nDis2 <= nLimit)
		ptStone = CPoint(nPosX+1, nPosY+1);
	else if (nDis3 <= nLimit)
		ptStone = CPoint(nPosX, nPosY+1);
	else
	{
		int nMin1 = min(nDis0, nDis1);
		int nMin2 = min(nDis2, nDis3);
		int nMin = min(nMin1, nMin2);

		if (nMin == nDis0)
			ptStone = CPoint(nPosX, nPosY);
		else if (nMin == nDis1)
			ptStone = CPoint(nPosX+1, nPosY);
		else if (nMin == nDis2)
			ptStone = CPoint(nPosX+1, nPosY+1);
		else if (nMin == nDis3)
			ptStone = CPoint(nPosX, nPosY+1);

		IsStonePoint(ptStone);

		return FALSE;
	}

	return IsStonePoint(ptStone);
}

CPoint CPenteDlg::UrgentPoint(BYTE byColor)
{           
	int i, i0, j, j0;             

	BeginWaitCursor();
	                 
	CPoint  ptUrgent[2025], ptInit(-1,-1);
	for (i=0; i<2025; ++i)
		ptUrgent[i] = ptInit;
              
    // Seed the random-number generator with current time so that
    // the numbers will be different every time we run.
    //
    srand((unsigned)time(NULL));
	CPoint ptCurrent;
	i0=(rand()%2) ? 0:BOARD_LINE_NUMBER-1;               
	int nGrade1, nGrade2;
	int nUrgent1, nUrgent2, nUrgent;
	
	for (i=i0; !i0 ? i<BOARD_LINE_NUMBER:i>=0; !i0 ? ++i:--i)
	{
	    srand((unsigned)time(NULL));
	    j0=(rand()%2) ? 0:BOARD_LINE_NUMBER-1;
	    for (j=j0; !j0 ? j<BOARD_LINE_NUMBER:j>=0; !j0 ? ++j:--j)
	    {
	    	if (m_Board[i][j]==0)
	    	{
        		ptCurrent.x = i;
        		ptCurrent.y = j;
                
				nGrade1 = Judge(i, j, (char)(byColor+1));
				nGrade2 = Judge(i, j, (char)(!byColor+1));
				
				if (g_nSkill == 3)
				{
		    		switch (nGrade1)
		    		{
						case 0 : nUrgent1 = 0; break;
						case 1 : nUrgent1 = 2; break;					
						case 2 : nUrgent1 = 4; break;					
						case 3 : nUrgent1 = 5; break;					
						case 4 : nUrgent1 = 8; break;					
						
						case 5 : nUrgent1 = 10; break;																									
						case 6 : nUrgent1 = 11; break;					
						case 7 : nUrgent1 = 12; break;					
						case 8 : nUrgent1 = 13; break;					
 						case 9 : nUrgent1 = 14; break;					
						case 10 : nUrgent1 = 15; break;					
						case 11 : nUrgent1 = 16; break;					
						case 12 : nUrgent1 = 17; break;					
						case 13 : nUrgent1 = 18; break;					
						case 14 : nUrgent1 = 19; break;					
						case 15 : nUrgent1 = 20; break;					
						
						case 16 : nUrgent1 = 32; break;					
						case 17 : nUrgent1 = 34; break;					
						case 18 : nUrgent1 = 36; break;					
						case 19 : nUrgent1 = 38; break;					
						case 20 : nUrgent1 = 40; break;					
						default : nUrgent1 = 40; break;					
					}
		    		switch (nGrade2)
		    		{
						case 0 : nUrgent2 = 1; break;
						case 1 : nUrgent2 = 3; break;					
						case 2 : nUrgent2 = 6; break;					
						case 3 : nUrgent2 = 7; break;					
						case 4 : nUrgent2 = 9; break;					
						
						case 5 : nUrgent2 = 21; break;																									
						case 6 : nUrgent2 = 22; break;					
						case 7 : nUrgent2 = 23; break;					
						case 8 : nUrgent2 = 24; break;					
						case 9 : nUrgent2 = 25; break;					
						case 10 : nUrgent2 = 26; break;					
						case 11 : nUrgent2 = 27; break;					
						case 12 : nUrgent2 = 28; break;					
						case 13 : nUrgent2 = 29; break;					
						case 14 : nUrgent2 = 30; break;					
						case 15 : nUrgent2 = 31; break;					
						
						case 16 : nUrgent2 = 33; break;											
						case 17 : nUrgent2 = 35; break;					
						case 18 : nUrgent2 = 37; break;					
						case 19 : nUrgent2 = 39; break;					
						case 20 : nUrgent2 = 41; break;					
						default : nUrgent2 = 41; break;											
					}
				}
				if (g_nSkill == 2)
				{
		    		switch (nGrade1)
		    		{
						case 0 : nUrgent1 = 0; break;
						case 1 : nUrgent1 = 2; break;					
						case 2 : nUrgent1 = 4; break;					
						case 3 : nUrgent1 = 5; break;					
						case 4 : nUrgent1 = 8; break;					
						
						case 5 : 
						case 6 : 
						case 7 : nUrgent1 = 10; break;					
						case 8 : 
 						case 9 : 
						case 10 : nUrgent1 = 11; break;					
						case 11 : nUrgent1 = 12; break;					
						case 12 : nUrgent1 = 13; break;					
						case 13 : 
						case 14 : 
						case 15 : nUrgent1 = 14; break;					
						
						case 16 : nUrgent1 = 20; break;					
						case 17 : nUrgent1 = 22; break;					
						case 18 : nUrgent1 = 24; break;					
						case 19 : nUrgent1 = 26; break;					
						case 20 : nUrgent1 = 28; break;					
						default : nUrgent1 = 28; break;					
					}
		    		switch (nGrade2)
		    		{
						case 0 : nUrgent2 = 1; break;
						case 1 : nUrgent2 = 3; break;					
						case 2 : nUrgent2 = 6; break;					
						case 3 : nUrgent2 = 7; break;					
						case 4 : nUrgent2 = 9; break;					
						
						case 5 : 
						case 6 : 
						case 7 : nUrgent2 = 15; break;					
						case 8 : 
						case 9 : 
						case 10 : nUrgent2 = 16; break;					
						case 11 : nUrgent2 = 17; break;					
						case 12 : nUrgent2 = 18; break;					
						case 13 :
						case 14 :
						case 15 : nUrgent2 = 19; break;					
						
						case 16 : nUrgent2 = 21; break;											
						case 17 : nUrgent2 = 23; break;					
						case 18 : nUrgent2 = 25; break;					
						case 19 : nUrgent2 = 27; break;					
						case 20 : nUrgent2 = 29; break;					
						default : nUrgent2 = 29; break;											
					}
				}
				if (g_nSkill == 1)
				{
		    		switch (nGrade1)
		    		{
						case 0 : nUrgent1 = 0; break;
						case 1 : nUrgent1 = 2; break;					
						case 2 : nUrgent1 = 4; break;					
						case 3 : nUrgent1 = 5; break;					
						case 4 : nUrgent1 = 8; break;					
						
						case 5 : 
						case 6 : 
						case 7 : 
						case 8 : 
 						case 9 : 
						case 10 : nUrgent1 = 10; break;					
						case 11 : 
						case 12 : 
						case 13 : 
						case 14 : 
						case 15 : nUrgent1 = 11; break;					
						
						case 16 : nUrgent1 = 14; break;					
						case 17 : nUrgent1 = 16; break;					
						case 18 : nUrgent1 = 18; break;					
						case 19 : nUrgent1 = 20; break;					
						case 20 : nUrgent1 = 22; break;					
						default : nUrgent1 = 22; break;					
					}
		    		switch (nGrade2)
		    		{
						case 0 : nUrgent2 = 1; break;
						case 1 : nUrgent2 = 3; break;					
						case 2 : nUrgent2 = 6; break;					
						case 3 : nUrgent2 = 7; break;					
						case 4 : nUrgent2 = 9; break;					
						
						case 5 : 
						case 6 : 
						case 7 : 
						case 8 : 
						case 9 : 
						case 10 : nUrgent2 = 12; break;					
						case 11 :
						case 12 :
						case 13 :
						case 14 :
						case 15 : nUrgent2 = 13; break;					
						
						case 16 : nUrgent2 = 15; break;											
						case 17 : nUrgent2 = 17; break;					
						case 18 : nUrgent2 = 19; break;					
						case 19 : nUrgent2 = 21; break;					
						case 20 : nUrgent2 = 23; break;					
						default : nUrgent2 = 23; break;											
					}
				}
                
	            nUrgent = min(nUrgent1, nUrgent2)*45 + max(nUrgent1, nUrgent2);
				ptUrgent[nUrgent] = ptCurrent;
            }
		}
    }

	for (i=0; i<2025; ++i)   
		if (ptUrgent[i] != ptInit)              
			break;

    EndWaitCursor();
    
	if (ptUrgent[i] == ptInit)              
	{
		CString str1, str2;
		str1.LoadString(g_bChinese ? IDS_DRAW_CHINESE:IDS_DRAW_ENGLISH);
		str2.LoadString(g_bChinese ? IDS_TITLE_CHINESE:IDS_TITLE_ENGLISH);
		MessageBox(str1, str2);
	}
	
	return ptUrgent[i];
}

int CPenteDlg::Judge(int nX, int nY, char cValue)
{
	int  nGrade;
	int  i, j, k, l;
	int  nXStart, nXEnd;
	int  nYStart, nYEnd;
	int  nXYStart, nXYEnd;
	int  nYXStart, nYXEnd;
	int  nXStartAdd, nYStartAdd, nXYStartAdd, nYXStartAdd;            
	int  nXEndAdd, nYEndAdd, nXYEndAdd, nYXEndAdd;            
	BOOL bXStartEmpty, bXEndEmpty, bXStartEmpty1, bXEndEmpty1;
	BOOL bYStartEmpty, bYEndEmpty, bYStartEmpty1, bYEndEmpty1;
	BOOL bXYStartEmpty, bXYEndEmpty, bXYStartEmpty1, bXYEndEmpty1;
	BOOL bYXStartEmpty, bYXEndEmpty, bYXStartEmpty1, bYXEndEmpty1;
		
	nXStart  = nXEnd  = nX;
	nYStart  = nYEnd  = nY;
	nXYStart = nXYEnd = nX;
	nYXStart = nYXEnd = nX;                                           
	nXStartAdd = nYStartAdd = nXYStartAdd = nYXStartAdd = 0;            	
	nXEndAdd = nYEndAdd = nXYEndAdd = nYXEndAdd = 0;            	
	bXStartEmpty = bYStartEmpty = bXYStartEmpty = bYXStartEmpty = FALSE;
	bXEndEmpty = bYEndEmpty = bXYEndEmpty = bYXEndEmpty = FALSE;
	bXStartEmpty1 = bYStartEmpty1 = bXYStartEmpty1 = bYXStartEmpty1 = FALSE;
	bXEndEmpty1 = bYEndEmpty1 = bXYEndEmpty1 = bYXEndEmpty1 = FALSE;
	
	for (i=nX-1; i>=0; i--)            // <-
	{
	    if (m_Board[i][nY]==cValue)
	    	nXStart = i;
	    else if (m_Board[i][nY]==0) 
	    {
			bXStartEmpty = TRUE;
			for (j=i-1; j>=0; j--)            // <-
			{
			    if (m_Board[j][nY]==cValue)
	    			nXStartAdd = i-j;
			    else if (m_Board[j][nY]==0) 
			    {
					bXStartEmpty1 = TRUE;
					break;
				}
				else
					break;
			}
            break;
        }                         
        else
        	break;
	}
	for (i=nX+1; i<BOARD_LINE_NUMBER; i++)           // ->
	{
	    if (m_Board[i][nY]==cValue)
	    	nXEnd = i;
		else if (m_Board[i][nY]==0)
		{
			bXEndEmpty = TRUE;     
			for (j=i+1; j<BOARD_LINE_NUMBER; j++)            // ->
			{
			    if (m_Board[j][nY]==cValue)
	    			nXEndAdd = j-i;
			    else if (m_Board[j][nY]==0) 
			    {
					bXEndEmpty1 = TRUE;
					break;
				}
				else
					break;
			}
            break;
	    }
	    else
	    	break;
	}
	    	
	for (i=nY-1; i>=0; i--)            // ^|^
	{
	    if (m_Board[nX][i]==cValue)
	    	nYStart = i;
		else if (m_Board[nX][i]==0)
		{
			bYStartEmpty = TRUE;   
			for (j=i-1; j>=0; j--)            // <-
			{
			    if (m_Board[nX][j]==cValue)
	    			nYStartAdd = i-j;
			    else if (m_Board[nX][j]==0) 
			    {
					bYStartEmpty1 = TRUE;
					break;
				}
				else
					break;
			}
            break;
		}
	    else
	    	break;
	}
	for (i=nY+1; i<BOARD_LINE_NUMBER; i++)           // v|v
	{
	    if (m_Board[nX][i]==cValue)
	    	nYEnd = i;
		else if (m_Board[nX][i]==0)
		{
			bYEndEmpty = TRUE;     
			for (j=i+1; j<BOARD_LINE_NUMBER; j++)            // ->
			{
			    if (m_Board[nX][j]==cValue)
	    			nYEndAdd = j-i;
			    else if (m_Board[nX][j]==0) 
			    {
					bYEndEmpty1 = TRUE;
					break;
				}
				else
					break;
			}
            break;
		}
	    else
	    	break;
	}
	
	//j = nY;
	for (i=nX-1, j=nY+1; i>=0&&j<BOARD_LINE_NUMBER; i--, j++)            // /'
	{
	    //j++;
	    if (m_Board[i][j]==cValue)
	    	nXYStart = i;
		else if (m_Board[i][j]==0)
		{
			bXYStartEmpty = TRUE; 
			for (k=i-1, l=j+1; k>=0&&l<BOARD_LINE_NUMBER; k--, l++)            // /'
			{
			    if (m_Board[k][l]==cValue)
	    			nXYStartAdd = i-k;
				else if (m_Board[k][l]==0)
				{
					bXYStartEmpty1 = TRUE; 
                    break;
                }
                else
                	break;
            }
			break;
		}
	    else
	    	break;
	}   
	//j = nY;
	for (i=nX+1, j=nY-1; i<BOARD_LINE_NUMBER&&j>=0; i++, j--)           // ./
	{      
		//j--;
	    if (m_Board[i][j]==cValue)
	    	nXYEnd = i;
		else if (m_Board[i][j]==0)
		{
			bXYEndEmpty = TRUE;   
			for (k=i+1, l=j-1; l>=0&&k<BOARD_LINE_NUMBER; l--, k++)            // /'
			{
			    if (m_Board[k][l]==cValue)
	    			nXYEndAdd = k-i;
				else if (m_Board[k][l]==0)
				{
					bXYEndEmpty1 = TRUE; 
                    break;
                }
                else
                	break;
            }
			break;
		}
	    else
	    	break;
	}
	    	
	//j = nY;
	for (i=nX-1, j=nY-1; i>=0&&j>=0; i--, j--)            // '`
	{
	    //j--;
	    if (m_Board[i][j]==cValue)
	    	nYXStart = i;
		else if (m_Board[i][j]==0)
		{
			bYXStartEmpty = TRUE; 
			for (k=i-1, l=j-1; k>=0&&l>=0; k--, l--)            // /'
			{
			    if (m_Board[k][l]==cValue)
	    			nYXStartAdd = i-k;
				else if (m_Board[k][l]==0)
				{
					bYXStartEmpty1 = TRUE; 
                    break;
                }
                else
                	break;
            }
			break;
		}
	    else
	    	break;
	}   
	//j = nY;
	for (i=nX+1, j=nY+1; i<BOARD_LINE_NUMBER&&j<BOARD_LINE_NUMBER; i++, j++)           // `.
	{      
		//j++;
	    if (m_Board[i][j]==cValue)
	    	nYXEnd = i;
		else if (m_Board[i][j]==0)
		{
			bYXEndEmpty = TRUE;   
			for (k=i+1, l=j+1; l<BOARD_LINE_NUMBER&&k<BOARD_LINE_NUMBER; l++, k++)            // /'
			{
			    if (m_Board[k][l]==cValue)
	    			nYXEndAdd = k-i;
				else if (m_Board[k][l]==0)
				{
					bYXEndEmpty1 = TRUE; 
                    break;
                }
                else
                	break;
            }
			break;
		}
	    else
	    	break;
	}
	        
	int nXStep  = nXEnd-nXStart+1;
	int nYStep  = nYEnd-nYStart+1;
	int nXYStep = nXYEnd-nXYStart+1;
	int nYXStep = nYXEnd-nYXStart+1;
	
	BOOL bX_4 = ((nXStep==4)&&(bXStartEmpty&&bXEndEmpty));
	BOOL bY_4 = ((nYStep==4)&&(bYStartEmpty&&bYEndEmpty));
	BOOL bXY_4 = ((nXYStep==4)&&(bXYStartEmpty&&bXYEndEmpty));
	BOOL bYX_4 = ((nYXStep==4)&&(bYXStartEmpty&&bYXEndEmpty));
	BOOL bX4 = ((nXStep==4)&&(bXStartEmpty||bXEndEmpty));
	BOOL bY4 = ((nYStep==4)&&(bYStartEmpty||bYEndEmpty));
	BOOL bXY4 = ((nXYStep==4)&&(bXYStartEmpty||bXYEndEmpty));
	BOOL bYX4 = ((nYXStep==4)&&(bYXStartEmpty||bYXEndEmpty));

	BOOL bX_3 = ((nXStep==3)&&(bXStartEmpty&&bXEndEmpty));
	BOOL bY_3 = ((nYStep==3)&&(bYStartEmpty&&bYEndEmpty));
	BOOL bXY_3 = ((nXYStep==3)&&(bXYStartEmpty&&bXYEndEmpty));
	BOOL bYX_3 = ((nYXStep==3)&&(bYXStartEmpty&&bYXEndEmpty));

	BOOL bX3 = ((nXStep==3)&&(bXStartEmpty||bXEndEmpty));
	BOOL bY3 = ((nYStep==3)&&(bYStartEmpty||bYEndEmpty));
	BOOL bXY3 = ((nXYStep==3)&&(bXYStartEmpty||bXYEndEmpty));
	BOOL bYX3 = ((nYXStep==3)&&(bYXStartEmpty||bYXEndEmpty));

	BOOL bX_2 = ((nXStep==2)&&(bXStartEmpty&&bXEndEmpty));
	BOOL bY_2 = ((nYStep==2)&&(bYStartEmpty&&bYEndEmpty));
	BOOL bXY_2 = ((nXYStep==2)&&(bXYStartEmpty&&bXYEndEmpty));
	BOOL bYX_2 = ((nYXStep==2)&&(bYXStartEmpty&&bYXEndEmpty));
	BOOL bX2 = ((nXStep==2)&&(bXStartEmpty||bXEndEmpty));
	BOOL bY2 = ((nYStep==2)&&(bYStartEmpty||bYEndEmpty));
	BOOL bXY2 = ((nXYStep==2)&&(bXYStartEmpty||bXYEndEmpty));
	BOOL bYX2 = ((nYXStep==2)&&(bYXStartEmpty||bYXEndEmpty));

  	BOOL bX_1 = ((nXStep==1)&&(bXStartEmpty&&bXEndEmpty));
  	BOOL bY_1 = ((nYStep==1)&&(bYStartEmpty&&bYEndEmpty));
  	BOOL bXY_1 = ((nXYStep==1)&&(bXYStartEmpty&&bXYEndEmpty));
  	BOOL bYX_1 = ((nYXStep==1)&&(bYXStartEmpty&&bYXEndEmpty));

	int nXAdd, nYAdd, nXYAdd, nYXAdd;
	nXAdd = nYAdd = nXYAdd = nYXAdd = 0;

	if (nXEndAdd>=nXStartAdd)
	{
		nXAdd = nXEndAdd;
		bXEndEmpty = bXEndEmpty1;
	}
	else                      
	{
		nXAdd = nXStartAdd;
		bXStartEmpty = bXStartEmpty1;
    }
    
	if (nYEndAdd>=nYStartAdd)
	{
		nYAdd = nYEndAdd;
		bYEndEmpty = bYEndEmpty1;
	}
	else                      
	{
		nYAdd = nYStartAdd;
		bYStartEmpty = bYStartEmpty1;
    }

	if (nXYEndAdd>=nXYStartAdd)
	{
		nXYAdd = nXYEndAdd;
		bXYEndEmpty = bXYEndEmpty1;
	}
	else                      
	{
		nXYAdd = nXYStartAdd;
		bXYStartEmpty = bXYStartEmpty1;
    }

	if (nYXEndAdd>=nYXStartAdd)
	{
		nYXAdd = nYXEndAdd;
		bYXEndEmpty = bYXEndEmpty1;
	}
	else                      
	{
		nYXAdd = nYXStartAdd;
		bYXStartEmpty = bYXStartEmpty1;
    }
	
	BOOL b1X_4 = ((nXStep+nXAdd>=4)&&(bXStartEmpty&&bXEndEmpty));
	BOOL b1Y_4 = ((nYStep+nYAdd>=4)&&(bYStartEmpty&&bYEndEmpty));
	BOOL b1XY_4 = ((nXYStep+nXYAdd>=4)&&(bXYStartEmpty&&bXYEndEmpty));
	BOOL b1YX_4 = ((nYXStep+nYXAdd>=4)&&(bYXStartEmpty&&bYXEndEmpty));
	BOOL b1X4 = ((nXStep+nXAdd>=4)&&(bXStartEmpty||bXEndEmpty));
	BOOL b1Y4 = ((nYStep+nYAdd>=4)&&(bYStartEmpty||bYEndEmpty));
	BOOL b1XY4 = ((nXYStep+nXYAdd>=4)&&(bXYStartEmpty||bXYEndEmpty));
	BOOL b1YX4 = ((nYXStep+nYXAdd>=4)&&(bYXStartEmpty||bYXEndEmpty));

	BOOL b1X_3 = ((nXStep+nXAdd==3)&&(bXStartEmpty&&bXEndEmpty));
	BOOL b1Y_3 = ((nYStep+nYAdd==3)&&(bYStartEmpty&&bYEndEmpty));
	BOOL b1XY_3 = ((nXYStep+nXYAdd==3)&&(bXYStartEmpty&&bXYEndEmpty));
	BOOL b1YX_3 = ((nYXStep+nYXAdd==3)&&(bYXStartEmpty&&bYXEndEmpty));
	
	m_nType = -1;	                           
	
	////////	
	if (nXStep>=5 || nYStep>=5 || nXYStep>=5 || nYXStep>=5)
	{
		nGrade = 0;
		m_nType = 0;
	}
	////////	
	else if (bX_4 || bY_4 || bXY_4 || bYX_4)
	{
		nGrade = 1;             
		m_nType = 1;
	}
	////////	
	else if ((bX4 && (bY4 || bXY4 || bYX4 || b1Y4 || b1XY4 || b1YX4))||
			 (bY4 && (bX4 || bXY4 || bYX4 || b1X4 || b1XY4 || b1YX4))||
			 (bXY4 && (bY4 || bX4 || bYX4 || b1Y4 || b1X4 || b1YX4)) ||
			 (bYX4 && (bY4 || bXY4 || bX4 || b1Y4 || b1XY4 || b1X4)) ||
			 (b1X4 && (bY4 || bXY4 || bYX4 || b1Y4 || b1XY4 || b1YX4))||
			 (b1Y4 && (bX4 || bXY4 || bYX4 || b1X4 || b1XY4 || b1YX4))||
			 (b1XY4 && (bY4 || bX4 || bYX4 || b1Y4 || b1X4 || bYX4))  ||
			 (b1YX4 && (bY4 || bXY4 || bX4 || b1Y4 || b1XY4 || b1X4)))
	{
		nGrade = 2;
		m_nType = 1;
	}		
    /////////
	else if ((bX4 && (bY_3 || bXY_3 || bYX_3 || b1Y_3 || b1XY_3 || b1YX_3))||
	         (bY4 && (bX_3 || bXY_3 || bYX_3 || b1X_3 || b1XY_3 || b1YX_3))||
			 (bXY4 && (bY_3 || bX_3 || bYX_3 || b1Y_3 || b1X_3 || b1YX_3)) ||
			 (bYX4 && (bY_3 || bXY_3 || bX_3 || b1Y_3 || b1XY_3 || b1X_3)) ||
			 (b1X4 && (bY_3 || bXY_3 || bYX_3 || b1Y_3 || b1XY_3 || b1YX_3))||
			 (b1Y4 && (bX_3 || bXY_3 || bYX_3 || b1X_3 || b1XY_3 || b1YX_3))||
			 (b1XY4 && (bY_3 || bX_3 || bYX_3 || b1Y_3 || b1X_3 || b1YX_3)) ||
			 (b1YX4 && (bY_3 || bXY_3 || bX_3 || b1Y_3 || b1XY_3 || b1X_3)))
	{
		nGrade = 3;
		m_nType = 1;
	}		
	////////	
	else if ((bX_3 && (bY_3 || bXY_3 || bYX_3 || b1Y_3 || b1XY_3 || b1YX_3))||
	         (bY_3 && (bX_3 || bXY_3 || bYX_3 || b1X_3 || b1XY_3 || b1YX_3))||
			 (bXY_3 && (bY_3 || bX_3 || bYX_3 || b1Y_3 || b1X_3 || b1YX_3)) ||
			 (bYX_3 && (bY_3 || bXY_3 || bX_3 || b1Y_3 || b1XY_3 || b1X_3)) ||
			 (b1X_3 && (bY_3 || bXY_3 || bYX_3 || b1Y_3 || b1XY_3 || b1YX_3))||
			 (b1Y_3 && (bX_3 || bXY_3 || bYX_3 || b1X_3 || b1XY_3 || b1YX_3))||
			 (b1XY_3 && (bY_3 || bX_3 || bYX_3 || b1Y_3 || b1X_3 || b1YX_3)) ||
			 (b1YX_3 && (bY_3 || bXY_3 || bX_3 || b1Y_3 || b1XY_3 || b1X_3)))
	{
		nGrade = 4;
		m_nType = 2;
	}		
	////////
	else if ((bXY4 && (bYX_2 || bY_2 || bX_2))||
	         (bYX4 && (bXY_2 || bY_2 || bX_2))||
	         (bX4 && (bXY_2 || bYX_2 || bY_2))||
	         (bY4 && (bXY_2 || bYX_2 || bX_2)))
	
	{
		nGrade = 5;
		m_nType = 1;
	}		                     
	else if ((bXY4 && (bYX3 || bY3 || bX3))||
	         (bYX4 && (bXY3 || bY3 || bX3))||
	         (bX4 && (bXY3 || bYX3 || bY3))||
	         (bY4 && (bXY3 || bYX3 || bX3)))
	{
		nGrade = 6;
		m_nType = 1;
	}		
	else if ((bXY4 && (bYX_1 || bY_1 || bX_1))||
	         (bYX4 && (bXY_1 || bY_1 || bX_1))||
	         (bX4 && (bXY_1 || bYX_1 || bY_1))||
	         (bY4 && (bXY_1 || bYX_1 || bX_1)))
	{
		nGrade = 7;
		m_nType = 2;
	}		
	else if ((bXY4 && (bYX2 || bY2 || bX2))||
	         (bYX4 && (bXY2 || bY2 || bX2))||
	         (bX4 && (bXY2 || bYX2 || bY2))||
	         (bY4 && (bXY2 || bYX2 || bX2)))
	
	{
		nGrade = 8;
		m_nType = 1;
	}		
	else if (bXY4 || bYX4 || bX4 || bY4)
	{
		nGrade = 9;
		m_nType = 1;
	}		
	////////
	else if ((bXY_3 && (bYX_2 || bY_2 || bX_2))||
	         (bYX_3 && (bXY_2 || bY_2 || bX_2))||
	         (bX_3 && (bXY_2 || bYX_2 || bY_2))||
	         (bY_3 && (bXY_2 || bYX_2 || bX_2)))
	{
		nGrade = 10;
		m_nType = 2;
	}		
	else if ((bXY_3 && (bYX3 || bY3 || bX3))||
	         (bYX_3 && (bXY3 || bY3 || bX3))||
	         (bX_3 && (bXY3 || bYX3 || bY3))||
	         (bY_3 && (bXY3 || bYX3 || bX3)))
	{
		nGrade = 11;
		m_nType = 2;
	}		
	else if ((bXY_3 && (bYX_1 || bY_1 || bX_1))||
	         (bYX_3 && (bXY_1 || bY_1 || bX_1))||
	         (bX_3 && (bXY_1 || bYX_1 || bY_1))||
	         (bY_3 && (bXY_1 || bYX_1 || bX_1)))
	{
		nGrade = 12;
		m_nType = 2;
	}		
	else if ((bXY_3 && (bYX2 || bY2 || bX2))||
	         (bYX_3 && (bXY2 || bY2 || bX2))||
	         (bX_3 && (bXY2 || bYX2 || bY2))||
	         (bY_3 && (bXY2 || bYX2 || bX2)))
	{
		nGrade = 13;
		m_nType = 2;
	}		
	else if (bXY_3 || bYX_3 || bX_3 || bY_3)
	{
		nGrade = 14;
		m_nType = 2;
	}		           
	////////		
	else if (bXY_2 || bYX_2 || bX_2 || bY_2)
		nGrade = 16;
    ////////
    else if (bXY3 || bYX3 || bX3 || bY3)
		nGrade = 17;
	else if (bXY2 || bYX2 || bX2 || bY2)
		nGrade = 18;
	else if (bXY_1 || bYX_1 || bX_1 || bY_1)    
		nGrade = 19; 
	else
		nGrade = 20;

	///////	                
	if (nGrade>15)
		if ((m_Board[nX][nY-1]==0 || m_Board[nX][nY-1]==cValue) &&
			(m_Board[nX+1][nY]==0 || m_Board[nX+1][nY]==cValue) &&
			(m_Board[nX][nY+1]==0 || m_Board[nX][nY+1]==cValue) &&
			(m_Board[nX-1][nY]==0 || m_Board[nX-1][nY]==cValue) &&
			((nX+2<15 && nY-2>=0 && (m_Board[nX+2][nY-2]==0 || m_Board[nX+2][nY-2]==cValue) &&
			 m_Board[nX][nY-2]==cValue && m_Board[nX+1][nY-1]==cValue && m_Board[nX+2][nY]==cValue) ||
	     	(nX+2<15 && nY+2<15 && (m_Board[nX+2][nY+2]==0 || m_Board[nX+2][nY+2]==cValue) &&
	     	 m_Board[nX][nY+2]==cValue && m_Board[nX+1][nY+1]==cValue && m_Board[nX+2][nY]==cValue) ||
	     	(nX-2>=0 && nY+2<15 && (m_Board[nX-2][nY+2]==0 || m_Board[nX-2][nY+2]==cValue) &&
	     	 m_Board[nX][nY+2]==cValue && m_Board[nX-1][nY+1]==cValue && m_Board[nX-2][nY]==cValue) || 
		  	(nX-2>=0 && nY-2>=0 && (m_Board[nX-2][nY-2]==0 || m_Board[nX-2][nY-2]==cValue) &&
		  	 m_Board[nX][nY-2]==cValue && m_Board[nX-1][nY-1]==cValue && m_Board[nX-2][nY]==cValue)))
			nGrade = 15;
	/////////

	return nGrade;		
}

void CPenteDlg::Ring()
{	               
	g_nStoneNum++;
	if (m_nType == 0)
	{		  
		m_bGameOver = TRUE;
		m_bCanUndo = FALSE;  
		m_bCanHint = FALSE;
		m_pMenu->EnableMenuItem(IDM_UNDO, m_bCanUndo ? MF_ENABLED : MF_GRAYED);   
		m_pMenu->EnableMenuItem(IDM_HINT, m_bCanHint&&(g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
		
		CString str1,str2, str3, str4, str5;
		str1.LoadString(g_bChinese ? IDS_TOTAL_CHINESE:IDS_TOTAL_ENGLISH);
		str2.LoadString(g_bChinese ? IDS_BLACKWIN_CHINESE:IDS_BLACKWIN_ENGLISH);
		str3.LoadString(g_bChinese ? IDS_WHITEWIN_CHINESE:IDS_WHITEWIN_ENGLISH);
		str4.LoadString(g_bChinese ? IDS_UNDO_CHINESE:IDS_UNDO_ENGLISH);
		str5.LoadString(g_bChinese ? IDS_TITLE_CHINESE:IDS_TITLE_ENGLISH);
		
		char str[64];
		wsprintf(str, str1.GetBuffer(256), g_nStoneNum);
		CString strPrompt = m_byColor ? str2 : str3;
		strPrompt += str;                                    
		wsprintf(str, str4.GetBuffer(256), m_wBlackUndo,m_wWhiteUndo);		
		strPrompt += str;                                    
        if (g_nRunMode == MODE_WITH_COMPUTER && 
        	((g_bUserBlack && !m_byColor)||(!g_bUserBlack && m_byColor)))
        {
        	CString str6;              
        	if (g_nStoneNum<20)                                                 
        		str6.LoadString(g_bChinese ? IDS_BAD_CHINESE:IDS_BAD_ENGLISH);	
        	else
        		str6.LoadString(g_bChinese ? IDS_MORE_CHINESE:IDS_MORE_ENGLISH);	
        	strPrompt += str6;
        }
		PlaySound(m_byColor ? IDSOUND_BLACKWIN : IDSOUND_WHITEWIN);
		MessageBox(strPrompt, str5);
        
	    BOOL bWinner = FALSE;
	    if ((g_nSkill==1 && g_nStoneNum<g_nTime1) ||
	    	(g_nSkill==2 && g_nStoneNum<g_nTime2) ||
	    	(g_nSkill==3 && g_nStoneNum<g_nTime3))
	    	bWinner = TRUE;
	    
	    if (g_nRunMode == MODE_WITH_COMPUTER && bWinner &&
	    	((g_bUserBlack && m_byColor) || (! g_bUserBlack && ! m_byColor)))
	    {
	    	CRecordDlg recordDlg;
    		recordDlg.DoModal();
			OnBest();
		}
	}                           
	////////////////////////////
	else if (m_nType == 1)
	{		
		PlaySound(m_byColor ? IDSOUND_BLACK4 : IDSOUND_WHITE4);
	}
	////////////////////////////                                
	else if (m_nType == 2)
	{		                                                 
		PlaySound(m_byColor ? IDSOUND_BLACKD3 : IDSOUND_WHITED3);
	}
}

void CPenteDlg::OnUndo()
{             
    if (m_bCanUndo)
    {
		if (m_bGameOver)     
		{
			PlaySound(IDSOUND_ERROR);
			CString str1, str2;
			str1.LoadString(g_bChinese ? IDS_CANNOT_UNDO_CHINESE:IDS_CANNOT_UNDO_ENGLISH);
			str2.LoadString(g_bChinese ? IDS_TITLE_CHINESE:IDS_TITLE_ENGLISH);
			MessageBox(str1, str2);
			return;
		}

		if (m_byColor)
			m_wWhiteUndo++;
		else
			m_wBlackUndo++;
	
		g_nStoneNum -= 2;
		
		m_Board[m_wXCur1][m_wYCur1] = 0;
		m_Board[m_wXCur2][m_wYCur2] = 0;	
		InvalidateRect(m_rcCur1, TRUE);
		InvalidateRect(m_rcCur2, TRUE);
		UpdateWindow();
		PlaySound(IDSOUND_UNDO);
		m_bCanUndo = FALSE;
		m_pMenu->EnableMenuItem(IDM_UNDO, m_bCanUndo ? MF_ENABLED : MF_GRAYED);   
		m_rcCur1.SetRectEmpty();
		m_rcCur2.SetRectEmpty();
	}   
	else
	{
		PlaySound(IDSOUND_ERROR);
		CString str1, str2;
		str1.LoadString(g_bChinese ? IDS_CANNOT_UNDO_CHINESE1:IDS_CANNOT_UNDO_ENGLISH1);
		str2.LoadString(g_bChinese ? IDS_TITLE_CHINESE:IDS_TITLE_ENGLISH);
		MessageBox(str1, str2);
	}
}

void CPenteDlg::OnHint()
{          
	if (m_bGameOver)     
	{
		PlaySound(IDSOUND_ERROR);
		CString str1, str2;
		str1.LoadString(g_bChinese ? IDS_CANNOT_HINT_CHINESE:IDS_CANNOT_HINT_ENGLISH);
		str2.LoadString(g_bChinese ? IDS_TITLE_CHINESE:IDS_TITLE_ENGLISH);
		MessageBox(str1, str2);
		return;
	}
	
	int x1, y1, x2, y2;
	if (g_nStoneNum==0)
	{
		x1 = 7*m_cxGrid + m_wXNull-4;
		y1 = 7*m_cyGrid + m_wYNull-4;
		x2 = 7*m_cxGrid + m_wXNull+4;
		y2 = 7*m_cyGrid + m_wYNull+4;
	}
	else
	{
		CPoint ptBest = UrgentPoint(!m_byColor);
		x1 = ptBest.x*m_cxGrid + m_wXNull-4;
		y1 = ptBest.y*m_cyGrid + m_wYNull-4;
		x2 = ptBest.x*m_cxGrid + m_wXNull+4;
		y2 = ptBest.y*m_cyGrid + m_wYNull+4;
	}
	CClientDC dc(this);
	COLORREF crColor = m_byColor ? RGB(255,255,255) : RGB(0,0,0);
	CPen pen(PS_SOLID, 1, crColor);
	CPen *pOldPen = dc.SelectObject(&pen);	
	dc.MoveTo(x1, y1);
	dc.LineTo(x2, y1);
	dc.LineTo(x2, y2);
	dc.LineTo(x1, y2);
	dc.LineTo(x1, y1);
	dc.SelectObject(pOldPen);	          

	m_rcHint = CRect(x1-1,y1-1,x2+1,y2+1);
}

void CPenteDlg::OnNew()
{
	InitParams();
	Invalidate();   
	UpdateWindow();         

	CSetupDlg setupDlg;
	if (setupDlg.DoModal()==IDCANCEL)
	{
		CDialog::EndDialog(0);
		return;
	}

	CString str;
	if (g_nRunMode==MODE_WITH_COMPUTER && g_bUserBlack)
		str.LoadString(g_bChinese ? IDS_SINGLE_USER_CHINESE:IDS_SINGLE_USER_ENGLISH);
	else if (g_nRunMode==MODE_WITH_COMPUTER && !g_bUserBlack)
		str.LoadString(g_bChinese ? IDS_SINGLE_COMPUTER_CHINESE:IDS_SINGLE_COMPUTER_ENGLISH);
	else if	(g_nRunMode==MODE_2PLAYER)
    	str.LoadString(g_bChinese ? IDS_DOUBLE_CHINESE:IDS_DOUBLE_ENGLISH);
	SetWindowText(str);

	m_pMenu->DestroyMenu();
	m_pMenu->LoadMenu(g_bChinese ? IDR_MENU_CHINESE : IDR_MENU_ENGLISH);
	SetMenu(m_pMenu);
	m_pMenu->CheckMenuItem(IDM_SOUND, g_bSoundOn ? MF_CHECKED : MF_UNCHECKED);
	m_pMenu->EnableMenuItem(IDM_UNDO, m_bCanUndo ? MF_ENABLED : MF_GRAYED);   
	m_pMenu->EnableMenuItem(IDM_HINT, m_bCanHint&&(g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	m_pMenu->EnableMenuItem(IDM_GRADE1, (g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	m_pMenu->EnableMenuItem(IDM_GRADE2, (g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	m_pMenu->EnableMenuItem(IDM_GRADE3, (g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	if (g_nSkill == 1)
	{
		m_pMenu->CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_CHECKED);  
		m_pMenu->CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_UNCHECKED);
		m_pMenu->CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_UNCHECKED);
	}
	else if (g_nSkill == 2)
	{
		m_pMenu->CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_CHECKED);  
		m_pMenu->CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_UNCHECKED);
		m_pMenu->CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_UNCHECKED);
	}
	else if (g_nSkill == 3)
	{          
		m_pMenu->CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_CHECKED);  
		m_pMenu->CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_UNCHECKED);
		m_pMenu->CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_UNCHECKED);
	}

	PlaySound(IDSOUND_NEWGAME);

	if (g_nRunMode==MODE_WITH_COMPUTER && !g_bUserBlack)
	{
		CPoint pt;            			// Computer first
		m_byColor = !m_byColor;			// 1-Black  0-White
		m_Board[7][7]=m_byColor+1;
		pt.x = 7*m_cxGrid + m_wXNull;
		pt.y = 7*m_cyGrid + m_wYNull;
		InvalidateRect(GetCurStoneArea(pt), FALSE);
		UpdateWindow();
		ClientToScreen(&pt);
		SetCursorPos(pt.x, pt.y);
		PlaySound(IDSOUND_PUTSTONE);
		Judge(7, 7, m_Board[7][7]);
		Ring();
	}              
}

void CPenteDlg::ReponseNew()
{
	InitParams();
	Invalidate();   
	UpdateWindow();         

	CSetupDlg setupDlg;
	if (setupDlg.DoModal()==IDCANCEL)
	{
		CDialog::EndDialog(0);
		return;
	}

	CString str;
	if (g_nRunMode==MODE_WITH_COMPUTER && g_bUserBlack)
		str.LoadString(g_bChinese ? IDS_SINGLE_USER_CHINESE:IDS_SINGLE_USER_ENGLISH);
	else if (g_nRunMode==MODE_WITH_COMPUTER && !g_bUserBlack)
		str.LoadString(g_bChinese ? IDS_SINGLE_COMPUTER_CHINESE:IDS_SINGLE_COMPUTER_ENGLISH);
	else if	(g_nRunMode==MODE_2PLAYER)
    	str.LoadString(g_bChinese ? IDS_DOUBLE_CHINESE:IDS_DOUBLE_ENGLISH);
	SetWindowText(str);

	m_pMenu->DestroyMenu();
	m_pMenu->LoadMenu(g_bChinese ? IDR_MENU_CHINESE : IDR_MENU_ENGLISH);
	SetMenu(m_pMenu);
	m_pMenu->CheckMenuItem(IDM_SOUND, g_bSoundOn ? MF_CHECKED : MF_UNCHECKED);
	m_pMenu->EnableMenuItem(IDM_UNDO, m_bCanUndo ? MF_ENABLED : MF_GRAYED);   
	m_pMenu->EnableMenuItem(IDM_HINT, m_bCanHint&&(g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	m_pMenu->EnableMenuItem(IDM_GRADE1, (g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	m_pMenu->EnableMenuItem(IDM_GRADE2, (g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	m_pMenu->EnableMenuItem(IDM_GRADE3, (g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	if (g_nSkill == 1)
	{
		m_pMenu->CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_CHECKED);  
		m_pMenu->CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_UNCHECKED);
		m_pMenu->CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_UNCHECKED);
	}
	else if (g_nSkill == 2)
	{
		m_pMenu->CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_CHECKED);  
		m_pMenu->CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_UNCHECKED);
		m_pMenu->CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_UNCHECKED);
	}
	else if (g_nSkill == 3)
	{          
		m_pMenu->CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_CHECKED);  
		m_pMenu->CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_UNCHECKED);
		m_pMenu->CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_UNCHECKED);
	}

	PlaySound(IDSOUND_NEWGAME);

	if (g_nRunMode==MODE_WITH_COMPUTER && !g_bUserBlack)
	{
		CPoint pt;            			// Computer first
		m_byColor = !m_byColor;			// 1-Black  0-White
		m_Board[7][7]=m_byColor+1;
		pt.x = 7*m_cxGrid + m_wXNull;
		pt.y = 7*m_cyGrid + m_wYNull;
		InvalidateRect(GetCurStoneArea(pt), FALSE);
		UpdateWindow();
		ClientToScreen(&pt);
		SetCursorPos(pt.x, pt.y);
		PlaySound(IDSOUND_PUTSTONE);
		Judge(7, 7, m_Board[7][7]);
		Ring();
	}              
}

void CPenteDlg::OnSkill1()
{          
	g_nSkill = 1;
	m_pMenu->CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_CHECKED);  
	m_pMenu->CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_UNCHECKED);
	m_pMenu->CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_UNCHECKED);
}

void CPenteDlg::OnSkill2()
{          
	g_nSkill = 2;
	m_pMenu->CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_CHECKED);  
	m_pMenu->CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_UNCHECKED);
	m_pMenu->CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_UNCHECKED);
}

void CPenteDlg::OnSkill3()
{          
	g_nSkill = 3;         
	m_pMenu->CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_CHECKED);  
	m_pMenu->CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_UNCHECKED);
	m_pMenu->CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_UNCHECKED);
}

void CPenteDlg::OnSound()
{                        
	g_bSoundOn = ! g_bSoundOn;
	GetMenu()->CheckMenuItem(IDM_SOUND, g_bSoundOn ? MF_CHECKED : MF_UNCHECKED);
}
                                        
void CPenteDlg::OnAbout()
{
    CAboutDlg aboutDlg(this);
    aboutDlg.DoModal();
}

void CPenteDlg::OnBest()
{
    CBestDlg bestDlg;
    bestDlg.DoModal();
}

void CPenteDlg::OnLanguage()
{
	g_bChinese = ! g_bChinese;

	CString str;
	if (g_nRunMode==MODE_WITH_COMPUTER && g_bUserBlack)
		str.LoadString(g_bChinese ? IDS_SINGLE_USER_CHINESE:IDS_SINGLE_USER_ENGLISH);
	else if (g_nRunMode==MODE_WITH_COMPUTER && !g_bUserBlack)
		str.LoadString(g_bChinese ? IDS_SINGLE_COMPUTER_CHINESE:IDS_SINGLE_COMPUTER_ENGLISH);
	else if	(g_nRunMode==MODE_2PLAYER)
    	str.LoadString(g_bChinese ? IDS_DOUBLE_CHINESE:IDS_DOUBLE_ENGLISH);
	SetWindowText(str);

	m_pMenu->DestroyMenu();
	m_pMenu->LoadMenu(g_bChinese ? IDR_MENU_CHINESE : IDR_MENU_ENGLISH);
	SetMenu(m_pMenu);
	m_pMenu->CheckMenuItem(IDM_SOUND, g_bSoundOn ? MF_CHECKED : MF_UNCHECKED);
	m_pMenu->EnableMenuItem(IDM_UNDO, m_bCanUndo ? MF_ENABLED : MF_GRAYED);   
	m_pMenu->EnableMenuItem(IDM_HINT, m_bCanHint&&(g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	if (g_nSkill == 1)
	{
		m_pMenu->CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_CHECKED);  
		m_pMenu->CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_UNCHECKED);
		m_pMenu->CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_UNCHECKED);
	}
	else if (g_nSkill == 2)
	{
		m_pMenu->CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_CHECKED);  
		m_pMenu->CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_UNCHECKED);
		m_pMenu->CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_UNCHECKED);
	}
	else if (g_nSkill == 3)
	{          
		m_pMenu->CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_CHECKED);  
		m_pMenu->CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_UNCHECKED);
		m_pMenu->CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_UNCHECKED);
	}
}

void CPenteDlg::OnExit()
{
	KillTimer(ID_TIMER_NET);

	PlaySound(IDSOUND_GOODBYE, SND_SYNC);

	CDialog::EndDialog(0);
}

void CPenteDlg::PutStone(BYTE byColor, CPoint point, CDC *pDC)
{                                      
	int nX = point.x*m_cxGrid + m_wXNull - m_wStoneWidth/2;                               
	int nY = point.y*m_cyGrid + m_wYNull - m_wStoneHeight/2;
	CPoint ptLUCorner(nX, nY);
	m_pDibMask->Display(pDC, ptLUCorner, SRCAND);
	if (byColor == 0)
		m_pDibWhite->Display(pDC, ptLUCorner, SRCPAINT);
	else if (byColor == 1)
		m_pDibBlack->Display(pDC, ptLUCorner, SRCPAINT);
}

CRect CPenteDlg::GetCurStoneArea(CPoint point)
{
	int nXd = point.x-m_wStoneWidth/2;                               
	int nYd = point.y-m_wStoneHeight/2;
	
	return CRect(nXd, nYd, nXd+m_wStoneWidth, nYd+m_wStoneHeight);
}                                                           

BOOL CPenteDlg::OnSetCursor(CWnd* pWnd, UINT nHitTest, UINT message)
{
	CPoint point, pt;
	::GetCursorPos(&point);
   	ScreenToClient(&point);
	if (PointToStonePos(point, pt) && !m_bGameOver)
	{	
		::SetCursor(AfxGetApp()->LoadCursor(m_byColor ? IDC_WHITE_HAND:IDC_BLACK_HAND));
		return TRUE;
	}

	return CDialog::OnSetCursor(pWnd, nHitTest, message);
}

void CPenteDlg::OnPaletteChanged(CWnd* pFocusWnd) 
{
	CDialog::OnPaletteChanged(pFocusWnd);
	
	if (pFocusWnd != this)
		OnQueryNewPalette();
}

BOOL CPenteDlg::OnQueryNewPalette() 
{
    CClientDC dc(this);
    CPalette *pOldPal = dc.SelectPalette (m_pDibBoard->GetPalette(), FALSE);

    int nChangedColors = dc.RealizePalette();

    dc.SelectPalette (pOldPal, FALSE);

    // If any palette entries changed, repaint the window.
    if (nChangedColors > 0)
        Invalidate();
	
	return CDialog::OnQueryNewPalette();
}

void CPenteDlg::OnSysCommand(UINT nID, LPARAM lParam) 
{
	if (nID == SC_CLOSE)
		OnExit();
	
	CDialog::OnSysCommand(nID, lParam);
}

void CPenteDlg::OnContextMenu(CWnd* pWnd, CPoint point) 
{
	CMenu menu;
	menu.LoadMenu(g_bChinese ? IDR_MENU_CONTEXT_CHINESE : IDR_MENU_CONTEXT_ENGLISH);
	menu.CheckMenuItem(IDM_SOUND, g_bSoundOn ? MF_CHECKED : MF_UNCHECKED);
	menu.EnableMenuItem(IDM_UNDO, m_bCanUndo ? MF_ENABLED : MF_GRAYED);   
	menu.EnableMenuItem(IDM_HINT, m_bCanHint&&(g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	menu.EnableMenuItem(IDM_GRADE1, (g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	menu.EnableMenuItem(IDM_GRADE2, (g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	menu.EnableMenuItem(IDM_GRADE3, (g_nRunMode==MODE_WITH_COMPUTER) ? MF_ENABLED : MF_GRAYED);   
	if (g_nSkill == 1)
	{
		menu.CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_CHECKED);  
		menu.CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_UNCHECKED);
		menu.CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_UNCHECKED);
	}
	else if (g_nSkill == 2)
	{
		menu.CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_CHECKED);  
		menu.CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_UNCHECKED);
		menu.CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_UNCHECKED);
	}
	else if (g_nSkill == 3)
	{          
		menu.CheckMenuItem(IDM_GRADE3, MF_BYCOMMAND | MF_CHECKED);  
		menu.CheckMenuItem(IDM_GRADE1, MF_BYCOMMAND | MF_UNCHECKED);
		menu.CheckMenuItem(IDM_GRADE2, MF_BYCOMMAND | MF_UNCHECKED);
	}
    menu.GetSubMenu(0)->TrackPopupMenu(TPM_LEFTALIGN|TPM_LEFTBUTTON|TPM_RIGHTBUTTON, 
										   point.x, point.y, this);
}

void CPenteDlg::OnCancel()
{
}
void CPenteDlg::OnOK()
{
}

////////////////////////// End of PENTEWND.CPP ////////////////////////////////




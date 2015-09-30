// ChessDlg.cpp : implementation file
//

#include "stdafx.h"
#include "Chess.h"
#include "ChessDlg.h"
#include "CoolButton.h"
#include "BaseDef.h"
#include "OptionDlg.h"
#include "ThinkOptionDlg.h"
#include "BaiDlg.h"
#include "OpenDlg.h"

#define CHESS_C_UNDO 0
#define CHESS_C_REDO 1
#define CHESS_C_HELP 2
#define CHESS_C_CUT	 3


#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CAboutDlg dialog used for App About

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// Dialog Data
	//{{AFX_DATA(CAboutDlg)
	enum { IDD = IDD_ABOUTBOX };
	CCoolButton	m_btWeb;
	CCoolButton	m_btEmail;
	CCoolButton	m_btOK;
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CAboutDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	//{{AFX_MSG(CAboutDlg)
	afx_msg void OnButtonweb();
	afx_msg void OnButtonemail();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
	//{{AFX_DATA_INIT(CAboutDlg)
	//}}AFX_DATA_INIT
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CAboutDlg)
	DDX_Control(pDX, IDC_BUTTONWEB, m_btWeb);
	DDX_Control(pDX, IDC_BUTTONEMAIL, m_btEmail);
	DDX_Control(pDX, IDOK, m_btOK);
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
	//{{AFX_MSG_MAP(CAboutDlg)
	ON_BN_CLICKED(IDC_BUTTONWEB, OnButtonweb)
	ON_BN_CLICKED(IDC_BUTTONEMAIL, OnButtonemail)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CChessDlg dialog

CChessDlg::CChessDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CChessDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(CChessDlg)
	//}}AFX_DATA_INIT
	// Note that LoadIcon does not require a subsequent DestroyIcon in Win32
	CWinApp* pApp=AfxGetApp();

	m_hIcon		= pApp->LoadIcon(IDR_MAINFRAME);
	m_hIconUndo = pApp->LoadIcon(IDI_UNDO);
	m_hIconRedo = pApp->LoadIcon(IDI_REDO);
	m_hIconHelp = pApp->LoadIcon(IDI_HELP);
	m_hIconCut	= pApp->LoadIcon(IDI_CUT );

	//棋子图标
	m_hIconMan[RED_K]=pApp->LoadIcon(IDI_R_K);
	m_hIconMan[RED_S]=pApp->LoadIcon(IDI_R_S);
	m_hIconMan[RED_X]=pApp->LoadIcon(IDI_R_X);
	m_hIconMan[RED_M]=pApp->LoadIcon(IDI_R_M);
	m_hIconMan[RED_J]=pApp->LoadIcon(IDI_R_J);
	m_hIconMan[RED_P]=pApp->LoadIcon(IDI_R_P);
	m_hIconMan[RED_B]=pApp->LoadIcon(IDI_R_B);

	m_hIconMan[BLACK_K]=pApp->LoadIcon(IDI_B_K);
	m_hIconMan[BLACK_S]=pApp->LoadIcon(IDI_B_S);
	m_hIconMan[BLACK_X]=pApp->LoadIcon(IDI_B_X);
	m_hIconMan[BLACK_M]=pApp->LoadIcon(IDI_B_M);
	m_hIconMan[BLACK_J]=pApp->LoadIcon(IDI_B_J);
	m_hIconMan[BLACK_P]=pApp->LoadIcon(IDI_B_P);
	m_hIconMan[BLACK_B]=pApp->LoadIcon(IDI_B_B);
	
	m_pdcBack=new CDC;
	m_pdcBoard=new CDC;

	m_bThinking=FALSE;
	m_bCheck[0]=FALSE;
	m_bCheck[1]=FALSE;

	FixManMap();

}

void CChessDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CChessDlg)
	DDX_Control(pDX, IDC_CHESS_CUT, m_btCut);
	DDX_Control(pDX, IDC_PROGRESS1, m_progressStep);
	DDX_Control(pDX, IDC_STEPLIST, m_listStep);
	DDX_Control(pDX, IDC_CHESS_UNDO, m_btUndo);
	DDX_Control(pDX, IDC_CHESS_REDO, m_btRedo);
	DDX_Control(pDX, IDC_CHESS_HELP, m_btHelp);
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CChessDlg, CDialog)
	//{{AFX_MSG_MAP(CChessDlg)
	ON_COMMAND(ID_ABOUTBOX, OnAboutbox)
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_WM_MOUSEMOVE()
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_DESTROY()
	ON_COMMAND(ID_FILE_BAI, OnFileBai)
	ON_COMMAND(ID_FILE_NEW, OnFileNew)
	ON_COMMAND(ID_FILE_SAVE, OnFileSave)
	ON_COMMAND(ID_FILE_OPEN, OnFileOpen)
	ON_COMMAND(ID_FILE_OPTION, OnFileOption)
	ON_BN_CLICKED(IDC_CHESS_UNDO, OnEditUndo)
	ON_BN_CLICKED(IDC_CHESS_REDO, OnEditRedo)
	ON_BN_CLICKED(IDC_CHESS_HELP, OnEditHelp)
	ON_COMMAND(ID_CHESS_CUT, OnEditCut)
	ON_WM_TIMER()
	ON_LBN_DBLCLK(IDC_STEPLIST, OnDblclkSteplist)
	ON_COMMAND(ID_HELP_EMAIL, OnHelpEmail)
	ON_COMMAND(ID_HELP_WEB, OnHelpWeb)
	ON_COMMAND(ID_FILE_THINKOPTION, OnFileThinkOption)
	ON_COMMAND(ID_CHESS_HELP, OnEditHelp)
	ON_COMMAND(ID_CHESS_REDO, OnEditRedo)
	ON_COMMAND(ID_CHESS_UNDO, OnEditUndo)
	ON_BN_CLICKED(IDC_CHESS_CUT, OnEditCut)
	ON_COMMAND(ID_HELP_USE, OnHelpUse)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CChessDlg message handlers

BOOL CChessDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, FALSE);
	SetIcon(m_hIcon, TRUE);		// Set small icon
	
	m_btUndo.SetIcon(m_hIconUndo,32,32);
	m_btRedo.SetIcon(m_hIconRedo,32,32);
	m_btHelp.SetIcon(m_hIconHelp,32,32);
	m_btCut .SetIcon(m_hIconCut ,32,32);
	
	CClientDC dc(this);

	//Create Bitmap for DC
	m_Bitmap1.CreateCompatibleBitmap(&dc,XBW,YBW);
	m_Bitmap2.CreateCompatibleBitmap(&dc,XBW,YBW);

	m_pdcBack->CreateCompatibleDC(&dc);
	m_pdcBoard->CreateCompatibleDC(&dc);

	m_pdcBoard->SelectObject(&m_Bitmap1);
	m_pdcBack->SelectObject(&m_Bitmap2);
	
	MakeBoard();	//draw Chessboard in Board DC(3D)
	
	m_Setting.Load();

	m_Thinker.SetLevel(m_Setting.m_nLevel);
	m_Thinker.LoadThinkSetting();

	m_progressStep.CProgressCtrl::SetRange(0,100);

	m_nTimer=SetTimer(1,80,NULL);

	New();
	return TRUE;  // return TRUE  unless you set the focus to a control
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CChessDlg::OnPaint() 
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

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
		CClientDC dc(this);
		CRect rc;
		GetUpdateRect(&rc,TRUE);
		rc.OffsetRect(-XOFFSET,-YOFFSET);
		UpdateFace(rc);
		CDialog::OnPaint();
	}
}

// The system calls this to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CChessDlg::OnQueryDragIcon()
{
	return (HCURSOR) m_hIcon;
}

void CChessDlg::OnMouseMove(UINT nFlags, CPoint point) 
{

	if(m_nManPickUp!=32)
	{
		CPoint last=m_pointPickUp;
		point.Offset(-XOFFSET-MW/2,-YOFFSET-MW/2);
		if(point.x<SW)point.x=SW;
		if(point.x>BWA*8+SW)point.x=BWA*8+SW;
		if(point.y<SW)point.y=SW;
		if(point.y>BWA*9+SW)point.y=BWA*9+SW;

		m_pointPickUp=point;
		CRect rc=MakeUpdateRect(last,m_pointPickUp);
		UpdateFace(rc);		
	}


	CDialog::OnMouseMove(nFlags, point);
}

void CChessDlg::OnAboutbox() 
{
	CAboutDlg dlgAbout;
 	dlgAbout.DoModal();
}



CChessDlg::~CChessDlg()
{
 	if(m_pdcBack)delete m_pdcBack;
 	if (m_pdcBoard)delete m_pdcBoard;
}

void CChessDlg::OnFileOption() 
{
	COptionDlg optiondlg;
	optiondlg.m_CORM0=m_Setting.m_nCOrM[0];
	optiondlg.m_CORM1=m_Setting.m_nCOrM[1];
	optiondlg.m_nMode =m_Setting.m_nMode;
	optiondlg.m_nLevel=m_Setting.m_nLevel;

	m_bThinking=FALSE;
	EnableCommand(CHESS_C_CUT,FALSE);

	int respond=optiondlg.DoModal();
	if(respond==IDOK)
	{
		m_Setting.m_nCOrM[0]=optiondlg.m_CORM0;
		m_Setting.m_nCOrM[1]=optiondlg.m_CORM1;
		m_Setting.m_nMode=optiondlg.m_nMode;
		m_Setting.m_nLevel=optiondlg.m_nLevel;

		m_Thinker.SetLevel(m_Setting.m_nLevel);

		m_Setting.Save();

		ShowStatus();
		if(m_Setting.m_nCOrM[m_Setting.m_nPlayer[m_Face.side]]==COM)
			Think();
	}
}

void CChessDlg::MakeBoard()
{
	//填充
	CBrush br(GetSysColor(COLOR_3DFACE));
	CRect rect(0,0,XBW,YBW);
	m_pdcBoard->FillRect(rect,&br);
	//画棋盘
	CPen pen[2];
	pen[0].CreatePen(PS_SOLID,0,GetSysColor(COLOR_3DHILIGHT));
	pen[1].CreatePen(PS_SOLID,0,GetSysColor(COLOR_3DSHADOW));
	
	for(int i=0;i<=1;i++)
	{
		m_pdcBoard->SelectObject(pen[i]);
		//画边框(双线)
		m_pdcBoard->MoveTo(XC[i]-3,YC[i]-3);
		m_pdcBoard->LineTo(XC[i]-3,YC[i]+3+BWA*9);
		m_pdcBoard->LineTo(XC[i]+3+BWA*8,YC[i]+3+BWA*9);
		m_pdcBoard->LineTo(XC[i]+3+BWA*8,YC[i]-3);
		m_pdcBoard->LineTo(XC[i]-3,YC[i]-3);

		m_pdcBoard->MoveTo(XC[i],YC[i]);
		m_pdcBoard->LineTo(XC[i],YC[i]+BWA*9);
		m_pdcBoard->LineTo(XC[i]+BWA*8,YC[i]+BWA*9);
		m_pdcBoard->LineTo(XC[i]+BWA*8,YC[i]);
		m_pdcBoard->LineTo(XC[i],YC[i]);
		//画竖线
		int n;
		for( n=1;n<=8;n++)
		{
			m_pdcBoard->MoveTo(XC[i],YC[i]+BWA*n);
			m_pdcBoard->LineTo(XC[i]+BWA*8,YC[i]+BWA*n);
		}
		for( n=1;n<=7;n++)
		{
			m_pdcBoard->MoveTo(XC[i]+BWA*n,YC[i]);
			m_pdcBoard->LineTo(XC[i]+BWA*n,YC[i]+BWA*4);
		
			m_pdcBoard->MoveTo(XC[i]+BWA*n,YC[i]+BWA*5);
			m_pdcBoard->LineTo(XC[i]+BWA*n,YC[i]+BWA*9);
		}
		//画宫
		m_pdcBoard->MoveTo(XC[i]+BWA*3,YC[i]+BWA*0);	
		m_pdcBoard->LineTo(XC[i]+BWA*5,YC[i]+BWA*2);	//上捺
		
		m_pdcBoard->MoveTo(XC[i]+BWA*5,YC[i]+BWA*0);	
		m_pdcBoard->LineTo(XC[i]+BWA*3,YC[i]+BWA*2);	//上撇
		
		m_pdcBoard->MoveTo(XC[i]+BWA*3,YC[i]+BWA*7);	
		m_pdcBoard->LineTo(XC[i]+BWA*5,YC[i]+BWA*9);	//下捺
		
		m_pdcBoard->MoveTo(XC[i]+BWA*5,YC[i]+BWA*7);	
		m_pdcBoard->LineTo(XC[i]+BWA*3,YC[i]+BWA*9);	//下撇
		//画星
		MB_DrawStar(7,2,i);
		MB_DrawStar(1,2,i);
		MB_DrawStar(0,3,i);
		MB_DrawStar(2,3,i);
		MB_DrawStar(4,3,i);
		MB_DrawStar(6,3,i);
		MB_DrawStar(8,3,i);

		MB_DrawStar(7,7,i);
		MB_DrawStar(1,7,i);
		MB_DrawStar(0,6,i);
		MB_DrawStar(2,6,i);
		MB_DrawStar(4,6,i);
		MB_DrawStar(6,6,i);
		MB_DrawStar(8,6,i);
	}
}

void CChessDlg::MB_DrawStar(int x,int y,int i)
{
	if(x!=0)
	{
		m_pdcBoard->MoveTo(XC[i]+BWA*x-3,YC[i]+BWA*y-3);
		m_pdcBoard->LineTo(XC[i]+BWA*x-6,YC[i]+BWA*y-3);

		m_pdcBoard->MoveTo(XC[i]+BWA*x-3,YC[i]+BWA*y-3);
		m_pdcBoard->LineTo(XC[i]+BWA*x-3,YC[i]+BWA*y-6);

		m_pdcBoard->MoveTo(XC[i]+BWA*x-3,YC[i]+BWA*y+3);
		m_pdcBoard->LineTo(XC[i]+BWA*x-6,YC[i]+BWA*y+3);

		m_pdcBoard->MoveTo(XC[i]+BWA*x-3,YC[i]+BWA*y+3);
		m_pdcBoard->LineTo(XC[i]+BWA*x-3,YC[i]+BWA*y+6);
	}
	if(x!=8)
	{
		m_pdcBoard->MoveTo(XC[i]+BWA*x+3,YC[i]+BWA*y-3);
		m_pdcBoard->LineTo(XC[i]+BWA*x+6,YC[i]+BWA*y-3);

		m_pdcBoard->MoveTo(XC[i]+BWA*x+3,YC[i]+BWA*y-3);
		m_pdcBoard->LineTo(XC[i]+BWA*x+3,YC[i]+BWA*y-6);

		m_pdcBoard->MoveTo(XC[i]+BWA*x+3,YC[i]+BWA*y+3);
		m_pdcBoard->LineTo(XC[i]+BWA*x+6,YC[i]+BWA*y+3);

		m_pdcBoard->MoveTo(XC[i]+BWA*x+3,YC[i]+BWA*y+3);
		m_pdcBoard->LineTo(XC[i]+BWA*x+3,YC[i]+BWA*y+6);
	}

}

BOOL CChessDlg::CanGo()
{
	for(int i=0;i<2;i++)
		if (m_Face.man[i*16].x==0 && m_nManPickUp!=i*16)
		{
			MessageBox( "已经结束.","提醒",MB_OK|MB_ICONINFORMATION);
			return FALSE;
		}
	return TRUE;
}

BOOL CChessDlg::CanGo(int man, int x, int y)
{
	int xfrom,yfrom;
	if(!CanGo())return FALSE;
	if(man==32)
	{
		if(m_pointBeforePickUp.x==x&&m_pointBeforePickUp.y==y)//是原来的点
			return FALSE;
		man=m_nManPickUp;
		xfrom=m_pointBeforePickUp.x;
		yfrom=m_pointBeforePickUp.y;
	}
	else
	{
		if(m_Face.man[man].x==0)return FALSE;
		if(m_Face.man[man].x==x&&m_Face.man[man].y==y)return FALSE;
		xfrom=m_Face.man[man].x;
		yfrom=m_Face.man[man].y;
	}
	
	if(!::CanGo(m_nManMap,man,xfrom,yfrom,x,y))return FALSE;

	return TRUE;
}

BOOL CChessDlg::Go(int man, int x, int y)
{
	if(!CanGo( man,x, y))return FALSE;


	m_listStep.AddString(GetStepName(man,x,y));
	m_listStep.SetCurSel(m_listStep.GetCount()-1);
	CStep* pStep=new CStep;
	if(man==32)
	{
		pStep->man=m_nManPickUp;
		pStep->from.x=int(m_pointBeforePickUp.x);
		pStep->from.y=int(m_pointBeforePickUp.y);
	}
	else 
	{
		pStep->man=man;
		pStep->from.x=int(m_Face.man[man].x);
		pStep->from.y=int(m_Face.man[man].y);
	}
	pStep->to.x=x;
	pStep->to.y=y;

	
	if(m_nManMap[x][y]!=32)
	{
		pStep->eaten=m_nManMap[x][y];//吃掉的子
		m_Face.man[m_nManMap[x][y]].x=0;
	}

	m_StepList.Go(pStep);
	Move(man,x,y);
	m_Face.side=!m_Face.side;

	EnableCommand(CHESS_C_UNDO,TRUE);
	EnableCommand(CHESS_C_REDO,FALSE);
	EnableCommand(CHESS_C_CUT,FALSE);

	m_nStepCount++;
	m_bThinking=FALSE;


	ShowStatus();

	if(m_Setting.m_nCOrM[m_Setting.m_nPlayer[m_Face.side]]==COM)	//轮到电脑
		Think();

	return TRUE;
}

void CChessDlg::Move(int man, int x, int y)
{
	if(man!=32)
	{
	PickUp(man);
	}
	CPoint base(m_pointPickUp);
	
	int count=max(abs((x-1)*BWA+SW-m_pointPickUp.x),abs((y-1)*BWA+SW-m_pointPickUp.y));

	float xstep=float((x-1)*BWA+SW-m_pointPickUp.x)/count;
	float ystep=float((y-1)*BWA+SW-m_pointPickUp.y)/count;

	int index=ManToIcon[m_nManPickUp];
	UINT last=GetTickCount();
	count/=3;
	for(int i=0;i<count;i++)
	{
		while(GetTickCount()>last && GetTickCount()-last<25);//改变这个数来改变移动速度
		last=GetTickCount();
			
		CPoint point=m_pointPickUp;
		m_pointPickUp.x=long(base.x+xstep*i*3);
		m_pointPickUp.y=long(base.y+ystep*i*3);
		CRect rc;
		rc=MakeUpdateRect(m_pointPickUp,point);
		UpdateFace(rc);		

	}
	PutDown(x,y);
}

void CChessDlg::UpdateFace(CRect &rc)
{	

	CRect rc1(0,0,BWA*9,BWA*10);
	//取得真正要更新的区域:
	rc1.IntersectRect(&rc1,&rc);

	m_pdcBack->BitBlt(rc.left,rc.top,rc.Width(),rc.Height(),m_pdcBoard,rc.left,rc.top,SRCCOPY);
	
	int left=(rc1.left)/BWA,
		top=(rc1.top)/BWA,
		right=(rc1.right)/BWA,
		bottom=(rc1.bottom)/BWA;

	for(int i=left;i<=right;i++)
		for(int j=top;j<=bottom;j++)
			if(m_nManMap[i+1][j+1]!=32)
				m_pdcBack->DrawIcon(i*BWA+SW,j*BWA+SW,m_hIconMan[ManToIcon[m_nManMap[i+1][j+1]]]);

	if(m_nManPickUp!=32)	m_pdcBack->DrawIcon(m_pointPickUp,m_hIconMan[ManToIcon[m_nManPickUp]]);

	CClientDC dc(this);
	dc.BitBlt(rc.left+XOFFSET,rc.top+YOFFSET,rc.Width(),rc.Height(),m_pdcBack,rc.left,rc.top,SRCCOPY);
}

void CChessDlg::FixManMap()
{
	for(int i=0;i<11;i++)
		for(int j=0;j<12;j++)m_nManMap[i][j]=32;

	for(int i=0;i<32;i++)
	{
		if(m_Face.man[i].x)
			m_nManMap[m_Face.man[i].x][m_Face.man[i].y]=i;
	}
		
}

void CChessDlg::OnLButtonDown(UINT nFlags, CPoint point) 
{
	CPoint p=point;
	if(m_Setting.m_nCOrM[m_Setting.m_nPlayer[m_Face.side]]==MAN&&!m_bThinking)	//是人走
		if(FaceToPoint(p))									//点的是棋位
			if(m_nManMap[p.x][p.y]!=32						//有棋子
				&& m_nManMap[p.x][p.y]/16==m_Face.side)		//是现在走的一方
			{
				SetCapture();
				m_pointBeforePickUp=p;
				PickUp(m_nManMap[p.x][p.y]);
			}
	CDialog::OnLButtonDown(nFlags, point);
}

void CChessDlg::OnLButtonUp(UINT nFlags, CPoint point) 
{
	if(m_nManPickUp!=32)			//移动中
	{
		CPoint p=point;
		if(FaceToPoint(p))			//是棋位
		
		{
			if(!Go(32,p.x,p.y))		//不能走
				Move(32,m_pointBeforePickUp.x,m_pointBeforePickUp.y);
		}
		else						//不是棋位
		{
			Move(32,m_pointBeforePickUp.x,m_pointBeforePickUp.y);
		}
		ReleaseCapture();
	}
	CDialog::OnLButtonUp(nFlags, point);
}

void CChessDlg::PickUp(int man)
{
	m_pointPickUp.x=(m_Face.man[man].x-1)*BWA+SW;
	m_pointPickUp.y=(m_Face.man[man].y-1)*BWA+SW;
	m_nManPickUp=man;
	m_Face.man[man].x=0;
	FixManMap();
}

void CChessDlg::PutDown(int x,int y)
{

	m_Face.man[m_nManPickUp].x=x;
	m_Face.man[m_nManPickUp].y=y;
	m_nManPickUp=32;	
	
	FixManMap();
	CRect rc;
	CPoint point((x-1)*BWA,(y-1)*BWA);
	rc=MakeUpdateRect(m_pointPickUp,point);
	UpdateFace(rc);
}

CRect CChessDlg::MakeUpdateRect(CPoint p1, CPoint p2)
{
	CRect rc;
	rc.SetRect(p1,p2);
	rc.NormalizeRect();
	rc.bottom+=BWA;
	rc.right+=BWA;	
	return rc;
}

BOOL CChessDlg::FaceToPoint(CPoint &point)
{
	if((point.x-XOFFSET)%BWA<SW||(point.x-XOFFSET)%BWA>BWA-SW)return FALSE;
	if((point.y-YOFFSET)%BWA<SW||(point.y-YOFFSET)%BWA>BWA-SW)return FALSE;
	CPoint p;
	p.x=(point.x-XOFFSET)/BWA+1;
	p.y=(point.y-YOFFSET)/BWA+1;
	if(p.x<1||p.x>9||p.y<1||p.y>10)return FALSE;
	
	point=p;
	return TRUE;
}

void CChessDlg::OnDestroy() 
{
	CDialog::OnDestroy();
	m_Thinker.Exit();
}



void CChessDlg::OnFileBai() 
{
	CBaiDlg baidlg(this);
	baidlg.DoModal();
}

void CChessDlg::New(CFace face)
{
	m_Thinker.Cut();
	m_StepList.RemoveAll();
	while(m_listStep.GetCount()!=0)m_listStep.DeleteString(0);
	m_nStepCount=1;
	switch(m_Setting.m_nMode)
	{
	case 0:
		m_Setting.m_nPlayer[0]=!m_Setting.m_nPlayer[0];
		m_Setting.m_nPlayer[1]=!m_Setting.m_nPlayer[1];
		break;
	case 1:
		m_Setting.m_nPlayer[0]=0;
		m_Setting.m_nPlayer[1]=1;
		break;
	case 2:
		m_Setting.m_nPlayer[0]=1;
		m_Setting.m_nPlayer[1]=0;
		break;
	}
	
	EnableCommand(CHESS_C_UNDO,FALSE);
	EnableCommand(CHESS_C_REDO,FALSE);
	EnableCommand(CHESS_C_CUT ,FALSE);

	m_bThinking=FALSE;

	m_Face=face;
	FixManMap();
	m_nManPickUp=32;
	CRect rc(0,0,BWA*9,BWA*10);
	UpdateFace(rc);
	ShowStatus();
	if(m_Setting.m_nCOrM[m_Setting.m_nPlayer[m_Face.side]]==COM)
		Think();

}

void CChessDlg::New()
{
	CFace face;
	New(face);
}

void CChessDlg::OnFileNew() 
{
	New();
}

void CChessDlg::OnFileSave() 
{
	CFileDialog filedlg(FALSE,"ccr","未命名",OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT,"棋谱|*.ccr||",this);
	int respond=filedlg.DoModal();
	if(respond==IDOK)m_Face.Save(filedlg.GetFileName());
}

void CChessDlg::OnFileOpen() 
{
	CFileDialog filedlg(TRUE,"ccr","",NULL,"棋谱|*.ccr||",this);
	int respond=filedlg.DoModal();
	if(respond==IDOK)
	{
		CFace face;
		if(face.Open(filedlg.GetFileName()))
		{
			if(face.IsNormal())New(face);
			else 
			{
				CString str;
				str.Format("\"%s\"\n不是正确的棋谱",filedlg.GetFileName());
				MessageBox(str,"错误-象棋",MB_OK|MB_ICONERROR);
			}
		}
		else 
		{
			CString str;
			str.Format("不能打开\"%s\"",filedlg.GetFileName());
			MessageBox(str,"错误-象棋",MB_OK|MB_ICONERROR);
		}
	}
}

void CChessDlg::OnEditUndo() 
{
	m_nStepCount--;
	CStep* pStep=m_StepList.Undo();
	if(pStep==NULL)
	{
		return;
	}

	m_progressStep.SetPos(0);
	PickUp(pStep->man);

	if(pStep->eaten!=32)
	{
		m_Face.man[pStep->eaten]=pStep->to;
		FixManMap();
	}

	EnableCommand(CHESS_C_REDO,TRUE);
	if(m_StepList.IsHead())	EnableCommand(CHESS_C_UNDO,FALSE);

	Move(32,pStep->from.x,pStep->from.y);
	m_Face.side=!m_Face.side;

	m_listStep.DeleteString(m_listStep.GetCount()-1);
	m_listStep.SetCurSel(m_listStep.GetCount()-1);
	m_bThinking=FALSE;

	ShowStatus();

	EnableCommand(CHESS_C_CUT,FALSE);
}

void CChessDlg::EnableCommand(int n, BOOL benable)
{
	CMenu* menu=GetMenu();
	switch(n)
	{
	case CHESS_C_UNDO:
		m_btUndo.EnableWindow(benable);
		menu->EnableMenuItem(ID_CHESS_UNDO,!benable);
		break;
	case CHESS_C_REDO:
		m_btRedo.EnableWindow(benable);
		menu->EnableMenuItem(ID_CHESS_REDO,!benable);
		break;
	case CHESS_C_HELP:
		m_btHelp.EnableWindow(benable);
		menu->EnableMenuItem(ID_CHESS_HELP,!benable);
		break;
	case CHESS_C_CUT:
		m_btCut.EnableWindow(benable);
		menu->EnableMenuItem(ID_CHESS_CUT,!benable);
		break;

	}
}

void CChessDlg::OnEditRedo() 
{
	
	CStep* pStep=m_StepList.Redo();
	if(pStep==NULL)
	{
		return;
	}

	EnableCommand(CHESS_C_UNDO,TRUE);
	if(m_StepList.IsEnd())	EnableCommand(CHESS_C_REDO,FALSE);


	m_listStep.AddString(GetStepName(pStep->man,pStep->to.x,pStep->to.y));
	m_listStep.SetCurSel(m_listStep.GetCount()-1);

	if(pStep->eaten!=32)m_Face.man[pStep->eaten].x=0;

	m_Face.side=!m_Face.side;

	Move(pStep->man,pStep->to.x,pStep->to.y);
	m_nStepCount++;

	ShowStatus();
}

void CChessDlg::OnTimer(UINT nIDEvent) 
{

	if(m_bThinking)
	{
		m_progressStep.SetPos(m_Thinker.GetPercent());

		if(m_Thinker.IsThinkOver())
		{
			CMove move;
			if(m_bThinking&&m_Thinker.GetMove(move,m_Face))
			{
				m_progressStep.SetPos(0);
				if(!Go(move))
				{
					TRACE("Result is error!\n");
					Think();
				}
				else TRACE("Result is OK!\n");	
			}
			else TRACE("Can't get Move!\n");
		}
	}

	CDialog::OnTimer(nIDEvent);
}

CString CChessDlg::GetStepName(int man, int x, int y)
{
	int xfrom,yfrom;
	if (man==32)
	{
		 man=m_nManPickUp;
		 xfrom=m_pointBeforePickUp.x;
		 yfrom=m_pointBeforePickUp.y;
	}
	else
	{
		xfrom=m_Face.man[man].x;
		yfrom=m_Face.man[man].y;
	}
	
	const char strman[14][3]=
	{"帅","仕","相","马","车","炮","兵","将","士","象","马","车","炮","卒"};
	
	const char strnumc[10][3]=
	{"零","一","二","三","四","五","六","七","八","九"};

	const char strnuma[10][3]=
	{"０","１","２","３","４","５","６","７","８","９"};

	const char strjpt[3][3]={"进","退","平"};

	const char strqh[2][3]={"前","后"};


	int j,nformat,nresult,njpt,ndis,nA=0,nB=0;
	CString strresult;

	if(SideOfMan[man]==RED)
	{
		for(j=yfrom-1 ;j>0;j--)
			if(ManToType[man]==ManToType[m_nManMap[xfrom][j]] )nA++;
		for(j=yfrom+1 ;j<11;j++)
			if(ManToType[man]==ManToType[m_nManMap[xfrom][j]] )nB++;
	
		if		(nA+nB> 1)	{nformat=2;nresult=nA+1;}
		else if	(nA+nB==1)	{nformat=1;nresult=nA==0?0:1;}
		else				{nformat=0;nresult=10-xfrom;}

		if		(y> yfrom)	njpt=1;
		else if	(y==yfrom)	njpt=2;
		else				njpt=0;

		if(	ManToType[man]==RED_M||
			ManToType[man]==RED_S||
			ManToType[man]==RED_X)ndis=10-x;
		else
		{
		switch(njpt)
			{
			case 0:	ndis = yfrom-y;	break;
			case 1:	ndis = y-yfrom;	break;
			case 2:	ndis =10-x;	break;
			default:	break;
			}
		}
		switch(nformat)
		{
		case 0:	strresult.Format("%s%s%s%s",
								strman[ManToType[man]],
								strnumc[nresult],
								strjpt[njpt],
								strnumc[ndis]);
			break;
		case 2:	strresult.Format("%s%s%s%s",
								strnuma[nresult],
								strman[ManToType[man]],
								strjpt[njpt],
								strnumc[ndis]);
			break;
		case 1:	strresult.Format("%s%s%s%s",
								strqh[nresult],
								strman[ManToType[man]],
								strjpt[njpt],
								strnumc[ndis]);
			break;
		default:break;
		}
	}
	else	//Black side
	{
		for(j=yfrom+1 ;j<11;j++)
			if(ManToType[man]==ManToType[m_nManMap[xfrom][j]] )nA++;
		for(j=yfrom-1 ;j>0 ;j--)
			if(ManToType[man]==ManToType[m_nManMap[xfrom][j]] )nB++;
	
		if		(nA+nB> 1)	{nformat=2;nresult=nA+1;}
		else if	(nA+nB==1)	{nformat=1;nresult=nA==0?0:1;}
		else				{nformat=0;nresult=xfrom;}

		if		(y< yfrom)	njpt=1;
		else if	(y==yfrom)	njpt=2;
		else				njpt=0;

		if(	ManToType[man]==BLACK_M||
			ManToType[man]==BLACK_S||
			ManToType[man]==BLACK_X)ndis=x;
		else
		{
		switch(njpt)
			{
			case 0:	ndis = y-yfrom;	break;
			case 1:	ndis = yfrom-y;	break;
			case 2:	ndis = x;	break;
			default:	break;
			}
		}
		switch(nformat)
		{
		case 0:	strresult.Format("%s%s%s%s",
								strman[ManToType[man]],
								strnuma[nresult],
								strjpt[njpt],
								strnuma[ndis]);
			break;
		case 2:	strresult.Format("%s%s%s%s",
								strnuma[nresult],
								strman[ManToType[man]],
								strjpt[njpt],
								strnuma[ndis]);
			break;
		case 1:	strresult.Format("%s%s%s%s",
								strqh[nresult],
								strman[ManToType[man]],
								strjpt[njpt],
								strnuma[ndis]);
			break;
		default:break;
		}
	}

	CString str1;
	str1.Format("%2d ",m_nStepCount);

  return str1+strresult;
}


void CChessDlg::ShowStatus()
{	
	CString str,str1,str2;
	str1=m_Face.side==BLACK?"黑":"红";

	TestCheck();
	
	if(m_Setting.m_nCOrM[m_Setting.m_nPlayer[m_Face.side]]==COM)
		str2.Format("电脑%d级",m_Setting.m_nLevel);
	else
		str2="人";

	str.Format("第%d步-%s方-棋手%d(%s)",
		m_nStepCount,str1,m_Setting.m_nPlayer[m_Face.side]+1,str2);

	if(m_bThinking)str+="  [Thinking ...]";
	else
	{
		if (m_bCheck[0]||m_bCheck[1])
		{
			str+="  将军!";
		}
		if (m_bCheck[0])str+=" 红方危险!";
		if (m_bCheck[1])str+=" 黑方危险!";
	}
	SetWindowText(str);
}

void CChessDlg::Think()
{
	if(CanGo())
	{
		m_Thinker.Think(m_Face);
		m_bThinking=TRUE;
		EnableCommand(CHESS_C_CUT);
		ShowStatus();
	}
}

void CChessDlg::OnEditHelp() 
{
	if(!m_bThinking)Think();	
}

BOOL CChessDlg::Go(CMove move)
{
	return	Go(move.man,move.x,move.y);
}

BOOL CChessDlg::CanGo( const CMove &move)
{
	return CanGo(move.man,move.x,move.y);
}

void CChessDlg::OnDblclkSteplist() 
{
	int count=m_listStep.GetCurSel();
	count++;
	while(m_nStepCount!=count)OnEditUndo();
}




BOOL CChessDlg::OpenURL(const char *LinkName)
{
	HINSTANCE hRun = ShellExecute(GetParent()->GetSafeHwnd(), _T("open"), LinkName, NULL, NULL, SW_SHOW);
	if ((int) hRun <= 32)
	{
	TRACE("Failed to invoke URL using ShellExecute\n");
	return FALSE;
	}
	return TRUE;
}

void CChessDlg::OnHelpEmail() 
{
	OpenEmail();
	
}

void CChessDlg::OnHelpWeb() 
{
	// TODO: Add your command handler code here
	OpenWeb();
	
}

BOOL CChessDlg::OpenEmail()
{
	return OpenURL("mailto:thecct@netease.com");
}

BOOL CChessDlg::OpenWeb()
{
	return OpenURL("http://thecct.go.163.com");
}

void CAboutDlg::OnButtonweb() 
{
	((CChessDlg*)GetParent())->OpenWeb();
}

void CAboutDlg::OnButtonemail() 
{
	((CChessDlg*)GetParent())->OpenEmail();
}

void CChessDlg::OnEditCut() 
{
	if(m_bThinking)
	{
		m_Thinker.Cut();
		m_bThinking=FALSE;
		EnableCommand(CHESS_C_CUT,FALSE);
		ShowStatus();
		m_progressStep.SetPos(0);
	}	
}

void CChessDlg::OnFileThinkOption() 
{
	CThinkOptionDlg dlg;

	m_Thinker.Cut();
	m_bThinking=FALSE;
	ShowStatus();

	dlg.m_edits1=m_Thinker.BV1[1];
	dlg.m_editx1=m_Thinker.BV1[2];
	dlg.m_editm1=m_Thinker.BV1[3];
	dlg.m_editj1=m_Thinker.BV1[4];
	dlg.m_editp1=m_Thinker.BV1[5];
	dlg.m_editb1=m_Thinker.BV1[6];

	dlg.m_edits2=m_Thinker.BV2[1];
	dlg.m_editx2=m_Thinker.BV2[2];
	dlg.m_editm2=m_Thinker.BV2[3];
	dlg.m_editj2=m_Thinker.BV2[4];
	dlg.m_editp2=m_Thinker.BV2[5];
	dlg.m_editb2=m_Thinker.BV2[6];

	dlg.m_edit30=m_Thinker.BV3[0];
	dlg.m_edit31=m_Thinker.BV3[1];
	dlg.m_edit32=m_Thinker.BV3[2];
	dlg.m_edit33=m_Thinker.BV3[3];
	dlg.m_edit34=m_Thinker.BV3[4];

	int respond=dlg.DoModal();	
	if(respond==IDOK)
	{
		m_Thinker.BV1[1]=dlg.m_edits1;
		m_Thinker.BV1[2]=dlg.m_editx1;
		m_Thinker.BV1[3]=dlg.m_editm1;
		m_Thinker.BV1[4]=dlg.m_editj1;
		m_Thinker.BV1[5]=dlg.m_editp1;
		m_Thinker.BV1[6]=dlg.m_editb1;

		m_Thinker.BV2[1]=dlg.m_edits2;
		m_Thinker.BV2[2]=dlg.m_editx2;
		m_Thinker.BV2[3]=dlg.m_editm2;
		m_Thinker.BV2[4]=dlg.m_editj2;
		m_Thinker.BV2[5]=dlg.m_editp2;
		m_Thinker.BV2[6]=dlg.m_editb2;

		m_Thinker.BV3[0]=dlg.m_edit30;
		m_Thinker.BV3[1]=dlg.m_edit31;
		m_Thinker.BV3[2]=dlg.m_edit32;
		m_Thinker.BV3[3]=dlg.m_edit33;
		m_Thinker.BV3[4]=dlg.m_edit34;

		m_Thinker.SaveThinkSetting();
	}
}


void CChessDlg::TestCheck()
{
	int i;
	m_bCheck[0]=FALSE;
	m_bCheck[1]=FALSE;
	FixManMap();
	if(m_Face.man[16].x )
		for (i=0;i<=15;i++)
		{
			if(!m_Face.man[i].x)continue;
			if(::CanGo(m_nManMap
				,i
				,m_Face.man[i].x,m_Face.man[i].y
				,m_Face.man[16].x,m_Face.man[16].y))
			{
				m_bCheck[1]=TRUE;
				break;
			}
		}	
	if(m_Face.man[0].x )
		for (i=16;i<=31;i++)
		{
			if(!m_Face.man[i].x)continue;
			if(::CanGo(m_nManMap,i
				,m_Face.man[i].x,m_Face.man[i].y
				,m_Face.man[0].x,m_Face.man[0].y))
			{
				m_bCheck[0]=TRUE;
				break;
			}
		}	
}



void CChessDlg::OnHelpUse() 
{
	HINSTANCE hRun = ShellExecute(GetParent()->GetSafeHwnd(), _T("open"), "chesshelp.html", NULL, NULL, SW_SHOW);
	 if ((int) hRun <= 32)
	 {
		MessageBox("找不到帮助文件\"chesshelp.html\"","错误",MB_ICONERROR);
	 }

}

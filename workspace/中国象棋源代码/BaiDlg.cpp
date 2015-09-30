// BaiDlg.cpp : implementation file
//

#include "stdafx.h"
#include "Chess.h"
#include "BaseDef.h"
#include "CoolButton.h"
#include "Thinker.h"

#include "ChessDlg.h"
#include "BaiDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CBaiDlg dialog


CBaiDlg::CBaiDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CBaiDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(CBaiDlg)
	m_nGoSide = -1;
	//}}AFX_DATA_INIT
}


void CBaiDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CBaiDlg)
	DDX_Control(pDX, IDC_BT_SAVE, m_btSave);
	DDX_Control(pDX, IDC_BT_OPEN, m_btOpen);
	DDX_Control(pDX, IDC_BT_NORMAL, m_btNormal);
	DDX_Control(pDX, IDC_BT_CLEAR, m_btClear);
	DDX_Control(pDX, IDCANCEL, m_btCancel);
	DDX_Control(pDX, IDC_BAIOK, m_btOK);
	DDX_Radio(pDX, IDC_RADIO_GOSIDE_RED, m_nGoSide);
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(CBaiDlg, CDialog)
	//{{AFX_MSG_MAP(CBaiDlg)
	ON_BN_CLICKED(IDC_BAIOK, OnBaiok)
	ON_WM_PAINT()
	ON_WM_RBUTTONDOWN()
	ON_WM_MOUSEMOVE()
	ON_BN_CLICKED(IDC_BT_CLEAR, OnBtClear)
	ON_BN_CLICKED(IDC_BT_NORMAL, OnBtNormal)
	ON_COMMAND(ID_BLACK_B, OnBlackB)
	ON_COMMAND(ID_BLACK_J, OnBlackJ)
	ON_COMMAND(ID_BLACK_K, OnBlackK)
	ON_COMMAND(ID_BLACK_M, OnBlackM)
	ON_COMMAND(ID_BLACK_P, OnBlackP)
	ON_COMMAND(ID_BLACK_S, OnBlackS)
	ON_COMMAND(ID_BLACK_X, OnBlackX)
	ON_COMMAND(ID_RED_B, OnRedB)
	ON_COMMAND(ID_RED_J, OnRedJ)
	ON_COMMAND(ID_RED_K, OnRedK)
	ON_COMMAND(ID_RED_M, OnRedM)
	ON_COMMAND(ID_RED_P, OnRedP)
	ON_COMMAND(ID_RED_S, OnRedS)
	ON_COMMAND(ID_RED_X, OnRedX)
	ON_COMMAND(ID_BAI_DELETE, OnBaiDelete)
	ON_WM_LBUTTONDOWN()
	ON_BN_CLICKED(IDC_BT_OPEN, OnBtOpen)
	ON_BN_CLICKED(IDC_BT_SAVE, OnBtSave)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CBaiDlg message handlers

BOOL CBaiDlg::OnInitDialog() 
{
	CDialog::OnInitDialog();
	
	m_pParent=(CChessDlg*)GetParent();

	m_pdcBack=m_pParent->m_pdcBack;
	m_pdcBoard=m_pParent->m_pdcBoard;
	New(m_pParent->m_Face);
	
	UpdateData(FALSE);
	
	return TRUE;  // return TRUE unless you set the focus to a control
	              // EXCEPTION: OCX Property Pages should return FALSE
}

void CBaiDlg::OnBaiok() 
{
	// TODO: Add your control notification handler code here
	UpdateData(TRUE); 
	CFace face;
	if(GetFace(face))
	{
		face.side=m_nGoSide;
		m_pParent->New(face);	
		OnOK();
	}
}

void CBaiDlg::UpdateFace()
{
	CRect rc(0,0,BWA*9,BWA*10);
	m_pdcBack->BitBlt(rc.left,rc.top,rc.Width(),rc.Height(),m_pdcBoard,rc.left,rc.top,SRCCOPY);
	
	for(int i=0;i<=8;i++)
		for(int j=0;j<=9;j++)
			if(m_IconMap[i+1][j+1]!=32)
				m_pdcBack->DrawIcon(i*BWA+SW,j*BWA+SW,m_pParent->m_hIconMan[m_IconMap[i+1][j+1]]);
	CClientDC dc(this);
	dc.BitBlt(rc.left,rc.top,rc.Width(),rc.Height(),m_pdcBack,rc.left,rc.top,SRCCOPY);

}



void CBaiDlg::OnPaint() 
{
	CPaintDC dc(this);
	UpdateFace();	
}

void CBaiDlg::OnRButtonDown(UINT nFlags, CPoint point) 
{
	CPoint p=point;
	if(FaceToPoint(p))
	{
		m_Point=p;
		CMenu menu;
		menu.LoadMenu(IDR_MENUBAI);
		if(m_IconMap[p.x][p.y]==32)	menu.EnableMenuItem(ID_BAI_DELETE,1);		
		else 	menu.EnableMenuItem(ID_BAI_DELETE,0);
		ClientToScreen(&point);
		menu.GetSubMenu(0)->TrackPopupMenu(TPM_LEFTALIGN | TPM_RIGHTBUTTON,
			point.x, point.y, this);

	}
	CDialog::OnRButtonDown(nFlags, point);
}

void CBaiDlg::OnMouseMove(UINT nFlags, CPoint point) 
{
	// TODO: Add your message handler code here and/or call default
	
	CDialog::OnMouseMove(nFlags, point);
}

BOOL CBaiDlg::FaceToPoint(CPoint &point)
{
	if((point.x)%BWA<SW||(point.x)%BWA>BWA-SW)return FALSE;
	if((point.y)%BWA<SW||(point.y)%BWA>BWA-SW)return FALSE;
	CPoint p;
	p.x=(point.x)/BWA+1;
	p.y=(point.y)/BWA+1;
	if(p.x<1||p.x>9||p.y<1||p.y>10)return FALSE;	
	point=p;
	return TRUE;
}

void CBaiDlg::OnBtClear() 
{
	// TODO: Add your control notification handler code here
	for(int i=0;i<10;i++)
		for(int j=0;j<11;j++)m_IconMap[i][j]=32;
	m_IconMap[5][1]=7;
	m_IconMap[5][10]=0;
	UpdateFace();
	
}

void CBaiDlg::OnBtNormal() 
{
	Reset();
	UpdateFace();
}

void CBaiDlg::OnBlackB() 
{
	if(::IsNormal(BLACK_B,m_Point.x,m_Point.y))
	{
		m_IconMap[m_Point.x][m_Point.y]=BLACK_B;
		UpdateFace();	
	}
}

void CBaiDlg::OnBlackJ() 
{
	m_IconMap[m_Point.x][m_Point.y]=BLACK_J;
	UpdateFace();
	
}

void CBaiDlg::OnBlackK() 
{
	if(::IsNormal(BLACK_K,m_Point.x,m_Point.y))
	{
		m_IconMap[m_Point.x][m_Point.y]=BLACK_K;
		UpdateFace();
	}
}

void CBaiDlg::OnBlackM() 
{
	m_IconMap[m_Point.x][m_Point.y]=BLACK_M;
	UpdateFace();
	
}

void CBaiDlg::OnBlackP() 
{
	m_IconMap[m_Point.x][m_Point.y]=BLACK_P;
	UpdateFace();
	
}

void CBaiDlg::OnBlackS() 
{
	if(::IsNormal(BLACK_S,m_Point.x,m_Point.y))
	{
		m_IconMap[m_Point.x][m_Point.y]=BLACK_S;
		UpdateFace();
	}
}

void CBaiDlg::OnBlackX() 
{
	if(::IsNormal(BLACK_X,m_Point.x,m_Point.y))
	{
		m_IconMap[m_Point.x][m_Point.y]=BLACK_X;
		UpdateFace();
	}
}

void CBaiDlg::OnRedB() 
{
	if(::IsNormal(RED_B,m_Point.x,m_Point.y))
	{
		m_IconMap[m_Point.x][m_Point.y]=RED_B;
		UpdateFace();
	}
}

void CBaiDlg::OnRedJ() 
{
	m_IconMap[m_Point.x][m_Point.y]=RED_J;
	UpdateFace();
	
}

void CBaiDlg::OnRedK() 
{
	if(::IsNormal(RED_K,m_Point.x,m_Point.y))
	{
		m_IconMap[m_Point.x][m_Point.y]=RED_K;
		UpdateFace();
	}
}

void CBaiDlg::OnRedM() 
{
	m_IconMap[m_Point.x][m_Point.y]=RED_M;
	UpdateFace();
	
}

void CBaiDlg::OnRedP() 
{
	m_IconMap[m_Point.x][m_Point.y]=RED_P;
	UpdateFace();
	
}

void CBaiDlg::OnRedS() 
{
	if(::IsNormal(RED_S,m_Point.x,m_Point.y))
	{
		m_IconMap[m_Point.x][m_Point.y]=RED_S;
		UpdateFace();
	}

}

void CBaiDlg::OnRedX() 
{
	if(::IsNormal(RED_X,m_Point.x,m_Point.y))
	{
		m_IconMap[m_Point.x][m_Point.y]=RED_X;
		UpdateFace();
	}
	
}

void CBaiDlg::OnBaiDelete() 
{
	
	m_IconMap[m_Point.x][m_Point.y]=32;
	UpdateFace();
	
}

BOOL CBaiDlg::GetFace(CFace& face)
{
	int i,j,count[14];
	for(i=0;i<14;i++)count[i]=0;
	for(i=1;i<=9;i++)
		for(j=1;j<=10;j++)
			if(m_IconMap[i][j]!=32)count[m_IconMap[i][j]]++;
	const static char caption[15]="错误";
	if(count[0]==0)
	{
		MessageBox("没有\"帅\"?",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	if(count[7]==0)
	{
		MessageBox("没有\"将\"?",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	if(count[0]>1)
	{
		MessageBox("\"帅\"太多.",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	if(count[1]>2)
	{
		MessageBox("\"仕\"太多.",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	if(count[2]>2)
	{
		MessageBox("\"相\"太多.",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	if(count[3]>2)
	{
		MessageBox("红\"马\"太多.",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	if(count[4]>2)
	{
		MessageBox("红\"车\"太多.",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	if(count[5]>2)
	{
		MessageBox("红\"炮\"太多.",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	if(count[6]>5)
	{
		MessageBox("\"兵\"太多.",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	if(count[7]>1)
	{
		MessageBox("\"将\"太多.",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	if(count[8]>2)
	{
		MessageBox("\"士\"太多.",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	if(count[9]>2)
	{
		MessageBox("\"象\"太多.",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	if(count[10]>2)
	{
		MessageBox("黑\"马\"太多.",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	if(count[11]>2)
	{
		MessageBox("黑\"车\"太多.",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	if(count[12]>2)
	{
		MessageBox("黑\"炮\"太多.",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	if(count[13]>5)
	{
		MessageBox("\"卒\"太多.",caption,MB_OK|MB_ICONINFORMATION);
		return FALSE;
	}
	const static int manoff[14]={0,1,3,5,7,9,11,16,17,19,21,23,25,27};
	for(i=0;i<14;i++)count[i]=0;
	for(i=0;i<32;i++)face.man[i].x=0;

	for(i=1;i<=9;i++)
		for(j=1;j<=10;j++)
			if(m_IconMap[i][j]!=32)
			{
				face.man[manoff[m_IconMap[i][j]]+count[m_IconMap[i][j]]].x=i;
				face.man[manoff[m_IconMap[i][j]]+count[m_IconMap[i][j]]].y=j;
				count[m_IconMap[i][j]]++;
			}

					
	return TRUE;
}

void CBaiDlg::Reset()
{
	for(int i=0;i<10;i++)
		for(int j=0;j<11;j++)m_IconMap[i][j]=32;
	m_IconMap[1][10]=4;m_IconMap[1][1]=11;
	m_IconMap[2][10]=3;m_IconMap[2][1]=10;
	m_IconMap[3][10]=2;m_IconMap[3][1]=9;
	m_IconMap[4][10]=1;m_IconMap[4][1]=8;
	m_IconMap[5][10]=0;m_IconMap[5][1]=7;
	m_IconMap[6][10]=1;m_IconMap[6][1]=8;
	m_IconMap[7][10]=2;m_IconMap[7][1]=9;
	m_IconMap[8][10]=3;m_IconMap[8][1]=10;
	m_IconMap[9][10]=4;m_IconMap[9][1]=11;
	m_IconMap[2][8]=5;m_IconMap[2][3]=12;
	m_IconMap[8][8]=5;m_IconMap[8][3]=12;
	m_IconMap[1][7]=6;m_IconMap[1][4]=13;
	m_IconMap[3][7]=6;m_IconMap[3][4]=13;
	m_IconMap[5][7]=6;m_IconMap[5][4]=13;
	m_IconMap[7][7]=6;m_IconMap[7][4]=13;
	m_IconMap[9][7]=6;m_IconMap[9][4]=13;

}

void CBaiDlg::OnLButtonDown(UINT nFlags, CPoint point) 
{
	OnRButtonDown(nFlags,point);
	CDialog::OnLButtonDown(nFlags, point);
}

void CBaiDlg::OnBtOpen() 
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
				MessageBox(str,"错误",MB_OK|MB_ICONERROR);
			}
		}
		else 
		{
			CString str;
			str.Format("不能打开\"%s\"",filedlg.GetFileName());
			MessageBox(str,"错误",MB_OK|MB_ICONERROR);
		}

	}
	
}

void CBaiDlg::OnBtSave() 
{
	UpdateData(TRUE); 
	CFace face;
	if(GetFace(face))
	{
		face.side=m_nGoSide;
		CFileDialog filedlg(FALSE,"ccr","未命名",OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT,"棋谱|*.ccr||",this);
		int respond=filedlg.DoModal();
		if(respond==IDOK)face.Save(filedlg.GetFileName());
	}
}

void CBaiDlg::New(const CFace &face)
{
	for(int i=0;i<10;i++)
		for(int j=0;j<11;j++)m_IconMap[i][j]=32;

	for(int i=0;i<32;i++)
		if(face.man[i].x!=32)
		{
			m_IconMap[face.man[i].x][face.man[i].y]
				=ManToIcon[i];
		}
	m_nGoSide=m_pParent->m_Face.side;	
	UpdateData(FALSE);
	UpdateFace();
}



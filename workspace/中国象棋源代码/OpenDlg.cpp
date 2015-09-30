// OpenDlg.cpp : implementation file
//

#include "stdafx.h"
#include "chess.h"
#include "OpenDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// COpenDlg dialog


COpenDlg::COpenDlg(CWnd* pParent /*=NULL*/)
	: CDialog(COpenDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(COpenDlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT

	CWinApp* pApp=AfxGetApp();
	m_hIconDir		= pApp->LoadIcon(IDI_DIR);

}


void COpenDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(COpenDlg)
	DDX_Control(pDX, IDOK, m_btOK);
	DDX_Control(pDX, IDCANCEL, m_btCancel);
	DDX_Control(pDX, IDC_FILELIST, m_btList);
	DDX_Control(pDX, IDC_DIR, m_btDir);
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(COpenDlg, CDialog)
	//{{AFX_MSG_MAP(COpenDlg)
	ON_BN_CLICKED(IDC_DIR, OnBtDir)
	ON_WM_PAINT()
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// COpenDlg message handlers

BOOL COpenDlg::OnInitDialog() 
{
	CDialog::OnInitDialog();
	
	m_btDir.SetIcon(m_hIconDir,32,32);
	
	return TRUE;  // return TRUE unless you set the focus to a control
	              // EXCEPTION: OCX Property Pages should return FALSE
}

void COpenDlg::OnBtDir() 
{

	m_btDir.SetWindowText("D:\\aaaaa\\dddddddc测试ddddBbb\\CCT\\");
	
}

void COpenDlg::OnPaint() 
{
	CPaintDC dc(this); // device context for painting
	const char strman[14][3]=
	{"帅","仕","相","马","车","炮","兵","将","士","象","马","车","炮","卒"};

	
	// Do not call CDialog::OnPaint() for painting messages
}

// BestDlg.cpp : implementation file
//

#include "stdafx.h"
#include "Pente.h"
#include "Globalvar.h"
#include "BestDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CBestDlg dialog


CBestDlg::CBestDlg(CWnd* pParent /*=NULL*/)
	: CDialog(::g_bChinese ? IDD_BEST1 : IDD_BEST)
{
	//{{AFX_DATA_INIT(CBestDlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
}


void CBestDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CBestDlg)
		// NOTE: the ClassWizard will add DDX and DDV calls here
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(CBestDlg, CDialog)
	//{{AFX_MSG_MAP(CBestDlg)
	ON_BN_CLICKED(IDC_RESTARTCOUNT, OnRestartcount)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CBestDlg message handlers

BOOL CBestDlg::OnInitDialog() 
{
	CDialog::OnInitDialog();
	
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	Name1Edit().SetWindowText(g_strName1);
	Name2Edit().SetWindowText(g_strName2);
	Name3Edit().SetWindowText(g_strName3);
		
	char str[4]; 
	wsprintf(str,"%d",g_nTime1);
	Time1Edit().SetWindowText(str);
	wsprintf(str,"%d",g_nTime2);
	Time2Edit().SetWindowText(str);
	wsprintf(str,"%d",g_nTime3);
	Time3Edit().SetWindowText(str);
	
	return TRUE;  // return TRUE unless you set the focus to a control
	              // EXCEPTION: OCX Property Pages should return FALSE
}

void CBestDlg::OnRestartcount() 
{
	g_strName1 = "Anonymous";
	g_strName2 = "Anonymous";
	g_strName3 = "Anonymous";
	g_nTime1 = 225;
	g_nTime2 = 225;
	g_nTime3 = 225;

	Name1Edit().SetWindowText(g_strName1);
	Name2Edit().SetWindowText(g_strName2);
	Name3Edit().SetWindowText(g_strName3);
	Time1Edit().SetWindowText("225");
	Time2Edit().SetWindowText("225");
	Time3Edit().SetWindowText("225");
}

// ThinkOptionDlg.cpp : implementation file
//

#include "stdafx.h"
#include "chess.h"
#include "ThinkOptionDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CThinkOptionDlg dialog


CThinkOptionDlg::CThinkOptionDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CThinkOptionDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(CThinkOptionDlg)
	m_editb1 = 0;
	m_editb2 = 0;
	m_editj1 = 0;
	m_editj2 = 0;
	m_editm1 = 0;
	m_editm2 = 0;
	m_editp1 = 0;
	m_editp2 = 0;
	m_edits1 = 0;
	m_edits2 = 0;
	m_editx1 = 0;
	m_editx2 = 0;
	m_edit30 = 0;
	m_edit31 = 0;
	m_edit32 = 0;
	m_edit33 = 0;
	m_edit34 = 0;
	//}}AFX_DATA_INIT
}


void CThinkOptionDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CThinkOptionDlg)
	DDX_Control(pDX, IDC_SPIN17, m_spin17);
	DDX_Control(pDX, IDC_SPIN16, m_spin16);
	DDX_Control(pDX, IDC_SPIN15, m_spin15);
	DDX_Control(pDX, IDC_SPIN14, m_spin14);
	DDX_Control(pDX, IDC_SPIN13, m_spin13);
	DDX_Control(pDX, IDC_SPIN9, m_spin9);
	DDX_Control(pDX, IDC_SPIN8, m_spin8);
	DDX_Control(pDX, IDC_SPIN7, m_spin7);
	DDX_Control(pDX, IDC_SPIN6, m_spin6);
	DDX_Control(pDX, IDC_SPIN5, m_spin5);
	DDX_Control(pDX, IDC_SPIN4, m_spin4);
	DDX_Control(pDX, IDC_SPIN3, m_spin3);
	DDX_Control(pDX, IDC_SPIN2, m_spin2);
	DDX_Control(pDX, IDC_SPIN12, m_spin12);
	DDX_Control(pDX, IDC_SPIN11, m_spin11);
	DDX_Control(pDX, IDC_SPIN10, m_spin10);
	DDX_Control(pDX, IDC_SPIN1, m_spin1);
	DDX_Control(pDX, IDCANCEL, m_btCancel);
	DDX_Control(pDX, ID_MYOK, m_btOk);
	DDX_Text(pDX, IDC_EDITB1, m_editb1);
	DDV_MinMaxInt(pDX, m_editb1, 0, 1000);
	DDX_Text(pDX, IDC_EDITB2, m_editb2);
	DDV_MinMaxInt(pDX, m_editb2, 0, 1000);
	DDX_Text(pDX, IDC_EDITJ1, m_editj1);
	DDV_MinMaxInt(pDX, m_editj1, 0, 1000);
	DDX_Text(pDX, IDC_EDITJ2, m_editj2);
	DDV_MinMaxInt(pDX, m_editj2, 0, 1000);
	DDX_Text(pDX, IDC_EDITM1, m_editm1);
	DDV_MinMaxInt(pDX, m_editm1, 0, 1000);
	DDX_Text(pDX, IDC_EDITM2, m_editm2);
	DDV_MinMaxInt(pDX, m_editm2, 0, 1000);
	DDX_Text(pDX, IDC_EDITP1, m_editp1);
	DDV_MinMaxInt(pDX, m_editp1, 0, 1000);
	DDX_Text(pDX, IDC_EDITP2, m_editp2);
	DDV_MinMaxInt(pDX, m_editp2, 0, 1000);
	DDX_Text(pDX, IDC_EDITS1, m_edits1);
	DDV_MinMaxInt(pDX, m_edits1, 0, 1000);
	DDX_Text(pDX, IDC_EDITS2, m_edits2);
	DDV_MinMaxInt(pDX, m_edits2, 0, 1000);
	DDX_Text(pDX, IDC_EDITX1, m_editx1);
	DDV_MinMaxInt(pDX, m_editx1, 0, 1000);
	DDX_Text(pDX, IDC_EDITX2, m_editx2);
	DDV_MinMaxInt(pDX, m_editx2, 0, 1000);
	DDX_Text(pDX, IDC_EDIT30, m_edit30);
	DDV_MinMaxInt(pDX, m_edit30, 0, 1000);
	DDX_Text(pDX, IDC_EDIT31, m_edit31);
	DDV_MinMaxInt(pDX, m_edit31, 0, 1000);
	DDX_Text(pDX, IDC_EDIT32, m_edit32);
	DDV_MinMaxInt(pDX, m_edit32, 0, 1000);
	DDX_Text(pDX, IDC_EDIT33, m_edit33);
	DDV_MinMaxInt(pDX, m_edit33, 0, 1000);
	DDX_Text(pDX, IDC_EDIT34, m_edit34);
	DDV_MinMaxInt(pDX, m_edit34, 0, 1000);
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(CThinkOptionDlg, CDialog)
	//{{AFX_MSG_MAP(CThinkOptionDlg)
	ON_BN_CLICKED(ID_MYOK, OnMyok)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CThinkOptionDlg message handlers

BOOL CThinkOptionDlg::OnInitDialog() 
{
	CDialog::OnInitDialog();
	m_spin1.SetRange(1,1000);
	m_spin2.SetRange(1,1000);
	m_spin3.SetRange(1,1000);
	m_spin4.SetRange(1,1000);
	m_spin5.SetRange(1,1000);
	m_spin6.SetRange(1,1000);
	m_spin7.SetRange(1,1000);
	m_spin8.SetRange(1,1000);
	m_spin9.SetRange(1,1000);
	m_spin10.SetRange(1,1000);
	m_spin11.SetRange(1,1000);
	m_spin12.SetRange(1,1000);
	m_spin13.SetRange(1,1000);
	m_spin14.SetRange(1,1000);
	m_spin15.SetRange(1,1000);
	m_spin16.SetRange(1,1000);
	m_spin17.SetRange(1,1000);
	
	// TODO: Add extra initialization here
	
	return TRUE;  // return TRUE unless you set the focus to a control
	              // EXCEPTION: OCX Property Pages should return FALSE
}

void CThinkOptionDlg::OnMyok() 
{

	OnOK();
}


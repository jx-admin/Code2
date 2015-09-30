// OptionDlg.cpp : implementation file
//

#include "stdafx.h"
#include "Chess.h"
#include "BaseDef.h"
#include "CoolButton.h"
#include "Thinker.h"

#include "ChessDlg.h"
#include "OptionDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// COptionDlg dialog


COptionDlg::COptionDlg(CWnd* pParent /*=NULL*/)
	: CDialog(COptionDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(COptionDlg)
	m_nMode = -1;
	m_CORM0 = -1;
	m_CORM1 = -1;
	m_nLevel = 0;
	//}}AFX_DATA_INIT
}


void COptionDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(COptionDlg)
	DDX_Control(pDX, IDOK, m_btOK);
	DDX_Control(pDX, IDC_RESET, m_btReset);
	DDX_Control(pDX, IDCANCEL, m_btCancel);
	DDX_Control(pDX, IDC_SPIN1, m_spinLevel);
	DDX_Radio(pDX, IDC_RADIOMODE1, m_nMode);
	DDX_Radio(pDX, IDC_RADIOMAN1, m_CORM0);
	DDX_Radio(pDX, IDC_RADIOMAN2, m_CORM1);
	DDX_Text(pDX, IDC_EDITLEVEL1, m_nLevel);
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(COptionDlg, CDialog)
	//{{AFX_MSG_MAP(COptionDlg)
	ON_BN_CLICKED(IDC_RESET, OnReset)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// COptionDlg message handlers

//DEL void COptionDlg::OnOption() 
//DEL {
//DEL 	COptionDlg optiondlg;
//DEL 	optiondlg.DoModal();
//DEL }

BOOL COptionDlg::OnInitDialog() 
{
	CDialog::OnInitDialog();	
	m_spinLevel.SetRange32(1,4);
	UpdateData(FALSE);
	return TRUE;	// return TRUE unless you set the focus to a control	           
					// EXCEPTION: OCX Property Pages should return FALSE
}


void COptionDlg::OnReset() 
{
	m_nMode=1;
	m_CORM0=0;
	m_CORM1=1;
	m_nLevel=2;

	UpdateData(FALSE);
}



// RecordDlg.cpp : implementation file
//

#include "stdafx.h"
#include "Pente.h"
#include "Globalvar.h"
#include "RecordDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CRecordDlg dialog


CRecordDlg::CRecordDlg(CWnd* pParent /*=NULL*/)
	: CDialog(::g_bChinese ? IDD_RECORD1 : IDD_RECORD)
{
	//{{AFX_DATA_INIT(CRecordDlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
}


void CRecordDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CRecordDlg)
		// NOTE: the ClassWizard will add DDX and DDV calls here
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(CRecordDlg, CDialog)
	//{{AFX_MSG_MAP(CRecordDlg)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CRecordDlg message handlers

BOOL CRecordDlg::OnInitDialog() 
{
	CDialog::OnInitDialog();
	
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	CString str;
	if (g_nSkill == 1)
	{
		str.LoadString(g_bChinese ? IDS_BEGINNER_CHINESE:IDS_BEGINNER_ENGLISH);
		SkillEdit().SetWindowText(str);
		NameEdit().SetWindowText(g_strName1);
	}
	else if (g_nSkill == 2)
	{
		str.LoadString(g_bChinese ? IDS_INTERMEDIATE_CHINESE:IDS_INTERMEDIATE_ENGLISH);
		SkillEdit().SetWindowText(str);
		NameEdit().SetWindowText(g_strName2);
	}
	if (g_nSkill == 3)
	{
		str.LoadString(g_bChinese ? IDS_EXPERT_CHINESE:IDS_EXPERT_ENGLISH);
		SkillEdit().SetWindowText(str);
		NameEdit().SetWindowText(g_strName3);
	}
	
	NameEdit().SetSel(0, -1);
	NameEdit().SetFocus();
	
	return TRUE;  // return TRUE unless you set the focus to a control
	              // EXCEPTION: OCX Property Pages should return FALSE
}

void CRecordDlg::OnOK() 
{
	if (g_nSkill == 1)
	{
		NameEdit().GetWindowText(g_strName1);
		g_nTime1 = g_nStoneNum;
	}
	else if (g_nSkill == 2)
	{
		NameEdit().GetWindowText(g_strName2);
		g_nTime2 = g_nStoneNum;
	}
	if (g_nSkill == 3)
	{
		NameEdit().GetWindowText(g_strName3);
		g_nTime3 = g_nStoneNum;
	}                   
	
	CDialog::OnOK();
}

// SetupDlg.cpp : implementation file
//

#include "stdafx.h"
#include "pente.h"
#include "resource.h"
#include "GlobalVar.h"
#include "setupdlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CSetupDlg dialog


CSetupDlg::CSetupDlg(CWnd* pParent /*=NULL*/)
	: CDialog(g_bChinese ? IDD_SETUPDLG1 : IDD_SETUPDLG, pParent)
{
	//{{AFX_DATA_INIT(CSetupDlg)
	//}}AFX_DATA_INIT
}


void CSetupDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CSetupDlg)
	DDX_Control(pDX, IDC_RADIO_WITHCOMPUTER, m_RadioWithComputer);
	DDX_Control(pDX, IDC_RADIO_2PLAYER, m_Radio2Player);
	DDX_Control(pDX, IDC_RADIO_YOU_FIRST, m_RadioYouFirst);
	DDX_Control(pDX, IDC_RADIO_COMPUTER_FIRST, m_RadioComputerFirst);
	DDX_Control(pDX, IDC_CHECK_SOUND, m_CheckSound);
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(CSetupDlg, CDialog)
	//{{AFX_MSG_MAP(CSetupDlg)
	ON_BN_CLICKED(IDC_CHECK_SOUND, OnCheckSound)
	ON_BN_CLICKED(IDC_RADIO_2PLAYER, OnRadio2player)
	ON_BN_CLICKED(IDC_RADIO_COMPUTER_FIRST, OnRadioComputerFirst)
	ON_BN_CLICKED(IDC_RADIO_WITHCOMPUTER, OnRadioWithcomputer)
	ON_BN_CLICKED(IDC_RADIO_YOU_FIRST, OnRadioYouFirst)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CSetupDlg message handlers

void CSetupDlg::OnCheckSound() 
{
	g_bSoundOn = ! g_bSoundOn;
}

void CSetupDlg::OnRadioWithcomputer() 
{
	g_nRunMode = MODE_WITH_COMPUTER;
	m_RadioYouFirst.EnableWindow(TRUE);
	m_RadioComputerFirst.EnableWindow(TRUE);
	m_RadioYouFirst.SetCheck(g_bUserBlack);
	m_RadioComputerFirst.SetCheck(!g_bUserBlack);
}

void CSetupDlg::OnRadio2player() 
{
	g_nRunMode = MODE_2PLAYER;
	m_RadioYouFirst.EnableWindow(FALSE);
	m_RadioComputerFirst.EnableWindow(FALSE);
}

void CSetupDlg::OnRadioYouFirst() 
{
	g_bUserBlack = TRUE;
}

void CSetupDlg::OnRadioComputerFirst() 
{
	g_bUserBlack = FALSE;
}

BOOL CSetupDlg::OnInitDialog() 
{
	CDialog::OnInitDialog();
	
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon
	CenterWindow();
	
	m_CheckSound.SetCheck(g_bSoundOn);
	m_RadioYouFirst.SetCheck(g_bUserBlack);
	m_RadioComputerFirst.SetCheck(!g_bUserBlack);

	switch (g_nRunMode)
	{
		case MODE_WITH_COMPUTER:
			m_RadioWithComputer.SetCheck(1);
			m_RadioYouFirst.EnableWindow(TRUE);
			m_RadioComputerFirst.EnableWindow(TRUE);
			break;
		case MODE_2PLAYER:
			m_Radio2Player.SetCheck(1);
			m_RadioYouFirst.EnableWindow(FALSE);
			m_RadioComputerFirst.EnableWindow(FALSE);
			break;
	}

	return TRUE;  // return TRUE unless you set the focus to a control
	              // EXCEPTION: OCX Property Pages should return FALSE
}

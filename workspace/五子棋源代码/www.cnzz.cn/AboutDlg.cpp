// AboutDlg.cpp : implementation file
//

#include "stdafx.h"
#include <dos.h>
#include <direct.h>
#include <mmsystem.h>

#include "resource.h"
#include "HelperAPI.h"
#include "Globalvar.h"
#include "AboutDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CAboutDlg dialog


CAboutDlg::CAboutDlg(CWnd* pParent /*=NULL*/)
	: CDialog(::g_bChinese ? IDD_ABOUTDLG1 : IDD_ABOUTDLG, pParent)
{
	//{{AFX_DATA_INIT(CAboutDlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
}


void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CAboutDlg)
		// NOTE: the ClassWizard will add DDX and DDV calls here
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
	//{{AFX_MSG_MAP(CAboutDlg)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CAboutDlg message handlers

BOOL CAboutDlg::OnInitDialog() 
{
	CDialog::OnInitDialog();
	
	HICON hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
	SetIcon(hIcon, TRUE);		// Set big icon
	SetIcon(hIcon, FALSE);		// Set small icon

	CString str;

	// fill memory info
	MEMORYSTATUS MemStat;
	MemStat.dwLength = sizeof(MEMORYSTATUS);
	GlobalMemoryStatus(&MemStat);
	// Total Physical mem
	str.Format("%lu KB", MemStat.dwTotalPhys / 1024L);
	SetDlgItemText(IDC_PHYSICAL_MEM, str);
	// Free Physical mem
	str.Format("%lu KB", MemStat.dwAvailPhys / 1024L);
	SetDlgItemText(IDC_AVAIL_MEM, str);
	// Free Virtual mem
	str.Format("%lu KB", MemStat.dwAvailVirtual / 1024L);
	SetDlgItemText(IDC_AVAIL_VIRTUAL_MEM, str);

	// fill CPU info
	SYSTEM_INFO SysInfo;
	GetSystemInfo(&SysInfo);
	switch (SysInfo.dwProcessorType)
	{
	case PROCESSOR_INTEL_486:
			str = "Intel 80486";
			break;
	case PROCESSOR_INTEL_PENTIUM:
			str = "Intel Pentium";
			break;
	default:
			str = "";
			break;
	}
	SetDlgItemText(IDC_CPU, str);

	// fill OS info
	OSVERSIONINFO OsInfo;
	OsInfo.dwOSVersionInfoSize = sizeof(OSVERSIONINFO);
	if (GetVersionEx(&OsInfo))
	{
		// platform
		switch (OsInfo.dwPlatformId)
		{
		case VER_PLATFORM_WIN32_WINDOWS:
			str = "Windows 95";
			break;
		case VER_PLATFORM_WIN32_NT:
			str = "Windows NT";
			break;
		default:
			str = "";
			break;
		}
		SetDlgItemText(IDC_OS, str);

		// version and language
		char   szVer[512];
		GetWindowsVersion((LPSTR)szVer);

		str.Format("%ld.%ld, %s", OsInfo.dwMajorVersion, 
							OsInfo.dwMinorVersion, szVer);
		SetDlgItemText(IDC_VERSION, str);
	}

	// fill disk information
	char szWindowsPath[_MAX_PATH];
	GetWindowsDirectory(szWindowsPath, _MAX_PATH);
	ULARGE_INTEGER TotalNumberOfBytes, TotalNumberOfFreeBytes;
	GetDiskFreeSpaceEx((LPSTR)szWindowsPath,
						NULL,
						&TotalNumberOfBytes,
						&TotalNumberOfFreeBytes);
	// total space
	str.Format("%lu KB", TotalNumberOfBytes.QuadPart / 1024L);
	SetDlgItemText(IDC_TOTAL_SPACE, str);
	// free space
	str.Format("%lu KB", TotalNumberOfFreeBytes.QuadPart / 1024L);
	SetDlgItemText(IDC_FREE_SPACE, str);

 	PlaySound(IDSOUND_WELCOME);	                       
	
	return TRUE;  // return TRUE unless you set the focus to a control
	              // EXCEPTION: OCX Property Pages should return FALSE
}

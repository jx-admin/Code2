#include "stdafx.h"
#include <mmsystem.h>

#include "GlobalVar.h"
#include "HelperAPI.h"

// Get window version description
//
int GetWindowsVersion(LPSTR strVersion)
{
	char lpszSysPath[512];	/* address of buffer for Windows directory	*/

	GetWindowsDirectory(lpszSysPath, 512);
	strcat(lpszSysPath,"\\system\\user.exe");

	BYTE   abData[512];
	DWORD  handle;
	DWORD  dwSize;
	typedef struct tagLANGANDCP
	{
    	WORD wLanguage;
    	WORD wCodePage;
  	} LANGANDCP;
    LANGANDCP FAR  *lpBuffer;           // holds lang and codepage

	dwSize = GetFileVersionInfoSize(lpszSysPath, &handle);

	GetFileVersionInfo(lpszSysPath, handle, dwSize, abData);

	VerQueryValue(abData, "\\VarFileInfo\\Translation", (VOID FAR* FAR*)&lpBuffer, (UINT FAR *)&dwSize);
  	VerLanguageName (lpBuffer->wLanguage, strVersion, 512);

    return LOBYTE(LOWORD(GetVersion())); // windows version greater than 3.x?
}

// Play sound in resource
//
BOOL PlaySound(UINT IDSoundRes, WORD wFlag)
{               
	if (g_bSoundOn)
		if (PlaySound(MAKEINTRESOURCE(IDSoundRes),
			AfxGetInstanceHandle(),
			wFlag|SND_RESOURCE|SND_NODEFAULT))
			return TRUE;
	return FALSE;
}


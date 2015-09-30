#ifndef _INC_HELPERAPI
#define _INC_HELPERAPI

#include <mmsystem.h>

int  GetWindowsVersion(LPSTR strVersion);
BOOL PlaySound(UINT IDSoundRes, WORD wFlag = SND_ASYNC);

#endif	//_INC_HELPERAPI
// Dib.cpp : implementation file
//

#include "stdafx.h"

#include "Dib.h"
//#include <windowsx.h>		// especially for GlobalAllocPtr

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

 
IMPLEMENT_SERIAL(CDib, CObject, 0)

////////////////////////////////////////////////////////////////////////////
CDib::CDib()
{
	m_dwLength  = 0L;
	m_nBits		= 0;
	m_lpBuf		= NULL;
	m_pPalette  = NULL;
	m_pBitmap   = NULL;
	m_hDib		= NULL;
}           

CDib::	CDib(HANDLE hDib, BOOL bDataHandleOnly)
{                                
	if (hDib==NULL)
	{
		m_dwLength  = 0L;
		m_nBits		= 0;
		m_lpBuf		= NULL;
		m_pPalette  = NULL;
		m_pBitmap   = NULL;
		m_hDib		= NULL;
		return;
	}

	// Only use Bit-Data Handle
	if (bDataHandleOnly)
	{
		m_pPalette  = NULL;
		m_pBitmap   = NULL;
		m_dwLength	= ::GlobalSize(hDib)+sizeof(BITMAPFILEHEADER);
		
		if (! AllocMemory())
			return;
		
		memcpy((LPSTR)m_lpBMI, (LPSTR)::GlobalLock((HGLOBAL)hDib), ::GlobalSize(hDib));
		::GlobalUnlock(hDib);

		// Fill in file type (first 2 bytes must be "BM" for a bitmap) 
		m_lpBMFH->bfType = 0x4d42;  // "BM"

		// Calculating the size of the DIB is a bit tricky (if we want to
		// do it right).  The easiest way to do this is to call GlobalSize()
		// on our global handle, but since the size of our global memory may have
		// been padded a few bytes, we may end up writing out a few too
		// many bytes to the file (which may cause problems with some apps).
		//
		// So, instead let's calculate the size manually (if we can)
		//
		// First, find size of header plus size of color table.  Since the
		// first DWORD in both BITMAPINFOHEADER and BITMAPCOREHEADER conains
		// the size of the structure, let's use this.

		DWORD dwDIBSize;
		
		dwDIBSize = *(LPDWORD)m_lpBMIH + GetPaletteSize();  // Partial Calculation

		// Now calculate the size of the image

		if ((m_lpBMIH->biCompression == BI_RLE8) || 
			(m_lpBMIH->biCompression == BI_RLE4))
		{
			// It's an RLE bitmap, we can't calculate size, so trust the
			// biSizeImage field
			dwDIBSize += m_lpBMIH->biSizeImage;
		}
		else
		{
			DWORD dwBmBitsSize;  // Size of Bitmap Bits only

			// It's not RLE, so size is Width (DWORD aligned) * Height

			dwBmBitsSize = WIDTHBYTES((m_lpBMIH->biWidth)*((DWORD)m_lpBMIH->biBitCount)) * m_lpBMIH->biHeight;

			dwDIBSize += dwBmBitsSize;

			// Now, since we have calculated the correct size, why don't we
			// fill in the biSizeImage field (this will fix any .BMP files which
			// have this field incorrect).

			m_lpBMIH->biSizeImage = dwBmBitsSize;
		}

		// Calculate the file size by adding the DIB size to sizeof(BITMAPFILEHEADER)
		m_lpBMFH->bfSize = dwDIBSize + sizeof(BITMAPFILEHEADER);
		m_lpBMFH->bfReserved1 = 0;
		m_lpBMFH->bfReserved2 = 0;

		/*
		* Now, calculate the offset the actual bitmap bits will be in
		* the file -- It's the Bitmap file header plus the DIB header,
		* plus the size of the color table.
		*/
		m_lpBMFH->bfOffBits = (DWORD)sizeof(BITMAPFILEHEADER) + 
								m_lpBMIH->biSize + GetPaletteSize();
		
		//if (IS_WIN30_DIB(m_lpBMIH))
		//	ASSERT((m_lpBMIH->biBitCount==1)||(m_lpBMIH->biBitCount== 4)||
		//		   (m_lpBMIH->biBitCount==8)||(m_lpBMIH->biBitCount==24));
		//else
		//	ASSERT((m_lpBMCH->bcBitCount==1)||(m_lpBMCH->bcBitCount== 4)||
		//		   (m_lpBMCH->bcBitCount==8)||(m_lpBMCH->bcBitCount==24));
		   
		m_lpData = (LPSTR)m_lpBMFH + m_lpBMFH->bfOffBits;
		if (IS_WIN30_DIB(m_lpBMIH))
			m_nBits = m_lpBMIH->biBitCount;
		else
			m_nBits = m_lpBMCH->bcBitCount;
	
		return;
	}

	// Use DIB Handle
	m_hDib		= hDib;
	m_pPalette  = NULL;
	m_pBitmap   = NULL;
	m_dwLength	= ::GlobalSize(m_hDib);
	m_lpBuf		= (LPSTR) ::GlobalLock((HGLOBAL) m_hDib);

	m_lpBMFH = (LPBITMAPFILEHEADER)m_lpBuf;
	m_lpBMIH = (LPBITMAPINFOHEADER)((LPSTR)m_lpBuf + sizeof(BITMAPFILEHEADER));
	m_lpBMI  = (LPBITMAPINFO)m_lpBMIH;
	m_lpBMCH = (LPBITMAPCOREHEADER)m_lpBMIH;
	m_lpBMC  = (LPBITMAPCOREINFO)m_lpBMCH;

	if (IS_WIN30_DIB(m_lpBMIH))
		ASSERT((m_lpBMIH->biBitCount==1)||(m_lpBMIH->biBitCount== 4)||
			   (m_lpBMIH->biBitCount==8)||(m_lpBMIH->biBitCount==24));
	else
		ASSERT((m_lpBMCH->bcBitCount==1)||(m_lpBMCH->bcBitCount== 4)||
			   (m_lpBMCH->bcBitCount==8)||(m_lpBMCH->bcBitCount==24));
		   
	m_lpData = (LPSTR)m_lpBMFH + m_lpBMFH->bfOffBits;
	if (IS_WIN30_DIB(m_lpBMIH))
		m_nBits = m_lpBMIH->biBitCount;
	else
		m_nBits = m_lpBMCH->bcBitCount;
}

CDib::CDib(CDC*     pDC, 		// Memory DC pointer, which has
								// selected a DDB bitmap
		   int      nBt, 		// color bits/pixel (default=0, use the value in DDB)
		   BOOL     bCompr)		// compression? (default = TRUE);
{
	BITMAP	bm;
	int		nPaletteSize;
	
	m_pPalette  = NULL;
	m_pBitmap   = NULL;

	CBitmap *pEmptyBitmap = new CBitmap;
	pEmptyBitmap->CreateCompatibleBitmap(pDC,0,0);
	CBitmap *pBitmap = (CBitmap *)(pDC->SelectObject(pEmptyBitmap));
	delete pDC->SelectObject(pBitmap);
	pBitmap->GetObject(sizeof(bm), &bm);
	switch (nBt)
	{
		case 1 :
		case 4 :
		case 8 :
		case 24:	m_nBits = nBt;
					break;          
		default:	m_nBits = bm.bmPlanes * bm.bmBitsPixel;
					break;
	}
	switch (m_nBits)
	{
		case 1 :	nPaletteSize = 2;
					break;
		case 4 :	nPaletteSize = 16;
					break;
		case 8 :	nPaletteSize = 256;
					break;
		default:	nPaletteSize = 0;
					break;
	}
	
	DWORD dwBytes = ((DWORD) bm.bmWidth * m_nBits) / 32;
	if (((DWORD) bm.bmWidth * m_nBits) % 32)
		dwBytes++;
	dwBytes *= 4;
	
	m_dwLength = sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER) +
				 sizeof(RGBQUAD) * nPaletteSize;

	if (! AllocMemory())
		return;
	
	m_lpBMIH->biSize  = sizeof(BITMAPINFOHEADER);
	m_lpBMIH->biWidth = bm.bmWidth;
	m_lpBMIH->biHeight = bm.bmHeight;
	m_lpBMIH->biPlanes = 1;
	m_lpBMIH->biBitCount = m_nBits;
	
	if (bCompr && m_nBits==4)
		m_lpBMIH->biCompression = BI_RLE4;
	else if (bCompr && m_nBits==8)        
		m_lpBMIH->biCompression = BI_RLE8;
	else
		m_lpBMIH->biCompression = BI_RGB;

	m_lpBMIH->biSizeImage = 0;
    m_lpBMIH->biXPelsPerMeter = 0;
    m_lpBMIH->biYPelsPerMeter = 0;
    m_lpBMIH->biClrUsed = 0;
    m_lpBMIH->biClrImportant = 0;
    
    ::GetDIBits(pDC->GetSafeHdc(), (HBITMAP)pBitmap->GetSafeHandle(),
    			0, (WORD)bm.bmHeight, NULL, m_lpBMI, DIB_RGB_COLORS);
    if (m_lpBMIH->biSizeImage == 0)
    {
    	m_dwLength += dwBytes*bm.bmHeight;
    	m_lpBMIH->biCompression = BI_RGB;
    	TRACE("Can not do compression\n");
    }                                     
    else              
    	m_dwLength += m_lpBMIH->biSizeImage;
    	
	if (! AllocMemory(TRUE))
		return;           
	
	m_lpData = (LPSTR)m_lpBMIH + sizeof(BITMAPINFOHEADER) +
			   sizeof(RGBQUAD) * nPaletteSize;

    m_lpBMFH->bfType = 0x4d42;  //'BM'
    m_lpBMFH->bfSize = m_dwLength;
    m_lpBMFH->bfReserved1 = 0;
    m_lpBMFH->bfReserved2 = 0;
    m_lpBMFH->bfOffBits = m_lpData - m_lpBuf;
    
    if (! ::GetDIBits(pDC->GetSafeHdc(), (HBITMAP)pBitmap->GetSafeHandle(),
    				  0, (WORD)bm.bmHeight, m_lpData, m_lpBMI, DIB_RGB_COLORS))
    	m_dwLength = 0L;
}

CDib::CDib(CDC*     pDC, 		// DC pointer
		   CBitmap* pBitmap,	// DDB bitmap
		   int      nBt, 		// color bits/pixel (default=0, use the value in DDB)
		   BOOL     bCompr)		// compression? (default = TRUE);
{
	BITMAP	bm;
	int		nPaletteSize;
	
	m_pPalette  = NULL;
	m_pBitmap   = NULL;

	CDC *pMemDC = new CDC;
	pMemDC->CreateCompatibleDC(pDC);
	CBitmap* pOldBitmap = (CBitmap *)(pMemDC->SelectObject(pBitmap));
	pBitmap->GetObject(sizeof(bm), &bm);
	switch (nBt)
	{
		case 1 :
		case 4 :
		case 8 :
		case 24:	m_nBits = nBt;
					break;          
		default:	m_nBits = bm.bmPlanes * bm.bmBitsPixel;
					break;
	}
	switch (m_nBits)
	{
		case 1 :	nPaletteSize = 2;
					break;
		case 4 :	nPaletteSize = 16;
					break;
		case 8 :	nPaletteSize = 256;
					break;
		default:	nPaletteSize = 0;
					break;
	}
	
	DWORD dwBytes = ((DWORD) bm.bmWidth * m_nBits) / 32;
	if (((DWORD) bm.bmWidth * m_nBits) % 32)
		dwBytes++;
	dwBytes *= 4;
	
	m_dwLength = sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER) +
				 sizeof(RGBQUAD) * nPaletteSize;
	if (! AllocMemory())
		return;
	
	m_lpBMIH->biSize  = sizeof(BITMAPINFOHEADER);
	m_lpBMIH->biWidth = bm.bmWidth;
	m_lpBMIH->biHeight = bm.bmHeight;
	m_lpBMIH->biPlanes = 1;
	m_lpBMIH->biBitCount = m_nBits;
	
	if (bCompr && m_nBits==4)
		m_lpBMIH->biCompression = BI_RLE4;
	else if (bCompr && m_nBits==8)        
		m_lpBMIH->biCompression = BI_RLE8;
	else
		m_lpBMIH->biCompression = BI_RGB;

	m_lpBMIH->biSizeImage = 0;
    m_lpBMIH->biXPelsPerMeter = 0;
    m_lpBMIH->biYPelsPerMeter = 0;
    m_lpBMIH->biClrUsed = 0;
    m_lpBMIH->biClrImportant = 0;
    
    ::GetDIBits(pMemDC->GetSafeHdc(), (HBITMAP)pBitmap->GetSafeHandle(),
    			0, (WORD)bm.bmHeight, NULL, m_lpBMI, DIB_RGB_COLORS);
    if (m_lpBMIH->biSizeImage == 0)
    {
    	m_dwLength += dwBytes*bm.bmHeight;
    	m_lpBMIH->biCompression = BI_RGB;
    	TRACE("Can not do compression\n");
    }                                     
    else              
    	m_dwLength += m_lpBMIH->biSizeImage;
    	
	if (! AllocMemory(TRUE))
		return;           
	
	m_lpData = (LPSTR)m_lpBMIH + sizeof(BITMAPINFOHEADER) +
			   sizeof(RGBQUAD) * nPaletteSize;
    m_lpBMFH->bfType = 0x4d42;  //'BM'
    m_lpBMFH->bfSize = m_dwLength;
    m_lpBMFH->bfReserved1 = 0;
    m_lpBMFH->bfReserved2 = 0;
    m_lpBMFH->bfOffBits = m_lpData - m_lpBuf;
    
    if (! ::GetDIBits(pMemDC->GetSafeHdc(), (HBITMAP)pBitmap->GetSafeHandle(),
    				  0, (WORD)bm.bmHeight, m_lpData, m_lpBMI, DIB_RGB_COLORS))
    	m_dwLength = 0L;

	pMemDC->SelectObject(pOldBitmap);
	delete pMemDC;
}
                                      
CDib::~CDib()
{
	if (m_hDib!=NULL)
	{
		::GlobalUnlock((HGLOBAL) m_hDib);
		::GlobalFree((HGLOBAL) m_hDib);
		m_lpBuf	= NULL;
		m_hDib  = NULL;
	}
	if (m_pPalette!=NULL)
	{
		delete m_pPalette;
		m_pPalette = NULL;
	}
	if (m_pBitmap!=NULL)
	{
		delete m_pBitmap;
		m_pBitmap = NULL;
	}
}

void CDib::Serialize(CArchive& ar)
{
	CObject::Serialize(ar);
	ar.Flush();
	if (ar.IsStoring())
	{
		WriteDib(ar.GetFile());
	}
    else
    {
   		ReadDib(ar.GetFile());
    }
}

BOOL CDib::LoadDib(UINT uIDS, LPCSTR lpszDibType)
{                                
	ASSERT(m_dwLength == 0L);

	LPCSTR lpszDibRes = MAKEINTRESOURCE(uIDS);
	HINSTANCE hInst = AfxGetInstanceHandle();
	HRSRC   hRes    = ::FindResource(hInst, lpszDibRes, lpszDibType);
	HGLOBAL hData   = ::LoadResource(hInst, hRes);

	if (hRes != NULL && hData != NULL)
	{
		char *lpBuf = (char *)::LockResource(hData);
		m_dwLength = ((LPBITMAPFILEHEADER)lpBuf)->bfSize;
		if (! AllocMemory())
			return FALSE;
		
		for (register DWORD i=0;i<m_dwLength;++i)
			*(m_lpBuf+i) = *(lpBuf+i);
	}
	else
    {
		CString str = lpszDibRes;
		str += " : Read Resource error!";
		AfxMessageBox(str);
		return FALSE;
	}

	if (m_lpBMFH->bfType != 0x4d42)
	{
		CString str = lpszDibRes;
		str += " : Invalid bitmap file!";
		AfxMessageBox(str);
		return FALSE;
	}                
	
	if (IS_WIN30_DIB(m_lpBMIH))
		ASSERT((m_lpBMIH->biBitCount==1)||(m_lpBMIH->biBitCount== 4)||
			   (m_lpBMIH->biBitCount==8)||(m_lpBMIH->biBitCount==24));
	else
		ASSERT((m_lpBMCH->bcBitCount==1)||(m_lpBMCH->bcBitCount== 4)||
			   (m_lpBMCH->bcBitCount==8)||(m_lpBMCH->bcBitCount==24));
		   
	m_lpData = (LPSTR)m_lpBMFH + m_lpBMFH->bfOffBits;
	m_nBits = m_lpBMIH->biBitCount;
	
	return TRUE;
}

BOOL CDib::LoadDib(LPCSTR lpszDibRes, LPCSTR lpszDibType)
{                                
	ASSERT(m_dwLength == 0L);

	HINSTANCE hInst = AfxGetInstanceHandle();
	HRSRC   hRes    = ::FindResource(hInst, lpszDibRes, lpszDibType);
	HGLOBAL hData   = ::LoadResource(hInst, hRes);

	if (hRes != NULL && hData != NULL)
	{
		char *lpBuf = (char *)::LockResource(hData);
		m_dwLength = ((LPBITMAPFILEHEADER)lpBuf)->bfSize;
		if (! AllocMemory())
			return FALSE;
		
		for (register DWORD i=0;i<m_dwLength;++i)
			*(m_lpBuf+i) = *(lpBuf+i);
	}
	else
    {
		CString str = lpszDibRes;
		str += " : Read Resource error!";
		AfxMessageBox(str);
		return FALSE;
	}

	if (m_lpBMFH->bfType != 0x4d42)
	{
		CString str = lpszDibRes;
		str += " : Invalid bitmap file!";
		AfxMessageBox(str);
		return FALSE;
	}                
	
	if (IS_WIN30_DIB(m_lpBMIH))
		ASSERT((m_lpBMIH->biBitCount==1)||(m_lpBMIH->biBitCount== 4)||
			   (m_lpBMIH->biBitCount==8)||(m_lpBMIH->biBitCount==24));
	else
		ASSERT((m_lpBMCH->bcBitCount==1)||(m_lpBMCH->bcBitCount== 4)||
			   (m_lpBMCH->bcBitCount==8)||(m_lpBMCH->bcBitCount==24));
		   
	m_lpData = (LPSTR)m_lpBMFH + m_lpBMFH->bfOffBits;
	m_nBits = m_lpBMIH->biBitCount;
	
	return TRUE;
}


BOOL CDib::LoadDib(LPCSTR lpszDibFile)
{                                
	ASSERT(m_dwLength == 0L);
	TRY
	{
		CFile file(lpszDibFile, CFile::modeRead|CFile::shareDenyNone);

		if (! ReadDib(&file))
		{
			CString str = lpszDibFile;
			str += " : Read file error!";
			AfxMessageBox(str);
			return FALSE;
		}
	}
	CATCH (CException, e)
	{
		CString str = lpszDibFile;
		str += " : Read file error!";
		AfxMessageBox(str);
		return FALSE;
	}
	END_CATCH

	return TRUE;
}               

BOOL CDib::SaveDib(LPCSTR lpszDibFile)
{
	TRY
	{
		CFile file(lpszDibFile, CFile::modeCreate|CFile::modeWrite);

		if (! WriteDib(&file))
		{
			CString str = lpszDibFile;
			str += " : Write file error!";
			AfxMessageBox(str);
			return FALSE;
		}
	}
	CATCH (CException, e)
	{
		CString str = lpszDibFile;
		str += " : Write file error!";
		AfxMessageBox(str);
		return FALSE;
	}
	END_CATCH

	return TRUE;
}

BOOL CDib::ReadDib(CFile *pFile)
{
	ASSERT(m_dwLength == 0L);
	TRY
	{
		m_dwLength = pFile->GetLength();
		if (! AllocMemory())
			return FALSE;
	
		DWORD dwCount = pFile->ReadHuge(m_lpBuf, m_dwLength);
		if (dwCount != m_dwLength)
			return FALSE;
	}
	CATCH (CException, e)
	{
		return FALSE;
	}
	END_CATCH
	
	if (m_lpBMFH->bfType != 0x4d42)
		return FALSE;
	
	if (IS_WIN30_DIB(m_lpBMIH))
		ASSERT((m_lpBMIH->biBitCount==1)||(m_lpBMIH->biBitCount== 4)||
			   (m_lpBMIH->biBitCount==8)||(m_lpBMIH->biBitCount==24));
	else
		ASSERT((m_lpBMCH->bcBitCount==1)||(m_lpBMCH->bcBitCount== 4)||
			   (m_lpBMCH->bcBitCount==8)||(m_lpBMCH->bcBitCount==24));
		   
	m_lpData = (LPSTR)m_lpBMFH + m_lpBMFH->bfOffBits;
	m_nBits = m_lpBMIH->biBitCount;

	return TRUE;
}

BOOL CDib::WriteDib(CFile *pFile)
{
	TRY
	{
		pFile->WriteHuge(m_lpBuf, m_dwLength);
	}
	CATCH (CException, e)
	{
		return FALSE;
	}
	END_CATCH

	return TRUE;
}

BOOL CDib::Display(CDC * pDC, CPoint ptDest, DWORD dwRop)
{
	BOOL bSuccess = FALSE;

	if (m_lpBuf == NULL)
		return bSuccess;
	
	CPalette* pOldPal = pDC->SelectPalette(GetPalette(), TRUE);
    pDC->RealizePalette();
	pDC->SetStretchBltMode(COLORONCOLOR);
	
	bSuccess = ::StretchDIBits(pDC->GetSafeHdc(), 
								ptDest.x, 
								ptDest.y,
						  	   (int)m_lpBMIH->biWidth, 
							   (int)m_lpBMIH->biHeight, 
								0,
								0,
						  	   (int)m_lpBMIH->biWidth, 
							   (int)m_lpBMIH->biHeight, 
								m_lpData, 
								m_lpBMI, 
								DIB_RGB_COLORS, 
								dwRop);
	
	pDC->SelectPalette(pOldPal, FALSE);	//TRUE);

	return bSuccess;
}

BOOL CDib::Display(CDC* pDC, CRect rcDest, CRect rcSrc, DWORD dwRop)
{
	BOOL bSuccess = FALSE;

	if (m_lpBuf == NULL)
		return bSuccess;
		
	CPalette* pOldPal = pDC->SelectPalette(GetPalette(), TRUE);
		
    pDC->RealizePalette();
	pDC->SetStretchBltMode(COLORONCOLOR);

    bSuccess = ::StretchDIBits(pDC->GetSafeHdc(), 
								rcDest.left, 
								rcDest.top,
							  	rcDest.Width(), 
								rcDest.Height(), 
								rcSrc.left,
								(int)m_lpBMIH->biHeight-rcSrc.top-rcSrc.Height(),   // SrcY --rcSrc.top,
							  	rcSrc.Width(), 
								rcSrc.Height(), 
								m_lpData, 
								m_lpBMI, 
								DIB_RGB_COLORS, 
								dwRop);
   
    pDC->SelectPalette(pOldPal, FALSE);	//TRUE);

	return bSuccess;
}


CPalette *  CDib::GetPalette()
{
	if (m_pPalette == NULL)
		MakePalette();
	return m_pPalette;
}

int CDib::GetColorBits()
{
	return m_nBits;
}

BOOL CDib::IsEmpty()
{
	if (m_dwLength == 0L || m_hDib == NULL)
		return TRUE;
	else
		return FALSE;
}

DWORD CDib::GetLength()
{
	return m_dwLength;
}

WORD CDib::GetWidth()
{
	if (IsEmpty())
		return 0;
	if (IS_WIN30_DIB(m_lpBMIH))
		return (WORD)(m_lpBMIH->biWidth);
	else
		return (WORD)(m_lpBMCH->bcWidth);
}

WORD CDib::GetHeight()
{
	if (IsEmpty())
		return 0;
	if (IS_WIN30_DIB(m_lpBMIH))
		return (WORD)(m_lpBMIH->biHeight);
	else
		return (WORD)(m_lpBMCH->bcHeight);
}

HDIB CDib::GetHandle()
{
	return m_hDib;
}

HDIB CDib::CopyHandle()
{
	if (IsEmpty())
		return NULL;

	HGLOBAL hCopy = ::GlobalAlloc(GHND, m_dwLength);
	if (hCopy == NULL)
		return NULL;

	void* lpCopy = ::GlobalLock((HGLOBAL) hCopy);
	memcpy(lpCopy, m_lpBMI, m_dwLength);
	::GlobalUnlock(hCopy);

	return hCopy;
}

HANDLE CDib::CopyDataHandle()
{
	if (IsEmpty())
		return NULL;

	DWORD   dwDataLength = m_dwLength-sizeof(BITMAPFILEHEADER);
	HGLOBAL hCopy = ::GlobalAlloc(GHND, dwDataLength);
	if (hCopy == NULL)
		return NULL;

	void* lpCopy = ::GlobalLock((HGLOBAL) hCopy);
	memcpy(lpCopy, m_lpBMI, dwDataLength);
	::GlobalUnlock(hCopy);

	return hCopy;
}

WORD CDib::GetNumColors()
{
	WORD wBitCount;  // DIB bit count

	/*  If this is a Windows-style DIB, the number of colors in the
	 *  color table can be less than the number of bits per pixel
	 *  allows for (i.e. lpbi->biClrUsed can be set to some value).
	 *  If this is the case, return the appropriate value.
	 */

	if (IS_WIN30_DIB(m_lpBMIH))
	{
		DWORD dwClrUsed;

		dwClrUsed = ((LPBITMAPINFOHEADER)m_lpBMIH)->biClrUsed;
		if (dwClrUsed != 0)
			return (WORD)dwClrUsed;
	}

	/*  Calculate the number of colors in the color table based on
	 *  the number of bits per pixel for the DIB.
	 */
	if (IS_WIN30_DIB(m_lpBMIH))
		wBitCount = ((LPBITMAPINFOHEADER)m_lpBMIH)->biBitCount;
	else
		wBitCount = ((LPBITMAPCOREHEADER)m_lpBMIH)->bcBitCount;

	/* return number of colors based on bits per pixel 
	 * This function calculates the number of colors in the DIB's color table
	 * by finding the bits per pixel for the DIB (whether Win3.0 or other-style
	 * DIB). If bits per pixel is 1: colors=2, if 4: colors=16, if 8: colors=256,
	 * if 24, no colors in color table.
	 */
	switch (wBitCount)
	{
		case 1:
			return 2;

		case 4:
			return 16;

		case 8:
			return 256;

		default:
			return 0;
	}
}

WORD CDib::GetPaletteSize()
{
   // calculate the size required by the palette
   if (IS_WIN30_DIB (m_lpBMIH))
	  return (WORD)(GetNumColors() * sizeof(RGBQUAD));
   else
	  return (WORD)(GetNumColors() * sizeof(RGBTRIPLE));
}
                           
BOOL CDib::AllocMemory(BOOL bRealloc /* default = FALSE */)
{
	if (bRealloc)
		m_hDib = ::GlobalReAlloc(m_hDib, m_dwLength, GMEM_MOVEABLE | GMEM_ZEROINIT);
		//m_lpBuf = (char *)GlobalReAllocPtr(m_lpBuf, m_dwLength, GHND);
	else
		m_hDib = ::GlobalAlloc(GMEM_MOVEABLE | GMEM_ZEROINIT, m_dwLength);
		//m_lpBuf = (char *)GlobalAllocPtr(GHND, m_dwLength);
		
	if (m_hDib == NULL)
	{
		return FALSE;
	}
	m_lpBuf = (LPSTR) ::GlobalLock((HGLOBAL) m_hDib);

	if (m_lpBuf == NULL)
	{
		AfxMessageBox("Allocate DIB memory error!");
		m_dwLength = 0L;
		m_nBits = 0;
		m_hDib  = NULL;
		return FALSE;
	}
	
	m_lpBMFH = (LPBITMAPFILEHEADER)m_lpBuf;
	m_lpBMIH = (LPBITMAPINFOHEADER)((LPSTR)m_lpBuf + sizeof(BITMAPFILEHEADER));
	m_lpBMI  = (LPBITMAPINFO)m_lpBMIH;
	m_lpBMCH = (LPBITMAPCOREHEADER)m_lpBMIH;
	m_lpBMC  = (LPBITMAPCOREINFO)m_lpBMIH;
	
	return TRUE;
}

BOOL CDib::MakePalette()
{
	LPLOGPALETTE lpPal;      // pointer to a logical palette
	HANDLE hLogPal;          // handle to a logical palette
	HPALETTE hPal = NULL;    // handle to a palette
	WORD wNumColors;         // number of colors in color table

	if (m_pPalette != NULL)
	  return TRUE;

	if (m_lpBuf == NULL)
	  return FALSE;
	
    wNumColors = GetNumColors();

    if (wNumColors == 0)
    	return FALSE;
    	
	hLogPal = ::GlobalAlloc(GHND, sizeof(LOGPALETTE) +
					 			  sizeof(PALETTEENTRY) * wNumColors);
	if (hLogPal == 0)
		return FALSE;

	lpPal = (LPLOGPALETTE) ::GlobalLock((HGLOBAL) hLogPal);

	/* set version and number of palette entries */
	lpPal->palVersion = 0x300;
	lpPal->palNumEntries = (WORD)wNumColors;

	/* is this a Win 3.0 DIB? */
	BOOL bWinStyleDIB = IS_WIN30_DIB(m_lpBMIH);
	for (int i=0; i<(int)wNumColors; i++)
	{
		if (bWinStyleDIB)
		{
			lpPal->palPalEntry[i].peRed   = m_lpBMI->bmiColors[i].rgbRed;
			lpPal->palPalEntry[i].peGreen = m_lpBMI->bmiColors[i].rgbGreen;
			lpPal->palPalEntry[i].peBlue  = m_lpBMI->bmiColors[i].rgbBlue;
			lpPal->palPalEntry[i].peFlags = 0;
		}
		else
		{
			lpPal->palPalEntry[i].peRed   = m_lpBMC->bmciColors[i].rgbtRed;
			lpPal->palPalEntry[i].peGreen = m_lpBMC->bmciColors[i].rgbtGreen;
			lpPal->palPalEntry[i].peBlue  = m_lpBMC->bmciColors[i].rgbtBlue;
			lpPal->palPalEntry[i].peFlags = 0;
		}
	}

	m_pPalette = new CPalette;
	BOOL bResult = m_pPalette->CreatePalette(lpPal);
	::GlobalUnlock((HGLOBAL) hLogPal);
	::GlobalFree((HGLOBAL) hLogPal);
	
	return bResult;
}

CBitmap * CDib::MakeBitmap(CDC * pDC)
{	
	if (m_pBitmap != NULL)
		return pDC->SelectObject(m_pBitmap); // return Original-bitmap

	if (IsEmpty())
		return NULL;

	CPalette *pOldPal = pDC->SelectPalette(GetPalette(), TRUE);
	pDC->RealizePalette();

	int nPlanes	   = pDC->GetDeviceCaps(PLANES);
	int nBitsPixel = pDC->GetDeviceCaps(BITSPIXEL);
	CBitmap* pConfigBitmap = new CBitmap;
	char bits[100];
	if (m_lpBMIH->biBitCount == 1)
		pConfigBitmap->CreateBitmap(1, 1, 1, 1, bits);
	else
		pConfigBitmap->CreateBitmap(1, 1, nPlanes, nBitsPixel, bits);

	CBitmap* pOriginalBitmap = (CBitmap *)pDC->SelectObject(pConfigBitmap);
	
	DWORD	dwFore, dwBack;
	if (GetMonoColors(dwFore, dwBack))
		SetMonoColors(0L, 0xFFFFFFL);

	HBITMAP hBitmap = ::CreateDIBitmap(pDC->GetSafeHdc(), 
									   m_lpBMIH, CBM_INIT, 
									   (CONST BYTE *)(m_lpBuf+m_lpBMFH->bfOffBits),
									   m_lpBMI, DIB_RGB_COLORS);

	if (hBitmap == NULL)
	{
		TRACE("null bitmap!\n");
		return NULL;
	}

	SetMonoColors(dwFore, dwBack);
	
	m_pBitmap = new CBitmap;
	m_pBitmap->Attach(hBitmap);

	pDC->SelectObject(m_pBitmap);    // delete config-bitmap
	delete pConfigBitmap;
	
	pDC->SelectPalette(pOldPal, FALSE);
	return pOriginalBitmap;					// return original-bitmap
}

void CDib::SetMonoColors(DWORD dwForegrpund, DWORD dwBackGround)
{
	if (m_nBits != 1)
		return;
	
	unsigned long far* pPalette = (unsigned long far*)
								  ((LPSTR)m_lpBMIH+sizeof(BITMAPINFOHEADER));
	*pPalette = dwForegrpund;
	*(++pPalette) = dwBackGround;
	
	return;
}

BOOL CDib::GetMonoColors(DWORD& dwForegrpund, DWORD& dwBackGround)
{
	if (m_nBits != 1)
		return FALSE;
	
	unsigned long far* pPalette = (unsigned long far*)
								  ((LPSTR)m_lpBMIH+sizeof(BITMAPINFOHEADER));
	dwForegrpund = *pPalette;
	dwBackGround = *(++pPalette);
	
	return TRUE;
}

HANDLE CopyHandle (HANDLE h)
{
	if (h == NULL)
		return NULL;

	DWORD dwLen = ::GlobalSize((HGLOBAL) h);
	HGLOBAL hCopy = ::GlobalAlloc(GHND, dwLen);

	if (hCopy != NULL)
	{
		void* lpCopy = ::GlobalLock((HGLOBAL) hCopy);
		void* lp     = ::GlobalLock((HGLOBAL) h);
		memcpy(lpCopy, lp, dwLen);
		::GlobalUnlock(hCopy);
		::GlobalUnlock(h);
	}

	return hCopy;
}

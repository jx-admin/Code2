#if !defined(AFX_DIB_H__33817561_072D_11D1_8C94_000021003EA5__INCLUDED_)
#define AFX_DIB_H__33817561_072D_11D1_8C94_000021003EA5__INCLUDED_

#if _MSC_VER >= 1000
#pragma once
#endif // _MSC_VER >= 1000
// Dib.h : header file
//

#define IS_WIN30_DIB(lpbi)  ((*(LPDWORD)(lpbi)) == sizeof(BITMAPINFOHEADER))
#define WIDTHBYTES(bits)    (((bits) + 31) / 32 * 4)

typedef HANDLE	HDIB;
HANDLE CopyHandle (HANDLE h);

////////////////////////////////////////////////////////////////////////////
// CDib
class CDib : public CObject
{                          
	DECLARE_SERIAL(CDib)

// Public member
public: 
	// Constructor
	CDib();
	CDib(CDC * pDC,			 // Memory DC
		 int nbits=0,		 // use default bits/pixel 
		 BOOL bCompr=TRUE);
	CDib(CDC * pDC,			 // DC
		 CBitmap *pBitmap,	 // Bitmap pointer
		 int nbits=0,		 // use default bits/pixel 
		 BOOL bCompr=TRUE);
	CDib(HDIB hDib,			 // DIB Handle
		 BOOL bDataHandleOnly=TRUE);
	virtual ~CDib();
	
	virtual void Serialize(CArchive &ar);
    
	// Operations
    BOOL	LoadDib(UINT uIDS, LPCSTR lpszDibType = "DIB");
    BOOL	LoadDib(LPCSTR lpszDibRes, LPCSTR lpszDibType = "DIB");
    BOOL	LoadDib(LPCSTR lpszDibFile);
    BOOL	SaveDib(LPCSTR lpszDibFile);

    BOOL	Display(CDC *, CPoint ptDest, DWORD dwRop=SRCCOPY);
	BOOL	Display(CDC *, CRect rcDest, CRect rcSrc,DWORD dwRop=SRCCOPY);

	BOOL	IsEmpty();
    int		GetColorBits();
    DWORD	GetLength();
    WORD	GetWidth();
    WORD	GetHeight();    
	HDIB	GetHandle();
	HDIB	CopyHandle();
	HANDLE	CopyDataHandle();
    WORD	GetNumColors();
	WORD	GetPaletteSize();
	CPalette* GetPalette();
    CBitmap*  MakeBitmap(CDC * pDC);
    void	SetMonoColors(DWORD dwForeground, DWORD dwBackground);
	BOOL	GetMonoColors(DWORD& dwForeground, DWORD& dwBackground);

private:
	BOOL	AllocMemory(BOOL bRealloc=FALSE);
	BOOL	MakePalette();
	BOOL	ReadDib(CFile *pFile);
	BOOL	WriteDib(CFile *pFile);

// Data
private:
	HDIB				m_hDib;
	LPBITMAPFILEHEADER	m_lpBMFH;
	LPBITMAPINFOHEADER	m_lpBMIH;	// pointer to an Win30-style DIB
	LPBITMAPCOREHEADER	m_lpBMCH;	// pointer to an other-style DIB
	LPBITMAPINFO		m_lpBMI;		// pointer to an Win30-style DIB
	LPBITMAPCOREINFO	m_lpBMC;		// pointer to an other-style DIB

	LPSTR		m_lpData;
	CPalette * 	m_pPalette;
	CBitmap	 *	m_pBitmap;

	char *		m_lpBuf; 	 // DIB data buffer
	DWORD		m_dwLength;  // total buffer length, including header
	int			m_nBits;	 // number of color bits per pixel
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Developer Studio will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_DIB_H__33817561_072D_11D1_8C94_000021003EA5__INCLUDED_)

// ChessDlg.h : header file
//
#include "CoolButton.h"
#include "BaseClasses.h"
#include "StepList.h"
#include "Thinker.h"

#if !defined(AFX_CHESSDLG_H__AA41CA9E_A877_11D4_9A6C_973E5A1E3F59__INCLUDED_)
#define AFX_CHESSDLG_H__AA41CA9E_A877_11D4_9A6C_973E5A1E3F59__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

/////////////////////////////////////////////////////////////////////////////
// CChessDlg dialog

class CChessDlg : public CDialog
{
// Construction
public:
	void TestCheck();
	CSetting m_Setting;
	BOOL OpenWeb();
	BOOL OpenEmail(void);
	BOOL OpenURL(const char * LinkName);
	CChessDlg(CWnd* pParent = NULL);	// standard constructor
	~CChessDlg();

// Dialog Data
	//{{AFX_DATA(CChessDlg)
	enum { IDD = IDD_CHESS_DIALOG };
	CCoolButton	m_btCut;
	CProgressCtrl	m_progressStep;
	CListBox	m_listStep;
	CCoolButton	m_btUndo;
	CCoolButton	m_btRedo;
	CCoolButton	m_btHelp;
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CChessDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:	
	BOOL m_bCheck[2];
	void New(void);			//新棋局:
	void New(CFace face);	//新棋局
	void Think();			//让电脑计算并走一步
	void ShowStatus();		//更新标题栏(显示当前状态)
	void Move(int man,int x,int y);	//把棋子移动到目标点放下		
	void MB_DrawStar(int x,int y,int i);	//画星点(MakeBoard 中调用)
	void MakeBoard();		//画棋盘(*m_pdcBoard)
	void FixManMap();		//每次更新m_Face时调用,更新m_nManMap
	void UpdateFace(CRect& rc);	//刷新棋盘指定区域
	void PickUp(int man);		//拿起棋子
	void PutDown(int x,int y);	//放下棋子
	void EnableCommand(int n,BOOL benable=TRUE);//允许或禁止某个操作
	BOOL FaceToPoint(CPoint& point);	//坐标转换城棋位坐标
	BOOL CanGo(int man,int x,int y);	//判断能不能走某步棋
	BOOL CanGo(const CMove& move );		//判断能不能走某步棋
	BOOL CanGo();						//判断当前能不能走
	BOOL Go(int man,int x,int y);		//走某步棋
	BOOL Go(CMove move);				//走某步棋
	CRect MakeUpdateRect(CPoint p1,CPoint p2);	//通过两点鼠标坐标得到更新区域
	CString GetStepName(int man,int x,int y);	//取得走法名

	CStepList m_StepList;		//走法栈
	CPoint m_pointBeforePickUp;	//拿起的棋子的原来的棋位
	CPoint m_pointPickUp;		//拿起的棋子的屏幕位置
	int m_nTimer;				//定时器
	int m_nManPickUp;			//拿起的棋子序号
	int m_nManMap[11][12];		//纪录棋盘各棋位的状态(有无棋子/棋子序号)
	int m_nStepCount;			//当前步数
	HICON m_hIcon;				//程序图标
	HICON m_hIconUndo;			//左箭头
	HICON m_hIconRedo;			//右箭头
	HICON m_hIconHelp;			//快进箭头
	HICON m_hIconCut;			//取消
	HICON m_hIconMan[14];		//棋子图标
	CBitmap m_Bitmap1;			//给 m_pdcBoard 做的位图
	CBitmap m_Bitmap2;			//给 m_pdcBack 做的位图
	BOOL m_bThinking;			//电脑是否正在计算
	CFace m_Face;				//棋局数据
	CThinker m_Thinker;			//***
	CDC* m_pdcBoard;			//储存棋盘图案的 DC
	CDC* m_pdcBack;				//缓冲 DC

	// Generated message map functions
	//{{AFX_MSG(CChessDlg)
	virtual BOOL OnInitDialog();
	afx_msg void OnAboutbox();
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnDestroy();
	afx_msg void OnFileBai();
	afx_msg void OnFileNew();
	afx_msg void OnFileSave();
	afx_msg void OnFileOpen();
	afx_msg void OnFileOption();
	afx_msg void OnEditUndo();
	afx_msg void OnEditRedo();
	afx_msg void OnEditHelp();
	afx_msg void OnEditCut();
	afx_msg void OnTimer(UINT nIDEvent);
	afx_msg void OnDblclkSteplist();
	afx_msg void OnHelpEmail();
	afx_msg void OnHelpWeb();
	afx_msg void OnFileThinkOption();
	afx_msg void OnHelpUse();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
	friend class CBaiDlg;
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_CHESSDLG_H__AA41CA9E_A877_11D4_9A6C_973E5A1E3F59__INCLUDED_)

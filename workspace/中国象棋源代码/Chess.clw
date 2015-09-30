; CLW file contains information for the MFC ClassWizard

[General Info]
Version=1
LastClass=CChessDlg
LastTemplate=CDialog
NewFileInclude1=#include "stdafx.h"
NewFileInclude2=#include "chess.h"
LastPage=0

ClassCount=12
Class1=CBaiDlg
Class2=CChessApp
Class3=CAboutDlg
Class4=CChessDlg
Class5=CCoolButton
Class6=CHLinkCtrl
Class7=CHLinkPage
Class8=CHLinkSheet
Class9=COptionDlg
Class10=CReportDlg

ResourceCount=9
Resource1=IDD_OPTION
Resource2=IDD_THINKOPTION
Resource3=IDR_MENUBAI
Resource4=IDD_OPEN
Resource5=IDD_ABOUTBOX
Resource6=IDR_MAINFRAME
Resource7=IDD_CHESS_DIALOG
Class11=CThinkOptionDlg
Resource8=IDD_BAI
Class12=COpenDlg
Resource9=IDR_MENUBAIMAIN

[CLS:CBaiDlg]
Type=0
BaseClass=CDialog
HeaderFile=BaiDlg.h
ImplementationFile=BaiDlg.cpp

[CLS:CChessApp]
Type=0
BaseClass=CWinApp
HeaderFile=Chess.h
ImplementationFile=Chess.cpp

[CLS:CAboutDlg]
Type=0
BaseClass=CDialog
HeaderFile=ChessDlg.cpp
ImplementationFile=ChessDlg.cpp
LastObject=CAboutDlg

[CLS:CChessDlg]
Type=0
BaseClass=CDialog
HeaderFile=ChessDlg.h
ImplementationFile=ChessDlg.cpp
Filter=D
VirtualFilter=dWC
LastObject=ID_HELP_USE

[CLS:CCoolButton]
Type=0
BaseClass=CButton
HeaderFile=CoolButton.h
ImplementationFile=CoolButton.cpp

[CLS:CHLinkCtrl]
Type=0
BaseClass=CStatic
HeaderFile=hlinkctl.h
ImplementationFile=hlinkctl.cpp

[CLS:CHLinkPage]
Type=0
BaseClass=CPropertyPage
HeaderFile=hlinkctl.h
ImplementationFile=hlinkctl.cpp

[CLS:CHLinkSheet]
Type=0
BaseClass=CPropertySheet
HeaderFile=hlinkctl.h
ImplementationFile=hlinkctl.cpp

[CLS:COptionDlg]
Type=0
BaseClass=CDialog
HeaderFile=OptionDlg.h
ImplementationFile=OptionDlg.cpp
LastObject=COptionDlg

[CLS:CReportDlg]
Type=0
BaseClass=CDialog
HeaderFile=ReportDlg.h
ImplementationFile=ReportDlg.cpp

[DLG:IDD_BAI]
Type=1
Class=CBaiDlg
ControlCount=9
Control1=IDC_BAIOK,button,1342177291
Control2=IDCANCEL,button,1342177291
Control3=IDC_RADIO_GOSIDE_RED,button,1342373897
Control4=IDC_RADIO_GOSIDE_BLACK,button,1342177289
Control5=IDC_STATIC,button,1342308359
Control6=IDC_BT_NORMAL,button,1342177291
Control7=IDC_BT_CLEAR,button,1342177291
Control8=IDC_BT_OPEN,button,1342242827
Control9=IDC_BT_SAVE,button,1342242827

[DLG:IDD_ABOUTBOX]
Type=1
Class=CAboutDlg
ControlCount=6
Control1=IDC_STATIC,static,1342177283
Control2=IDC_STATIC,static,1342308480
Control3=IDC_STATIC,static,1342308352
Control4=IDOK,button,1342373899
Control5=IDC_BUTTONWEB,button,1342242827
Control6=IDC_BUTTONEMAIL,button,1342242827

[DLG:IDD_CHESS_DIALOG]
Type=1
Class=CChessDlg
ControlCount=6
Control1=IDC_CHESS_UNDO,button,1342177291
Control2=IDC_CHESS_HELP,button,1342177291
Control3=IDC_CHESS_REDO,button,1342177291
Control4=IDC_STEPLIST,listbox,1352667393
Control5=IDC_PROGRESS1,msctls_progress32,1350565889
Control6=IDC_CHESS_CUT,button,1342177291

[DLG:IDD_HLINK_PROPERTIES]
Type=1
Class=CHLinkPage

[DLG:IDD_OPTION]
Type=1
Class=COptionDlg
ControlCount=16
Control1=IDOK,button,1342177291
Control2=IDCANCEL,button,1342177291
Control3=IDC_STATIC1,button,1342308359
Control4=IDC_RADIOMAN1,button,1342373897
Control5=IDC_RADIOCOM1,button,1342177289
Control6=IDC_STATIC2,button,1342308359
Control7=IDC_RADIOMAN2,button,1342373897
Control8=IDC_RADIOCOM2,button,1342177289
Control9=IDC_STATIC,button,1342308359
Control10=IDC_RADIOMODE1,button,1342373897
Control11=IDC_RADIOMODE2,button,1342177289
Control12=IDC_RADIOMODE3,button,1342177289
Control13=IDC_STATIC,static,1342308352
Control14=IDC_EDITLEVEL1,edit,1350641792
Control15=IDC_SPIN1,msctls_updown32,1342177334
Control16=IDC_RESET,button,1342177291

[DLG:IDD_REPORT]
Type=1
Class=CReportDlg

[MNU:IDR_MAINFRAME]
Type=1
Class=CChessDlg
Command1=ID_FILE_NEW
Command2=ID_FILE_OPEN
Command3=ID_FILE_SAVE
Command4=ID_FILE_BAI
Command5=ID_FILE_OPTION
Command6=ID_FILE_THINKOPTION
Command7=IDOK
Command8=ID_CHESS_UNDO
Command9=ID_CHESS_REDO
Command10=ID_CHESS_HELP
Command11=ID_CHESS_CUT
Command12=ID_HELP_USE
Command13=ID_HELP_EMAIL
Command14=ID_HELP_WEB
Command15=ID_ABOUTBOX
CommandCount=15

[MNU:IDR_MENUBAI]
Type=1
Class=?
Command1=ID_RED_B
Command2=ID_RED_P
Command3=ID_RED_J
Command4=ID_RED_M
Command5=ID_RED_X
Command6=ID_RED_S
Command7=ID_RED_K
Command8=ID_BLACK_B
Command9=ID_BLACK_P
Command10=ID_BLACK_J
Command11=ID_BLACK_M
Command12=ID_BLACK_X
Command13=ID_BLACK_S
Command14=ID_BLACK_K
Command15=ID_BAI_DELETE
Command16=ID_BAI_CANCEL
CommandCount=16

[MNU:IDR_MENUBAIMAIN]
Type=1
Class=?
Command1=IDC_BT_CLEAR
Command2=IDC_BT_NORMAL
Command3=IDC_BT_OPEN
Command4=IDC_BT_SAVE
Command5=IDC_BAIOK
Command6=IDCANCEL
CommandCount=6

[DLG:IDD_THINKOPTION]
Type=1
Class=CThinkOptionDlg
ControlCount=46
Control1=ID_MYOK,button,1342242827
Control2=IDCANCEL,button,1342242827
Control3=IDC_EDITS1,edit,1350639616
Control4=IDC_SPIN1,msctls_updown32,1342177302
Control5=IDC_STATIC,button,1342177287
Control6=IDC_EDITX1,edit,1350639616
Control7=IDC_SPIN2,msctls_updown32,1342177302
Control8=IDC_EDITM1,edit,1350639616
Control9=IDC_SPIN3,msctls_updown32,1342177302
Control10=IDC_EDITJ1,edit,1350639616
Control11=IDC_SPIN4,msctls_updown32,1342177302
Control12=IDC_EDITP1,edit,1350639616
Control13=IDC_SPIN5,msctls_updown32,1342177334
Control14=IDC_EDITB1,edit,1350639616
Control15=IDC_SPIN6,msctls_updown32,1342177302
Control16=IDC_STATIC,static,1342308352
Control17=IDC_STATIC,static,1342308352
Control18=IDC_STATIC,static,1342308352
Control19=IDC_STATIC,static,1342308352
Control20=IDC_STATIC,static,1342308352
Control21=IDC_STATIC,static,1342308352
Control22=IDC_EDITS2,edit,1350639616
Control23=IDC_SPIN7,msctls_updown32,1342177302
Control24=IDC_STATIC,button,1342177287
Control25=IDC_EDITX2,edit,1350639616
Control26=IDC_SPIN8,msctls_updown32,1342177302
Control27=IDC_EDITM2,edit,1350639616
Control28=IDC_SPIN9,msctls_updown32,1342177302
Control29=IDC_EDITJ2,edit,1350639616
Control30=IDC_SPIN10,msctls_updown32,1342177302
Control31=IDC_EDITP2,edit,1350639616
Control32=IDC_SPIN11,msctls_updown32,1342177302
Control33=IDC_EDITB2,edit,1350639616
Control34=IDC_SPIN12,msctls_updown32,1342177302
Control35=IDC_EDIT30,edit,1350639616
Control36=IDC_SPIN13,msctls_updown32,1342177302
Control37=IDC_STATIC,button,1342177287
Control38=IDC_EDIT31,edit,1350639616
Control39=IDC_SPIN14,msctls_updown32,1342177302
Control40=IDC_EDIT32,edit,1350639616
Control41=IDC_SPIN15,msctls_updown32,1342177302
Control42=IDC_EDIT33,edit,1350639616
Control43=IDC_SPIN16,msctls_updown32,1342177302
Control44=IDC_EDIT34,edit,1350639616
Control45=IDC_SPIN17,msctls_updown32,1342177302
Control46=IDC_STATIC,static,1342308352

[CLS:CThinkOptionDlg]
Type=0
HeaderFile=ThinkOptionDlg.h
ImplementationFile=ThinkOptionDlg.cpp
BaseClass=CDialog
Filter=D
VirtualFilter=dWC
LastObject=CThinkOptionDlg

[DLG:IDD_OPEN]
Type=1
Class=COpenDlg
ControlCount=4
Control1=IDOK,button,1342242827
Control2=IDCANCEL,button,1342242827
Control3=IDC_DIR,button,1342242827
Control4=IDC_FILELIST,listbox,1352728835

[CLS:COpenDlg]
Type=0
HeaderFile=OpenDlg.h
ImplementationFile=OpenDlg.cpp
BaseClass=CDialog
Filter=D
VirtualFilter=dWC
LastObject=COpenDlg


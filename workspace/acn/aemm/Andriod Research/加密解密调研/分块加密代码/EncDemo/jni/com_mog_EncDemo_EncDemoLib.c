
#include <jni.h>
#include <stdio.h>
#include <memory.h>
#include <time.h>
#include "com_mog_EncDemo_EncDemoLib.h"
#include "stdlib.h"

#define FILE_OPEN_ERROR 	2
#define FILE_SIZE_ERROR		3
#define FILE_CREATE_ERROR	4 
#define FILE_DATA_ERROR		5
#define FILE_PASS_ERROR		6
#define FILE_OP_SUCCESS		1

#define FS_ENC_STR		"MogEncFS 1.0"
#define FS_DEC_STR		"MogDecFS 1.0"
#define FS_DST_FNAME	"/sdcard/EncFile.mec"
#define FS_SRC_FNAME	"/sdcard/a.txt"
#define MAX_BLOCK_SIZE	512
#define MAX_MD5_VAL		17
#define MAX_DECALG_VAL	5
#define MAX_FILE_EXTVAL	13
#define TRUE			1
#define FALSE			0

/*初始置换表IP*/
int IP_Table[64] = {  57,49,41,33,25,17,9,1,
59,51,43,35,27,19,11,3,
61,53,45,37,29,21,13,5,
63,55,47,39,31,23,15,7,
56,48,40,32,24,16,8,0,
58,50,42,34,26,18,10,2,
60,52,44,36,28,20,12,4,
62,54,46,38,30,22,14,6}; 
/*逆初始置换表IP^-1*/
int IP_1_Table[64] = {39,7,47,15,55,23,63,31,
38,6,46,14,54,22,62,30,
37,5,45,13,53,21,61,29,
36,4,44,12,52,20,60,28,
35,3,43,11,51,19,59,27,
34,2,42,10,50,18,58,26,
33,1,41,9,49,17,57,25,
32,0,40,8,48,16,56,24};

/*扩充置换表E*/
int E_Table[48] = {31, 0, 1, 2, 3, 4,
3,  4, 5, 6, 7, 8,
7,  8,9,10,11,12,
11,12,13,14,15,16,
15,16,17,18,19,20,
19,20,21,22,23,24,
23,24,25,26,27,28,
27,28,29,30,31, 0};

/*置换函数P*/
int P_Table[32] = {15,6,19,20,28,11,27,16,
0,14,22,25,4,17,30,9,
1,7,23,13,31,26,2,8,
18,12,29,5,21,10,3,24};

/*S盒*/
int S[8][4][16] =
/*S1*/
{{{14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},
{0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},
{4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
{15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}},
/*S2*/
{{15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
{3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
{0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
{13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9}},
/*S3*/
{{10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8},
{13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1},
{13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7},
{1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12}},
/*S4*/
{{7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},
{13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},
{10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},
{3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14}},
/*S5*/
{{2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9},
{14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6},
{4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14},
{11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3}},
/*S6*/
{{12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11},
{10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8},
{9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6},
{4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13}},
/*S7*/
{{4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1},
{13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6},
{1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2},
{6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12}},
/*S8*/
{{13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7},
{1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2},
{7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8},
{2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}}};
/*置换选择1*/
int PC_1[56] = {56,48,40,32,24,16,8,
0,57,49,41,33,25,17,
9,1,58,50,42,34,26,
18,10,2,59,51,43,35,
62,54,46,38,30,22,14,
6,61,53,45,37,29,21,
13,5,60,52,44,36,28,
20,12,4,27,19,11,3};

/*置换选择2*/
int PC_2[48] = {13,16,10,23,0,4,2,27,
14,5,20,9,22,18,11,3,
25,7,15,6,26,19,12,1,
40,51,30,36,46,54,29,39,
50,44,32,46,43,48,38,55,
33,52,45,41,49,35,28,31};

/*对左移次数的规定*/
int MOVE_TIMES[16] = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};  
//#define BOOL			bool

typedef struct tag_FileHead                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
{
	char	chFileTag[MAX_FILE_EXTVAL]; // 文件标识 
	char	chFileExt[MAX_FILE_EXTVAL]; // 加密前的文件扩展名
	int		nStartOffset ; // 起始位置偏移地址
	int		nBlockNum	 ; // 文件内容块数
	int		nFileSize    ;
	char	chMd5Val[MAX_MD5_VAL] ; // 分块标识区MD5值，用作整体文件校验
}ST_FileHead;

typedef struct tag_FileHeadCheck
{
	ST_FileHead stFileHead ; 
	char		chMd5Val[MAX_MD5_VAL] ; 
}ST_FileHeadCheck ; 

typedef struct tag_stBlockInfo
{
	int		nBlockOrder ; // 当文件内容块被打乱时标识块的编号
	int		nBlockSrcSize	; // 本块数据
	int		nBlockSize	; // 本块数据的大小
	char	chMd5Val[MAX_MD5_VAL]; // 本块的MD5值，用作校验用
	char	chDecAlg[MAX_DECALG_VAL]	; // 本块数据的算法
}ST_BlockInfo;


typedef char ElemType;

int EncStrlen(char* buf)
{
	int nCount = 0 ; 
	for( nCount ; buf[nCount] != '\0' ; nCount++ )
		; 
	return nCount ; 
}
int EncStrcpy(char* dstbuf , char* srcbuf)
{
	int nCount = 0 ; 
	while( srcbuf[nCount] != '\0' )
	{
		dstbuf[nCount] = srcbuf[nCount] ;
		nCount++ ; 
	}
	return nCount ; 
}
int EncStrncpy(char* dstbuf , char* srcbuf , int nlen )
{
	int nCount = 0 ; 
	while( srcbuf[nCount] != '\0' && nCount < nlen )
	{
		dstbuf[nCount] = srcbuf[nCount] ;
		nCount++ ; 
	}
	return nCount ; 
}
int EncStrcmp(char* src , char* dst) 
{
	int i = 0 ; 
	if( EncStrlen(src) != EncStrlen(dst) )
		return 1 ; 
	for( i = 0 ; src[i] != '\0' && dst[i] != '\0' ; i++ )
		if( src[i] != dst[i] )
			return 1 ; 
	return 0 ; 
}
int EncStrncmp(char* src , char* dst , int nlen) 
{
	int i = 0 ; 
	for( i = 0 ; src[i] != '\0' && dst[i] != '\0' && i < nlen ; i++ )
		if( src[i] != dst[i] )
			return 1 ; 
	return 0 ; 
}
int EncMin(int m , int n)
{
	if( m> n )
		return n ; 
	else
		return m ; 
}

/*字节转换成二进制*/
int ByteToBit(ElemType ch, ElemType bit[8]){
	int cnt;
	for(cnt = 0;cnt < 8; cnt++){
		*(bit+cnt) = (ch>>cnt)&1;
	}
	return 0;
}

/*二进制转换成字节*/
int BitToByte(ElemType bit[8],ElemType *ch){
	int cnt;
	for(cnt = 0;cnt < 8; cnt++){
		*ch |= *(bit + cnt)<<cnt;
	}
	return 0;
}

/*将长度为8的字符串转为二进制位串*/
int Char8ToBit64(ElemType ch[8],ElemType bit[64]){
	int cnt;
	for(cnt = 0; cnt < 8; cnt++){  
		ByteToBit(*(ch+cnt),bit+(cnt<<3));
	}
	return 0;
}

/*将二进制位串转为长度为8的字符串*/
int Bit64ToChar8(ElemType bit[64],ElemType ch[8]){
	int cnt;
	memset(ch,0,8);
	for(cnt = 0; cnt < 8; cnt++){
		BitToByte(bit+(cnt<<3),ch+cnt);
	}
	return 0;
}
int DES_PC1_Transform(ElemType key[64], ElemType tempbts[56]) ; 
int DES_ROL(ElemType data[56], int time); 
int DES_PC2_Transform(ElemType key[56], ElemType tempbts[48]) ; 
/*生成子密钥*/
int DES_MakeSubKeys(ElemType key[64],ElemType subKeys[16][48]){
	ElemType temp[56];
	int cnt;
	DES_PC1_Transform(key,temp);/*PC1置换*/
	for(cnt = 0; cnt < 16; cnt++){/*16轮跌代，产生16个子密钥*/
		DES_ROL(temp,MOVE_TIMES[cnt]);/*循环左移*/
		DES_PC2_Transform(temp,subKeys[cnt]);/*PC2置换，产生子密钥*/
	}
	return 0;
}

/*密钥置换1*/
int DES_PC1_Transform(ElemType key[64], ElemType tempbts[56]){
	int cnt; 
	for(cnt = 0; cnt < 56; cnt++){
		tempbts[cnt] = key[PC_1[cnt]];
	}
	return 0;
}

/*密钥置换2*/
int DES_PC2_Transform(ElemType key[56], ElemType tempbts[48]){
	int cnt;
	for(cnt = 0; cnt < 48; cnt++){
		tempbts[cnt] = key[PC_2[cnt]];
	}
	return 0;
}

/*循环左移*/
int DES_ROL(ElemType data[56], int time){
	ElemType temp[56];

	/*保存将要循环移动到右边的位*/
	memcpy(temp,data,time);
	memcpy(temp+time,data+28,time);

	/*前28位移动*/
	memcpy(data,data+time,28-time);
	memcpy(data+28-time,temp,time);

	/*后28位移动*/
	memcpy(data+28,data+28+time,28-time);
	memcpy(data+56-time,temp+time,time); 

	return 0;
}

/*IP置换*/
int DES_IP_Transform(ElemType data[64]){
	int cnt;
	ElemType temp[64];
	for(cnt = 0; cnt < 64; cnt++){
		temp[cnt] = data[IP_Table[cnt]];
	}
	memcpy(data,temp,64);
	return 0;
}

/*IP逆置换*/
int DES_IP_1_Transform(ElemType data[64]){
	int cnt;
	ElemType temp[64];
	for(cnt = 0; cnt < 64; cnt++){
		temp[cnt] = data[IP_1_Table[cnt]];
	}
	memcpy(data,temp,64);
	return 0;
}

/*扩展置换*/
int DES_E_Transform(ElemType data[48]){
	int cnt;
	ElemType temp[48];
	for(cnt = 0; cnt < 48; cnt++){
		temp[cnt] = data[E_Table[cnt]];
	}
	memcpy(data,temp,48);
	return 0;
}

/*P置换*/
int DES_P_Transform(ElemType data[32]){
	int cnt;
	ElemType temp[32];
	for(cnt = 0; cnt < 32; cnt++){
		temp[cnt] = data[P_Table[cnt]];
	}
	memcpy(data,temp,32);
	return 0;
}

/*异或*/
int DES_XOR(ElemType R[48], ElemType L[48] ,int count){
	int cnt;
	for(cnt = 0; cnt < count; cnt++){
		R[cnt] ^= L[cnt];
	}
	return 0;
}

/*S盒置换*/
int DES_SBOX(ElemType data[48]){
	int cnt;
	int line,row,output;
	int cur1,cur2;
	for(cnt = 0; cnt < 8; cnt++){
		cur1 = cnt*6;
		cur2 = cnt<<2;

		/*计算在S盒中的行与列*/
		line = (data[cur1]<<1) + data[cur1+5];
		row = (data[cur1+1]<<3) + (data[cur1+2]<<2)
			+ (data[cur1+3]<<1) + data[cur1+4];
		output = S[cnt][line][row];

		/*化为2进制*/
		data[cur2] = (output&0X08)>>3;
		data[cur2+1] = (output&0X04)>>2;
		data[cur2+2] = (output&0X02)>>1;
		data[cur2+3] = output&0x01;
	}
	return 0;
}

/*交换*/
int DES_Swap(ElemType left[32], ElemType right[32]){
	ElemType temp[32];
	memcpy(temp,left,32);
	memcpy(left,right,32);  
	memcpy(right,temp,32);
	return 0;
}

/*加密单个分组*/
int DES_EncryptBlock(ElemType plainBlock[8], ElemType subKeys[16][48], ElemType cipherBlock[8]){
	ElemType plainBits[64];
	ElemType copyRight[48];
	int cnt;

	Char8ToBit64(plainBlock,plainBits);  
	/*初始置换（IP置换）*/
	DES_IP_Transform(plainBits);

	/*16轮迭代*/
	for(cnt = 0; cnt < 16; cnt++){ 
		memcpy(copyRight,plainBits+32,32);
		/*将右半部分进行扩展置换，从32位扩展到48位*/
		DES_E_Transform(copyRight);
		/*将右半部分与子密钥进行异或操作*/
		DES_XOR(copyRight,subKeys[cnt],48); 
		/*异或结果进入S盒，输出32位结果*/
		DES_SBOX(copyRight);
		/*P置换*/
		DES_P_Transform(copyRight);
		/*将明文左半部分与右半部分进行异或*/
		DES_XOR(plainBits,copyRight,32);
		if(cnt != 15){
			/*最终完成左右部的交换*/
			DES_Swap(plainBits,plainBits+32);
		}
	}
	/*逆初始置换（IP^1置换）*/
	DES_IP_1_Transform(plainBits);
	Bit64ToChar8(plainBits,cipherBlock);
	return 0;
}

/*解密单个分组*/
int DES_DecryptBlock(ElemType cipherBlock[8], ElemType subKeys[16][48],ElemType plainBlock[8]){
	ElemType cipherBits[64];
	ElemType copyRight[48];
	int cnt;

	Char8ToBit64(cipherBlock,cipherBits); 
	/*初始置换（IP置换）*/
	DES_IP_Transform(cipherBits);

	/*16轮迭代*/
	for(cnt = 15; cnt >= 0; cnt--){
		memcpy(copyRight,cipherBits+32,32);
		/*将右半部分进行扩展置换，从32位扩展到48位*/
		DES_E_Transform(copyRight);
		/*将右半部分与子密钥进行异或操作*/
		DES_XOR(copyRight,subKeys[cnt],48);  
		/*异或结果进入S盒，输出32位结果*/
		DES_SBOX(copyRight);
		/*P置换*/
		DES_P_Transform(copyRight);  
		/*将明文左半部分与右半部分进行异或*/
		DES_XOR(cipherBits,copyRight,32);
		if(cnt != 0){
			/*最终完成左右部的交换*/
			DES_Swap(cipherBits,cipherBits+32);
		}
	}
	/*逆初始置换（IP^1置换）*/
	DES_IP_1_Transform(cipherBits);
	Bit64ToChar8(cipherBits,plainBlock);
	return 0;
}

/*加密文件*/
int DES_Encrypt(char *plainFile, char *keyStr,FILE *cipherFile)
{
	FILE *cipher ; 
	int count;
	int nRetLen = 0 ; 
	ElemType plainBlock[8],cipherBlock[8],keyBlock[8];
	ElemType bKey[64];
	ElemType subKeys[16][48];
	int nLenTag = 0 ; 
	int nFileLen ; 
	if( (cipher = cipherFile ) == NULL)
	{
		return FILE_OPEN_ERROR ; 
	}
	/*设置密钥*/
	memcpy(keyBlock,keyStr,8);
	/*将密钥转换为二进制流*/
	Char8ToBit64(keyBlock,bKey);
	/*生成子密钥*/
	DES_MakeSubKeys(bKey,subKeys);
	nFileLen = EncStrlen(plainFile) ; 
	while(nLenTag < nFileLen )
	{
		if( nLenTag + 8 <= nFileLen )
		{
			memcpy(plainBlock , plainFile+nLenTag , 8 );
		}
		else
		{
			memcpy(plainBlock , plainFile+nLenTag , nFileLen - nLenTag );
			count = nFileLen - nLenTag ; 
			break;
		}
		nLenTag = nLenTag + 8 ; 
		DES_EncryptBlock(plainBlock,subKeys,cipherBlock);
		fwrite(cipherBlock,sizeof(char),8,cipher); 
		nRetLen = nRetLen + 8 ; 
		//printf("%s" , cipherBlock ) ; 
		if( nLenTag == nFileLen )
		{
			count = 0 ; 
			break;
		}
	}
	if(count){
		/*填充*/
		memset(plainBlock + count,'\0',7 - count);
		/*最后一个字符保存包括最后一个字符在内的所填充的字符数量*/
		plainBlock[7] = 8 - count;
		DES_EncryptBlock(plainBlock,subKeys,cipherBlock); 
		fwrite(cipherBlock,sizeof(char),8,cipher); 
		nRetLen = nRetLen + 8 ; 
	}
	return nRetLen;
}

/*加密文件密码*/
int DES_EncryptKey(char *plainFile , char* outbuf)
{
	int count;
	int nRetLen = 0 ; 
	ElemType plainBlock[8],cipherBlock[8],keyBlock[8];
	ElemType bKey[64];
	ElemType subKeys[16][48];
	int nLenTag = 0 ; 
	int nFileLen = 0 ; 
	/*设置密钥*/
	memcpy(keyBlock,"AeMMmoge",8);
	/*将密钥转换为二进制流*/
	Char8ToBit64(keyBlock,bKey);
	/*生成子密钥*/
	DES_MakeSubKeys(bKey,subKeys);
	nFileLen = EncStrlen(plainFile) ; 
	while(nLenTag < nFileLen )
	{
		if( nLenTag + 8 <= nFileLen )
		{
			memcpy(plainBlock , plainFile+nLenTag , 8 );
		}
		else
		{
			memcpy(plainBlock , plainFile+nLenTag , nFileLen - nLenTag );
			count = nFileLen - nLenTag ; 
			break;
		}
		nLenTag = nLenTag + 8 ; 
		DES_EncryptBlock(plainBlock,subKeys,cipherBlock);
		//fwrite(cipherBlock,sizeof(char),8,cipher);  
		// the data to write fo file is the data to write to struct . 
		nRetLen = nRetLen + 8 ; 
		//printf("%s" , cipherBlock ) ; 
		if( nLenTag == nFileLen )
		{
			count = 0 ; 
			break;
		}
	}
	if(count>0){
		/*填充*/
		memset(plainBlock + count,'\0',7 - count);
		/*最后一个字符保存包括最后一个字符在内的所填充的字符数量*/
		plainBlock[7] = 8 - count;
		DES_EncryptBlock(plainBlock,subKeys,cipherBlock); 
		//fwrite(cipherBlock,sizeof(char),8,cipher); 
		// the data to return 
		nRetLen = nRetLen + 8 ; 
	}
	EncStrcpy( outbuf , cipherBlock ) ; 
	return nRetLen;
}
int DES_Decrypt(char *cipherFile, int nLen , char *keyStr,FILE *plainFile)
{
	FILE *cipher ; 
	int count,times = 0;
	long fileLen;
	ElemType plainBlock[8],cipherBlock[8],keyBlock[8];
	ElemType bKey[64];
	ElemType subKeys[16][48];
	ElemType plainVar[8*1024] ; 
	int nTagCount = 0 ; 
	memset( plainVar , 0 ,8*1024*sizeof(char));
	if((cipher = plainFile ) == NULL)
	{
		return FILE_OPEN_ERROR;
	}	
	/*设置密钥*/
	memcpy(keyBlock,keyStr,8);
	/*将密钥转换为二进制流*/
	Char8ToBit64(keyBlock,bKey);
	/*生成子密钥*/
	DES_MakeSubKeys(bKey,subKeys);
	/*取文件长度 */
	fileLen = nLen ; 
	while(1){
		/*密文的字节数一定是8的整数倍*/
		memcpy( cipherBlock , cipherFile+times , 8 ) ; 
		DES_DecryptBlock(cipherBlock,subKeys,plainBlock);  
		times += 8;
		if(times < fileLen)
		{
			fwrite(plainBlock,sizeof(char),8,cipher);
		//	memcpy(plainVar+nTagCount , plainBlock , 8 );
			nTagCount = nTagCount + 8 ; 
		}
		else
		{
			break;
		}
	}
	/*判断末尾是否被填充*/
	if(plainBlock[7] < 8){
		for(count = 8 - plainBlock[7]; count < 7; count++){
			if(plainBlock[count] != '\0'){
				break;
			}
		}
	}
	if(count == 7){/*有填充*/ 
		fwrite(plainBlock,sizeof(char),8 - plainBlock[7],cipher);
	}
	else
	{/*无填充*/ 
		fwrite(plainBlock,sizeof(char),8,cipher);
	}
//	printf("%s\r\n",plainVar) ;
	return FILE_OP_SUCCESS;
}

// 创建随机数，来对分块进行重新排列
int CreateRandomNum(int bRestart , int nRandomSeed) 
{
	static int* nRandomArray = NULL ; 
	static int nRandomCount = 0 ; 
	if(bRestart == TRUE)
	{
		if(nRandomArray != NULL)
		{
			//delete nRandomArray ; 
			//memory leaks 
			nRandomArray = NULL ;
		}
		nRandomArray = (int*)malloc(sizeof(int)*nRandomSeed);
		//nRandomArray = new int[nRandomSeed] ; 
		nRandomCount = 0 ; 
	}
	if(nRandomArray == NULL )
	{
		printf("Error CreateRandomNumArray\r\n"); 
		return -1 ; 
	}
	srand(time(0));
	while(1)
	{
		int nTemNum  = rand()%nRandomSeed ; // 生成一个从0到nRandomSeed-1的一个随机数
		int iLoop ; 
		for(iLoop = 0 ; iLoop < nRandomCount ; iLoop++)
		{
			if(nRandomArray[iLoop] == nTemNum)
				break;
		}
		if( iLoop == nRandomCount )
		{
			nRandomArray[nRandomCount++] = nTemNum ; 
			return nTemNum ; 
		}
	}
}

//获取文件的扩展名
int GetFileExtStr(char* filePathName , char* outBuf)
{
	int i = 0 ; 
	if( filePathName == NULL || outBuf == NULL )
	{
		printf("Error InParamIsNULL\r\n") ; 
		return FALSE; 
	}
	for(i = EncStrlen(filePathName) - 1 ; i >= 0 ; i-- )
	{
		if( filePathName[i] == '.' )
		{
			memcpy( outBuf , outBuf , EncMin(MAX_FILE_EXTVAL , EncStrlen(filePathName) - 1 - i )); 
			return TRUE ; 
		}
	}
	printf("Error NoExtFind\r\n") ; 
	return FALSE ; 
}

int ConstructFileHeader(int nBlockNum , char* chExtVal , ST_FileHead *stFHead ) 
{
	ST_FileHead tmpFHead ; 
	memset(&tmpFHead ,0 , sizeof(ST_FileHead)) ; 
	memcpy( tmpFHead.chFileTag , FS_ENC_STR , EncMin(MAX_FILE_EXTVAL , EncStrlen(FS_ENC_STR))) ; 
	if( chExtVal != NULL )
	{
		memcpy( tmpFHead.chFileExt , chExtVal , EncMin(MAX_FILE_EXTVAL , EncStrlen(chExtVal))); 
	}
	tmpFHead.nStartOffset = 0 ; 
	tmpFHead.nBlockNum = nBlockNum ; 
	memcpy( stFHead , &tmpFHead , sizeof(ST_FileHead)); 
	return FILE_OP_SUCCESS ;	
}

long CalcBlockSize(ST_BlockInfo* bInfo , int nOrder , int nFileSize , int nTag ) 
{
	long nSizeCount = 0 ; 
	int i = 0 ; 
	if( nOrder <= 0 )
		return 0 ; 
	for( i = 0 ; i < nFileSize ; i++ )
	{
		if( nTag == 0 )
		{
			if( bInfo[i].nBlockOrder < nOrder ) 
			{
				nSizeCount += bInfo[i].nBlockSrcSize ; 
			}
		}
		else
		{
			if( i < nOrder ) 
			{
				nSizeCount += bInfo[i].nBlockSize ; 
			}
		}
	}
	return nSizeCount ; 
}

//写入新的文件
int GenerateNewEncFile(char* fName , int bCreate , void* data , int nSize)
{
	static FILE* fio = NULL  ; 
	if( fName == NULL )
	{
		if( fio != NULL )
		{
			fclose(fio);
			printf("Opera FileCloseSucc\r\n"); 
			return FILE_OP_SUCCESS ; 
		}
		else
		{
			printf("Error CloseEnc\r\n"); 
		}
	}
	if(bCreate)
	{
		if( (fio = fopen(fName , "wb" ) ) == NULL )
		{
			printf("Error CreateEncFile\r\n") ; 
			return FILE_CREATE_ERROR ; 
		}
	}
	fwrite((char*)data , sizeof(char) ,nSize , fio ) ; 
	//fio.write((char*)data,nSize);
	return FILE_OP_SUCCESS ; 
}
#define FileHeadSize 55
#define BlockInfoSize 34

typedef unsigned char byte ; 

int bytetoint(byte* data)
{
	return (((int)data[0]) << 24) + (((int)data[1]) << 16) + (((int)data[2]) << 8) + data[3]; 
}
void inttobyte(int n , byte* buf)
{
	int i = 0 ; 
	for(i = 0;i < 4;i++){
		buf[i] = (byte)(n >> (24 - i * 8)); 
	}
}

int FileEditRead(char* filepathname , char* pass , long nPos , char* outbuf , int nRetLen ) ; 
int FileEditWrite(char* filepathname , char* pass , char* data , long nPos ) ; 
//读文件，并进行加密
//BOOL GetSourceFile(char* filePath)
JNIEXPORT jint JNICALL Java_com_mog_EncDemo_EncDemoLib_EncryptFile(JNIEnv *env, jobject obj, jbyteArray jba, jbyteArray jba2)
{
	FILE* fin = NULL ;
	FILE* fio = NULL ; 
	long nFileSize = 0 ; 
	int nRandomSeed = 0 ; 
	//写文件头
	ST_FileHead stFHead ; 
	ST_BlockInfo *stBlockInfo ; 
	int i = 0 ; 
	char filename[128] ; 
	char inputdata[9] ; 
	int nLen = 0 ; 
	char outbuf[8] ; 
#if 1 
	jbyte* temfname = (*env)->GetByteArrayElements(env, jba, NULL);
	jbyte* temindata = (*env)->GetByteArrayElements(env, jba2, NULL); 
	memset( filename , 0 , sizeof(char)*128 ); 
	memset( inputdata , 0 , sizeof(char) * 9 ); 
	sprintf(filename , temfname) ; 
	sprintf( inputdata , temindata) ; 
#else
	sprintf(filename , filePath) ; 
	sprintf( inputdata , pass) ; 
#endif 
#if 1
	//FileEditRead(char* filepathname , char* pass , long nPos , char* outbuf , int nRetLen ) ;
	FileEditRead(filename , inputdata , 1090 , outbuf , 6 ) ;
	return 1 ; 
#endif 	
	nLen = EncStrlen(inputdata) ; 
	if( nLen > 8 )
		return FILE_DATA_ERROR ; 
	if(( fin = fopen(filename,"rb")) == NULL )
	{
		printf("Error OpenSourceFile\r\n");
		return FILE_OPEN_ERROR; 
	}
	sprintf( filename , "%s%s" , filename , ".mec" ) ; 
	if(( fio = fopen( filename , "wb")) == NULL )
	{
		printf("Error CreateDstEncFile\r\n");
		fclose(fin);
		return FILE_CREATE_ERROR ; 
	}
	fseek(fin,0,SEEK_END) ;
	nFileSize = ftell(fin);
	if(nFileSize == 0)
	{
		printf("Error FileSizeIsZero\r\n") ; 
		fclose(fin);
		fclose(fio) ; 
		return FILE_SIZE_ERROR ; 
	}
	nRandomSeed = nFileSize/MAX_BLOCK_SIZE + (nFileSize%MAX_BLOCK_SIZE > 0 ? 1:0) ; 
	memset( &stFHead , 0 , sizeof(ST_FileHead)) ; 
	// inputdata the password . 
	// stBlockInfo[k].nBlockSize = DES_Encrypt(temBuf , "key.txt" , fio ) ;  
	if( ConstructFileHeader(nRandomSeed , "txt" , &stFHead ) )
	{
		char outbuf[16] ; 
		byte tembyte[4] ; 
		stFHead.nFileSize = nFileSize ; 
		//fwrite( (void*)(&stFHead) , sizeof(char) , sizeof(ST_FileHead) , fio ) ; 
		fwrite(stFHead.chFileTag , MAX_FILE_EXTVAL , 1 , fio ) ; 
		fwrite(stFHead.chFileExt , MAX_FILE_EXTVAL , 1 , fio ) ; 
		memset( tembyte , 0 , 4 ) ; 
		inttobyte(stFHead.nStartOffset , tembyte ) ; 
		fwrite( tembyte , sizeof(int) , 1 , fio ) ; 
		memset( tembyte , 0 , 4 ) ; 
		inttobyte(stFHead.nBlockNum , tembyte ) ; 
		fwrite( tembyte , sizeof(int) , 1 , fio ) ; 
		memset( tembyte , 0 , 4 ) ; 
		inttobyte(stFHead.nFileSize , tembyte ) ; 
		fwrite( tembyte , sizeof(int) , 1 , fio ) ; 
		memset(outbuf , 0 , sizeof(char)*16);
		DES_EncryptKey(inputdata , outbuf) ; 
		memset( stFHead.chMd5Val , 0 , sizeof(char) * MAX_MD5_VAL ) ; 
		EncStrncpy( stFHead.chMd5Val , outbuf , 8 ) ; 
		fwrite(stFHead.chMd5Val , MAX_MD5_VAL , 1 , fio ) ; 
	}
	// 还是先分配区块号再读文件。
	// 调用随机数函数，为文件分配位置
	stBlockInfo = (ST_BlockInfo*)malloc(sizeof(ST_BlockInfo)*nRandomSeed) ; 
//	stBlockInfo = new ST_BlockInfo[nRandomSeed] ; 

	for( i = 0 ; i< nRandomSeed ; i++ ) // has problem here . 
	{
		int nBlockOrder = CreateRandomNum( i > 0 ? FALSE:TRUE , nRandomSeed ); 
		memset( &stBlockInfo[i] , 0 , sizeof(ST_BlockInfo)); 
		stBlockInfo[i].nBlockOrder = nBlockOrder ; 
		if( nBlockOrder != nRandomSeed - 1 ) 
		{
			stBlockInfo[i].nBlockSrcSize = MAX_BLOCK_SIZE ; 
		}
		else // 最后一块，大小有可能不确定
			stBlockInfo[i].nBlockSrcSize = EncMin(nFileSize-nBlockOrder*MAX_BLOCK_SIZE , MAX_BLOCK_SIZE) ; 
		EncStrcpy( stBlockInfo[i].chDecAlg  , "DES" ) ; 
		if(stBlockInfo[i].nBlockSrcSize%8 == 0)
		{
			stBlockInfo[i].nBlockSize = stBlockInfo[i].nBlockSrcSize ;
		}
		else
		{
			stBlockInfo[i].nBlockSize =  stBlockInfo[i].nBlockSrcSize + ( 8 - stBlockInfo[i].nBlockSrcSize%8 ) ; 
		}
		//fwrite( (void*)(&stBlockInfo[i]) , sizeof(char) , sizeof(ST_BlockInfo) , fio ) ; 
		{
			byte tembyte[4] ; 
			memset( tembyte , 0 , 4 ) ; 
			inttobyte(stBlockInfo[i].nBlockOrder , tembyte ) ; 
			fwrite( tembyte , sizeof(int) , 1 , fio) ; 
			memset( tembyte , 0 , 4 ) ; 
			inttobyte(stBlockInfo[i].nBlockSrcSize , tembyte ) ; 
			fwrite( tembyte , sizeof(int) , 1 , fio) ;
			memset( tembyte , 0 , 4 ) ; 
			inttobyte(stBlockInfo[i].nBlockSize , tembyte ) ; 
			fwrite( tembyte , sizeof(int) , 1 , fio) ;
			fwrite(stBlockInfo[i].chMd5Val , MAX_MD5_VAL , 1 , fio) ; 
			fwrite(stBlockInfo[i].chDecAlg , MAX_DECALG_VAL , 1 , fio) ; 
		}
	}

	for( i = 0  ; i < nRandomSeed ; i++ )
	{
		char temBuf[MAX_BLOCK_SIZE+1] = {0} ; 
		int nBlockOrder = stBlockInfo[i].nBlockOrder ; 
		long nTemPos = CalcBlockSize(stBlockInfo , nBlockOrder , nRandomSeed , 0 ) ; 
		rewind(fin);
		fseek(fin,nTemPos,SEEK_CUR);  
		fread(temBuf , sizeof(char) , stBlockInfo[i].nBlockSrcSize , fin );
		// 调用加密文件内容的接口，对明文进行加密。
		// 将加密的文件内容写入到内存或者直接写入到文件里面。
		//stBlockInfo[k].nBlockSize = DES_Encrypt(temBuf , "key.txt" , fio ) ; 
		stBlockInfo[i].nBlockSize = DES_Encrypt(temBuf , inputdata , fio ) ; 
	}
	fclose(fin) ; 
	// reconstruct the file
	rewind(fio); 
	fclose(fio) ; 
	return FILE_OP_SUCCESS ; 
}

//BOOL DecFile(char* filePathName)
JNIEXPORT jint JNICALL Java_com_mog_EncDemo_EncDemoLib_DecryptFile(JNIEnv *env, jobject obj, jbyteArray jba, jbyteArray jba2)
{
	FILE* fin = NULL  ; 
	FILE* fio = NULL  ; 
	ST_FileHead temFileHead ; 
	ST_BlockInfo *temBlockInfo ; 
	int *temArray ; 
	int i = 0 ; 
	int nBaseOffset = 0 ; 
	char filename[128] ; 
	char inputdata[9] ; 
	int nLen = 0 ; 
#if 1 
	jbyte* temfname = (*env)->GetByteArrayElements(env, jba, NULL);
	jbyte* temindata = (*env)->GetByteArrayElements(env, jba2, NULL);  
	memset( filename , 0 , sizeof(char)*128 ); 
	memset( inputdata , 0 , sizeof(char) * 9 ); 
	sprintf(filename , temfname) ; 
	sprintf( inputdata , temindata) ; 
#else
	sprintf(filename , filePathName) ; 
	sprintf( inputdata , pass) ;
#endif 
#if 1
	//FileEditWrite(char* filepathname , char* pass , char* data , long nPos ) ;
	FileEditWrite(filename , inputdata , "~~~~~~" , 1090 ) ;
	return 1 ; 
#endif 
	nLen = EncStrlen(inputdata) ; 
	if( nLen > 8 )
		return FILE_DATA_ERROR ; 
	if( (fin = fopen(filename , "rb" )) == NULL )
	{
		printf("Error OpenDecFile\r\n") ;
		return FILE_OPEN_ERROR ; 
	}
	rewind(fin);
	{
		char outbuf[16] ; 
		//fread(&temFileHead , sizeof(char) , sizeof(ST_FileHead) , fin ) ; 
		byte tembyte[4] ; 
		fread(temFileHead.chFileTag , MAX_FILE_EXTVAL , 1 , fin) ; 
		if( EncStrcmp( temFileHead.chFileTag , FS_ENC_STR ) != 0 )
		{
			printf("Error FileData") ; 
			fclose(fin); 
			return FILE_DATA_ERROR ; 
		}
		fread(temFileHead.chFileExt , MAX_FILE_EXTVAL , 1 , fin) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temFileHead.nStartOffset = bytetoint(tembyte) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temFileHead.nBlockNum = bytetoint(tembyte) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temFileHead.nFileSize = bytetoint(tembyte) ; 
		fread(temFileHead.chMd5Val , MAX_MD5_VAL ,1 , fin) ; 
		// 
		memset( outbuf , 0 , sizeof(char)*16 ) ; 
		DES_EncryptKey(inputdata , outbuf) ; 
		if( EncStrncmp( temFileHead.chMd5Val , outbuf  , 8 ) != 0 )
		{
			printf("Error Password\r\n") ;
			fclose(fin);
			return FILE_PASS_ERROR ; 
		}
	}
	if(temFileHead.nBlockNum == 0 )
	{
		printf("Error EmptyFile\r\n") ; 
		fclose(fin);
		return FILE_DATA_ERROR ; 
	}
	temBlockInfo = (ST_BlockInfo*)malloc(sizeof(ST_BlockInfo)*temFileHead.nBlockNum) ; 
	temArray = (int*)malloc(sizeof(int)*temFileHead.nBlockNum) ; 
	for(i = 0  ; i < temFileHead.nBlockNum ; i++ )
	{
		byte tembyte[4] ; 
		memset( &temBlockInfo[i] , 0 , sizeof(ST_BlockInfo) ) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temBlockInfo[i].nBlockOrder = bytetoint(tembyte) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temBlockInfo[i].nBlockSrcSize = bytetoint(tembyte) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temBlockInfo[i].nBlockSize = bytetoint(tembyte) ; 
		//fread( &temBlockInfo[i] , sizeof(char) , sizeof(ST_BlockInfo) , fin ) ; 
		fread(temBlockInfo[i].chMd5Val , MAX_MD5_VAL , 1 , fin) ; 
		fread(temBlockInfo[i].chDecAlg , MAX_DECALG_VAL , 1 , fin) ; 
		temArray[temBlockInfo[i].nBlockOrder] = i ; 
	}
	//ofstream fio("DecFile.txt" , ios::out);
	sprintf( filename , "%s%s" , filename , ".txt" ) ; 
	if( ( fio = fopen( filename , "wb") ) == NULL )
	{
		printf("Error CreateDecFile\r\n") ; 
		fclose(fin);
		return FILE_CREATE_ERROR ; 
	}
	//int nBaseOffset = sizeof(ST_FileHead) + sizeof(ST_BlockInfo)*temFileHead.nBlockNum ; 
	nBaseOffset = FileHeadSize + BlockInfoSize * temFileHead.nBlockNum ; 
	for( i = 0 ; i < temFileHead.nBlockNum ; i++)
	{
		int nBlockSize = 0 ; 
		int nTemLen = 0 ; 
		char temBuf[MAX_BLOCK_SIZE+1] = {0} ; 
		long nTemBlockOffset = CalcBlockSize(temBlockInfo , temArray[i] , temFileHead.nBlockNum , 1 ) ; 
		rewind(fin);
		fseek(fin , nBaseOffset + nTemBlockOffset , SEEK_CUR ) ; 
		fread( temBuf , temBlockInfo[temArray[i]].nBlockSize , 1 , fin ) ; 
		//进行解密操作
		nTemLen = temBlockInfo[temArray[i]].nBlockSize ; 
		//DES_Decrypt( temBuf , nTemLen , "key.txt", fio ) ; 
		DES_Decrypt( temBuf , nTemLen , inputdata , fio ) ; 
	} 
	fclose(fin) ; 
	fclose(fio) ; 
	return FILE_OP_SUCCESS ; 
}

long getEncdataPos(ST_BlockInfo *binfo , int nCount , int n) 
{
	int i = 0 ; 
	long nRet = 0 ; 
	for(i = 0 ; i< nCount ; i++ )
	{
		if( n == i )
		{
			return nRet ; 
		}
		nRet = nRet + binfo[i].nBlockSize ; 
	}
	return -1 ; 
}

int FileEditWrite(char* filepathname , char* pass , char* data , long nPos )
{
	FILE* fin = NULL  ; 
	FILE* fio = NULL ; 
	ST_FileHead temFileHead ; 
	ST_BlockInfo *temBlockInfo ; 
	int *temArray ; 
	int i = 0 ; 
	int nBaseOffset = 0 ; 
	char filename[128] ; 
	char inputdata[9] ; 
	int nLen = 0 ; 
	long getpos = 0 ; 
#if 0 
	jbyte* temfname = (*env)->GetByteArrayElements(env, jba, NULL);
	jbyte* temindata = (*env)->GetByteArrayElements(env, jba2, NULL);  
	memset( filename , 0 , sizeof(char)*128 ); 
	memset( inputdata , 0 , sizeof(char) * 9 ); 
	sprintf(filename , temfname) ; 
	sprintf( inputdata , temindata) ; 
#else
	sprintf(filename , filepathname) ; 
	sprintf( inputdata , pass) ;
#endif 
	nLen = EncStrlen(inputdata) ; 
	if( nLen > 8 )
		return FILE_DATA_ERROR ; 
	if( (fin = fopen(filename , "rb+" )) == NULL )
	{
		printf("Error OpenDecFile\r\n") ;
		return FILE_OPEN_ERROR ; 
	}
	rewind(fin);
	{
		char outbuf[16] ; 
		//fread(&temFileHead , sizeof(char) , sizeof(ST_FileHead) , fin ) ; 
		byte tembyte[4] ; 
		fread(temFileHead.chFileTag , MAX_FILE_EXTVAL , 1 , fin) ; 
		if( EncStrcmp( temFileHead.chFileTag , FS_ENC_STR ) != 0 )
		{
			printf("Error FileData") ; 
			fclose(fin); 
			return FILE_DATA_ERROR ; 
		}
		fread(temFileHead.chFileExt , MAX_FILE_EXTVAL , 1 , fin) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temFileHead.nStartOffset = bytetoint(tembyte) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temFileHead.nBlockNum = bytetoint(tembyte) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temFileHead.nFileSize = bytetoint(tembyte) ; 
		fread(temFileHead.chMd5Val , MAX_MD5_VAL ,1 , fin) ; 
		memset( outbuf , 0 , sizeof(char)*16 ) ; 
		DES_EncryptKey(inputdata , outbuf) ; 
		if( EncStrncmp( temFileHead.chMd5Val , outbuf  , 8 ) != 0 )
		//if( EncStrncmp( temFileHead.chMd5Val , inputdata  , 8 ) != 0 )
		{
			printf("Error Password\r\n") ;
			fclose(fin);
			return FILE_PASS_ERROR ; 
		}
	}
	if(temFileHead.nBlockNum == 0 )
	{
		printf("Error EmptyFile\r\n") ; 
		fclose(fin);
		return FILE_DATA_ERROR ; 
	}
	temBlockInfo = (ST_BlockInfo*)malloc(sizeof(ST_BlockInfo)*temFileHead.nBlockNum) ; 
	temArray = (int*)malloc(sizeof(int)*temFileHead.nBlockNum) ; 
	for(i = 0  ; i < temFileHead.nBlockNum ; i++ )
	{
		byte tembyte[4] ; 
		memset( &temBlockInfo[i] , 0 , sizeof(ST_BlockInfo) ) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temBlockInfo[i].nBlockOrder = bytetoint(tembyte) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temBlockInfo[i].nBlockSrcSize = bytetoint(tembyte) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temBlockInfo[i].nBlockSize = bytetoint(tembyte) ; 
		//fread( &temBlockInfo[i] , sizeof(char) , sizeof(ST_BlockInfo) , fin ) ; 
		fread(temBlockInfo[i].chMd5Val , MAX_MD5_VAL , 1 , fin) ; 
		fread(temBlockInfo[i].chDecAlg , MAX_DECALG_VAL , 1 , fin) ; 
		temArray[temBlockInfo[i].nBlockOrder] = i ; 
	}
	nBaseOffset = FileHeadSize + BlockInfoSize * temFileHead.nBlockNum ;
	rewind(fin);
	getpos = 0 ; 
	for( i = 0 ; i < temFileHead.nBlockNum ; i++)
	{
		int nBlockSize = 0 ; 
		int nTemLen = 0 ; 
		char temBuf[MAX_BLOCK_SIZE+1]  ; 
		memset( temBuf , 0 , sizeof(char)*(MAX_BLOCK_SIZE+1)) ; 
		// 起始位置 小于 目标位置， 起始位置 加上 偏移位置 大于目标位置  
		// 如果是跨越块与块之间的数据，再议。 
		if( getpos <= nPos &&  nPos < ( getpos = getpos + temBlockInfo[temArray[i]].nBlockSize) )
		{
			// that's the position . write it . 
			// get the real pos in encrypted file . 
			char tem[8] ; 
			long tempos = getEncdataPos(temBlockInfo , temFileHead.nBlockNum , temArray[i] ) ; 
			tempos = tempos + nBaseOffset + nPos%MAX_BLOCK_SIZE - (nPos%MAX_BLOCK_SIZE)%8 ; 
			fseek(fin , tempos , SEEK_CUR ) ; 
			memset( tem , 0 , sizeof(char) * 8 ) ; 
			fread(tem , sizeof(char) , 8 , fin) ; 
			{
				ElemType plainBlock[8],cipherBlock[8],keyBlock[8];
				ElemType bKey[64];
				ElemType subKeys[16][48];
				/*设置密钥*/
				memcpy(keyBlock,inputdata,8);
				/*将密钥转换为二进制流*/
				Char8ToBit64(keyBlock,bKey);
				/*生成子密钥*/
				DES_MakeSubKeys(bKey,subKeys);
				memcpy( cipherBlock , tem , 8 ) ; 
				memset( plainBlock , 0 , sizeof(char)*8 ) ; 
				/*取文件长度 */
				/*密文的字节数一定是8的整数倍*/
				DES_DecryptBlock(cipherBlock,subKeys,plainBlock) ;  
				memcpy( tem , plainBlock , 8 ) ;
				memcpy( tem + 2 , data , sizeof(char)*EncStrlen(data)) ; 
				memset( cipherBlock , 0 , sizeof(char) * 8 ) ; 
				memcpy( plainBlock , tem , 8 ) ; 
				DES_EncryptBlock(plainBlock,subKeys,cipherBlock);
				/*
				fclose(fin); 
				fin = NULL ; 
				if( (fio = fopen(filename , "wb" )) == NULL )
				{
					printf("Error OpenDecFile\r\n") ;
					return FILE_OPEN_ERROR ; 
				}
				*/
				rewind(fin) ; 
				fseek(fin , tempos , SEEK_CUR ) ; 
				fwrite(cipherBlock , sizeof(char) , 8 , fin ) ; 
				break; 
			}
		}
#if 0 
		int exactNpos=nPos-getpos;//块内的实际偏移地址
		int vid=temArray[i];//编码文件中所在的块编号
		long exactBlockStart=0;//所在块的起始地址
		for(int k=0;k<=i;k++)
		{
			exactBlockStart=+temBlockInfo[i].nBlockSize;

		}
		long exactaddress=exactBlockStart+exactNpos //编码文件中实际的地址
#endif 
	} 
	if( fin != NULL )
		fclose(fin) ; 
	if( fio != NULL )
		fclose(fio) ; 
	return FILE_OP_SUCCESS ; 
}


int FileEditRead(char* filepathname , char* pass , long nPos , char* outbuf , int nRetLen )
{
	FILE* fin = NULL  ; 
	FILE* fio = NULL ; 
	ST_FileHead temFileHead ; 
	ST_BlockInfo *temBlockInfo ; 
	int *temArray ; 
	int i = 0 ; 
	int nBaseOffset = 0 ; 
	char filename[128] ; 
	char inputdata[9] ; 
	int nLen = 0 ; 
	long getpos = 0 ; 
#if 0 
	jbyte* temfname = (*env)->GetByteArrayElements(env, jba, NULL);
	jbyte* temindata = (*env)->GetByteArrayElements(env, jba2, NULL);  
	memset( filename , 0 , sizeof(char)*128 ); 
	memset( inputdata , 0 , sizeof(char) * 9 ); 
	sprintf(filename , temfname) ; 
	sprintf( inputdata , temindata) ; 
#else
	sprintf(filename , filepathname) ; 
	sprintf( inputdata , pass) ;
#endif 
	nLen = EncStrlen(inputdata) ; 
	if( nLen > 8 )
		return FILE_DATA_ERROR ; 
	if( (fin = fopen(filename , "rb+" )) == NULL )
	{
		printf("Error OpenDecFile\r\n") ;
		return FILE_OPEN_ERROR ; 
	}
	rewind(fin);
	{
		char outbuf[16] ; 
		//fread(&temFileHead , sizeof(char) , sizeof(ST_FileHead) , fin ) ; 
		byte tembyte[4] ; 
		fread(temFileHead.chFileTag , MAX_FILE_EXTVAL , 1 , fin) ; 
		if( EncStrcmp( temFileHead.chFileTag , FS_ENC_STR ) != 0 )
		{
			printf("Error FileData") ; 
			fclose(fin); 
			return FILE_DATA_ERROR ; 
		}
		fread(temFileHead.chFileExt , MAX_FILE_EXTVAL , 1 , fin) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temFileHead.nStartOffset = bytetoint(tembyte) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temFileHead.nBlockNum = bytetoint(tembyte) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temFileHead.nFileSize = bytetoint(tembyte) ; 
		fread(temFileHead.chMd5Val , MAX_MD5_VAL ,1 , fin) ; 
		memset( outbuf , 0 , sizeof(char)*16 ) ; 
		DES_EncryptKey(inputdata , outbuf) ; 
		if( EncStrncmp( temFileHead.chMd5Val , outbuf  , 8 ) != 0 )
		//if( EncStrncmp( temFileHead.chMd5Val , inputdata  , 8 ) != 0 )
		{
			printf("Error Password\r\n") ;
			fclose(fin);
			return FILE_PASS_ERROR ; 
		}
	}
	if(temFileHead.nBlockNum == 0 )
	{
		printf("Error EmptyFile\r\n") ; 
		fclose(fin);
		return FILE_DATA_ERROR ; 
	}
	temBlockInfo = (ST_BlockInfo*)malloc(sizeof(ST_BlockInfo)*temFileHead.nBlockNum) ; 
	temArray = (int*)malloc(sizeof(int)*temFileHead.nBlockNum) ; 
	for(i = 0  ; i < temFileHead.nBlockNum ; i++ )
	{
		byte tembyte[4] ; 
		memset( &temBlockInfo[i] , 0 , sizeof(ST_BlockInfo) ) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temBlockInfo[i].nBlockOrder = bytetoint(tembyte) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temBlockInfo[i].nBlockSrcSize = bytetoint(tembyte) ; 
		fread( tembyte , sizeof(int) , 1 , fin) ; 
		temBlockInfo[i].nBlockSize = bytetoint(tembyte) ; 
		//fread( &temBlockInfo[i] , sizeof(char) , sizeof(ST_BlockInfo) , fin ) ; 
		fread(temBlockInfo[i].chMd5Val , MAX_MD5_VAL , 1 , fin) ; 
		fread(temBlockInfo[i].chDecAlg , MAX_DECALG_VAL , 1 , fin) ; 
		temArray[temBlockInfo[i].nBlockOrder] = i ; 
	}
	nBaseOffset = FileHeadSize + BlockInfoSize * temFileHead.nBlockNum ;
	rewind(fin);
	getpos = 0 ; 
	for( i = 0 ; i < temFileHead.nBlockNum ; i++)
	{
		int nBlockSize = 0 ; 
		int nTemLen = 0 ; 
		char temBuf[MAX_BLOCK_SIZE+1]  ; 
		memset( temBuf , 0 , sizeof(char)*(MAX_BLOCK_SIZE+1)) ; 
		// 起始位置 小于 目标位置， 起始位置 加上 偏移位置 大于目标位置  
		// 如果是跨越块与块之间的数据，再议。 
		if( getpos <= nPos &&  nPos < ( getpos = getpos + temBlockInfo[temArray[i]].nBlockSize) )
		{
			// that's the position . write it . 
			// get the real pos in encrypted file . 
			char tem[8] ; 
			long tempos = getEncdataPos(temBlockInfo , temFileHead.nBlockNum , temArray[i] ) ; 
			tempos = tempos + nBaseOffset + nPos%MAX_BLOCK_SIZE - (nPos%MAX_BLOCK_SIZE)%8 ; 
			fseek(fin , tempos , SEEK_CUR ) ; 
			memset( tem , 0 , sizeof(char) * 8 ) ; 
			fread(tem , sizeof(char) , 8 , fin) ; 
			{
				ElemType plainBlock[8],cipherBlock[8],keyBlock[8];
				ElemType bKey[64];
				ElemType subKeys[16][48];
				/*设置密钥*/
				memcpy(keyBlock,inputdata,8);
				/*将密钥转换为二进制流*/
				Char8ToBit64(keyBlock,bKey);
				/*生成子密钥*/
				DES_MakeSubKeys(bKey,subKeys);
				memcpy( cipherBlock , tem , 8 ) ; 
				memset( plainBlock , 0 , sizeof(char)*8 ) ; 
				/*取文件长度 */
				/*密文的字节数一定是8的整数倍*/
				DES_DecryptBlock(cipherBlock,subKeys,plainBlock) ;  
				memcpy( outbuf , plainBlock + (nPos%MAX_BLOCK_SIZE)%8 , 8 - (nPos%MAX_BLOCK_SIZE)%8 ) ; 
				break; 
			}
		}
#if 0 
		int exactNpos=nPos-getpos;//块内的实际偏移地址
		int vid=temArray[i];//编码文件中所在的块编号
		long exactBlockStart=0;//所在块的起始地址
		for(int k=0;k<=i;k++)
		{
			exactBlockStart=+temBlockInfo[i].nBlockSize;

		}
		long exactaddress=exactBlockStart+exactNpos //编码文件中实际的地址
#endif 
	} 
	fclose(fin) ; 
	return FILE_OP_SUCCESS ; 
}
#if 0 
void main()
{
	char temFileName[100] = {0} ; 
	clock_t a,b; 	
	int i = 0 ; 
	//scanf("%s" , temFileName) ; 
	EncStrcpy( temFileName , "12345678" ) ; 
	for( i = 0 ; i < 100 ; i++ )
	{
#if 0 
		a = clock();
		if( GetSourceFile("a.txt" , temFileName ) )
		{
			b = clock();
			printf("加密消耗%d毫秒\n",b-a);
		}
		else
		{
			printf("加密失败") ; 
		}	
#endif 
#if 0 
		//memset( temFileName , 0 , sizeof(char) * 100 ) ; 
		//scanf("%s" , temFileName) ; 
		a = clock();
		if( DecFile("a.txt.mec" , temFileName ) )
		{
			b = clock();
			printf("解密消耗%d毫秒\n",b-a); 
		}
		else
		{
			printf("解密失败") ; 
		}
#endif 
#if 1 
		a = clock();
		if( FileEditWrite( "a.txt.mec" , temFileName , "******" , 1090 ) == 1 )
		{
			b = clock();
			printf("块加密消耗%d毫秒\n",b-a); 
		}
		else
		{
			printf("块加密失败") ; 
		}
#endif 
#if 1 
		{
			char outbuf[10] = {0} ; 
			a = clock() ; 
			if( FileEditRead("a.txt.mec" , temFileName , 1090 , outbuf , 6 ) == 1 ) 
			{
				b = clock() ; 
				printf("块加密消耗%d毫秒\n",b-a); 
				printf("独到的数据是 ： %s\n" , outbuf ) ; 
			}
			else
			{
				printf("块加密失败") ; 
			}
		}
#endif 
#if 0 	
		a = clock();
		if( DecFile("a.txt.mec" , temFileName ) )
		{
			b = clock();
			printf("解密消耗%d毫秒\n",b-a); 
		}
		else
		{
			printf("解密失败") ; 
		}
#endif 
	}

	system("pause"); 
}
#endif
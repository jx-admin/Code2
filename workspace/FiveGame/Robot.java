import java.awt.Color;
import java.util.Random;
import javax.swing.JPanel;

class Robot {
	private int row;//当前机器人走的行和列
	private int col;
	private boolean myIsPlayingChess=false;//标志机器人是否正在下棋
	public void setMyIsPlayingChess(boolean status){//设置机器人是事正在下棋
		this.myIsPlayingChess=status;
	}
	public boolean getMyIsPlayingChess(){//获得机器人是否正在下棋
		return this.myIsPlayingChess;
	}
	public int getRow(){//获得当前机器人走的行
		return this.row;
	}
	public int getCol(){//获得当前机器人走的列
		return this.col;
	}
	public void cerebra(Color[][] allChesses, JPanel canvas,Color robotChessColor,Stack stack) {//机器人的大脑
		this.myIsPlayingChess=true;//机器人正在走棋
		long powerValue=0;//初始化防守权值
		int row=0;//初始化防守位置行和列
		int col=0;
		long attackPowerValue=0;//初始化进攻权值
		int attactRow=0;//初始化进攻位置行和列
		int attactCol=0;
		boolean isFirst=true;//是否是第一次，初始化为是
		for(int i=0;i< allChesses.length;i++){//通过计算得到防守的最佳位置,因为是防守所以统计白色
			for(int j=0;j<allChesses.length;j++){
				if(allChesses[i][j]==null){//对还没有走的空棋格进行统计
					if(isFirst==true){//第一个为空的空格
						row=i;//在棋盘中的位置
						col=j;
						powerValue=RowPowerValue(allChesses, i, j,Color.white)+ColPowerValue(allChesses, i, j,Color.white)+RightBiasPowerValue(allChesses, i, j,Color.white)+LeftBiasPowerValue(allChesses, i, j,Color.white);//计算当前位置的权值
						isFirst=false;//表示第一个位置计算完毕
					}
					else{
						long nowPowerValue;//打擂计算最佳防守位置
						nowPowerValue=RowPowerValue(allChesses, i, j,Color.white)+ColPowerValue(allChesses, i, j,Color.white)+RightBiasPowerValue(allChesses, i, j,Color.white)+LeftBiasPowerValue(allChesses, i, j,Color.white);
						if(nowPowerValue>powerValue){
							row=i;
							col=j;
							powerValue=nowPowerValue;
						}
					}
				}
			}
		}
		
		isFirst=true;//初始化isFirst为true		
		for(int i=0;i< allChesses.length;i++){
			for(int j=0;j<allChesses.length;j++){
				if(allChesses[i][j]==null){//计算空棋格的权值
					if(isFirst==true){//第一个空棋格,只执行一次
						attactRow=i;//第一个空棋格的行和列
						attactCol=j;
						attackPowerValue=RowPowerValue(allChesses, i, j,Color.black)+ColPowerValue(allChesses, i, j,Color.black)+RightBiasPowerValue(allChesses, i, j,Color.black)+LeftBiasPowerValue(allChesses, i, j,Color.black);//计算第一个空棋格的权值
						isFirst=false;
					}
					else{
						long nowPowerValue;//打擂计算最佳进攻位置
						nowPowerValue=RowPowerValue(allChesses, i, j,Color.black)+ColPowerValue(allChesses, i, j,Color.black)+RightBiasPowerValue(allChesses, i, j,Color.black)+LeftBiasPowerValue(allChesses, i, j,Color.black);
						if(nowPowerValue>powerValue){
							attactRow=i;
							attactCol=j;
							attackPowerValue=nowPowerValue;
						}
					}
				}
			}
		}
		
		if(stack.StackLength()==0){//如果机器人走第一颗棋，就不用计算了随机走一步棋
			Random random=new Random();
			this.row=random.nextInt(allChesses.length);
			this.col=random.nextInt(allChesses.length);
			
		}
		else{//否则就确定是防守还是进攻
			if(attackPowerValue>=powerValue){//比较防守和进攻权值来确定防守还是进攻
				this.row=attactRow;
				this.col=attactCol;
			}
			else{
				this.row=row;
				this.col=col;
			}
		}
		allChesses[getRow()][getCol()]=robotChessColor;//修改二维数组
		stack.Push(getRow(), getCol(), robotChessColor);//将走棋信息压入栈中
		canvas.repaint();
		this.myIsPlayingChess=false;//机器人走棋结束
	}
	
	public long LeftBiasPowerValue(Color[][] allChesses,int row,int col,Color color){
		NullAndCount colUp=LeftBiasUpPowerValue(allChesses, row, col, color);
		NullAndCount colDown=LeftBiasDownPowerValue(allChesses, row, col, color);
		long powerValue;
		switch(colUp.getChessCount()+colDown.getChessCount()+1){
			case  1:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=1;
				}
				else {
					powerValue=5;
				}
				break;
			case 2:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=21;
				}
				else {
					powerValue=85;
				}
				break;
			case 3:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=341;
				}
				else {
					powerValue=1365;
				}
				break;
			case 4:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=5461;
				}
				else {
					powerValue=21845;
				}
				break;
			default:
				powerValue=87381;
			break;
		}
		return powerValue;
	}
	
	public NullAndCount LeftBiasDownPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		int i=row;
		int j=col;
		while(i<allChesses.length-1&&j>0){
			if(allChesses[i+1][j-1]==color){
				count++;
				i=i+1;
				j=j-1;
			}
			else {
				if(allChesses[i+1][j-1]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
	
	public NullAndCount LeftBiasUpPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		int i=row;
		int j=col;
		while(i>0&&j<allChesses.length-1){
			if(allChesses[i-1][j+1]==color){
				count++;
				i=i-1;
				j=j+1;
			}
			else {
				if(allChesses[i-1][j+1]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
	
	public long RightBiasPowerValue(Color[][] allChesses,int row,int col,Color color){
		NullAndCount colUp=RightBiasUpPowerValue(allChesses, row, col, color);
		NullAndCount colDown=RightBiasDownPowerValue(allChesses, row, col, color);
		long powerValue;
		switch(colUp.getChessCount()+colDown.getChessCount()+1){
			case  1:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=1;
				}
				else {
					powerValue=5;
				}
				break;
			case 2:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=21;
				}
				else {
					powerValue=85;
				}
				break;
			case 3:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=341;
				}
				else {
					powerValue=1365;
				}
				break;
			case 4:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=5461;
				}
				else {
					powerValue=21845;
				}
				break;
			default:
				powerValue=87381;
			break;
		}
		return powerValue;
	}
	
	public NullAndCount RightBiasDownPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		int i=row;
		int j=col;
		while(i<allChesses.length-1&&j<allChesses.length-1){
			if(allChesses[i+1][j+1]==color){
				count++;
				i=i+1;
				j=j+1;
			}
			else {
				if(allChesses[i+1][j+1]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
	
	public NullAndCount RightBiasUpPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		int i=row;
		int j=col;
		while(i>0&&j>0){
			if(allChesses[i-1][j-1]==color){
				count++;
				i=i-1;
				j=j-1;
			}
			else {
				if(allChesses[i-1][j-1]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
	
	public long ColPowerValue(Color[][] allChesses,int row,int col,Color color){
		NullAndCount colUp=ColUpPowerValue(allChesses, row, col, color);
		NullAndCount colDown=ColDownPowerValue(allChesses, row, col, color);
		long powerValue;
		switch(colUp.getChessCount()+colDown.getChessCount()+1){
			case  1:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=1;
				}
				else {
					powerValue=5;
				}
				break;
			case 2:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=21;
				}
				else {
					powerValue=85;
				}
				break;
			case 3:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=341;
				}
				else {
					powerValue=1365;
				}
				break;
			case 4:
				if(colUp.getNullCount()==0&&colDown.getNullCount()==0){
					powerValue=0;
				}
				else if((colUp.getNullCount()==1&&colDown.getNullCount()==0)||(colUp.getNullCount()==0&&colDown.getNullCount()==1)){
					powerValue=5461;
				}
				else {
					powerValue=21845;
				}
				break;
			default:
				powerValue=87381;
			break;
		}
		return powerValue;
		
	}
	
	public NullAndCount ColDownPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		for(int i=row;i<allChesses.length-1;i++){
			if(allChesses[i+1][col]==color){
				count++;
			}
			else{
				if(allChesses[i+1][col]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
	
	public NullAndCount ColUpPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		for(int i=row;i>0;i--){
			if(allChesses[i-1][col]==color){
				count++;
			}
			else{
				if(allChesses[i-1][col]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
	
	public long RowPowerValue(Color[][] allChesses,int row,int col,Color color){
		NullAndCount rightRow=RightRowPowerValue(allChesses, row, col, color);
		NullAndCount leftRow=LeftRowPowerValue(allChesses, row, col, color);
		long powerValue;
		switch(rightRow.getChessCount()+leftRow.getChessCount()+1){
			case  1:
				if(rightRow.getNullCount()==0&&leftRow.getNullCount()==0){
					powerValue=0;
				}
				else if((rightRow.getNullCount()==1&&leftRow.getNullCount()==0)||(rightRow.getNullCount()==0&&leftRow.getNullCount()==1)){
					powerValue=1;
				}
				else {
					powerValue=5;
				}
				break;
			case 2:
				if(rightRow.getNullCount()==0&&leftRow.getNullCount()==0){
					powerValue=0;
				}
				else if((rightRow.getNullCount()==1&&leftRow.getNullCount()==0)||(rightRow.getNullCount()==0&&leftRow.getNullCount()==1)){
					powerValue=21;
				}
				else {
					powerValue=85;
				}
				break;
			case 3:
				if(rightRow.getNullCount()==0&&leftRow.getNullCount()==0){
					powerValue=0;
				}
				else if((rightRow.getNullCount()==1&&leftRow.getNullCount()==0)||(rightRow.getNullCount()==0&&leftRow.getNullCount()==1)){
					powerValue=341;
				}
				else {
					powerValue=1365;
				}
				break;
			case 4:
				if(rightRow.getNullCount()==0&&leftRow.getNullCount()==0){
					powerValue=0;
				}
				else if((rightRow.getNullCount()==1&&leftRow.getNullCount()==0)||(rightRow.getNullCount()==0&&leftRow.getNullCount()==1)){
					powerValue=5461;
				}
				else {
					powerValue=21845;
				}
				break;
			default:
				powerValue=87381;
			break;
		}
		return powerValue;
	}
	
	public NullAndCount LeftRowPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		for(int i=col;i>0;i--){
			if(allChesses[row][i-1]==color){
				count++;
			}
			else{
				if(allChesses[row][i-1]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
	
	public NullAndCount RightRowPowerValue(Color[][] allChesses,int row,int col,Color color){
		int count=0;
		NullAndCount nullAndCount=new NullAndCount();
		for(int i=col;i<allChesses.length-1;i++){
			if(allChesses[row][i+1]==color){
				count++;
			}
			else{
				if(allChesses[row][i+1]==null){
					nullAndCount.setNullCount(1);
				}
				break;
			}
		}
		nullAndCount.setChessCount(count);
		return nullAndCount;
	}
}
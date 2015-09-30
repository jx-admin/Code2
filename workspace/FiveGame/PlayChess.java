import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import sun.java2d.pipe.DrawImage;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.net.*;

public class PlayChess extends JFrame{//继承JFrame之后playChess就是一个窗体
	
	private Image imageBG;//背景图片对象
	private Image imageBlack;//黑色棋子图片对象
	private Image imageWhite;//白色棋子图片对象
	private Image imageMenu;//菜单图片对象
	private Image imageW;//没有笑的绿豆蛙图片对象
	private Image imageX;//在笑的绿豆蛙图片对象
	private Image imageRim;//红色框图片对象
	private Color[][] allChesses=new Color[14][14];//所有的棋格
	private MyCanvas canvas;//自定义的MyCanvas类并继承了JPanel
	private boolean isGameBegin=false;//游戏是否开始
	private boolean isGameOver=true;//游戏是否结束
	private Stack stack;//链栈对象
	private Robot robot;//机器人对象
	private Thread PlayerThread;//玩家时间所对应的线程
	private Thread robotThread;//机器人时间所对应的线程
	private Thread unDoThread;//悔棋时间所对应的线程
	private int unDoTime=5;//悔棋的时间间隔
	private int playerTime=1800;//玩家的总时间
	private int robotTime=1800;//机器人的总时间
	private Graphics2D graphics2d;//画图对象
	private Color whoSmile=null;//谁赢了谁显示在笑的绿豆蛙图片对象
	private boolean isUndo=false;
	public PlayChess(){//构造函数
		this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().createImage(PlayChess.class.getResource("pointer.png")),new Point(0,0), ""));
		Applet.newAudioClip(PlayChess.class.getResource("bg.mid")).loop();
		
		stack=new Stack();
		robot=new Robot();
		
		URL imgUrl1 = PlayChess.class.getResource("bg_game.JPG");// 获取图片资源的路径
		imageBG = Toolkit.getDefaultToolkit().getImage(imgUrl1);// 获取图片资源
		
		URL imgUrl2 = PlayChess.class.getResource("black.png");// 获取图片资源的路径
		imageBlack= Toolkit.getDefaultToolkit().getImage(imgUrl2);// 获取图片资源
		
		URL imgUrl3 = PlayChess.class.getResource("white.png");// 获取图片资源的路径
		imageWhite = Toolkit.getDefaultToolkit().getImage(imgUrl3);// 获取图片资源
		
		URL imgUrl4 = PlayChess.class.getResource("menu.png");// 获取图片资源的路径
		imageMenu = Toolkit.getDefaultToolkit().getImage(imgUrl4);// 获取图片资源
		
		URL imgUrl5 = PlayChess.class.getResource("W.png");// 获取图片资源的路径
		imageW = Toolkit.getDefaultToolkit().getImage(imgUrl5);// 获取图片资源
		
		URL imgUrl6 = PlayChess.class.getResource("X.gif");// 获取图片资源的路径
		imageX = Toolkit.getDefaultToolkit().getImage(imgUrl6);// 获取图片资源
		
		URL imgUrl8 = PlayChess.class.getResource("rim.png");// 获取图片资源的路径
		imageRim = Toolkit.getDefaultToolkit().getImage(imgUrl8);// 获取图片资源
		this.setSize(700,550);//设置窗囗大小
		setWindowCenter();//将窗囗居中
		canvas=new MyCanvas();//创建一个MyCanvas对象
		this.getContentPane().add(canvas);//将canvas对象添加到面板中
		
		robotThread=new Thread(new Runnable(){
			public void run(){
				while(true){
					if(robot.getMyIsPlayingChess()==true&&isGameBegin==true&&isUndo==false){
						try{
							robotThread.sleep(1000);
						}
						catch(Exception e){
							e.printStackTrace();
						}
						robotTime--;
						canvas.repaint();
						if(robotTime==0){
							isGameBegin=false;
							isGameOver=true;
							whoSmile=Color.white;
							canvas.repaint();
							JOptionPane.showMessageDialog(null,"你赢了！");
						}
					}
				}
			}
		});
		
		PlayerThread=new Thread(new Runnable(){
			public void run(){//相执行start方法时该线程启动
				while(true){//在循环中呢要控制时间走或停止，如果是玩家走棋时间就走否则就不走
					if(robot.getMyIsPlayingChess()==false&&isGameBegin==true&&isUndo==false){
						try{//如果机器人没有走棋而且游戏已经开始那么就表示玩家走棋
							PlayerThread.sleep(1000);//线程睡一秒钟
						}
						catch(Exception e){
							e.printStackTrace();
						}
						playerTime--;//玩家时间减一秒钟
						canvas.repaint();//绘制窗囗
						if(playerTime==0){//如果玩家的时间用完了，表示玩家输
							isGameBegin=false;//游戏没有开始
							isGameOver=true;//游戏结束
							whoSmile=Color.black;//黑色笑
							canvas.repaint();//绘制窗囗
							JOptionPane.showMessageDialog(null,"机器人赢了！");
						}
					}
				}
			}
		});
		unDoThread=new Thread(new Runnable(){
			public void run(){//到执行start方法时线程启动
				while(true){
					if(unDoTime==0){//悔棋时间到,开始走棋
						unDoTime--;//设置悔棋时间为-1
						if(stack.getStackTop()!=null){//如果栈顶不为空表示有记录
							if(stack.getStackTop().getChessColor()==Color.white){{//如果为白色表示该机器人走棋
								robot.cerebra(allChesses, canvas, Color.black,stack);//机器人走棋
								if(checkRowIsFive(robot.getRow(), robot.getCol())||checkColIsFive(robot.getRow(), robot.getCol())||checkRightBias(robot.getRow(), robot.getCol())||checkLeftBias(robot.getRow(), robot.getCol())){//机器人是否赢 
									isGameBegin=false;
									isGameOver=true;
									whoSmile=Color.black;
									canvas.repaint();
									JOptionPane.showMessageDialog(null,"机器人赢了！");
									return;
								}
								if(isDogfall()==true){//是否平局
									isGameBegin=false;
									isGameOver=true;
									canvas.repaint();
									JOptionPane.showMessageDialog(null,"平局了！");
									return;
								}
							}
						}
					}
					else{//表示棋盘上没有棋子了，也该机器人走棋
						robot.cerebra(allChesses, canvas, Color.black,stack);
						if(checkRowIsFive(robot.getRow(), robot.getCol())||checkColIsFive(robot.getRow(), robot.getCol())||checkRightBias(robot.getRow(), robot.getCol())||checkLeftBias(robot.getRow(), robot.getCol())){
							isGameBegin=false;
							isGameOver=true;
							whoSmile=Color.black;
							canvas.repaint();
							whoSmile=Color.black;
							canvas.repaint();
							JOptionPane.showMessageDialog(null,"机器人赢了！");
							return;
						}
					}
					isUndo=false;//继续走棋
					}
					else{
						try{
							PlayerThread.sleep(1000);//线程睡一秒钟
							unDoTime--;//悔棋时间减一
							if(unDoTime==-10){
								unDoTime=-1;
							}
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		canvas.addMouseListener(new MouseInputAdapter(){
			
			public void mousePressed(MouseEvent e){
				if((e.getX()>=179&&e.getX()<=652)&&(e.getY()>=30&&e.getY()<=497)&&isGameBegin==true&&robot.getMyIsPlayingChess()==false&&isUndo==false){  //如果鼠标在棋盘区域单击而且游戏开始而且机器人没有走棋那么就执行if块代码 
						int row=(int)((e.getY()-30)/33.4);//将坐标转换成下标行
						int col=(int)((e.getX()-179)/33.8);//将坐标转换成下标列
						if(allChesses[row][col]==null){//如果在该棋盘位置等于空就表示该位置没有下棋可以走棋
							allChesses[row][col]=Color.white;//将白色赋值给该位置的二维数组
							stack.Push(row, col, Color.white);//将该信息压入栈
							canvas.repaint();//重新绘制,玩家走棋完毕
							if(checkRowIsFive(row, col)||checkColIsFive(row, col)||checkRightBias(row, col)||checkLeftBias(row, col)){    //调用自定义函数判断四个方向是否有五子连珠，是否羸
								isGameBegin=false;//游戏没有开始
								isGameOver=true;//游戏结束
								whoSmile=Color.white;//玩家羸，玩家笑
								canvas.repaint();//重新绘制
								JOptionPane.showMessageDialog(null,"你赢了！");
								return;
							}
							if(isDogfall()==true){//如果玩家没有羸判断是否是平局
								isGameBegin=false;//游戏没有开始
								isGameOver=true;//游戏结束
								canvas.repaint();//重新绘制
								JOptionPane.showMessageDialog(null,"平局了！");
								return;
							}
							robot.cerebra(allChesses, canvas, Color.black,stack);//玩家没有蠃，机器人走棋
							if(checkRowIsFive(robot.getRow(), robot.getCol())||checkColIsFive(robot.getRow(), robot.getCol())||checkRightBias(robot.getRow(), robot.getCol())||checkLeftBias(robot.getRow(), robot.getCol())){  ////调用自定义函数判断四个方向是否有五子连珠，是否羸
								isGameBegin=false;//游戏没有开始
								isGameOver=true;//游戏结束
								whoSmile=Color.black;//机器人羸，机器人笑
								canvas.repaint();//重新绘制
								JOptionPane.showMessageDialog(null,"机器人赢了！");
								return;
							}
							if(isDogfall()==true){//如果机器人没有赢，判断是否是平局
								isGameBegin=false;//游戏没有开始
								isGameOver=true;//游戏结束
								canvas.repaint();//重新绘制
								JOptionPane.showMessageDialog(null,"平局了！");
								return;
							}
						}
						else {
							JOptionPane.showMessageDialog(null,"该位置有棋子了！");
						}
				}
				else{
					if((e.getX()>=17&&e.getX()<=39)&&(e.getY()>=249&&e.getY()<=289)){  //如果坐标在该区域表示单击了开始
						for(int i=0;i<allChesses.length;i++){//清空棋盘
							for(int j=0;j<allChesses.length;j++){
								allChesses[i][j]=null;
							}
						}
						
						canvas.repaint();//重新绘制
						isGameBegin=true;//游戏开始
						isGameOver=false;//游戏结束
						playerTime=1800;//重新初始化玩家下棋时间
						robotTime=1800;//重新初始化玩家下棋时间
						whoSmile=null;//重新初始化谁笑
						stack.clearStack();//清空栈
						if(PlayerThread.getState()==Thread.State.NEW){//如果玩家线程还没启动，就启动线程
							PlayerThread.start();
						}
						if(robotThread.getState()==Thread.State.NEW){//如果机器人线程还没启动，就启动线程
							robotThread.start();
						}
						JOptionPane.showMessageDialog(null,"可以开始了！");
						robot.cerebra(allChesses, canvas, Color.black,stack);//机器人走棋
						if(checkRowIsFive(robot.getRow(), robot.getCol())||checkColIsFive(robot.getRow(), robot.getCol())||checkRightBias(robot.getRow(), robot.getCol())||checkLeftBias(robot.getRow(), robot.getCol())){  //判断机器人是否羸
							isGameBegin=false;
							isGameOver=true;
							whoSmile=Color.black;
							canvas.repaint();
							JOptionPane.showMessageDialog(null,"机器人赢了！");
							return;
						}
						if(isDogfall()==true){//是否是平局
							isGameBegin=false;
							isGameOver=true;
							canvas.repaint();
							JOptionPane.showMessageDialog(null,"平局了！");
							return;
						}
					}
					if((e.getX()>=48&&e.getX()<=72)&&(e.getY()>=249&&e.getY()<=289)){  //单击暂停区域
						if(isGameOver==true){//如果游戏结束就不执行暂停
							return;
						}
						isGameBegin=false;//设置游戏没有开始，来达到暂停效果
						JOptionPane.showMessageDialog(null,"暂停！");
					}
					if((e.getX()>=80&&e.getX()<=104)&&(e.getY()>=249&&e.getY()<=289)){ //单击继续区域
						if(isGameOver==true){//如果游戏结束就不执行暂停
							return;
						}
						isGameBegin=true;//设置游戏开始，来达到继续效果
						JOptionPane.showMessageDialog(null,"继续！");
					}
					if((e.getX()>=113&&e.getX()<=136)&&(e.getY()>=249&&e.getY()<=289)){  //单击悔棋区域
						if(isGameOver==true || isGameBegin==false){ //如果游戏没有开始或者游戏已经结束就不能悔棋
							return;
						}
						isUndo=true;
						for(int i=0;i<allChesses.length;i++){//清空棋盘
							for(int j=0;j<allChesses.length;j++){
								allChesses[i][j]=null;
							}
						}
						stack.Pop();//刚走的那一步棋出栈
						LNode p=stack.getHeadNode().getNext();//获得第一个结点
						while(p!=null){//如果第一个结点为空，表示棋盘为空，否则不为空就重新设置棋盘直到栈为空结束
							allChesses[p.getRow()][p.getCol()]=p.getChessColor();
							p=p.getNext();
						}
						canvas.repaint();
						unDoTime=5;//如果5秒钟不单击悔棋表示悔棋结束
						if(unDoThread.getState()==Thread.State.NEW){//如果该线程没有启动就启动
							unDoThread.start();
						}
				}
			}
		}
			});
		this.setTitle("五子棋游戏");//设置标题
		this.setResizable(false);//设置不能改表窗囗大小
		this.setVisible(true);//设置显示窗囗
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭窗囗后的操作,关闭窗囗后结束进程
	}
	
	public boolean isDogfall(){//是否是平局
		if(stack.StackLength()==14*14){//栈中记录了棋盘的信息，如果最后一步棋没有决定胜负,栈中的元素个数等于棋盘的棋子数就表示平局
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean checkLeftBias(int row,int col){//检查左斜
		int leftBiasUp=checkLeftBiasUp(row, col);//得到左斜上的个数
		int leftBiasDown=checkLeftBiasDown(row, col);//得到右斜下的个数
		int sum=leftBiasUp+leftBiasDown-1;//在计算过程中多算了一颗棋子，所以要减一颗棋子
		if(sum>=5){//已经五子连珠
			return true;
		}
		else{
			return false;
		}
	}
	
	public int checkLeftBiasDown(int row,int col){//检查左斜下
		int i=row;//将行赋值给i
		int j=col;//将列赋值给j
		int count=1;//将1赋值给count，count表示棋子的个数
		while(i<allChesses.length-1&&j>0){//终结条件
			if(allChesses[i][j]==allChesses[i+1][j-1]){//如果相同就进行统计
				count++;//棋子数加一
				i=i+1;//将i和j赋值为上次比较的位置
				j=j-1;
			}
			else {//如果有一次不相同就退出循环，比较结束
				break;
			}
		}
		return count;//返回统计的棋子数
	}
	
	public int checkLeftBiasUp(int row,int col){//统计左斜上的棋子数
		int i=row;
		int j=col;
		int count=1;
		while(i>0&&j<allChesses.length-1){
			if(allChesses[i-1][j+1]==allChesses[i][j]){
				count++;
				i=i-1;
				j=j+1;
			}
			else {
				break;
			}
		}
		return count;
	}

	public boolean checkRightBias(int row,int col){//统计右斜的棋子数
		int rightBiasUp=checkRightBiasUp(row, col);
		int rightBiasDown=checkRightBiasDown(row, col);
		int sum=rightBiasUp+rightBiasDown-1;
		if(sum>=5){
			return true;
		}
		else{
			return false;
		}
	}
	public int checkRightBiasUp(int row,int col){//统计右斜上的棋子数
		int i=row;
		int j=col;
		int count=1;
		while(i>0&&j>0){
			if(allChesses[i-1][j-1]==allChesses[i][j]){
				count++;
				i=i-1;
				j=j-1;
			}
			else {
				break;
			}
		}
		return count;
	}
	
	public int checkRightBiasDown(int row,int col){//统计右斜下的棋子数
		int i=row;
		int j=col;
		int count=1;
		while(i<allChesses.length-1&&j<allChesses.length-1){
			if(allChesses[i][j]==allChesses[i+1][j+1]){
				count++;
				i=i+1;
				j=j+1;
			}
			else {
				break;
			}
		}
		return count;
	}
	
	public boolean checkColIsFive(int row,int col){//统计列的棋子数
		int upCount=checkColUpIsFive(row, col);
		int downCount=checkColDownIsFive(row, col);
		int sum=upCount+downCount-1;
		if(sum>=5){
			return true;
		}
		else{
			return false;
		}
	}
	public boolean checkRowIsFive(int row,int col){//统计列的棋子数
		int rightCount=checkRowRightIsFive(row, col);
		int leftCount=checkRowLeftIsFive(row,col);
		int sum=rightCount+leftCount-1;
		if(sum>=5){
			return true;
		}
		else{
			return false;
		}
	}
	public int checkColDownIsFive(int row,int col){//统计列下的棋子数
		int count=1;
		for(int i=row;i<allChesses.length-1;i++){
			if(allChesses[i][col]==allChesses[i+1][col]){
				count++;
			}
			else{
				break;
			}
		}
		return count;
	}
	
	public int checkColUpIsFive(int row,int col){//统计列上的棋子数
		int count=1;
		for(int i=row;i>0;i--){
			if(allChesses[i-1][col]==allChesses[i][col]){
				count++;
			}
			else{
				break;
			}
		}
		return count;
	}
	
	public int checkRowLeftIsFive(int row,int col){//统计列左的棋子数
		int count=1;
		for(int i=col;i>0;i--){
			if(allChesses[row][i-1]==allChesses[row][i]){
				count++;
			}
			else{
				break;
			}
		}
		return count;
	}
	public int checkRowRightIsFive(int row,int col){//统计行右的棋子数
		int count=1;
		for(int i=col;i<allChesses.length-1;i++){
			if(allChesses[row][i]==allChesses[row][i+1]){
				count++;
			}
			else{
				break;
			}
		}
		return count;
	}
	public void setWindowPercent(int percent){//设置窗囗所屏幕的百分比
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int)(percent/100.0*screenSize.width),(int)(percent/100.0*screenSize.height));
	}
	
	public void setWindowCenter(){//设置窗囗居中
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		if(this.getWidth()>screenSize.width || this.getHeight()>screenSize.height){//如果窗囗的宽度和高度大于屏幕的宽度和高度，那么就将位置设置为0,0
			this.setLocation(0,0);
		}
		else{
			this.setLocation((screenSize.width-this.getWidth())/2,(screenSize.height-this.getHeight())/2);
		}
	}
	
	class MyCanvas extends JPanel{
		public void paint(Graphics g) {
			BufferedImage bImage=new BufferedImage(700,550,BufferedImage.TYPE_INT_RGB);//创建BufferedImage对象
			Graphics2D g2=bImage.createGraphics();//创建一个Graphcs2D对象，并将其绘制到BufferedImage中
			graphics2d=g2;//将g2对象赋值给graphics2d变量保存
			g2.drawImage(imageBG, 0, 0,this.getWidth(),this.getHeight(), this);// 绘制背景图片
			g2.drawImage(imageMenu,6 , 242, 140, 52, this);//绘制菜单图片
			g2.drawImage(imageW,26 , 42, imageW.getWidth(this),imageW.getHeight(this), this);//绘制绿豆蛙不笑时的图片
			g2.drawImage(imageW,26 , 307, imageW.getWidth(this),imageW.getHeight(this), this);
			if(whoSmile==Color.black){//如果whoSmile的值是black就表示黑方赢，在黑方位置绘制笑脸
				g2.drawImage(imageX,26 , 307, imageX.getWidth(this),imageX.getHeight(this), this);
			}
			else if(whoSmile==Color.white){//如果是white表示白方赢，在白方位置绘制笑脸
				g2.drawImage(imageX,26 , 42, imageX.getWidth(this),imageX.getHeight(this), this);
			}
			g2.setColor(Color.white);//将绘图对象设置成白色
			int minute=playerTime/60;//根据玩家的时间得到分钟
			int second=playerTime%60;//根据玩家的时间得到秒钟
			String minuteStr=minute<10?"0"+minute:minute+"";//如果分钟数是一位加一个0变成两位，否则不加0。秒钟也这样处理
			String secondStr=second<10?"0"+second:second+"";
			g2.drawString(minuteStr+":"+secondStr,77,209);//在BufferedImage中绘制时间字符串
			minute=robotTime/60;//根据机器人的时间得到分钟
			second=robotTime%60;//根据机器人的时间得到秒钟
			minuteStr=minute<10?"0"+minute:minute+"";//如果分钟数是一位加一个0变成两位，否则不加0。秒钟也这样处理
			secondStr=second<10?"0"+second:second+"";
			g2.drawString(minuteStr+":"+secondStr,77,477);//在BufferedImage中绘制时间字符串
			for(int i=0;i<allChesses.length;i++){//allChess二维数组记录了整个棋盘信息,这里就是将二维数据中的信息反映出来
				for(int j=0;j<allChesses.length;j++){
					if(allChesses[i][j]!=null){//如果某一个元素不为空就表示该位置有棋子
						if(allChesses[i][j]==Color.black){//如果元素值是black就在相应的位置绘制黑色的棋子
							g2.drawImage(imageBlack, (int)((179+j*34.8)+2), (int)((30+i*33.4)+2),31,31, this);
						}                   //列与列之间的距离是34.8,j*34.8就表示有多少个34.8得到长度，在加上179得到该棋子的x坐标 
						else{//否则是白色在相应位置画上棋子
							g2.drawImage(imageWhite, (int)((179+j*34.8)+2), (int)((30+i*33.4)+2),31,31, this);
						}
					}
				}
			}
			if(stack.getStackTop()!=null){//获得栈顶元素,栈顶元素记录了最后一步棋的位置和颜色信息，为了让玩家知道最后一步棋的位置，所以就在最后一步棋画一个矩形。
				g2.drawImage(imageRim, (int)((179+stack.getStackTop().getCol()*34.8)+2), (int)((30+stack.getStackTop().getRow()*33.4)+2),31,31, this);
			}
			g.drawImage(bImage, 0, 0, this);//将BufferedImage画到JPanel中
		}
	}
	
	public static void main(String[] args){
		new PlayChess();//创建一个PlayChess对象，游戏开始
	}
}


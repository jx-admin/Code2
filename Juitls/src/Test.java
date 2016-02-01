
public class Test {
	public static void main(String[] args) {
//		String str=null;
//		Object obj;
//		System.out.println(str==null||str.trim().length()==0?"1":"ha");
//		System.out.println("str=null == obj=null is "+(str==null));
//		 System.out.printf("%d=%x=%s=\n",((1)),((1)),Integer.toBinaryString((1)));
//		 System.out.printf("%d=%x=%s=\n",((-1)),((-1)),Integer.toBinaryString((-1)));
//		 System.out.printf("%d=%x=%s=\n",((2)),((2)),Integer.toBinaryString((2)));
//		 System.out.printf("%d=%x=%s=\n",((-2)),((-2)),Integer.toBinaryString((-2)));
//		 System.out.printf("%d=%x\n",((byte)0xfe),((byte)0xfe));
//		 
//		 System.out.println("∞µœ„".compareTo("¥∫≈Ø")+ " "+CharacterParser.getInstance().getSelling("∞µœ„")+" "+CharacterParser.getInstance().getSelling("¥∫≈Ø"));
//		 
//		 System.out.println("a".compareTo("b"));
//		 
//		 String colorStr="ffffffff";
//		 System.out.println(colorStr+"= "+((int)Long.parseLong(colorStr,16)));
		
		show();
		show("sss","sss1");
		show(new String[]{"ddd","ddd1"});
		
		Object a=null;
		Object b=null;
		show("a==b",(null==null)+"");
	}
	
	public static void show(String ...strs){
		for(String str:strs){
			System.out.println(str);
		}
	}
}

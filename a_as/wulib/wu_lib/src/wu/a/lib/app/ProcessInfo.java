package wu.a.lib.app;

//Model��
public class ProcessInfo {

	private int pid; // ����id  Android�涨android.system.uid=1000
	private int uid; // �������ڵ��û�id �����ý�������˭������ root/��ͨ�û���
	private int memSize; // ����ռ�õ��ڴ��С,��λΪkb
	private String processName; // ������
	
	//Ϊͼ���㣬ֱ������Ϊ������
	public String pkgnameList[] ;   //�����ڸý����������Ӧ�ó���İ���, packagname ;
	
	
	public ProcessInfo(){}
	
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getMemSize() {
		return memSize;
	}

	public void setMemSize(int memSize) {
		this.memSize = memSize;
	}

	public String getProcessName() {
		return processName;
	}

	public void setPocessName(String processName) {
		this.processName = processName;
	}

}

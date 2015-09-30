android\packages\apps\Settings\src\com\android\settings\vpn\VpnSettings.java 
VPN的读和写都是通过Setting来读写Profile文件实现的。
它的权限只属于Settings，如果其它APP想跳过Settings来访问相应的Profile文件，
	static void saveProfileToStorage(VpnProfile p) throws IOException {
        File f = new File(getProfileDir(p));
        if (!f.exists()) f.mkdirs();
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
                new File(f, PROFILE_OBJ_FILE)));
        oos.writeObject(p);
        oos.close();
    } 
	// 上面的代码说明，VPNSettings只是将一个Profile直接写入了文件。它拥有对Profile的操作的权限。
	
在第335行，添加如下代码

			String profileName = p.getName() ; 
			// 如果是AEMM的设置信息，则将设置菜单置灰。
			if( !profileName.contains("aemm")) // by wh 
			{
				menu.add(0, CONTEXT_MENU_EDIT_ID, 0, R.string.vpn_menu_edit)
						.setEnabled(isNotConnect);
				menu.add(0, CONTEXT_MENU_DELETE_ID, 0, R.string.vpn_menu_delete)
						.setEnabled(isNotConnect);
			}
			else
			{
				menu.add(0, CONTEXT_MENU_EDIT_ID, 0, R.string.vpn_menu_edit)
						.setEnabled(false);
				menu.add(0, CONTEXT_MENU_DELETE_ID, 0, R.string.vpn_menu_delete)
						.setEnabled(false);
			}
			
//startVpnEditor VPN的编辑界面是在Setting中启动的，
VpnSettings.java -> startVpnEditor 
// 它的调用流程如下：
startVpnEditor->profileChanged->writeToParcel->writeString(String val ) ;  // native 方法
上面的调用过程所对应的文件分别为：
VpnSettings.java、VpnEditor.java、VpnProfile.java、Parcel.java
推荐在Settings层调用。

deleteProfile 
		\packages\apps\Settings\src\com\android\settings\vpn\Util.java
		Util.deleteFile(String fName) ; 
	// VPN配置信息的删除是直接对文件进行的操作。其调用顺序如下：
	removeProfileFromStorage(VPN Settings)->Util.DeleteFile(String fName) 
PS:
    禁用修改和删除菜单 。 

解决思路： 
VPN的设置没有专门的服务来进行管理。
它的设置信息是从数据文件中读出的，如果对读取进行限制，VPN的连接将会受影响。
frameworks/base/vpn/java/android/net/vpn/VpnManager.java // 对VPN进行操作
VpnManager.java里面是对VPN进行配置的，比如说：
启动、停止VPN服务，添加VPN设置。不能达到所需要的目标。
所以，只能在上层菜单中进行处理，如果是AEMM配置项，则将编辑与删除选项禁用。
onCreate函数如下：
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.vpn_settings);

        // restore VpnProfile list and construct VpnPreference map
		// 加载VPN设置列表，并进行数据填充
        mVpnListContainer = (PreferenceCategory) findPreference(PREF_VPN_LIST);

        // 添加VPN选项(add vpn)
        mAddVpn = (PreferenceScreen) findPreference(PREF_ADD_VPN);
		// 设置（addvpn）的事件监听器
        mAddVpn.setOnPreferenceClickListener(
                new OnPreferenceClickListener() {
                    public boolean onPreferenceClick(Preference preference) {
                        startVpnTypeSelection();
                        return true;
                    }
                });

        // for long-press gesture on a profile preference
		// 为VPN选项添加长按事件监听器
		// 在长按事件里面添加响应，如果是AEMM组件，则禁用编辑与删除选项。
        registerForContextMenu(getListView());

        // listen to vpn connectivity event
		// 监听VPN连接事件。
        mVpnManager.registerConnectivityReceiver(mConnectivityReceiver);

        retrieveVpnListFromStorage();
        checkVpnConnectionStatusInBackground();
    }
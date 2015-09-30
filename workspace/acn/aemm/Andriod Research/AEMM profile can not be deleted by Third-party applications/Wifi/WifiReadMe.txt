android/frameworks/base/wifi/java/android/net/wifi/WifiManager.java

修改方法如下：
line 424 

	private boolean checkUpdateSSID(WifiConfiguration config , String strKey) // 判断是否AEMM的Profile
    {
    	if(config.SSID.contains(strKey))
    	{
    		return true ; 
    	}
    	return false ; 
    }
line 451
		if( checkUpdateSSID(config,"aemm") ) // 如果是AEMM的Profile，直接返回失败
        	return false ;
line 472 
	private boolean checkSSID(int netId , String strKey) // 判断是否AEMM的Profile
    {
        List<WifiConfiguration> configs = this.getConfiguredNetworks();
        if (configs != null) {
            for (WifiConfiguration config : configs) 
            {
            	if( netId == config.networkId )
            	{
            		if(config.SSID.contains(strKey))
            		{
            			return true ; 
            		}
            	}
            }
        }
        return false ; 
    }
line 500
			if( !checkSSID(netId , "aemm") )
        		return mService.removeNetwork(netId);
        	else
        		return false ;  

解决思路： 
        Wifi中涉及到调用的地方在WifiSettings.java(forget方法调用的WifiManager.java中的removeNetwork()方法与updateNetwork()方法)
        WifiManager已经属于系统级的调用。整个调用流程为：
        WifiSettings->WifiManager->WifiService->WifiStateTracker->WifiNative->android_net_wifi_Wifi.cpp
        在WifiManager层进行屏蔽已经可以达到目标，当然，再下层的屏蔽也可以，但是处理起来就不如WifiManager层的处理灵活。

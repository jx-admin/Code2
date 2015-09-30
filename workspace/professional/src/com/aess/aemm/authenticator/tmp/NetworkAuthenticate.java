package com.aess.aemm.authenticator.tmp;
//package com.aess.aemm.authenticator;
//
//import java.io.InputStream;
//import com.aess.aemm.apkmag.ApkInfoMag;
//import com.aess.aemm.commonutils.CommUtils;
//import com.aess.aemm.gps.AEMMLocation;
//import com.aess.aemm.networkutils.HttpHelp;
//import com.aess.aemm.networkutils.SystemConfig;
//import com.aess.aemm.protocol.UpdateResult;
//import com.aess.aemm.protocol.UpdateXmlParser;
//import com.aess.aemm.protocol.XmlBuilder;
//import com.aess.aemm.push.PushService;
//import com.aess.aemm.update.Update;
//import android.content.Context;
//import android.content.Intent;
//
//class NetworkAuthenticate {
//	public static final String DEBUGFILENAME = "authenticate.xml";
//
//	public static int authenticate(String username, String password,
//			Context context) {
//
//		String url = SystemConfig.getInstance(context).getUpdateURL();
//
//		if (null == url) {
//			return -1;
//		}
//
//		XmlBuilder.Info arg = XmlBuilder.Info.getInsByUser(username, password);
//
//		String loginData = XmlBuilder.buildInfo(context, XmlBuilder.LOGIN, arg);
//		if (null == loginData) {
//			return -2;
//		}
//		
//		InputStream protocolStream = HttpHelp.aemmHttpPost(context, url,
//				loginData, DEBUGFILENAME);
//
//		UpdateXmlParser parser = new UpdateXmlParser(context);
//		UpdateResult result = parser.parseUpdateXml(protocolStream);
//
//		if (null != result && null != result.mSessionId) {
//			CommUtils.setSessionId(context, result.mSessionId);
//			result.saveCycle(context);
//
//			context.startService(new Intent(context, PushService.class));
//
//			ApkInfoMag.sendIntentForApk(context);
//			AEMMLocation.sendIntentLoc(context);
//
//			Update.responseCommandId(context, result);
//			return 0;
//		}
//
//		try {
//			return Integer.parseInt(result.mErrorMsg);
//		} catch (Exception e) {
//		}
//		return -1;
//	}
//}
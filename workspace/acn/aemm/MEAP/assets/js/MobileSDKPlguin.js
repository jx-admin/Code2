/*
 * Javascript
 * Mobile SDK for Android
 */

/*操作队列*/
var actionQueue = new Array();

/*操作队列项*/
function ActionItem(){ 
	this.commandID = "";
	this.actionNumber = "";
	this.callbackSuccess = "";
	this.callbackFailed = "";
	this.param =  "";
}

/*将用户的请求发送给native*/
function sdkBridge(actionNumber, callbackSuccess, callbackFailed, param){
	var ai = new ActionItem();
	ai.commandID = Math.floor(Math.random() * 10000000000).toString();
	ai.actionNumber = actionNumber;
	ai.callbackSuccess = callbackSuccess;
	ai.callbackFailed = callbackFailed;
	param ? ai.param = param : ai.param = "";

	/*将此次action添加到操作队列中*/
	appendActionQueue(ai);
	setTimeout(function(){MobileSDKKernel.execNative(ai.commandID, actionNumber, param);}, 0);
}

/*定义回调函数*/
function callbackHandler(commandID, code, response){
	var ai = findActionQueue(commandID);

	if(ai == ""){
		alert("根据指令无法找到匹配的纪录");
		return;
	}

	if(code == 0){
		/*success*/
		ai.callbackSuccess(response);
	}else{
		/*failed*/
		ai.callbackFailed();
	}
}
/*
function callbackHandler(p){
	var param = JSON.parse(p);
	//console.log(JSON.stringify(param));

	var ai = findActionQueue(param.commandID);
	//console.log(JSON.stringify(ai));

	if(ai == ""){
		alert("根据指令无法找到匹配的纪录");
		return;
	}
	
	// param的结构 {"code":"true","desc":"","commandID":"","response":"{"p1":"","p2":"",…}"} 
	if(param.code){
		ai.callbackSuccess(param.response);
	}else{
		ai.callbackFailed();
	}
}
*/

function appendActionQueue(item){
	actionQueue.push(item);
}

function findActionQueue(commandID){
	var ai = "";

	for(var i = 0; i < actionQueue.length; i++){
		if(actionQueue[i].commandID == commandID){
			ai = actionQueue[i];
			return ai;
		}else{
			continue;
		}
	}

	return ai;
}

function removeActionQueue(commandID){
	var ai = "";
	
	for(var i = 0; i < actionQueue.length; i++){
		if(actionQueue[i].commnadID == commandID){
			var temp = actionQueue.splice(i, 1);
			ai = temp[0];
			return ai;
		}else{
			continue;
		}
	}

	return ai;
}

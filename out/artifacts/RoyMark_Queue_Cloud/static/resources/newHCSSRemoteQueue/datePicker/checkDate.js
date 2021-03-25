//第一个日期控件选择后的数据的当月最后一天
var lastDay ='';
//第二个日期控件的值，第一个日期控件onblur时用作临时变量
var nextDateVal='';

//第一个日期控件选择完成后的事件
function pickedFun(preDateId,nextDateId){
	var nextDate=document.getElementById(nextDateId);
	var preDateVal=document.getElementById(preDateId).value;

	if(checkDateVal(nextDateVal,preDateVal)){
	//第一个控件选择后，如果第二个控件的数据不合法
		nextDate.value=nextDateVal;
	}else{
	//第一个控件选择后，如果第二个控件的数据为空
		nextDate.focus();
	} 
}

//第一个日期控件的onblur事件，obj为自己，nextDateId为第二个日期控件的id
function onblurevent(obj,nextDateId){
	var nextDate=document.getElementById(nextDateId);
	nextDateVal=nextDate.value;
	if(!checkDateVal(nextDateVal,obj.value)){
		nextDate.value='';
	}
}

//第二个日期控件的focus事件
function nextOnfocus(preDateId){
	var preDateVal=document.getElementById(preDateId).value;
	if(preDateVal!=''){
		var dateStrArr=preDateVal.split("/");
		var year=dateStrArr[0];
 		var month=dateStrArr[1];
		var tempDate = new Date(year,month,0);
		lastDay=year+"/"+month+"/"+tempDate.getDate() ;
	}else{
		lastDay='';
	}
}

//校验date1是否大于等于date2且小于等于date2所在日期的当月最后一天
//date1是被校验的date，date2是参照date
function checkDateVal(date1,date2){
	var dateStrArr=date2.split("/");
	var year=dateStrArr[0];
 	var month=dateStrArr[1];

	var tempDate = new Date(year,month,0);

	//date2所在日期的当月最后一天
	lastDay=year+"/"+month+"/"+tempDate.getDate() ;
	return (date1<=lastDay&&date1>=date2);
}
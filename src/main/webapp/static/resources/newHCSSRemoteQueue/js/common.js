$(function() { 
	//时间格式转换
	Date.prototype.Format = function (fmt) {
	    var o = {
	        "M+": this.getMonth() + 1, //月份
	        "d+": this.getDate(), //日
	        "h+": this.getHours(), //小时
	        "m+": this.getMinutes(), //分
	        "s+": this.getSeconds(), //秒
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
	        "S": this.getMilliseconds() //毫秒
	    };
	    if (/(y+)/.test(fmt))
	        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o){
	        if (new RegExp("(" + k + ")").test(fmt)) {
	            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	        }
	    }
	    return fmt;
	}

	//获取对应位数的随机值
	window.getNum = function(size){  
	    var chars = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'];  
	    var nums="";  
	    for(var i=0;i<size;i++){
	        var id = parseInt(Math.random()*61);  
	        nums+=chars[id];  
	    }  
	    return nums;
	}

	//list 转 map
	window.getMap = function(aData){
	    var map = {};
	    for (var i = 0; i < aData.length; i++) {
	        var item = aData[i];
	        if (!map[item.name]) {
	            map[item.name] = item.value;
	        }
	    }
	    return map;
	}
   
});
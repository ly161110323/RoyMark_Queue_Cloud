
 $(document).ajaxError(function(event,jqxhr,settings,exception){
    if(jqxhr.status==408){   
	var contextPath=getContextPath();	
//	alert("操作超时,请重新登录！");
		layer.alert('操作超时,请重新登录!', {
  	closeBtn: 1    // 是否显示关闭按钮
    ,anim: 1 //动画类型
    ,btn: ['确定'] //按钮
    ,icon: 0    // icon
    ,yes:function(){
         top.location =contextPath+ "/manager_login.jsp";
    }
    });
    }
  });
 function getContextPath() {
           var contextPath = document.location.pathname;
           var index = contextPath.substr(1).indexOf("/");
           contextPath = contextPath.substr(0, index + 1);
           delete index;
           return contextPath;
       }

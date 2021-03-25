jQuery.browser = {};
(function () {
    jQuery.browser.msie = false;
    jQuery.browser.version = 0;
    if (navigator.userAgent.match(/MSIE ([0-9]+)\./)) {
        jQuery.browser.msie = true;
        jQuery.browser.version = RegExp.$1;
    }
})();



var zTreeObj;
var setting ={
    callback: {
        onClick: onClick
    },
    view: {
        showLine: true,
        showTitle: true,
        selectedMulti: false,
        expandSpeed: "fast"
    },
    data: {
        key: {
            name: "areaName" //对应服务器对象的属性
        },
        simpleData: {
            enable: true,
            idKey: "areaLs", //对应服务器对象的属性
            pIdKey: "parentAreaLs" //对应服务器对象的属性
        }
    }
};

$(function () {

    var rootPath=getWebRootPath();
    var serverUrl=rootPath+"/webaccount/getAllTree";
    // console.log("rootPath:"+rootPath);
    // console.log("serverUrl:"+serverUrl);
    getData(serverUrl);
});

function getData(url) {
    var getTimestamp=new Date().getTime();
    if(url.indexOf("?")>-1){
        url=url+"&timestamp="+getTimestamp
    }else{
        url=url+"?timestamp="+getTimestamp
    }
    var rootPath=getWebRootPath();
    var  imgUrl=rootPath+"/resources/images/indicator2.gif";
    $.blockUI({
        message: '<div><img src="'+imgUrl+'" style="margin-right:5px;"  />获取数据中,请稍候...</div>',
        css: {
            padding:'25px'
        }
    });
    $.ajax({
        type: 'GET',
        url: url,
        cache: false,
        async: true,
        dataType: 'json',
        success: function (resule) {
            $.unblockUI();
            var resultList = resule.returnObject;//树节点数组
            zTreeObj = $.fn.zTree.init($("#indextree"), setting, resultList);
            zTreeObj.expandAll(true);
        }
    });

}

function onClick(e, treeId, treeNode) {
	if (treeNode ) {
		var areaLs=treeNode["areaLs"];
        var areaName=treeNode["areaName"];
		//返回值给父页面
	    parent.$('#Area_Ls').val(areaLs);
	    parent.$('#Area_Name').val(areaName);
	    var index = parent.layer.getFrameIndex(window.name);  
	    parent.layer.close(index);//关闭当前页  
    }
}
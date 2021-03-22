jQuery.browser = {};
(function () {
    jQuery.browser.msie = false;
    jQuery.browser.version = 0;
    if (navigator.userAgent.match(/MSIE ([0-9]+)\./)) {
        jQuery.browser.msie = true;
        jQuery.browser.version = RegExp.$1;
    }
})();

var ts;

$(function () {

    var rootPath=getWebRootPath();
    var serverUrl=rootPath+"/QueueArea/gettreenodes";
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
    $.ajax({
        type: 'GET',
        url: url,
        cache: false,
        async: true,
        dataType: 'json',
        success: function (resule) {
            var resultList = resule.returnObject;//树节点数组

            ts=$("#parent_area_name").treeSelect(resultList,"parent_area_ls"); //最后加入一个隐藏域参数
        }
    });

}
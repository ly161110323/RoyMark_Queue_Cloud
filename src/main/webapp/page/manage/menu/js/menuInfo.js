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

$(document).ready(function() {
    //为选择按钮绑定点击事件
    // console.log("菜单管理!");
    $(document).on('click', '#btnChooseIcon', function() {
        $("#iconFile").click();
    });
    //为文件组合框绑定值改变事件
    $(document).on('change', '#iconFile', function() {
        var arrs = $(this).val().split('\\');
        var filename = arrs[arrs.length - 1];
        $("#Menu_ImagePath").val(filename);
    });
    
    var rootPath=getWebRootPath();
    var serverUrl=rootPath+"/QueueMenuinfo/getUserTree";
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
            ts=$("#Area_Name").treeSelect(resultList,"Area_Ls"); //最后加入一个隐藏域参数
        }
    });

}
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
    var serverUrl=rootPath+"/QueueArea/indextreenodes";
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

            zTreeObj = $.fn.zTree.init($("#indextree"), setting, resultList);
            zTreeObj.expandAll(true);
        }
    });

}

function onClick(e, treeId, treeNode) {

    var rootPath=getWebRootPath();
    if (treeNode ) {


        // console.log("treeNode:"+JSON.stringify(treeNode));
        // treeNode["areaLs"];
        layer.confirm('确定要切换大厅吗？', {
            btn: ['确定','取消']
        }, function(index){
            // console.log("treeNode:"+JSON.stringify(treeNode));
            var areaLs=treeNode["areaLs"];
            var areaName=treeNode["areaName"];
            if(defaultArea)
            {

                if(defaultArea==areaLs)
                {
                    layer.alert("当前大厅就是"+areaName+",不需要切换");
                    return false;
                }
            }

            var changeAreaUrl=rootPath+"/QueueArea/changearea?areaLs="+areaLs;
            $.ajax({
                type:"GET",
                url:changeAreaUrl,
                //返回数据的格式
                datatype: "json",
                success:function(data){
                    console.log("data:"+data);
                    if(data=='success')
                    {
                        // var loginUrl=rootPath+"/page/manage/login.jsp";
                        //
                       parent.location.reload(true);
                    }
                    else {
                        console.log("切换大厅错误");
                    }
                },
                error: function(){
                    console.log("error");
                }
            });
            layer.close(index);

        }, function(){

        });

    }
}
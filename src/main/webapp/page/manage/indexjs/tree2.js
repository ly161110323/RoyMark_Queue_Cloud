var zTreeObj;
var curMenu = null, zTree_Menu = null;
var setting ={

    view: {
        showLine: false,
        showIcon: false,
        selectedMulti: false,
        dblClickExpand: false,
        addDiyDom: addDiyDom
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
    },
    callback: {
        beforeClick: beforeClick
    }
};
function addDiyDom(treeId, treeNode) {
    var spaceWidth = 5;
    var switchObj = $("#" + treeNode.tId + "_switch"),
        icoObj = $("#" + treeNode.tId + "_ico");
    switchObj.remove();
    icoObj.before(switchObj);

    if (treeNode.level > 1) {
        var spaceStr = "<span style='display: inline-block;width:" + (spaceWidth * treeNode.level)+ "px'></span>";
        switchObj.before(spaceStr);
    }
}

function beforeClick(treeId, treeNode) {
    if (treeNode.level == 0 ) {
        var zTree = $.fn.zTree.getZTreeObj("indextree");
        zTree.expandNode(treeNode);
        return false;
    }
    return true;
}
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
            var treeObj = $("#indextree");
            $.fn.zTree.init(treeObj, setting, resultList);
            zTree_Menu = $.fn.zTree.getZTreeObj("indextree");
            curMenu = zTree_Menu.getNodes()[0].children[0].children[0];
            zTree_Menu.selectNode(curMenu);
            // zTreeObj.expandAll(true);
            // console.log("已展开")
            treeObj.hover(function () {
                if (!treeObj.hasClass("showIcon")) {
                    treeObj.addClass("showIcon");
                }
            }, function() {
                treeObj.removeClass("showIcon");
            });
        }

    });

}

function onClick(e, treeId, treeNode) {
    if (treeNode ) {
        console.log("treeNode:"+JSON.stringify(treeNode));
        // ts.$input.val(treeNode["areaName"]);
    }
}
//弹窗方法
function openwindow(url,name,iWidth,iHeight)
{
    var url;                            //转向网页的地址;
    var name;                           //网页名称，可为空;
    var iWidth;                         //弹出窗口的宽度;
    var iHeight;                        //弹出窗口的高度;
    //window.screen.height获得屏幕的高，window.screen.width获得屏幕的宽
    var iTop = (window.screen.height-30-iHeight)/2;       //获得窗口的垂直位置;
    var iLeft = (window.screen.width-10-iWidth)/2;        //获得窗口的水平位置;
    layer.open({
        type: 2,
        title: name,
        shadeClose: true,
        shade: 0,
        area: [iWidth+'px', iHeight+'px'],
        content: url,
        end: function () {
            location.reload();
        }
    });
}
//终端操作弹窗
function openwindow2(url,name,iWidth,iHeight,curParams,curTitle)
{
    var url;                            //转向网页的地址;
    var name;                           //网页名称，可为空;
    var iWidth;                         //弹出窗口的宽度;
    var iHeight;                        //弹出窗口的高度;
    //window.screen.height获得屏幕的高，window.screen.width获得屏幕的宽
    var iTop = (window.screen.height-30-iHeight)/2;       //获得窗口的垂直位置;
    var iLeft = (window.screen.width-10-iWidth)/2;        //获得窗口的水平位置;
    layer.open({
        type: 2,
        title: name,
        shadeClose: true,
        shade: 0,
        area: [iWidth+'px', iHeight+'px'],
        content: url,
        success: function(layero){
            var myIframe = window[layero.find('iframe')[0]['name']];
            var fnc = myIframe.loadPageParams(curParams,curTitle); //aaa()为子页面的方法
        }
    });
}
//弹窗被关闭后不刷新父页面
function openwindowNoRefresh(url,name,iWidth,iHeight)
{
    var url;                            //转向网页的地址;
    var name;                           //网页名称，可为空;
    var iWidth;                         //弹出窗口的宽度;
    var iHeight;                        //弹出窗口的高度;
    //window.screen.height获得屏幕的高，window.screen.width获得屏幕的宽
    var iTop = (window.screen.height-30-iHeight)/2;       //获得窗口的垂直位置;
    var iLeft = (window.screen.width-10-iWidth)/2;        //获得窗口的水平位置;
    layer.open({
        type: 2,
        title: name,
        shadeClose: true,
        shade: 0.8,
        area: [iWidth+'px', iHeight+'px'],
        content: url

    });
}
//js中获取上下文根路径的方法
function getWebRootPath() {
    var webroot=document.location.href;
    webroot=webroot.substring(webroot.indexOf('//')+2,webroot.length);
    webroot=webroot.substring(webroot.indexOf('/')+1,webroot.length);
    webroot=webroot.substring(0,webroot.indexOf('/'));
    var rootpath="/"+webroot;
    return rootpath;
}

function isInteger(obj) {
    return obj%1 === 0
}
/**
  * 判断是否为手机号
  * @param  {String|Number}  str
  * @return {Boolean} 
  */

function isPhoneNum(str) {
    return /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/.test(str)
}

/**
  * 判断是否为邮箱地址
  * @param  {String}  str
  * @return {Boolean} 
  */

function isEmail(str) {
    return /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/.test(str);
}

/**
 * 判断是否为正确的密码格式
 * @param  {String}  str
 * @return {Boolean} 
 */

function ispassWord(str) {
   return /^[0-9a-zA-Z]*$/g.test(str);
}

//localdatetime类型数据页面显示格式化
 function dateFormatter (value){

    var arr = value;
    if(arr==null || arr==""){
        return "-";
    }else{
        for(var i = 0; i < arr.length; i++){

            if(arr[i].toString().length == 1){
                arr[i] = "0" + arr[i];
            }
        }
        if(arr.length == 5){
            var getFormatTime = arr[0] + "-" + arr[1] + "-" + arr[2] + "\t" + arr[3] + ":" + arr[4] + ":" + "00";
        }else{
            var getFormatTime = arr[0] + "-" + arr[1] + "-" + arr[2] + "\t" + arr[3] + ":" + arr[4] + ":" + arr[5];
        }

        return getFormatTime;
    }

}

function getFormData($form) {
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function (n, i) {
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}
 
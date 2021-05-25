function openwindow(url,name,iWidth,iHeight)
{
    var url;
    var name;
    var iWidth;
    var iHeight;

    var iTop = (window.screen.height-30-iHeight)/2;
    var iLeft = (window.screen.width-10-iWidth)/2;
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
function openwindow2(url,name,iWidth,iHeight,curParams,curTitle)
{
    var url;
    var name;
    var iWidth;
    var iHeight;

    var iTop = (window.screen.height-30-iHeight)/2;
    var iLeft = (window.screen.width-10-iWidth)/2;
    layer.open({
        type: 2,
        title: name,
        shadeClose: true,
        shade: 0,
        area: [iWidth+'px', iHeight+'px'],
        content: url,
        success: function(layero){
            var myIframe = window[layero.find('iframe')[0]['name']];
            var fnc = myIframe.loadPageParams(curParams,curTitle);
        }
    });
}

function openwindowNoRefresh(url,name,iWidth,iHeight)
{
    var url;
    var name;
    var iWidth;
    var iHeight;

    var iTop = (window.screen.height-30-iHeight)/2;
    var iLeft = (window.screen.width-10-iWidth)/2;
    layer.open({
        type: 2,
        title: name,
        shadeClose: true,
        shade: 0.8,
        area: [iWidth+'px', iHeight+'px'],
        content: url

    });
}
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

function isPhoneNum(str) {
    return /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/.test(str)
}

function isEmail(str) {
    return /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/.test(str);
}

function ispassWord(str) {
   return /^[0-9a-zA-Z]*$/g.test(str);
}

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

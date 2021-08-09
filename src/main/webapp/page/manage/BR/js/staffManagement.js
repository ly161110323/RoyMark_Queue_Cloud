$(document).ready(function () {

    addClick();
    updateClick();
    contactManagerClick();
    deleteClick();
    searchClick();
    clearClick();
    configClick();

    queryWindowList();
    insertPhoto()
    choosePhoto();
    faceManagerButton();


    flashFaceManagerStatus();
});

function choosePhoto() {
    $(document).on('click', '#btnChooseStaffPhoto', function () {
        //让文件选择组件做一次点击
        $("#staffPhoto").click();
    });
    //为文件组合框绑定值改变事件
    $(document).on('change', '#staffPhoto', function () {
        var arrs = $(this).val().split('\\');
        var filename = arrs[arrs.length - 1];
        $("#staffPhotoFileName").val(filename);
    });
}



function queryWindowList() {

    var rootPath = getWebRootPath();
    var url = rootPath + "/window/queryData"
    var params = {
        "pageNo": 1,
        "pageSize": -1
    }

// //获取问卷调查
    $.ajax({
        type: 'POST',
        url: url,
        cache: false,
        async: true,
        dataType: 'json',
        data: params,
        success: function (data) {
// 调用成功时对返回的值进行解析
            var list = data.pageList.records;
//若未出错，则获取信息设置到控件中
            var str = "";
            for (var i = 0; i < list.length; i++) {
                str += "<option value='" + list[i].windowHiddenId + "'>" + list[i].windowId + "</option>";
            }

            // $("#formCaseAreaLs").empty();
            // $("#formCaseAreaLs").append(str);
            $("#windowId").empty();
            $("#windowId").append("<option value=''>请选择绑定窗口ID</option>");
            $("#windowId").append(str);
            $("#selectCommitWindowId").empty();
            $("#selectCommitWindowId").append("<option value=''>请选择绑定窗口ID</option>");
            $("#selectCommitWindowId").append(str);
        }
    });
}





function trClick() {

    //为表格行绑定点击事件
    $('#itemResultTable tbody').on('click', 'tr', function () {

        $("#itemResultTable tr:even").css({
            "background": "#f9f9f9",
            "color": "#676a6c"
        });
        $("#itemResultTable tr:odd").css({
            "background": "white",
            "color": "#676a6c"
        });
        $(this).css({
            "background": "rgb(255, 128, 64)",
            "color": "white"
        });
        dataId = $(this).find("td:eq(0) input[type='checkbox']").val();
        imgPath = $(this).find("td:eq(9)").text();
        staffInfo ={
            userName : $(this).find("td:eq(2)").text(),
            userSex : $(this).find("td:eq(3)").text(),
            userId :$(this).find("td:eq(4)").text(),
            windowId :$(this).find("td:eq(8)").text(),
            userDepartment:$(this).find("td:eq(6)").text(),
            userPost:$(this).find("td:eq(7)").text(),
            userPhoto:$(this).find("td:eq(9)").text()

        }
        $("#userName").val($(this).find("td:eq(2)").text());

        $("#userSex").val($(this).find("td:eq(3)").text());
        $("#userId").val($(this).find("td:eq(4)").text());
        $("#windowId").val($(this).find("td:eq(8)").text());
        $("#userDepartment").val($(this).find("td:eq(6)").text());
        $("#userPost").val($(this).find("td:eq(7)").text());



    });

}

function formatDate(date, format) {
    var o = {
        "M+": date.getMonth() + 1, //month
        "d+": date.getDate(),    //day
        "h+": date.getHours(),   //hour
        "m+": date.getMinutes(), //minute
        "s+": date.getSeconds(), //second
        "q+": Math.floor((date.getMonth() + 3) / 3),  //quarter
        "S": date.getMilliseconds() //millisecond
    }
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
            RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}

//表格选择框操作
function checkBoxStyle_Control() {
    $(".lsCheck").prop("checked", false);
    $("#checkall").prop("checked", false);
//表格头部复选框点击事件
    $("#checkall").unbind("#checkall").bind("click", function () {
        if ($(this).is(":checked")) {
            $(".lsCheck").prop("checked", true);
        } else {
            $(".lsCheck").prop("checked", false);
        }
    });

//表格主体复选框点击事件
    $(".lsCheck").unbind(".lsCheck").bind("click", function () {
        var allCheckNum = $(".lsCheck").length;
        var checkedNum = $(".lsCheck:checked").length;
        if (allCheckNum == checkedNum) {
            $("#checkall").prop("checked", true);
        } else if (checkedNum == 0) {
            $("#checkall").prop("checked", false);
        } else if (checkedNum > 0) {
            $("#checkall").prop("checked", true);
        }
    });
}

//页面数据合法性验证
function validateData(isAdd) {
    if ($("#userName").val().trim() == "") {
        layer.alert("人员姓名不能为空！");
        return;
    }
    if ($("#userId").val().trim() == "") {
        layer.alert("人员编号不能为空！");
        return;
    }
    // if ($("#userSex").val().trim() == "") {
    //     layer.alert("人员性别不能为空！");
    //     return;
    // }

    var trs = $("#itemResultTable tr:gt(0)");
    // var chooseName = $("#txtDeptName").val();
    var chooseId = $("#userId").val();
    var isExit = false;

//循环列表判断是否已经存在,放在客户端校验
    trs.each(function (index, element) {
        var objLs = $(element).find("td:eq(0)>input").val();

        if ($(element).find("td:eq(4)").text() == chooseId) {
            if (isAdd) {
                isExit = true;
                layer.alert("人员编号已存在！");
                return false;
            } else {
                if (objLs != dataId) {
                    isExit = true;
                    layer.alert("人员编号已存在！");
                    return false;
                }
            }
        }
    });

    if (isExit) {
        return false;
    }

    return true;
}

//清除数据
function clearData() {
    var $file = $("#staffPhoto");
    $file.after($file.clone().val(""));
    $file.remove();
    $('#staffPhotoFileName').val("")
    $("#userName").val("");

    $("#userSex").val("");
    $("#userId").val("");
    $("#windowId").val("");
    $("#userDepartment").val("");
    $("#userPost").val("");

}

function clearSearch(){
    $("#inputCommitUserName").val("");
    $("#inputCommitUserId").val("");
    $("#inputCommitUserDepartment").val("");
}

function addClick() {
    $(document).on('click', '#addCommit', function () {
        if (!validateData(true)) {
            return;
        }
        var formData = new FormData();
        if($('#staffPhotoFileName').val()!=""){
            formData.append("uploadinfo", $('#staffPhoto')[0].files[0]);
        }
        formData.append("userId", $('#userId').val());
        formData.append("userName", $("#userName").val());
        formData.append("userSex", $("#userSex").val());
        formData.append("userDepartment", $("#userDepartment").val());
        formData.append("userPost", $("#userPost").val());
        formData.append("windowHiddenId", $("#windowId").val());

        var rootPath = getWebRootPath();
        var url = rootPath + "/user/insert";
        $.ajax({
            type: 'POST',
            url: url,
            cache: false,
            processData: false, // 使数据不做处理
            contentType: false, // 不要设置Content-Type请求头
            data: formData,
            success: function (data) {
                console.log(data)
                if (data.result == "error") {
                    layer.alert(data.msg);
                    return;
                }
                if (data.result == "ok") {
                    layer.alert("新增成功！");
                } else if (data.result == "no") {
                    layer.alert(data.msg);
                }
                table.draw(false);
                clearData();
            }
        });
    });
}

function updateClick() {
//为修改绑定点击事件
    $(document).on('click', '#modifyCommit', function () {
        if (dataId == '') {
            layer.alert("请选择要修改的数据！");
            return;
        }
        if (!validateData(false)) {
            return;
        }
        var formData = new FormData();
        formData.append("userHiddenId", dataId);
        // formData.append("uploadinfo", $('#staffPhoto')[0].files[0]);
        formData.append("userId", $('#userId').val());
        formData.append("userName", $("#userName").val());
        formData.append("userSex", $("#userSex").val());
        formData.append("userDepartment", $("#userDepartment").val());
        formData.append("userPost", $("#userPost").val());

        formData.append("windowHiddenId", $("#windowId").val());



        var rootPath = getWebRootPath();
        var url = rootPath + "/user/update";

        $.ajax({
            url: url,
            type: "post",
            datatype: "json",
            processData: false, // 使数据不做处理
            contentType: false, // 不要设置Content-Type请求头
            data: formData,
            success: function (data) {
                if (data.result == "error") {
                    layer.alert("服务器错误！"+data.msg);
                    return;
                }
                if (data.result == "ok") {
                    layer.msg("修改成功！");
                } else if (data.result == "no") {
                    layer.alert("修改失败！"+data.msg);
                }
                table.draw(false);
                clearData();
            }
        });
    });//修改事件处理完毕
}
function flashFaceManagerStatus(){
    let url = getWebRootPath() +'/server/getFaceManagerStatus'
    $.ajax({
        url: url,
        type: "get",
        // datatype: "json",
        // processData: false, // 使数据不做处理
        // contentType: false, // 不要设置Content-Type请求头
        // data: formData,
        success: function (data) {
            if(data.result!='ok'){
                layer.msg('人脸服务器状态检查失败，请检查face_controller_server是否开启！')
            }
            if(data.msg=='off'){

                $('#faceManagerServerStatusLabel').text("人脸管理服务关闭")
            }else if(data.msg=='on') {

                $('#faceManagerServerStatusLabel').text("人脸管理服务开启")
            }else {
                $('#faceManagerServerStatusLabel').text("查询状态失败")
            }
        },error:function (data){

            layer.msg('后台错误！')
        }

    });
}
function faceManagerButton(){
    $('#startFaceManagerButton').click(function (){
        let url = getWebRootPath() +'/server/startFaceManager'
        $.ajax({
            url: url,
            type: "get",
            // datatype: "json",
            // processData: false, // 使数据不做处理
            // contentType: false, // 不要设置Content-Type请求头
            // data: formData,
            success: function (data) {


                if(data.result=="ok"){
                    flashFaceManagerStatus()
                }else {
                    layer.alert(data.msg)
                }
                // $('#faceManagerServerStatusLabel').text("人脸管理服务开启")
            }
        });
    });
    $('#stopFaceManagerButton').click(function (){
        let url = getWebRootPath() +'/server/stopFaceManager'
        $.ajax({
            url: url,
            type: "get",
            // datatype: "json",
            // processData: false, // 使数据不做处理
            // contentType: false, // 不要设置Content-Type请求头
            // data: formData,
            success: function (data) {
                layer.alert(data.msg)
                // $('#faceManagerServerStatusLabel').text("人脸管理服务关闭")
                flashFaceManagerStatus()
            }
        });
    });
}
function showPhotos(){
    var rootPath = getWebRootPath();
    var urls =  staffInfo['userPhoto'];
    // var img = new Image()
    if(dataId==''){
        layer.alert("请选择人员信息！");
        return
    }

    window.dataId = dataId;
    window.imgs = urls;
    console.log(urls)
    layer.open({
        type: 2,
        title: false,
        area: ['820px','560px'],
        // skin: 'layui-layer-nobg', //没有背景色
        shadeClose: true,
        content: getWebRootPath()+'/page/manage/BR/showFacePhoto.jsp',
        cancel:function (){
            table.draw(false);
        }
    });




}
function insertPhoto(){
    $(document).on('click', '#insertPhoto', function () {
        var rootPath = getWebRootPath();
        // var img = new Image()
        if(dataId==''){
            layer.alert("请选择人员信息！");
            return
        }
        compressImg($('#staffPhoto')[0].files[0],function (newFile){
            var formData = new FormData();
            formData.append("userHiddenId", dataId);
            formData.append("uploadinfo", newFile);
            var rootPath = getWebRootPath();
            var url = rootPath + "/user/insertFace";

            $.ajax({
                url: url,
                type: "post",
                datatype: "json",
                processData: false, // 使数据不做处理
                contentType: false, // 不要设置Content-Type请求头
                data: formData,
                success: function (data) {
                    console.log(data.result=="no")
                    if (data.result == "ok") {
                        layer.msg("上传成功！");
                    } else  {
                        console.log(data.msg)
                        layer.alert("上传失败！"+data.msg);
                    }

                }
            });
        });



    });
}
function deleteClick() {
//为删除按钮绑定点击事件
    $(document).on('click', '#deleteCommit', function () {
        var rootPath = getWebRootPath();
        var url = rootPath + "/user/delete";
        var items = new Array();
        var cBox = $("[name=choice]:checked");
        if (cBox.length == 0) {
            layer.alert("请勾选您所要删除的数据！");
            return;
        }
        layer.confirm("您确定要" + "删除" + "这" + cBox.length + "条记录吗？",
            {
                btn: ['确定', '取消']
            },
            function (index) {
                for (var i = 0; i < cBox.length; i++) {
                    items.push(cBox.eq(i).val());
                }
                var data = {"deleteId": items.toString()};

                $.ajax({
                    type: 'POST',
                    url: url,
                    data: data,
                    success: function (data) {

                        if (data.result == "ok") {
                            layer.alert("删除成功！");
                        }else {
                            layer.alert(data.msg);
                        }
                        table.draw(false);
                        clearData();
                    },
                    error: function (data) {
                        layer.alert("错误:"+data.msg);
                    }
                });

            }, function () {

            });
    });
}
function imgSizeCheck(file){//$("#fileUpload")[0].files[0]
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function (e){
        //初始化JavaScript图片对象
        var image = new Image();
        //FileReader获得Base64字符串
        image.src = e.target.result;
        image.onload = function () {
            //获得图片高宽
            var height = this.height;
            var width = this.width;
            // layer.msg("图片高=" + height + "px, 宽=" + width + "px");
            // if(height>720||width>720)
            return true;
        }
    }
}
function compressImg(file,callback) {

    var reader = new FileReader();
    reader.readAsDataURL(file);
    // 缩放图片需要的canvas
    var canvas = document.createElement('canvas');
    var context = canvas.getContext('2d');
    reader.onload=function () {
        var content = this.result; //图片的src，base64格式
        var img = new Image();
        img.src = content;
        img.onload = function (){ //图片加载完毕
            // 图片原始尺寸
            var originWidth = this.width;
            var originHeight = this.height;
            // 最大尺寸限制，可通过国设置宽高来实现图片压缩程度
            var maxWidth = 1000,
                maxHeight = 1000;
            // 目标尺寸
            var targetWidth = originWidth
            var targetHeight = originHeight;

            // 图片尺寸超过限制
            if(originWidth > maxWidth || originHeight > maxHeight) {
                if(originWidth / originHeight > maxWidth / maxHeight) {
                    // 更宽，按照宽度限定尺寸
                    targetWidth = maxWidth;
                    targetHeight = Math.round(maxWidth * (originHeight / originWidth));
                } else {
                    targetHeight = maxHeight;
                    targetWidth = Math.round(maxHeight * (originWidth / originHeight));
                }
            }
            // canvas对图片进行缩放
            canvas.width = targetWidth;
            canvas.height = targetHeight;
            // 清除画布
            context.clearRect(0, 0, targetWidth, targetHeight);
            // 图片压缩
            context.drawImage(img, 0, 0, targetWidth, targetHeight);
            /*第一个参数是创建的img对象；第二个参数是左上角坐标，后面两个是画布区域宽高*/
            //压缩后的图片base64 url
            /*canvas.toDataURL(mimeType, qualityArgument),mimeType 默认值是'image/jpeg';
             * qualityArgument表示导出的图片质量，只要导出为jpg和webp格式的时候此参数才有效果，默认值是0.92*/
            var dataURL = canvas.toDataURL(file.type||"image/*", 0.92);//base64 格式

            // let blob = dataURItoBlob(dataURL,file.name);
            let newFile = dataURItoBlob(dataURL,file.name);

            // let  formData = new FormData();
            // // formData.append(option.fileName,blob);
            // formData.append(option.fileName,newFile,file.name);
            callback(newFile);
            return ;
        }
    };
};
function dataURItoBlob(dataurl,filename){   //dataurl是base64格式
    var arr = dataurl.split(',');
    var mime = arr[0].match(/:(.*?);/)[1];
    var bstr = atob(arr[1]);
    var n = bstr.length;
    var u8arr = new Uint8Array(n);
    while(n--){
        u8arr[n] = bstr.charCodeAt(n);
    }
    //发现ios11不支持new File()
    let newFile = new File([u8arr], filename, {
      type: mime || "image/*"
    });
    return newFile;
    // return new Blob([u8arr], {type:mime || "image/*"});
};
function searchClick() {

    //为查询按钮绑定点击事件
    $(document).on('click', '#queCommit', function () {
        isSearch = "1";
        var inputUserName = $("#inputCommitUserName").val();
        var inputUserId = $("#inputCommitUserId").val();
        var inputUserDepartment = $("#inputCommitUserDepartment").val();

        let params = {};
        if(inputUserName != ""){
            params["userName"] = inputUserName;

        }
        if(inputUserId !=""){
            params["userId"] = inputUserId;

        }
        if(inputUserDepartment !=""){
            params["userDepartment"] = inputUserDepartment;
        }


        searchData =params;

        table.draw(false);
        // clearSearch();
        flashFaceManagerStatus();
    });

}

function clearClick() {
//清除
    $(document).on('click', '#clearData', function () {
        clearData();
    });
}

function configClick() {
//更多配置
    $(document).on('click', '#showPhoto', function () {
        showPhotos()
    });
}

function contactManagerClick(){
    $('#contactManagerButton').click(function (){
        var targetUrl = getWebRootPath() + "/page/manage/BR/alarmContact.jsp";
        layer.open({
            type: 2,
            title: "报警联系人管理",
            shadeClose: true,
            shade: 0.8,
            area: ['1280px', '720px'],
            content: targetUrl,
            cancel:function (index,layero){
                // queryMap();
            }
        });
    });
}




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
});
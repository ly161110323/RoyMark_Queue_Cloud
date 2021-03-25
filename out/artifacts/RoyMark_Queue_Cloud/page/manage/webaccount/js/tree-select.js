(function ($) {

    function TreeSelect() {
        var ts = new Object();
        ts.key = "xxx";
        ts.hiddenKey="yyy";
        ts.$input = null;
        ts.$hidden=null;
        ts.$selDiv = null;
        ts.$selUl = null;
        ts.$zTree = null;

        /**
         * 初始化下拉树
         * @param key
         * @param data
         */
        ts.initialize = function (key, data,hiddenKey) {
            ts.key = key;
            ts.hiddenKey=hiddenKey;
            ts.initInput();
            ts.initTree(data);

            ts.$input.bind("keyup", function () {
                console.log("value changed1!");
                ts.changeInput();
            });
            ts.$input.bind("change", function () {
                console.log("value changed2!");
                ts.changeInput();
            });

            $("body").bind("click", ts.bodyClick);
        };

        /**
         * 初始化输入框
         */
        ts.initInput = function () {

            ts.$input = $("#" + ts.key);
            ts.$hidden= $("#" + ts.hiddenKey);
            var input_w = ts.$input.width();
            var input_h = ts.$input.height();

            ts.$input.parent().css({
                "position": "relative",
                "display": "inline-block",
                "min-width": input_w,
                "min-height": input_h,
                "margin": "auto",
                "vertical-align": "middle"
            });
            ts.$input.css({
                "position": "absolute",
                "margin-left": "2px"
            });

            ts.$input.bind("focus", function () {
                ts.openTree();
            });

        };

        /**
         * 初始化树结构
         * @param data
         */
        ts.initTree = function (data) {
            var window_h = $(window).height();

            var input_x = ts.$input.offset().left;
            var input_y = ts.$input.offset().top;
            var input_w = ts.$input.outerWidth();
            var input_h = ts.$input.outerHeight();
            var div_w = input_w;
            var div_max_h = (window_h - input_y - input_h) * 0.8;

            var html = '<div id="sel_div_' + ts.key + '">' +
                '<ul id="sel_ul_' + ts.key + '" class="ztree"></ul>' +
                '</div>';
            ts.$input.after(html);

            ts.$selDiv = $("#sel_div_" + ts.key);
            ts.$selDiv.offset({
                "left": input_x,
                "top": input_y + input_h + 1//1px的缝隙
            });
            ts.$selDiv.css({
                "position": "absolute",
                "width": div_w,
                "max-width": div_w,
                "max-height": div_max_h,
                "overflow-x": "auto",
                "overflow-y": "auto",
                "background-color": "#F7F7F7",
                "z-index": 2147483647
            });

            ts.$selUl = $("#sel_ul_" + ts.key);
            ts.$selUl.css({
                "margin": 0,
                "padding": 0
            });

            $.fn.zTree.init(ts.$selUl, {
                callback: {
                    onClick: ts.clickTree
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
            }, data);

            ts.$zTree = $.fn.zTree.getZTreeObj("sel_ul_" + ts.key);
        };

        /**
         * 改变输入值
         */
        ts.changeInput = function () {
            var input_val = ts.$input.val();
            input_val = input_val.trim().toLowerCase();
            if ("" == input_val) {
                ts.$zTree.expandAll(false);
                return;
            }

            var findNode = ts.$zTree.getNodesByFilter(function (node) {
                if (node && node["areaName"].toLowerCase().indexOf(input_val) > -1) {
                    ts.$zTree.selectNode(node, false);//单一选中
                    return true;
                }
                return false;
            }, true);//只找第一个

            if (findNode ) {
                var parentNode = findNode.getParentNode();
                if (parentNode) {
                    var expands = new Set();
                    do {//展开符合的节点及其父、祖节点
                        expands.add(parentNode["areaLs"]);
                        ts.$zTree.expandNode(parentNode, true, false, true);
                        parentNode = parentNode.getParentNode();
                    } while (parentNode);

                    var openNodes = ts.$zTree.getNodesByFilter(function (node) {
                        if (node && node.isParent && node.open && !expands.has(node["areaLs"])) {
                            return true;
                        }
                        return false;
                    }, false);//找一群
                    if (openNodes && openNodes.length > 0) {
                        for (var i = 0; i < openNodes.length; i++) {
                            //关闭不符合的其他父节点
                            ts.$zTree.expandNode(openNodes[i], false, true, false);
                        }
                    }
                } else {
                    ts.$zTree.expandAll(false);
                }
            }

        };

        /**
         * 点击树节点
         */
        ts.clickTree = function (event, treeId, treeNode) {
        // && !treeNode.isParent 父节点也可以选中
            if (treeNode ) {
             // console.log("treeNode:"+JSON.stringify(treeNode));
                ts.$input.val(treeNode["areaName"]);
                console.log("treeNodeId:"+treeNode["areaLs"]);
                // console.log(" ts.$hidden:"+JSON.stringify(ts.$hidden));
                ts.$hidden.val(treeNode["areaLs"]);
                ts.closeTree();
            }
        };

        /**
         * 点击输入框和树结构之外的部分
         * @param event
         */
        ts.bodyClick = function (event) {
            var x1 = ts.$input.offset().left;
            var y1 = ts.$input.offset().top;
            var width = ts.$input.outerWidth();
            var height = ts.$input.outerHeight() + ts.$selDiv.outerHeight() + 1;//1px的缝隙
            var x2 = x1 + width;
            var y2 = y1 + height;

            var x = event.clientX;
            var y = event.clientY;
            if (x < x1 || x2 < x || y < y1 || y2 < y) {
                ts.closeTree();
            }
        };

        /**
         * 关闭树结构
         */
        ts.closeTree = function () {
            ts.$selDiv.hide();
        };

        /**
         * 展开数节点
         * @param key
         * @param options
         */
        ts.openTree = function () {
            ts.$selDiv.show();
        };

        /**
         * 树结构位置微调
         */
        ts.treeOffset = function () {
            //TODO
        };

        return ts;
    }

    /**
     * 主调方法
     * @param data
     * @returns {TreeSelect}
     */
    $.fn.treeSelect = function (data,hiddenkey) {
        $.fn.zTree.destroy();//重要，创建前要加销毁的方法，不然在新增后，刷新树时数据不对。add by wfz 2020-01-15
        var key = this.attr("id");

        var ts = new TreeSelect();
        // ts.$zTree.destroy();

        ts.initialize(key, data,hiddenkey);
        ts.$zTree.expandAll(true);

        ts.closeTree();

        return ts;
    }

})(jQuery);


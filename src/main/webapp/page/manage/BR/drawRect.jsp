<%--
  Created by IntelliJ IDEA.
  User: liucl
  Date: 2021/5/7
  Time: 10:39 上午
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/page/common/includewithnewztreestyle.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<c:set var="ctx" value="${pageContext.request.contextPath}"/>--%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <title>禾麦智能大厅管理系统</title>
</head>
<body>

<div width='720px' height='405px'>
    <canvas id="myCanvas" width='720px' height='405px' style="">
        Your browser does not support the HTML5 canvas tag.
    </canvas>
</div>
<div style="height: 40px">
    <button id="up" style="display: none">增大</button><button id="down" style="display: none">减小</button><button id="cancel" style="display: none">撤销</button><button id="clear">清空</button><button id="submit">提交</button>
</div>

<script>
    const c=document.getElementById("myCanvas");
    const ctx=c.getContext("2d");
    const scaleStep=1.05;
    const minWidth=0,minHeight=0,maxWidth=9000,maxHeight=7000;
    const img=document.createElement('img');
    const elementWidth=720,elementHeight=405;
    const minDis = 50
    let startx,//起始x坐标
        starty,//起始y坐标
        flag,//是否点击鼠标的标志
        x,
        y,
        leftDistance,
        topDistance,
        op=0,//op操作类型 0 无操作 1 画矩形框 2 拖动矩形框
        scale=1,
        type=0;
    let layers=[];//图层
    let currentR;//当前点击的矩形框
    let coord=window.parent.windowCoordinates;
    console.log("windowCoordinates:",coord)
    let msg = window.parent.windowId
    ctx.font = '30px "微软雅黑"';
    ctx.fillStyle = "#FFF"
    function loadImg(){

        img.src=window.parent.imgPath;
        img.onload=function(){

            c.style.backgroundImage="url("+img.src+")";
            c.style.backgroundSize='contain';
            if(coord){
                let coords = coord.split(',')
                let x1 = parseFloat(coords[0])*elementWidth
                let y1 = parseFloat(coords[1])*elementHeight
                let x2 = parseFloat(coords[2])*elementWidth
                let y2 = parseFloat(coords[3])*elementHeight

                layers.push(fixPosition({
                    x1:x1,
                    y1:y1,
                    x2:x2,
                    y2:y2,
                    strokeStyle:'#0000ff',
                    type:type
                }))
                reshow()
            }
        }
    }
    loadImg()
    document.querySelector('#up').onclick=function(){
        if(c.width<=maxWidth&&c.height<=maxHeight){
            c.width*=scaleStep;
            c.height*=scaleStep;
            scale=c.height/minHeight;
            ctx.scale(scale,scale)
            c.style.backgroundSize=`${c.width}px ${c.height}px`;
            reshow()
        }
    }
    document.querySelector('#down').onclick=function(){
        if(c.width>=minWidth&&c.height>=minHeight){
            c.width/=scaleStep;
            c.height/=scaleStep;
            scale=c.height/minHeight;
            ctx.scale(scale,scale);
            c.style.backgroundSize=`${c.width}px ${c.height}px`;
            reshow();
        }
    }
    document.querySelector('#cancel').onclick=function(){
        layers.pop();
        ctx.clearRect(0,0,elementWidth,elementHeight);
        reshow();
    }
    document.querySelector('#clear').onclick=function(){
        layers=[];
        ctx.clearRect(0,0,elementWidth,elementHeight);
        reshow();
    }
    document.querySelector('#submit').onclick=function(){
        if(layers.length>0){
            let item =layers[0]
            let x1 = (item.x1/elementWidth).toFixed(3);
            let x2 = (item.x2/elementWidth).toFixed(3);
            let y1 = (item.y1/elementHeight).toFixed(3);
            let y2 = (item.y2/elementHeight).toFixed(3);
            let coord = x1.toString()+','+y1.toString()+','+x2.toString()+','+y2.toString()

            window.parent.setCoordinate(coord)
        }else {
            let coord = ""
            window.parent.setCoordinate(coord)
        }

        let index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);//关闭当前页
        // reshow();
    }
    function moveLeft(rect){
        c.style.cursor="w-resize";
        if(flag&&op==0){op=3;return }
        if(flag&&op==3){
            if(!currentR){currentR=rect}
            if(x+minDis<currentR.x2){
                currentR.x1 = x
                currentR.width=currentR.x2-currentR.x1
            }

            // console.log(currentR.width)
        }
    }


    function moveTop(rect){
        c.style.cursor="n-resize";
        if(flag&&op==0){op=4;}
        if(flag&&op==4){
            if(!currentR){currentR=rect}
            if(y+minDis<currentR.y2){
                currentR.y1=y
                currentR.height=currentR.y2-currentR.y1
            }

        }
    }
    function moveRight(rect){
        c.style.cursor="e-resize";
        if(flag&&op==0){op=5;return }
        if(flag&&op==5){
            if(!currentR){currentR=rect}
            if(x-minDis>currentR.x1){
                currentR.x2 = x
                currentR.width=currentR.x2-currentR.x1
            }
        }
    }
    function moveBottom(rect){
        c.style.cursor="s-resize";
        if(flag&&op==0){op=6;}
        if(flag&&op==6){
            if(!currentR){currentR=rect}
            if(y-minDis>currentR.y1){
                currentR.y2=y
                currentR.height=currentR.y2-currentR.y1
            }
        }
    }
    function resizeLT(rect){
        c.style.cursor="se-resize";
        if(flag&&op==0){op=7;}
        if(flag&&op==7){
            if(!currentR){currentR=rect}
            currentR.x1=x
            currentR.y1=y
            currentR.height=currentR.y2-currentR.y1
            currentR.width=currentR.x2-currentR.x1
        }
    }
    function resizeWH(rect){
        c.style.cursor="se-resize";
        if(flag&&op==0){op=8;}
        if(flag&&op==8){
            if(!currentR){currentR=rect}
            if(x-minDis>currentR.x1&&y-minDis>currentR.y1){
                currentR.x2=x
                currentR.y2=y
                currentR.height=currentR.y2-currentR.y1
                currentR.width=currentR.x2-currentR.x1
            }

        }
    }
    function resizeLH(rect){
        c.style.cursor="ne-resize";
        if(flag&&op==0){op=9;}
        if(flag&&op==9){
            if(!currentR){currentR=rect}
            currentR.x1=x
            currentR.y2=y
            currentR.height=currentR.y2-currentR.y1
            currentR.width=currentR.x2-currentR.x1
        }
    }
    function resizeWT(rect){
        c.style.cursor="ne-resize";
        if(flag&&op==0){op=10;}
        if(flag&&op==10){
            if(!currentR){currentR=rect}
            currentR.x2=x
            currentR.y1=y
            currentR.height=currentR.y2-currentR.y1
            currentR.width=currentR.x2-currentR.x1
        }
    }
    function reshow(x,y){
        let allNotIn=1;
        layers.forEach(item=>{
            ctx.beginPath();
            ctx.rect(item.x1,item.y1,item.width,item.height);
            ctx.fillText(msg, item.x1, item.y1);
            ctx.strokeStyle=item.strokeStyle
            if(x>=(item.x1-minDis/scale)&&x<=(item.x1+minDis/scale)&&y<=(item.y2-minDis/scale)&&y>=(item.y1+minDis/scale)){
                moveLeft(item);
            }else if(x>=(item.x2-minDis/scale)&&x<=(item.x2+minDis/scale)&&y<=(item.y2-minDis/scale)&&y>=(item.y1+minDis/scale)){
                moveRight(item);
            }else if(y>=(item.y1-minDis/scale)&&y<=(item.y1+minDis/scale)&&x<=(item.x2-minDis/scale)&&x>=(item.x1+minDis/scale)){
                moveTop(item);
            }else if(y>=(item.y2-minDis/scale)&&y<=(item.y2+minDis/scale)&&x<=(item.x2-minDis/scale)&&x>=(item.x1+minDis/scale)){
                moveBottom(item);
            // }else if(x>=(item.x1-25/scale)&&x<=(item.x1+25/scale)&&y<=(item.y1+25/scale)&&y>=(item.y1-25/scale)){
            //     resizeLT(item);
            }else if(x>=(item.x2-minDis/scale)&&x<=(item.x2+minDis/scale)&&y<=(item.y2+minDis/scale)&&y>=(item.y2-minDis/scale)){
                resizeWH(item);
            // }else if(x>=(item.x1-25/scale)&&x<=(item.x1+25/scale)&&y<=(item.y2+25/scale)&&y>=(item.y2-25/scale)){
            //     resizeLH(item);
            // }else if(x>=(item.x2-25/scale)&&x<=(item.x2+25/scale)&&y<=(item.y1+25/scale)&&y>=(item.y1-25/scale)){
            //     resizeWT(item);
            }
            if(ctx.isPointInPath(x*scale,y*scale)){
                render(item);
                allNotIn=0;
            }
            ctx.stroke();
        })
        if(flag&&allNotIn&&op<3){
            op=1
        }

    }
    function render(rect){
        c.style.cursor="move";
        if(flag&&op==0){op=2;}
        if(flag&&op==2){
            if(!currentR){currentR=rect}
            currentR.x2+=x-leftDistance-currentR.x1
            currentR.x1+=x-leftDistance-currentR.x1
            currentR.y2+=y-topDistance-currentR.y1
            currentR.y1+=y-topDistance-currentR.y1
        }
    }
    function isPointInRetc(x,y){
        let len=layers.length;
        for(let i=0;i<len;i++){
            if(layers[i].x1<x&&x<layers[i].x2&&layers[i].y1<y&&y<layers[i].y2){
                return layers[i];
            }
        }
    }
    function fixPosition(position){
        if(position.x1>position.x2){
            let x=position.x1;
            position.x1=position.x2;
            position.x2=x;
        }
        if(position.y1>position.y2){
            let y=position.y1;
            position.y1=position.y2;
            position.y2=y;
        }
        position.width=position.x2-position.x1
        position.height=position.y2-position.y1
        // if(position.width<50||position.height<50){
        //     position.width=60;
        //     position.height=60;
        //     position.x2+=position.x1+60;
        //     position.y2+=position.y1+60;
        // }
        return position
    }
    let mousedown=function(e){
        startx=(e.pageX-c.offsetLeft+c.parentElement.scrollLeft)/scale;
        starty=(e.pageY-c.offsetTop+c.parentElement.scrollTop)/scale;
        currentR=isPointInRetc(startx,starty);
        if(currentR){
            leftDistance=startx-currentR.x1;
            topDistance=starty-currentR.y1;
        }
        // ctx.strokeRect(x,y,0,0);
        ctx.strokeStyle="#0000ff";
        flag=1;
    }
    let mousemove=function(e){
        x=(e.pageX-c.offsetLeft+c.parentElement.scrollLeft)/scale;
        y=(e.pageY-c.offsetTop+c.parentElement.scrollTop)/scale;
        ctx.save();
        ctx.setLineDash([5])
        c.style.cursor="default";
        ctx.clearRect(0,0,elementWidth,elementHeight)
        if(flag&&op==1){
            ctx.strokeRect(startx,starty,x-startx,y-starty);
        }
        ctx.restore();
        reshow(x,y);
    }
    let mouseup=function(e){
        if(op==1&&layers.length<1){
            layers.push(fixPosition({
                x1:startx,
                y1:starty,
                x2:x,
                y2:y,
                strokeStyle:'#0000ff',
                type:type
            }))
        }else if(op>=3){
            fixPosition(currentR)
        }

        currentR=null;
        flag=0;
        reshow(x,y);
        op=0;
    }
    c.onmouseleave=function(){
        c.onmousedown=null;
        c.onmousemove=null;
        c.onmouseup=null;
    }
    c.onmouseenter=function(){
        c.onmousedown=mousedown;
        c.onmousemove=mousemove;
        document.onmouseup=mouseup;
    }
</script>

</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <style>
        *{
            font-family: Tahoma;
        }
        body{
            margin:0;
            padding:0;
        }
        .title{
            background: #ffffff;
            height: 3rem;
            text-align: center;
            line-height: 3rem;
            color: #000;
            box-shadow: 1px 2px 1px #eee;
        }
        .logo{
            margin-top:3rem;
            width:100%;
            text-align: center;
        }
        .logo span{
            font-size: 0.8rem;
            color: #ccc;
        }
        .logo .chuxing{
            font-size: 1.3rem;
            margin-bottom: 0.5rem;
            color: #888;
        }
        .logo .version{
            font-weight: bold;
        }
        .service a{
            margin: 1rem 0;
            display: block;
            font-size: 0.8rem;
            color: #777;
            text-align: center;
        }
        .content{
            padding: 2rem 0;
            width: 90%;
            font-size: 0.9rem;
            margin:10% auto;
        }
        .content div{
            /*text-indent:1rem;*/
            height:3.5rem;
            line-height: 3.5rem;
        }
        .content .wechat{
            border-bottom: 1px solid #eee;
        }
        .footer{
            width: 100%;
            text-align: center;
            font-size: 0.7rem;
            position: absolute;
            bottom:1rem;
            color: #b7b7b7;
        }
    </style>
    
<div class="title"> 关于出行</div>
<div class="logo"><img src="/resource/images/lhcx_logo.png" alt=""><br> <span class="chuxing">来回出行</span><br><span class="version">V  1.0.0</span></div>
<div class="content">
    <div class="wechat">官方微信公众号 <span style="float: right;font-size: 0.8rem">laihuiApp</span> </div>
    <div class="email">商务合作邮箱 <span style="float: right;font-size: 0.8rem">lixiaochuan@laihuipinche.com</span></div>
</div>

<div class="footer">
    <div class="service"><a href="">来回出行用户服务协议</a></div>
    Copyright   <span style="color:#b7b7b7;font-weight: normal ">&copy;</span> 2016-2017  LaiHuichuxing.All Rights   Reserved.
</div>
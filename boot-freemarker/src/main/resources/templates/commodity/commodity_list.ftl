<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>商品信息</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <#include "../common/common.ftl" />
</head>

<body>
<header class="nav-down responsive-nav hidden-lg hidden-md">
    <button type="button" id="nav-toggle" class="navbar-toggle" data-toggle="collapse" data-target="#main-nav">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
    </button>

    <div id="main-nav" class="collapse navbar-collapse">
        <nav>
            <ul class="nav navbar-nav">
                <li><a href="#top">Home</a></li>
            </ul>
        </nav>
    </div>
</header>

<div class="sidebar-navigation hidde-sm hidden-xs">
    <div class="logo">
        <a href="#">Sen<em>tra</em></a>
    </div>
    <nav>
        <ul>
            <li>
                <a href="#featured">
                    <span class="rect"></span>
                    <span class="circle"></span>
                    商品列表
                </a>
            </li>
        </ul>
    </nav>
</div>

<div class="page-content">
    <section id="featured" class="content-section">
        <div class="section-heading">
            <h1>商品<br><em>列表</em></h1>
        </div>
        <div class="section-content">
            <div>
                <button type="button" class="btn btn-info" onclick="edit(0);" style="position: relative;float: left;margin-bottom: 10px;">
                    添加
                </button>
            </div>
            当前第 ${page.pageNum }页,总${page.pages }页,总 ${page.total } 条记录
            <table class="table table-striped">
                <tr>
                    <td>商品名称</td>
                    <td>商品规格</td>
                    <td>温度</td>
                    <td>价格</td>
                    <td>商品描述</td>
                    <td>操作一</td>
                    <td>操作二</td>
                </tr>
                <#list page.list as item>
                <tr>
                    <td>${(item.name)!}</td>
                    <td>${(item.standard)!}</td>
                    <td>${(item.temperature)!}</td>
                    <td>${(item.price)!}</td>
                    <td>${(item.description)!}</td>
                    <td><button type="button" class="btn btn-warning" onclick="edit(${(item.id)});">
                        修改
                    </button>
                    </td>
                    <td><button type="button" class="btn btn-danger" onclick="deleteById(${(item.id)!})">
                        删除
                    </button>
                    </td>
                </tr>
                </#list>
            </table>
        </div>
    </section>
    <section id="contact" class="content-section">
        <!-- 分页 -->
        <a style="color: #0f0f0f;" href="/commodity/findAll?currentPage=1">首页</a>
        <#if page.hasPreviousPage??>
            <a  style="color: #0f0f0f;"
               href="/commodity/findAll?currentPage=${page.pageNum-1 }">«</a>
        </#if>
        <#list page.navigatepageNums as page_Num >
            <#if page_Num == page.pageNum>
                <a  style="color: #0f0f0f;"
                   href="/commodity/findAll?currentPage=${page_Num}">${page_Num}</a>
            </#if>
            <#if page_Num != page.pageNum>
                <a  style="color: #0f0f0f;"
                   href="/commodity/findAll?currentPage=${page_Num}">${page_Num }</a>
            </#if>
        </#list>
        <#if page.hasNextPage>
            <a  style="color: #0f0f0f;"
               href="/commodity/findAll?currentPage=${page.pageNum+1 }">»</a>
        </#if>
            <a  style="color: #0f0f0f;"
               href="/commodity/findAll?currentPage=${page.pages}">末页</a>
        <!-- 分页 end-->
    </section>
</div>
<script>
    var didScroll;
    var lastScrollTop = 0;
    var delta = 5;
    var navbarHeight = $('header').outerHeight();

    $(window).scroll(function(event){
        didScroll = true;
    });

    setInterval(function() {
        if (didScroll) {
            hasScrolled();
            didScroll = false;
        }
    }, 250);

    function hasScrolled() {
        var st = $(this).scrollTop();

        if(Math.abs(lastScrollTop - st) <= delta)
            return;

        if (st > lastScrollTop && st > navbarHeight){
            $('header').removeClass('nav-down').addClass('nav-up');
        } else {
            if(st + $(window).height() < $(document).height()) {
                $('header').removeClass('nav-up').addClass('nav-down');
            }
        }
        lastScrollTop = st;
    }
</script>
<script>
    function edit(id) {
        var title = "新增商品";
        if (id != 0) {
            title = "编辑商品";
        }
        var ob = {
            title: "<label>"+title+"</label>",
            width: "800",
            height: "400",
            url: "findById?id="+id
        };
        my.open(ob);
    }
    function reload(){
        window.location.reload();
    }
    function deleteById(id) {
        my.confirm('提示信息','是否确认删除?',function() {
            $.get("/commodity/deleteById", {id: id}, function(data) {
                if (data.success) {
                    layer.msg("删除成功!",
                            {icon:3,time:1000},
                            function(){
                                reload();
                            })
                } else {
                    my.alert('删除失败!');
                }
            });
        })
    }
</script>
</body>
</html>
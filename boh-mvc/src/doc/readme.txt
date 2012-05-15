今天将前端，java分离项目按照ethan的图重构了一下，具体修改如下：
1，整体代码的重构
2，路由模式由原来的 useStatic[true, false]两个模式扩展到三中具体为：
	a, UI：直接路由到静态的json文件 
	b, UI_DEBUG：将请求转发到java服务器 
	c, JAVA：路由到具体的server module 获取业务数据
3, 配置文件由原来的/WEB-INF/classes/applicationContext.xml改为/WEB-INF/classes/application.properties
4, 配置项发生改变，新的配置选项如下：
	a, config.crossDomain=false
		请求时否是跨域请求
	b, config.routerMode=ui_debug
		路由模式 只能为[ui, ui_debug, java]这三种中的一个，对应的解释见2
	c, config.forwardUrl=http://localhost:8080/boh-mvc
		如果路由模式选择ui_debug 则需要配置该项，该项即为java服务器的路径
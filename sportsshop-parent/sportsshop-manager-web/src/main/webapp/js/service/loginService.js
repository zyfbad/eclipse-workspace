//品牌服务
app.service("loginService", function($http){

	//获取品牌选项下拉列表
	this.loginName = function(){
		return $http.get('../login/getName.do');
	}
});
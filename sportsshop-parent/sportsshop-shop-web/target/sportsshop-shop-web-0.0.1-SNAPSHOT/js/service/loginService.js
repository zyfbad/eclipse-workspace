//服务层
app.service('loginService',function($http){

	//获取当前用户姓名
	this.getLoginName = function(){
		return $http.get('../login/getName.do?page');
	}    	
});

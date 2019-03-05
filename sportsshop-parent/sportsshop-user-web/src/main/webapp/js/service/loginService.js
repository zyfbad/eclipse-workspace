//服务层
app.service('loginService',function($http){

	//获取当前用户姓名
	this.reg = function(entity){
		alert("message");
		return $http.post('../user/add.do', entity);
	}    	
});

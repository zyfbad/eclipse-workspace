//服务层
app.service('indexService',function($http){

	//获取当前用户姓名
	this.getUserName = function(){
		return $http.get('../user/getUserName.do');
	}    	
});

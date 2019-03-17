 //控制层 
app.controller('indexController' ,function($scope, indexService){	
	
	//获取用户名
	$scope.getUserName = function(){
		indexService.getUserName().success(
			function(response){

				$scope.userName = response.userName;

			}	
		);
	}
    
});	

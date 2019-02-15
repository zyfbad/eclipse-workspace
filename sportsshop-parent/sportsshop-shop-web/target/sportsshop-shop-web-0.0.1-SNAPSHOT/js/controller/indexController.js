 //控制层 
app.controller('indexController' ,function($scope,$controller   ,loginService){	
	
    //读取当前用户姓名  
	$scope.getLoginName = function(){
		loginService.getLoginName().success(
			function(response){
				$scope.loginName=response.loginName;
			}			
		);
	}    
	
});	

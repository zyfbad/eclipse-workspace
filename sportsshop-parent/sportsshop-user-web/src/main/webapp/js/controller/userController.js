 //控制层 
app.controller('userController' ,function($scope,loginService){	

	
	//用户实体
	//$scope.password = "";
	//用户注册
	$scope.reg = function(){
		
		/*alert($scope.entity.password);
		alert($scope.password);*/
		if($scope.entity.password != $scope.password){
			alert("两次密码不一致！");
		}else{
			loginService.reg($scope.entity).success(
					function(response){
						if(response.success){
							//如果注册成功，跳转到登录页面
							location.href="login.html";
						}else{
							alert(response.message);
						}
					}
				);
		}
	}
    
});	

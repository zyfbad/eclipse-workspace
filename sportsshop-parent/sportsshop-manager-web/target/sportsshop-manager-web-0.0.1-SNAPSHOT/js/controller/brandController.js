//品牌controller
app.controller('brandController', function($scope, $controller, brandService){
	
	$controller('baseController', {$scope:$scope});
	//查询商品列表
	$scope.findAll = function(){
		brandService.findAll().success(
			function(response){
				$scope.list = response;
			}		
		);
	}
	
	//查找页面
	$scope.findPage = function(page, size){
		brandService.findPage(page, size).success(
			function(response){
				$scope.list = response.rows; //显示当前数据
				$scope.paginationConf.totalItems = response.total; //更新总记录数
			}
		);
	}
	
	//新增商家
	$scope.save = function(){
		var obj = null;  //方法名
		if($scope.entity.id != null){
			obj = brandService.update($scope.entity);
		}else{
			obj = brandService.add($scope.entity);
		}
		obj.success(
			function(response){
				if(response.success){
					$scope.reloadList(); //刷新列表
				}else{
					alert(response.message)
				}
			}
		);
	}
	
	//查找单个
	$scope.findOne = function(id){
		brandService.findOne(id).success(
			function(response){
				$scope.entity = response;
			}
		);
	}
	
	$scope.dele = function(){
		brandService.dele($scope.selectIds).success(
			function(response){
				if(response.success){
					$scope.reloadList();
					$scope.selectIds=[];
				}else{
					alert(response.message);
				}
			}
		);
	}
	
	$scope.searchEntity={};
	$scope.search = function(page, size){
		brandService.search(page, size, $scope.searchEntity).success(
			function(response){
				$scope.list = response.rows; //显示当前数据
				$scope.paginationConf.totalItems = response.total; //更新总记录数
			}
		);
	}
});
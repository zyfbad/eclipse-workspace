app.controller("searchController", function($scope, searchService){
	
	
	//搜索
	$scope.search = function(){
		searchService.search($scope.searchMap).success(
			function(response){
				$scope.resultMap = response;
			}
		);
	}
	
	//
	$scope.searchMap = {'keywords':'', 'category':'', 'brand':'', 'spec':{}};
	
	$scope.addSearchItem = function(key, value){
		
		if(key=='category' || key=='brand'){ //如果点击的是品牌或规格
			
			$scope.searchMap[key] = value;
			
		}else{
			
			$scope.searchMap.spec[key] = value;
			
		}
		$scope.search();//执行搜索
	}
	
	$scope.removeSearchItem = function(key){
		
		if(key=='category' || key=='brand'){ //如果点击的是品牌或规格
			
			$scope.searchMap[key] = '';
			
		}else{
			
			delete $scope.searchMap.spec[key];
			
		}
		$scope.search();//执行搜索
	}
});
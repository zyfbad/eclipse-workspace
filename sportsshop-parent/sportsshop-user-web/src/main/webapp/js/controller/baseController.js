app.controller('baseController', function($scope){
	//分页控制配置
	//currentPage:当前页         totalItems:总记录数
	//itemsPerPerPage:每页记录数       perPageOptions:分页选项
	//onChange:当页码变更时自动触发
	$scope.paginationConf = {
		currentPage: 1,
		totalItems: 10,
		itemsPerPage: 10,
		perPageOptions: [10, 20, 30, 40, 50],
		onChange: function(){
			$scope.reloadList();
		}
	}
	
	$scope.reloadList = function(){
		$scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
	};
	
	$scope.selectIds=[]; //用户勾选的ID集合
	//用户勾选复选框
	$scope.updateSelection = function($event, id){
		if($event.target.checked){
			$scope.selectIds.push(id); // push向集合中添加元素
		}else{
			var index = $scope.selectIds.indexOf(id);  //查找值的位置
			$scope.selectIds.splice(index, 1); //位置，移除个数
		}
		
	}
	
	$scope.jsonToString = function(jsonString, key){
		var json = JSON.parse(jsonString);
		var value = "";
		for(var i=0; i<json.length; i++){
			if(i>0){
				value += ',';
			}
			value += json[i][key];
		}
		
		return value;
	}
	//在list集合中查询某key
	$scope.searchObjectByKey = function(list, keyName, keyValue){
		for(var i=0; i<list.length; i++){
			if(keyValue == list[i][keyName]){
				return list[i];
			}
		}
		return null;
	}
});
app.controller('itemController', function($scope){
	
	//用户选择的规格列表
	$scope.specificationList = {};
	
	$scope.alertMsg = function(){
		alert("ewfwef");
	}
	
	//改变数量
	$scope.addNum = function(addValue){
		$scope.goodsNum = $scope.goodsNum+addValue;
		//控制商品数量不能少于1
		if($scope.goodsNum<1){
			$scope.goodsNum = 1;
		}
	}
	
	//用户选择规格
	$scope.selectSpecification = function(speName, speValue){
		$scope.specificationList[speName] = speValue;
		searchSku(); //匹配sku
	}
	
	//判断某个规格是否被选择
	$scope.isSelectedSepc = function(speName, speValue){
		if($scope.specificationList[speName] == speValue){
			return true;
		}
		return false;
	}

	$scope.sku = {}; //当前选择的sku
	//加载默认的sku
	$scope.loadDefaultItem = function(){
		$scope.sku = skuList[0];
		$scope.specificationList=JSON.parse(JSON.stringify($scope.sku.spec));
	}
	
	//根据规格查找sku
	searchSku = function(){
		for(var i=0; i<skuList.length; i++){
			if(matchObject($scope.specificationList, skuList[i].spec)){
				$scope.sku = skuList[i];
				return ;
			}
		}
		$scope.sku = {'id':0, 'title':'没货哦', 'price':0};
	}
	//匹配两个对象是否相等
	matchObject = function(obj1, obj2){
		for(var key in obj1){
			if(obj1[key] != obj2[key]){
				return false;
			}
		}
		
		for(var key in obj2){
			if(obj2[key] != obj1[key]){
				return false;
			}
		}
		
		return true;
	}
	
	//添加到购物车
	$scope.addToCart = function(){
		alert("skuid:"+$scope.sku.id);
	}
});
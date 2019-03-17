app.controller('cartController', function($scope, cartService){
	//查找购物车列表
	$scope.findCartList = function(){
		cartService.findCartList().success(
			function(response){
				$scope.cartList = response;
				$scope.totalResult = $scope.calSum($scope.cartList);//计算总费用
			}
		);
	}
	//添加到购物车
	$scope.addGoodsToCartList = function(itemId, num){
		cartService.addGoodsToCartList(itemId, num).success(
			function(response){
				if(response.success){
					$scope.findCartList();
				}else{
					alert(response.message);
				}
			}
		);
	}
	
	//计算所有的费用
	$scope.calSum = function(cartList){
		var totalResult = {totalNum:0, totalFee:0.00};
		for(var i=0; i<cartList.length; i++){
			var cart = cartList[i];
			var itemList = cart.orderItemList;
			for(var j=0; j<itemList.length; j++){
				totalResult.totalNum += itemList[j].num; //计算总数量
				totalResult.totalFee += itemList[j].totalFee; //计算总价格
			}
		}
		return totalResult;
	}
});
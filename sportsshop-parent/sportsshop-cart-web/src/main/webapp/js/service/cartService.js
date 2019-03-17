app.service('cartService', function($http){
	
	this.findCartList = function(){
		return $http.get('cart/findCartList.do?');
	}
	
	this.addGoodsToCartList = function(itemId, num){
		return $http.get('cart/addToCartList.do?itemId='+itemId+"&num="+num);
	}
	
});
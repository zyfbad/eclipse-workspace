app.controller("contentController", function($scope,contentService){
	
	$scope.contentList = [];
	
	$scope.findByCategoryId = function(categoryId){

		contentService.findByCategoryId(categoryId).success(
			function(response){
				$scope.contentList[categoryId] = response;
			}	
		).error(
			function(){
				alert("错误");
			}	
		);
	}
	
	$scope.search = function(){
		location.href="http://localhost:9106/search.html#?keywords="+$scope.keywords;
	}
});
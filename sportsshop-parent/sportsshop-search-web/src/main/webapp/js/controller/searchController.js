app.controller("searchController", function($scope,$location, searchService){
	
	
	//搜索
	$scope.search = function(){
		$scope.searchMap.pageNo= parseInt($scope.searchMap.pageNo);
		//alert($scope.searchMap.pageNo);
		searchService.search($scope.searchMap).success(
			function(response){
				$scope.resultMap = response;
				
				buildPageLabel(); //计算页码
			}
		);
	}
	
	//
	$scope.searchMap = {'keywords':'', 'category':'', 'brand':'', 'spec':{}, 'price':'', 'pageNo':1, 'pageSize':40, 'sortField':'', 'sortValue':''};
	
	$scope.addSearchItem = function(key, value){
		
		if(key=='category' || key=='brand' || key=='price'){ //如果点击的是品牌或规格
			
			$scope.searchMap[key] = value;
			
		}else{
			
			$scope.searchMap.spec[key] = value;
			
		}
		$scope.search();//执行搜索
	}
	
	$scope.removeSearchItem = function(key){
		
		if(key=='category' || key=='brand' || key=='price'){ //如果点击的是品牌或规格
			
			$scope.searchMap[key] = '';
			
		}else{
			
			delete $scope.searchMap.spec[key];
			
		}
		$scope.search();//执行搜索
	}
	//封装起始页和终止页的页码
	$scope.pageMes = {'prePage':'1', 'preView':'1', 'nextPage':'1', 'nextView':'1'};
	
	buildPageLabel = function(){
		//数组存放页码
		$scope.pageLabelArray = [];
		
		//总页数
		var totalPages = $scope.resultMap.totalPages;
		
		//开始页码
		var startPage = 1;
		
		//最后页码
		var lastPage = totalPages;
		
		//封装起始页和终止页的页码
		$scope.pageMes = {'prePage':'1', 'preView':'1', 'nextPage':'1', 'nextView':'1'};
		
		if(totalPages > 5){ //如果总页数大于5，只显示5页
			
			//如果当前页码数小于等于3，则最后页码为5
			if($scope.searchMap.pageNo<=3){
				lastPage = 5;
				$scope.pageMes.preView = '0';
			}else if($scope.searchMap.pageNo>=totalPages-2){ //如果当前页码为后三页，则开始页码为总页码-4
				startPage = totalPages-4;
				$scope.pageMes.nextView = '0';
			}else{ //其他，为当前页-2 至 当前页+2
				startPage = $scope.searchMap.pageNo-2;
				lastPage = $scope.searchMap.pageNo+2;
				$scope.pageMes.preView = '1';
				$scope.pageMes.nextView = '1';
			}
		}else{
			$scope.pageMes.preView = '0';
			$scope.pageMes.nextView = '0';
		}
		//记录起始页，最终页
		if(1==$scope.searchMap.pageNo){
			//$scope.pageMes = {'prePage':'1', 'preView':'1', 'nextPage':'1', 'nextView':'1'};
			$scope.pageMes.prePage = '0';
		}
		if(totalPages==$scope.searchMap.pageNo){
			$scope.pageMes.nextPage = '0';
		}
	
		for(var i=startPage; i<=lastPage; i++){
			$scope.pageLabelArray.push(i);
		}
	}
	$scope.hasPrePage = function(){
		if('1' == $scope.pageMes.prePage){
			return true;
		}
		return false;
	}
	
	$scope.hasNextPage = function(){
		if('1' == $scope.pageMes.nextPage){
			return true;
		}
		return false;
	}
	$scope.queryByPage = function(pageNo){
		
		//非法页码
		if(pageNo<1 || pageNo>$scope.resultMap.totalPages){
			return ;
		}
		$scope.searchMap.pageNo = pageNo;
		$scope.search();
	}
	
	$scope.queryBySort = function(sortField, sortValue){
		$scope.searchMap.sortField = sortField;
		$scope.searchMap.sortValue = sortValue;
		$scope.search();
	}
	
	$scope.keywordsIsBrand = function(){
		var brandList = $scope.resultMap.brandList;
		for(var i=0; i<brandList.length; i++){
			if($scope.searchMap.keywords.indexOf(brandList[i].text)>=0){
				return true;
			}
		}
		
		return false;
	}
	
	$scope.loadKeywords = function(){
		$scope.searchMap.keywords = $location.search()['keywords'];
		$scope.search();
	}
});
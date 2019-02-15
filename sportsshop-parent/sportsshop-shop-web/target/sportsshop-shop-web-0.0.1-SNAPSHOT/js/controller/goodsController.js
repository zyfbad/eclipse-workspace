 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location  ,goodsService,uploadService,itemCatService,typeTemplateService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(){
		//获取页面传来的所有参数，数组
		var id = $location.search()['id'];
		if(id == null){
			return ;
		}
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;	
				
				editor.html($scope.entity.goodsDesc.introduction);
				
				//商品图片
				$scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);
			
				//拓展属性
				$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
			
				//规格选择
				$scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);
			
				
				//转化SKU列表对象
				for(var i=0; i<$scope.entity.itemList.length; i++){
					$scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
				}
			}
		);			
	}
	
	//保存 
	$scope.save=function(){		
		//产品介绍，富文本内容
		$scope.entity.goodsDesc.introduction = editor.html();
		
		var serviceObject;//服务层对象  				
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//提示成功
					alert("添加成功");
		        	location.href='goods.html';
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	//增加商品
	$scope.add=function(){	
		$scope.entity.goodsDesc.introduction = editor.html();
		goodsService.add( $scope.entity  ).success(
			function(response){
				if(response.success){
					//提示成功
					alert("添加成功");
		        	$scope.entity = {};//清空内容
		        	editor.html(''); //清空富文本编辑器
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    //上传图片
	$scope.uploadFile = function(){
		uploadService.uploadFile().success(
			function(response){
				if(response.success){
					$scope.image_entity.url = response.message;
				}else{
					alert(response.message)
				}
			}
		);
	}
	
	$scope.entity = {goods:{}, goodsDesc:{itemImages:[],specificationItems:[]}};
	//向集合中加元素
	$scope.add_image_entity = function(){
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity)
	}
	//移除图片
	$scope.remove_image_entity = function(index){
		$scope.entity.goodsDesc.itemImages.splice(index, 1);
	}
	
	//查找一级分类列表
	$scope.selectItemCat_1_List = function(){
		itemCatService.findByParentId(0).success(
			function(response){
				$scope.itemCat_1_List = response;
			}
		);
	}
	//监控一级分类列表值,查询二级分类列表
	$scope.$watch('entity.goods.category1Id', function(newValue, oldvalue){
		itemCatService.findByParentId(newValue).success(
			function(response){
				$scope.itemCat_2_List = response;
				$scope.itemCat_3_List = {};
			}
		);
	});
	//监控二级分类列表值,查询三级分类列表
	$scope.$watch('entity.goods.category2Id', function(newValue, oldvalue){
		itemCatService.findByParentId(newValue).success(
			function(response){
				$scope.itemCat_3_List = response;
			}
		);
	});
	
	//读取模板ID
	$scope.$watch('entity.goods.category3Id', function(newValue, oldvalue){
		itemCatService.findOne(newValue).success(
			function(response){
				$scope.entity.goods.typeTemplateId = response.typeId;
			}
		);
	});
	
	
	//读取模板ID后，获取品牌列表
	$scope.$watch('entity.goods.typeTemplateId', function(newValue, oldvalue){
		typeTemplateService.findOne(newValue).success(
			function(response){
				$scope.typeTemplate = response;//模板对象
				
				//品牌列表
				$scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);
				//品牌拓展属性
				if( $location.search()['id'] == null){
					$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
				}			
			}
		);
		
		//读取规格
		typeTemplateService.findSepcList(newValue).success(
			function(response){
				$scope.specList = response;
			}
		);

	});
	
	//更新列表
	//$scope.entity = {goods:{}, goodsDesc:{itemImages:[],specificationItems:[]}};
	//向集合中加元素
	$scope.updateSpecificationAttribute = function($event, name, value){
		//查询列表中的attributeName是否有name的值
		var obj = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems, 'attributeName', name);


		if(obj != null){
			if($event.target.checked){
				obj.attributeValue.push(value);
			}else{
				obj.attributeValue.splice(obj.attributeValue.indexOf(value), 1);
				
				if(obj.attributeValue.length == 0){
					$scope.entity.goodsDesc.specificationItems.splice(
						$scope.entity.goodsDesc.specificationItems.indexOf(obj), 1);
				}
			}
		}else{
			$scope.entity.goodsDesc.specificationItems.push( {"attributeName":name,"attributeValue":[value]} );
		}
		
	}
	
	//创建sku列表
	$scope.createItemList = function(){
		$scope.entity.itemList = [{spec:{}, price:0, num:9999, status:'0', isDefault:'0'}]; //列表初始化
		
		var items = $scope.entity.goodsDesc.specificationItems;
		//items:  内存： 8g 16g  网络 ： 3G 4G
		
		for(var i=0; i<items.length; i++){
			$scope.entity.itemList = addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue);
		}
		
	}
	
	addColumn = function(list, columnName, columnValue){
		
		var newList = [];
		
		for(var i=0; i<list.length; i++){
			
			var oldRow = list[i];
			
			for(var j=0; j<columnValue.length; j++){
				 var newRow = JSON.parse( JSON.stringify(oldRow));
				 
				 newRow.spec[columnName] = columnValue[j];
				 
				 newList.push(newRow);
				 
			}
		}
		
		return newList;
	}
	
	$scope.audit_Status=['未审核', '已审核', '审核未通过', '已关闭'];
	
	$scope.itemCatList = [];
	//查询商品分类列表
	
	$scope.findItemCatList = function(){
		itemCatService.findAll().success(
			function(response){
				for(var i=0; i<response.length; i++){
					$scope.itemCatList[response[i].id] = response[i].name;
				}
			}
		);
	}
	//判断规格是否要勾选
	$scope.checkAttributeValue = function(attributeName, attributeValue){
		var items = $scope.entity.goodsDesc.specificationItems;
		var obj = $scope.searchObjectByKey(items, 'attributeName', attributeName);
		
		if(obj  != null){
			if( obj.attributeValue.indexOf(attributeValue) >= 0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
});	

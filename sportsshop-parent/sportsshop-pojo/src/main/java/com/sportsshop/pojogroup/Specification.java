package com.sportsshop.pojogroup;

import java.io.Serializable;
import java.util.List;

import com.sportsshop.pojo.TbSpecification;
import com.sportsshop.pojo.TbSpecificationOption;

/**
 * g规格组合实体类
 * @author acer
 *
 */
public class Specification implements Serializable{
	
	private TbSpecification specification;
	
	private List<TbSpecificationOption> specificationOptionList;

	public Specification() {};
	
	public Specification(TbSpecification specification, List<TbSpecificationOption> specificationOptionList) {
		this.specification = specification;
		this.specificationOptionList = specificationOptionList;
	}

	public TbSpecification getSpecification() {
		return specification;
	}

	public void setSpecification(TbSpecification specification) {
		this.specification = specification;
	}

	public List<TbSpecificationOption> getSpecificationOptionList() {
		return specificationOptionList;
	}

	public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
		this.specificationOptionList = specificationOptionList;
	}
	
}

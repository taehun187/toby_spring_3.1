package com.intheeast.springframe.learningtest.factorybean;

import org.springframework.beans.factory.FactoryBean;

public class MyCustomObjectFactoryBean implements FactoryBean<MyCustomObject> {
	
	private String objectName;

	@Override
	public MyCustomObject getObject() throws Exception {
		// TODO Auto-generated method stub
		return new MyCustomObject(objectName);
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return MyCustomObject.class;
	}
	
	@Override
	public boolean isSingleton() {
		return true;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}	

}
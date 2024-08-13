package com.taehun.springframe.learningtest.template;

public interface LineCallback<T> {
	T doSomethingWithLine(String line, T value);
}
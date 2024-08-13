package com.taehun.springframe.learningtest.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("junit.xml")
public class JUnitTest {
    @Autowired ApplicationContext context;
    
    static Set<JUnitTest> testObjects = new HashSet<>();
    static ApplicationContext contextObject = null;
    
    @Test
    public void test1() {
        assertEquals(false, testObjects.contains(this));
        testObjects.add(this);
        
        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }
    
    @Test
    public void test2() {
        assertEquals(false, testObjects.contains(this));
        testObjects.add(this);
        
        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }
    
    @Test
    public void test3() {
        assertEquals(false, testObjects.contains(this));
        testObjects.add(this);
        
        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }
}
package com.ami.model;

import java.util.ArrayList;
import java.util.List;

public class ChartData {
	 
    
   
 
    public static class KeyValue {
        String key;
        String value;
 
        public KeyValue(String key, String value) {
            super();
            this.key = key;
            this.value = value;
        }
 
        public String getKey() {
            return key;
        }
 
        public void setKey(String key) {
            this.key = key;
        }
 
        public String getValue() {
            return value;
        }
 
        public void setValue(String value) {
            this.value = value;
        }
 
    }
 
}
package com.cpbonnell.PlayingCards;

import java.util.*;

public class Main {

    public static void main(String[] args) {
	// write your code here
        
        
        Stack<String> s = new Stack();
        
        s.push("One");
        s.push("two");
        s.push("three");
        s.push("Four");
        
        for(int i = 0; i < s.size(); i++){
            System.out.println("The element at index " + i + " is " + s.elementAt(i));
        }
        
        
        
    }
}

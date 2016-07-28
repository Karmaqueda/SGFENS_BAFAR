/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo.test;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 *
 * @author ISCesar
 */
public class libExp4jTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //String formula = "3 * sin(y) - 2 / (x - 2)";
        String formula = "20 + x - y + (A1 * B20)";
        
        Expression e = new ExpressionBuilder(formula)
            .variables("x", "y", "A1", "B20", "K", "L")
            .build()
            .setVariable("x", 2.3)
            .setVariable("y", 3.14)
            .setVariable("A1", 2)
            .setVariable("B20", 4)
            .setVariable("K", 1)
            .setVariable("L", 100);
        double result = e.evaluate();
        
        
        System.out.println("Formula: " + formula);
        System.out.println("Resultado: " + result);
    }
    
}

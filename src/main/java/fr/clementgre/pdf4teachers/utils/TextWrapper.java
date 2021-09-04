/*
 * Copyright (c) 2020-2021. Clément Grennerat
 * All rights reserved. You must refer to the licence Apache 2.
 */

package fr.clementgre.pdf4teachers.utils;

import fr.clementgre.pdf4teachers.components.ScratchText;
import javafx.scene.text.Font;

public class TextWrapper {
    
    private String text;
    private final Font font;
    private final int maxWidth;
    
    private String wrappedLine;
    private boolean hasWrapped = false;
    
    public TextWrapper(String text, Font font, int maxWidth){
        this.font = font;
        this.text = text;
        this.maxWidth = maxWidth;
    }
    
    public String wrapFirstLine(){
        
        // While there is still text, add the next line into wrappedLine and let the remaining text into text
        if(text.length() == 0){
            return "";
        }
        
        if(text.split(" ", -1).length == 0) return "";
        String firstWord = text.split(" ", -1)[0];
        
        if(!test(firstWord)){ // Split between chars
            
            String[] results = fillLineWithChar(text);
            if(!results[1].isEmpty()) hasWrapped = true;
            return results[0]; // Line generated
            
            
        }else{ // Split between words
            
            String[] results = fillLineWithWord(text);
            if(!results[1].isEmpty()) hasWrapped = true;
            return results[0]; // Line generated
        }
        
    }
    
    public String wrap(){
        
        // While there is still text, add the next line into wrappedLine and let the remaining text into text
        while(text.length() != 0){
            
            if(text.split(" ", -1).length == 0) break;
            String firstWord = text.split(" ", -1)[0];
            
            if(!test(firstWord)){ // Split between chars
                
                String[] results = fillLineWithChar(text);
                if(!results[1].isEmpty()) hasWrapped = true;
                
                appendLine(results[0]); // Line generated
                text = results[1]; // Remaining text
                
            }else{ // Split between words
                
                String[] results = fillLineWithWord(text);
                if(!results[1].isEmpty()) hasWrapped = true;
                
                appendLine(results[0]); // Line generated
                text = results[1]; // Remaining text
            }
        }
        return getWrappedLine();
    }
    
    public boolean doHasWrapped(){
        return hasWrapped;
    }
    
    private void appendLine(String text){
        if(wrappedLine == null) wrappedLine = text;
        else wrappedLine += "\n" + text;
    }
    private String getWrappedLine(){
        return wrappedLine == null ? "" : wrappedLine;
    }
    
    private boolean test(String line){
        ScratchText toTest = new ScratchText(line);
        toTest.setFont(font);
        return toTest.getBoundsInParent().getWidth() < maxWidth;
    }
    
    private String[] fillLineWithWord(String text){
        
        String[] splitted = text.split(" ", -1);
        String line = splitted[0];
        
        for(int i = 1; i < splitted.length; i++){ // Remplis la ligne avec le maximum de mots puis renvoie la ligne
            
            String lastLine = line;
            line += " " + splitted[i];
            
            if(!test(line)){
                StringBuilder remaining = new StringBuilder(splitted[i]);
                for(i++; i < splitted.length; i++){ // Remplis la ligne avec le maximum de mots puis renvoie la ligne
                    remaining.append(" ").append(splitted[i]);
                }
                return new String[]{lastLine, remaining.toString()};
            }
        }
        
        return new String[]{line, ""};
    }
    
    private String[] fillLineWithChar(String word){
        
        if(word.length() == 0) return new String[]{"", ""};
        String line = word.substring(0, 1);
        
        for(int i = 1; i < word.length(); i++){ // Remplis la ligne avec le maximum de mots puis renvoie la ligne
            String lastLine = line;
            line = word.substring(0, i + 1);
            
            if(!test(line)){
                return new String[]{lastLine, word.substring(i)};
            }
        }
        return new String[]{line, ""};
        
    }
    
}

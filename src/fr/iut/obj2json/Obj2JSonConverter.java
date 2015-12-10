package fr.iut.obj2json;

import java.awt.Window.Type;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Collection;

import javax.lang.model.type.PrimitiveType;
import javax.print.DocFlavor.STRING;

public class Obj2JSonConverter {
    private StringBuilder outs = new StringBuilder();
    private int indentLevel = 0;

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException 
	{
	}
	
	public static String convertObject(Object obj) throws IllegalArgumentException, IllegalAccessException {
        Obj2JSonConverter conv = new Obj2JSonConverter();
        conv.convertAny(obj);

        return conv.outs.toString();
    }

    protected void convertAny(Object obj) throws IllegalArgumentException, IllegalAccessException {
    	Class c = obj.getClass();
    	Field[] fields = c.getDeclaredFields();
    	outs.append("{");
    	outs.append("\n");
		convertPrimitive(obj, c);
		convertArray(obj, c);
    	convertObject(obj,c);
		//convertArrayPrimitive(obj,c);
		outs.append("}");


    }

    private void convertObject(Object obj, Class<?> clss) throws IllegalArgumentException, IllegalAccessException {
     	Class<?> SupC = clss.getSuperclass();
    	Field[] field = clss.getDeclaredFields();
    	for (Field field2 : field) {
    		field2.setAccessible(true);
    		if (field2.getType().isPrimitive()){}
    		else if (field2.getType().isArray()){}
    		else if(field2.getType().isAssignableFrom(Class.class))
    		{
    	    	indentLevel = 1;
	        	printIndent();
    			outs.append("\"" +field2.getName()+"\"" +": ");
    			outs.append(field2.get(obj));
            	outs.append(",\n");
			}else if(field2.getType().isAssignableFrom(Date.class))
			{
		    	indentLevel = 1;
	        	printIndent();
    			outs.append("\"" +field2.getName()+"\"" +": ");
    			outs.append(field2.get(obj));
            	outs.append(",\n");
			}else if(field2.getType().isAssignableFrom(String.class))
			{
		    	indentLevel = 1;
	        	printIndent();
    			outs.append("\"" +field2.getName()+"\"" +": ");
    			outs.append(field2.get(obj));
            	outs.append(",\n");
			}
			else
			{
	        	printIndent();
    			outs.append("\"" +field2.getName()+"\"" +": {");
            	outs.append("\n");
				convertObjectFields(obj, field2.get(obj).getClass());
	        	outs.append(" },");
            	outs.append("\n");
			}
    	}
    }

    private void convertObjectFields(Object obj, Class<?> clss) throws IllegalArgumentException, IllegalAccessException {
    	indentLevel = 2;
    	if (clss != Object.class) {
            Class<?> superClss = clss.getSuperclass();
            // recurse super class fields
            convertObjectFields(obj, superClss);
        }
        Field[] fields = clss.getDeclaredFields();
        for (Field field : fields) {
        	field.setAccessible(true);
        	printIndent();
        	outs.append("\"" +field.getName()+"\"" +": "+field.get(new SubObj()));
        	outs.append(",\n");
		}

    }

    private void convertPrimitive(Object obj, Class<?> clss) throws IllegalArgumentException, IllegalAccessException {    	Field[] field = clss.getDeclaredFields();
    	for (Field field2 : field) {
    		field2.setAccessible(true);
    		if (field2.getType().isPrimitive()) {
    			if (field2.getType()== Character.TYPE) {
    		    	indentLevel = 1;
    		    	printIndent();
    				outs.append("\"" +field2.getName()+"\""  + ": " + escapeChar((char) field2.get(obj)));
    	        	outs.append(",\n");
				}else
				{	
    		    	indentLevel = 1;
    		    	printIndent();
					outs.append("\"" +field2.getName()+"\"" + ": " + field2.get(obj));
    	        	outs.append(",\n");
				}
    		}

		}
    }

    private void convertArray(Object obj, Class<?> clss) throws IllegalArgumentException, IllegalAccessException {
    	Field[] field = clss.getDeclaredFields();
    	for (Field field2 : field) {
    		field2.setAccessible(true);
        	if (field2.getType().isArray())
        	{
        		Object array = field2.get(obj); 
        		int length = Array.getLength(array);
        	    Class cType = field2.getType().getComponentType();
		    	indentLevel = 1;
		    	printIndent();
				outs.append("\"" +field2.getName()+"\""+": ");
    			int b = 0,c = 0;
        		for (int i = 0; i < length; i++) {

        			Object element;
					element = Array.get(array, i);
        			if (!cType.isPrimitive()) {

        				if(element != null)
        				{
        					if(c==0){outs.append("[ ");c++;}
	    		        	outs.append("\n { \n");
	    					convertObjectFields(obj, element.getClass());
	    			    	printIndent();
	    					outs.append(" },");

        				}
        				else
        				{
        		        	outs.append("\n { \n");
        					outs.append(" null");
        					outs.append("\n ],\n");
        				}
        			}
        			else{
        				if(b == 0)
        				{
	        				outs.append("["+element +"," );
	        	        	outs.append(" ");
	        	        	b++;
        				}else
        					outs.append(element);

        			}
        		}
        		if(b==1)
        		{
        			outs.append("],\n");
        		}
        	}
		}

    }

    private void convertArrayPrimitive(Object obj, Class<?> eltClss) throws IllegalArgumentException, IllegalAccessException {
    	//System.out.println("------ARRAYPRIMITIVE-------");
    	Field[] field = eltClss.getDeclaredFields();
    	for (Field field2 : field) {
    		field2.setAccessible(true);
    		if (field2.getType().isArray())
    		{
        		Class array = field2.getType();
        	    Class cType = field2.getType().getComponentType();
	    		
        	    if (cType.isPrimitive())
        	    {
	    			Object arrayVal = field2.get(obj);
	    			int length = Array.getLength(arrayVal);
	    			for (int i = 0; i < length; i++)
	    			{
	    			    Object element = Array.get(arrayVal, i);
		    			outs.append(element);
	    				
	    			}
	    		}
    		}
		}

   }


   private String escapeChar(char charVal) {
        if (charVal == 0) {
            return "\\0";
        }
        return Character.toString(charVal);
    }

    private void printIndent() {
        for(int i = 0; i < indentLevel; i++) {
            outs.append(" ");
        }
    }
    
}

package fr.iut.obj2json;
import static java.lang.System.out;

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
		MockObj mObj = new MockObj();
		convertObject(mObj);
	}
	
	public static String convertObject(Object obj) throws IllegalArgumentException, IllegalAccessException {
        Obj2JSonConverter conv = new Obj2JSonConverter();
        conv.convertAny(obj);

        return conv.outs.toString();
    }

    protected void convertAny(Object obj) throws IllegalArgumentException, IllegalAccessException {
    	Class c = obj.getClass();
    	Field[] fields = c.getDeclaredFields();

    	convertObject(obj,c);
		convertPrimitive(obj, c);
		convertArray(obj, c);
		convertArrayPrimitive(obj,c);


    }

    private void convertObject(Object obj, Class<?> clss) throws IllegalArgumentException, IllegalAccessException {
    	System.out.println("---------------------------------");
     	Class<?> SupC = clss.getSuperclass();
    	Field[] field = clss.getDeclaredFields();
    	for (Field field2 : field) {
    		field2.setAccessible(true);
    		if (field2.getType().isPrimitive()){}
    		else if (field2.getType().isArray()){}
    		else if(field2.getType().isAssignableFrom(Class.class))
    		{
    			System.out.println("[OBJECT]: "+ field2.getName());
			}else if(field2.getType().isAssignableFrom(Date.class))
			{
				System.out.println("[OBJECT]: "+ field2.getName());
			}else if(field2.getType().isAssignableFrom(String.class))
			{
				System.out.println("[OBJECT]: "+ field2.getName());
			}
			else
			{
				System.out.println("[OBJECT]: "+ field2.getName()+"  " + field2.getType());
				convertObjectFields(obj, field2.get(obj).getClass());
			}
    	}
    }

    private void convertObjectFields(Object obj, Class<?> clss) throws IllegalArgumentException, IllegalAccessException {
    
    	if (clss != Object.class) {
            Class<?> superClss = clss.getSuperclass();
            // recurse super class fields
            convertObjectFields(obj, superClss);
        }
        Field[] fields = clss.getDeclaredFields();
        for (Field field : fields) {
        	field.setAccessible(true);
            System.out.println("[-- "+field.getName()+"[PRIMITIVE]"+": "+field.get(new SubObj()));
        	out.append(",\n");
		}

    }

    private void convertPrimitive(Object obj, Class<?> clss) throws IllegalArgumentException, IllegalAccessException {
    	System.out.println("------PRIMITIVE-------");    	Field[] field = clss.getDeclaredFields();
    	for (Field field2 : field) {
    		field2.setAccessible(true);
    		if (field2.getType().isPrimitive()) {
    			if (field2.getType()== Character.TYPE) {
					System.out.println("[PRIMITIVE]: "+ field2.getName() + " " + escapeChar((char) field2.get(obj)));
				}else
				{		
	    			System.out.println("[PRIMITIVE]: "+ field2.getName() + " " + field2.get(obj));
				}
    		}

		}
    }

    private void convertArray(Object obj, Class<?> clss) throws IllegalArgumentException, IllegalAccessException {
    	System.out.println("------ARRAY-------");
    	Field[] field = clss.getDeclaredFields();
    	for (Field field2 : field) {
    		field2.setAccessible(true);
        	if (field2.getType().isArray())
        	{
        		Object array = field2.get(obj); 
        		int length = Array.getLength(array);
        	    Class cType = field2.getType().getComponentType();
        		System.out.print("[ARRAY] "+field2.getName()+ ": [");
        		System.out.print("");
        		for (int i = 0; i < length; i++) {
        			Object element;
					element = Array.get(array, i);
        			if (!cType.isPrimitive()) {
        				if(element != null)
        				{
        					System.out.println("\n [SUB CLASS]");
        					convertObjectFields(obj, element.getClass());
        				}
        				else
        				{
        					System.out.println("\n [SUB CLASS]");
        					System.out.print("[-- null");
        				}
        			}
        			else{

					System.out.print("," + element);
        			}
        		}
    		    System.out.println("],");
        	}
		}

    }

    private void convertArrayPrimitive(Object obj, Class<?> eltClss) throws IllegalArgumentException, IllegalAccessException {
    	System.out.println("------ARRAYPRIMITIVE-------");
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
	    			System.out.print("[ARRAYPRIMITIVE] "+field2.getName()+ ": [");
	    		    System.out.print("");
	    			for (int i = 0; i < length; i++)
	    			{
	    			    Object element = Array.get(arrayVal, i);
	    				System.out.print("," + element );
	    				
	    			}
	    		    System.out.println("],");
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
            out.append(" ");
        }
    }
    
}

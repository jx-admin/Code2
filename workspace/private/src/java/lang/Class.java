package java.lang;

import java.io.InputStream;

public class Class {

	public InputStream getResourceAsStream(String name)
	{
		return null;
	}
	
	public String getName()
	{
		return null;
	}
	
	public static Class forName(String className) throws ClassNotFoundException
	{
		return null;
	}
    
	public Object newInstance() throws InstantiationException, IllegalAccessException
	{
		return null;
	}
    
	public boolean isInstance(Object obj){
		return false;
	}
	
	public boolean isAssignableFrom(Class cls){
		return false;
	}
	
	public boolean isInterface(){
		return false;
	}
	
	public boolean isArray(){
		return false;
	}
	
	public boolean isPrimitive(){
		return false;
	}
}

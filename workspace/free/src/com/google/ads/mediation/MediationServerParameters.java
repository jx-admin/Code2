package com.google.ads.mediation;

import com.google.ads.util.b;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class MediationServerParameters
{
  protected void a()
  {
  }

  public void load(Map<String, String> paramMap)
    throws MediationServerParameters.MappingException
  {
    HashMap localHashMap = new HashMap();
    for (Field localField3 : super.getClass().getFields())
    {
      Parameter localParameter = (Parameter)localField3.getAnnotation(Parameter.class);
      if (localParameter == null)
        continue;
      localHashMap.put(localParameter.name(), localField3);
    }
    if (localHashMap.isEmpty())
      b.e("No server options fields detected.  To suppress this message either add a field with the @Parameter annotation, or override the load() method");
    Iterator localIterator1 = paramMap.entrySet().iterator();
    while (localIterator1.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator1.next();
      Field localField2 = (Field)localHashMap.remove(localEntry.getKey());
      if (localField2 != null)
        try
        {
          localField2.set(this, localEntry.getValue());
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          b.b("Server Option '" + (String)localEntry.getKey() + "' could not be set: Illegal Access");
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          b.b("Server Option '" + (String)localEntry.getKey() + "' could not be set: Bad Type");
        }
      b.e("Unexpected Server Option: " + (String)localEntry.getKey() + " = '" + (String)localEntry.getValue() + "'");
    }
    Object localObject1 = null;
    Iterator localIterator2 = localHashMap.values().iterator();
    label318: Field localField1;
    StringBuilder localStringBuilder;
    String str;
    if (localIterator2.hasNext())
    {
      localField1 = (Field)localIterator2.next();
      if (!((Parameter)localField1.getAnnotation(Parameter.class)).required())
        break label512;
      b.b("Required Server Option missing: " + ((Parameter)localField1.getAnnotation(Parameter.class)).name());
      localStringBuilder = new StringBuilder();
      if (localObject1 == null)
        str = "";
    }
    for (Object localObject2 = str + ((Parameter)localField1.getAnnotation(Parameter.class)).name(); ; localObject2 = localObject1)
    {
      label412: localObject1 = localObject2;
      break label318:
      str = localObject1 + ", ";
      break label412:
      if (localObject1 != null)
        throw new MappingException("Required Server Option(s) missing: " + localObject1);
      a();
      label512: return;
    }
  }

  public static class MappingException extends Exception
  {
    public MappingException(String paramString)
    {
      super(paramString);
    }
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({java.lang.annotation.ElementType.FIELD})
  protected static @interface Parameter
  {
    public abstract String name();

    public abstract boolean required();
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.mediation.MediationServerParameters
 * JD-Core Version:    0.5.4
 */
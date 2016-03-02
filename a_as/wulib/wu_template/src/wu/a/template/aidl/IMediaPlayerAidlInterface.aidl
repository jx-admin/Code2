// IMediaPlayerAidlInterface.aidl
package wu.a.template.aidl;

// Declare any non-default types here with import statements
import wu.a.template.aidl.Person;

/**
 * @author Administrator http://android.blog.51cto.com/268543/537684/
 */
interface IMediaPlayerAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    void savePersonInfo(in Person person); 
    List<Person> getAllPerson();
}

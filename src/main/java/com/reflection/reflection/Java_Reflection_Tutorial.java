package com.reflection.reflection;

import com.reflection.models.ApplicationUser;
import com.reflection.repositories.ApplicationUserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@RestController
@RequestMapping("/rest")
public class Java_Reflection_Tutorial {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    /**
     * This method is used to obtain the name of the class for an instance of ApplicationUser.
     * @return The fully-qualified name of the class.
     */
    @PostMapping("/getClassName")
    public String getClassNameFromObject(){
        Object object = new ApplicationUser();
        Class<?> clazz = object.getClass();
        return clazz.getName();
    }

    /**
     * This method is a Spring MVC controller method that handles a POST request to the "/getClass" endpoint.
     * @return The Class object corresponding to the class name obtained from the "getClassNameFromObject" method.
     * @throws ClassNotFoundException if the specified class name cannot be found or loaded.
     */
    @PostMapping("/getClass")
    public Class<?> getClassFromName() throws ClassNotFoundException {
        String classname = getClassNameFromObject();
        if(StringUtils.isNotBlank(classname)) {
            return Class.forName(classname);
        }
        return null;
    }

    /**
     * This method is used to invoke the "getName" method on an instance of the ApplicationUser class
     * using reflection, and it retrieves the name.
     * @throws InvocationTargetException if there is an issue with invoking the method.
     * @throws IllegalAccessException if there is a problem accessing the method.
     * @return The name obtained by invoking the "getName" method.
     */
    @PostMapping("/getModifiers")
    public void getModifiers() throws InvocationTargetException, IllegalAccessException {
        ApplicationUser applicationUser = applicationUserRepository.findAll().get(0);
        Method[] methods = applicationUser.getClass().getDeclaredMethods();
        for(Method method : methods) {
            if(method.getName().equals("getName")) {
                method.setAccessible(true);
                method.invoke(applicationUser,"Dipankar", "abc");
            }
        }
    }

    /**
     * This method is a Spring MVC controller method that handles a POST request to the "/accessPrivate"endpoint.
     * It demonstrates how to use reflection to access and modify private fields and methods of an ApplicationUser instance.
     * @throws InvocationTargetException if there is an issue with invoking a method.
     * @throws IllegalAccessException if there is a problem accessing the field or method.
     */
    @PostMapping("/accessPrivate")
    public void accessPrivate() throws InvocationTargetException, IllegalAccessException {
        ApplicationUser applicationUser = applicationUserRepository.findAll().get(0);
        Method[] methods = applicationUser.getClass().getDeclaredMethods();
        Field[] fields = applicationUser.getClass().getDeclaredFields();
        for(Method method : methods) {
            if(method.getName().equals("getName")) {
                method.setAccessible(true);
                method.invoke(applicationUser,"Dipankar");
            }
        }
        System.out.println("Before Name : "+ applicationUser.getGender());
        for(Field field : fields) {
            if(field.getName().equals("gender")) {
                field.setAccessible(true);
                field.set(null,"Male-Male");
            }
        }
        System.out.println("After Name : "+ applicationUser.getGender());
    }

    /**
     *  @Class_Information_Printer: Create a program that takes a class name as input and prints information
     *                              about the class, including its fields, methods, and interfaces implemented.
     */
    @PostMapping("/classInformationPrinter")
    public void classInformationPrinter (@RequestParam String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        assert clazz != null;
        String collName = clazz.getName();
        System.out.println("Information About the Class : "+collName);

        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> System.out.println(field.getName()+", "+field.getType()));

        Method [] methods = clazz.getDeclaredMethods();
        Arrays.stream(methods).forEach(method -> System.out.println(method.getName()));
    }

    /**
     @Get_and_Set_Fields: Write a program that demonstrates how to get and set the values of public
                          fields in an object using reflection.
     */
    @PostMapping("/getSetFields")
    public void getSetFields() {
        ApplicationUser applicationUser = new ApplicationUser("12345", "Dipankar", "dipankarbhaduri8@gmail.com", 26);
        Class<?> clazz = applicationUser.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            if(StringUtils.isNotBlank(field.getName()) && field.getName().equals("_id")) {
                field.setAccessible(true);
                try {
                    field.set(applicationUser, "111122223333");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            if(StringUtils.isNotBlank(field.getName()) && field.getName().equals("name")) {
                field.setAccessible(true);
                try {
                    field.set(applicationUser, "Nitin");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            if(StringUtils.isNotBlank(field.getName()) && field.getName().equals("age")) {
                field.setAccessible(true);
                try {
                    field.set(applicationUser, 50);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        System.out.println("_id : "+applicationUser.get_id());
        System.out.println("_id : "+applicationUser.getName());
        System.out.println("_id : "+applicationUser.getAge());
    }

    /**
     @Invoke_Constructors : Use reflection to instantiate an object by calling one of its constructors
     dynamically. Print out the created object.
     */
    @PostMapping("/invokeConstructors")
    public ApplicationUser invokeConstructors() throws Exception {
        Class<?> clazz = ApplicationUser.class;
        // Get the constructor with a String parameter
        Constructor<?> constructor = clazz.getConstructor();

        ApplicationUser applicationUser = (ApplicationUser) constructor.newInstance();

        // Set the properties of the object
        applicationUser.set_id("1234");
        applicationUser.setName("Dipankar");
        applicationUser.setEmail("dipankar@e-labs.ai");
        applicationUser.setAge(26);

        return applicationUser;
    }
}
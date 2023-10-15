package com.reflection.reflection;

import com.reflection.models.ApplicationUser;
import com.reflection.repositories.ApplicationUserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
}


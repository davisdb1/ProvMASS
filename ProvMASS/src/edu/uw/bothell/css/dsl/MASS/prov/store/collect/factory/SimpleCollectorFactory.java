/*

 MASS Java Software License
 © 2012-2015 University of Washington

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 The following acknowledgment shall be used where appropriate in publications, presentations, etc.:      

 © 2012-2015 University of Washington. MASS was developed by Computing and Software Systems at University of 
 Washington Bothell.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.

 */
package edu.uw.bothell.css.dsl.MASS.prov.store.collect.factory;
// PERFORMANCE DOCUMENTED

import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a factory that uses reflection to instantiate and provide instances
 * of provenance collectors
 *
 * Modified from edu.uw.bothell.css.dsl.MASS.core.factory.SimpleObjectFactory
 *
 * @author Delmar B. Davis
 */
public class SimpleCollectorFactory implements CollectorFactory, Serializable {

    public static Class[] noParams = {};

    public static CollectorFactory getInstance() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return INSTANCE;
    }

    // this makes the SimpleObjectFactory a singleton, eagerly initialized
    // we may want to lazily create/init object factories if multiple types are available
    private static final SimpleCollectorFactory INSTANCE = new SimpleCollectorFactory();

    // a collection of the URLs to be used by the classloader
    private List<URL> classpathUrls = new ArrayList<URL>();

    // the classloader instance used by this factory
    // private URLClassLoader classLoader;
    private SimpleCollectorFactory() {
        StopWatch.start(true);

        try {
            // by default, use the current directory as a resource for the classloader
            classpathUrls.add((new File(".")).toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        // initialize the classloader
//        initClassLoader();
        StopWatch.stop(true);
    }

    @Override
    public void addLibrary(String libraryName) throws Exception {
        StopWatch.start(true);
        addUri("jar:file:" + libraryName + "!/");
        StopWatch.stop(true);
    }

    @Override
    public void addUri(String url) throws Exception {
        StopWatch.start(true);
        // add the URL to the collection
        try {
            classpathUrls.add(new URL(url));
        } catch (MalformedURLException e) {
            // the URL specified is not valid
            throw new Exception("The URL specified as a classpath source is invalid", e);
        }
//        // necessary to re-init the classloader using the updated collection of URLs
//        initClassLoader();
        StopWatch.stop(true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(String className, Object... constructorArgs) throws Exception {
        StopWatch.start(true);
        // class and constructor used to get an instance
        Class<T> newClass = null;
        Constructor<T> newClassConstructor = null;
        // derive the argument types from the provided arguments
        Class[] argTypes = null;
        if (constructorArgs != null) {
            if (constructorArgs != null) {
                argTypes = new Class[constructorArgs.length];
                if (IO.logFlag) {
                    IO.log("constructorArgs length is " + constructorArgs.length);
                }
                for (int i = 0, im = constructorArgs.length; i < im; i++) {
                    if (constructorArgs[i] != null) {
                        argTypes[i] = constructorArgs[i].getClass();
                    } else if (constructorArgs.length == 1) {
                        argTypes = null;
                        constructorArgs = null;
                    } else {
                        argTypes[i] = null;
                    }
                }
            } else {
                argTypes = new Class[0];
            }
        }
        // first, try using the current classloader to get the class
        try {
            newClass = (Class<T>) Class.forName(className, true,
                    Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            // not found, try using the local classloader to get the class
            try {
                newClass = (Class<T>) Class.forName(className, true,
                        getClassLoader());
            } catch (ClassNotFoundException cnfe) {
                // class could not be found using either method - fatal
                throw new Exception("Unable to find " + className, cnfe);
            }
        }
        // next, get the constructor that takes the specified argument types
        try {
            newClassConstructor = newClass.getConstructor(argTypes);
        } catch (NoSuchMethodException e) {
            throw new Exception("Class " + className
                    + " requires a constructor accepting"
                    + " an Object as an argument", e);
        } catch (SecurityException e) {
            throw new Exception("Class " + className + " lacks the necessary "
                    + "privileges to execute in this environment", e);
        }
        // object to return as the new instance
        T newObjectInstance = null;
        // use the constructor to get the new isntance Object
        try {
            newObjectInstance = newClassConstructor.newInstance(constructorArgs);
        } catch (InvocationTargetException e) {
            throw new Exception("Exception occurred during instantiation of "
                    + className + ": " + e.getCause(), e.getCause());
        }
        // provide the new instance object to the caller
        StopWatch.stop(true);
        return newObjectInstance;
    }

//    /**
//     * Set the classloader used by this factory, using the URLs previously set
//     */
//    private void initClassLoader() {
//        classLoader = new URLClassLoader(classpathUrls.toArray(new URL[classpathUrls.size()]));
//    }
    private URLClassLoader getClassLoader() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return new URLClassLoader(classpathUrls.toArray(new URL[classpathUrls.size()]));
    }
}

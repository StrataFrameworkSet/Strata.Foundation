// ##########################################################################
// # File Name:	SingletonProxy.java
// ##########################################################################

package strata.foundation.core.utility;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * A dynamic proxy class that manages singletons for any type.
 */
public 
class SingletonProxy 
	implements InvocationHandler
{
	private static Map<Object,Object> theirInstances
										= new HashMap<Object,Object>();
	private Object itsInstance;
	
	/************************************************************************
	 * Creates a new {@code SingletonProxy}. 
	 *
	 * @param instance a singleton instance
	 */
	protected 
	SingletonProxy(Object instance)
	{
		super();
		itsInstance = instance;
	}

	/************************************************************************
	 * Forwards method invocations to the singleton instance.
	 * <p>
	 * {@inheritDoc} 
	 */
	@Override
	public Object
	invoke(Object proxy,Method method,Object[] args)
		throws Throwable
	{
		Object result = null;
		
		try
		{
			result = method.invoke( itsInstance,args );
		}
		catch (InvocationTargetException e)
		{
			throw e.getTargetException();
		}
		return result;
	}
	
	/************************************************************************
	 * Compares this singleton with another object. 
	 *
	 * @param 	other	object being compared to singleton
	 * @return	true if the singleton instance and the other 
	 * 			are the same object.
	 */
	public boolean 
	isSame(Object other)
	{
		return itsInstance == other;
	}
	
	/************************************************************************
	 * Sets the singleton instance for the specified class. 
	 *
	 * @param <S>	   singleton type
	 * @param c		   singleton's class object
	 * @param instance the singleton instance
	 * 
	 * @postcondition  SingletonProxy.getInstance( c ).isSame( instance )
	 */
	public 
	static <S> void 
	setInstance(Class<S> c,S instance)
	{
		synchronized (SingletonProxy.class)
		{
			theirInstances.put( c,instance );
		}
	}
	
	/************************************************************************
	 * Releases the reference to the singleton instance 
	 * for the specified class. 
	 *
	 * @param <S>	singleton type
	 * @param c		singleton class object	
	 */
	public
	static <S> void 
	clearInstance(Class<S> c)
	{
		synchronized (SingletonProxy.class)
		{
			theirInstances.remove( c );
		}
	}
	
	/************************************************************************
	 * Gets the singleton instance for the specified class. 
	 *
	 * @param <S>	singleton type
	 * @param c		singleton class object
	 * @return		singleton instance associated with class object
	 */
	public 
	static <S> S 
	getInstance(Class<S> c)
	{
		synchronized (SingletonProxy.class)
		{
			Object instance = theirInstances.get( c );
			Object proxy    = Proxy.newProxyInstance(
				    			instance.getClass().getClassLoader(),
				    			instance.getClass().getInterfaces(),
				    			new SingletonProxy( instance ) );
			
			return c.cast( proxy );
		}
	}

}


// ##########################################################################

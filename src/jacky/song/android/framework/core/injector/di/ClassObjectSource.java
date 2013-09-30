package jacky.song.android.framework.core.injector.di;

import static java.lang.String.format;

// TODO javadoc
class ClassObjectSource<T, S extends T> extends ObjectSource<T> {
	private Class<S> clazz;

	ClassObjectSource(BeanFactory aDependencyInjectingObjectFactory, Class<S> aClass) {
		super(aDependencyInjectingObjectFactory);
		if (aClass.isInterface()) {
			throw new IllegalArgumentException(format(
					"Provided class must be a concrete type, however %s is an interface.", aClass.getName()));
		}
		clazz = aClass;
	}

	@Override
   Class<S> getObjectType() {
      return clazz;
   }

   @Override
   public T getObject() {
		final T object;
		try {
			object = clazz.newInstance();
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
		new DependencyInjector(dependencyInjectingObjectFactory).injectDependenciesForClassHierarchy(object);
		return object;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
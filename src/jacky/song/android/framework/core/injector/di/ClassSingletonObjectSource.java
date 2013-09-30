package jacky.song.android.framework.core.injector.di;

import static java.lang.String.format;

//TODO javadoc
class ClassSingletonObjectSource<T, S extends T> extends ObjectSource<T> {
	private final BeanFactory dependencyInjectingObjectFactory;

	private Class<S> clazz;

	private S singleObject;

	ClassSingletonObjectSource(BeanFactory dependencyInjectingObjectFactory,
			BeanFactory aDependencyInjectingObjectFactory, Class<S> aClass) {
		super(aDependencyInjectingObjectFactory);
		if (aClass.isInterface()) {
			throw new IllegalArgumentException(format(
					"Provided class must be a concrete type, however %s is an interface.", aClass.getName()));
		}
		this.dependencyInjectingObjectFactory = dependencyInjectingObjectFactory;
		clazz = aClass;
	}

   @Override
   Class<S> getObjectType() {
      return clazz;
   }

	@Override
	public T getObject() {
		if (singleObject == null) {
			try {
				singleObject = clazz.newInstance();
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			}
			new DependencyInjector(dependencyInjectingObjectFactory).injectDependenciesForClassHierarchy(singleObject);
		}
		return singleObject;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
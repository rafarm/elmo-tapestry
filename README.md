# elmo-tapestry
OpenRDF Elmo Tapestry integration module

Provides autobuilded `ElmoManager` configured through `ElmoContextProvider` (mapped `ElmoManagerFactory`) service contributions.

Exemple:

```
public class AppModule {
  public static void bind(ServiceBinder binder) {
    ManagerBuilder builder = new ManagerBuilder();
    binder.bind(ElmoManager.class, builder).withId("DefaultContext").scope(ScopeConstants.PERTHREAD);
    binder.bind(ElmoManager.class, builder).withId("OtherContext").scope(ScopeConstants.PERTHREAD);
  }
  
  public void contributeElmoContextProvider(MappedConfiguration<String,ElmoContext> contrib) {
    ElmoModule module = new ElmoModule();
    module.addConcept(Person.class);
    
    Repository repository = new SailRepository( new MemoryStore() );
    try {
      repository.initialize();
    } catch (RepositoryException e) {
      e.printStackTrace();
    }
    
    ElmoContext context = new ElmoContext(module,repository);
    contrib.add("DefaultContext", context);
    
    repository = new SailRepository( new MemoryStore() );
    try {
      repository.initialize();
    } catch (RepositoryException e) {
      e.printStackTrace();
    }
    
    context = new ElmoContext(module,repository);
    contrib.add("OtherContext", context);
  }
}
```

#### Rafa Rubio

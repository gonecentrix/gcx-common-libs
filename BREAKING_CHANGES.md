# Breaking changes for upcoming release

* `lockTimeoutMs` property moved from `spring.datasource` to `grandcentrix.datasource`
* `lockTimeoutMs` renamed to `lockTimeout` and can now be specified as Duration with Milliseconds as the default unit
* `BaseLibraryTest` is internal now as it shouldn't be used outside of `base-entity` anyway. If you need something 
similar, please recreate it per project with project specific settings
* The test base classes like `BaseDatabaseIntegrationTest` don't exist anymore and have been superseded by the
annotations `DatabaseIntegrationTest`, `DataJpaIntegrationTest` and `SpringBootIntegrationTest`
* `DataJpaIntegrationTest` sets `Transactional` implicitly. If your tests depend on not being within a transaction
  (or you need more control over that) you can use `@Transactional(propagation = Propagation.NEVER)`
# Spring Batch Auto Configuration Classes

If you annotate `EnableBatchProcessing`, spring application will run some configuration classes automatically.

1. SimpleBatchConfiguration
    - Create JobBuilderFactory and StepBuilderFactory.
    - Create components of spring batch as proxy object.

2. BatchConfigurerConfiguration
    1. BasicBatchConfigurer
        - Instantiate target object of proxy created on SimpleBatchConfiguration.
    2. JpaBasicBatchConfigurer
        - Instantiate batch objects about JPA.

    - If you want, you can implement BatchConfigurer and apply it to your batch application.

3. BatchAutoConfiguration
    - Register as bean JobLauncherApplicationRunner that executes all jobs registered as bean on startup, since it is
      implementation of ApplicationRunner that will be executed on startup.

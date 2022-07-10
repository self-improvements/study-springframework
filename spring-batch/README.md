# Spring Batch Auto Configuration Classes

If you annotate `EnableBatchProcessing`, spring application will run some configuration classes automatically.

### SimpleBatchConfiguration

- Create JobBuilderFactory and StepBuilderFactory.
- Create components of spring batch as proxy object.

### BatchConfigurerConfiguration

1. BasicBatchConfigurer
    - Instantiate target object of proxy created on SimpleBatchConfiguration.
2. JpaBasicBatchConfigurer
    - Instantiate batch objects about JPA.

If you want, you can implement `BatchConfigurer` and apply it to your batch application.

### BatchAutoConfiguration

- Register as bean JobLauncherApplicationRunner that executes all jobs registered as bean on startup, since it is
  implementation of ApplicationRunner that will be executed on startup.

# Metadata Schema

The spring batch metadata tables match the spring batch java object respectively.

- `JobInstance` (core): BATCH_JOB_INSTANCE
- `JobExecution` (core): BATCH_JOB_EXECUTION
- `JobParameters` (core): BATCH_JOB_EXECUTION_PARAMS
- `StepExecution` (core): BATCH_STEP_EXECUTION
- `ExecutionContext` (infrastructure): BATCH_JOB_EXECUTION_CONTEXT, BATCH_STEP_EXECUTION_CONTEXT

`JobRepository` is responsible for saving and storing each java object into its matched table.

[Reference](https://docs.spring.io/spring-batch/docs/current/reference/html/schema-appendix.html#metaDataSchema)

### Job

- BATCH_JOB_INSTANCE
- BATCH_JOB_EXECUTION
- BATCH_JOB_EXECUTION_PARAMS
- BATCH_JOB_EXECUTION_CONTEXT: Serialize all of job-level data as JSON. That data is sharable to its steps.

### Step

- BATCH_STEP_EXECUTION
- BATCH_STEP_EXECUTION_CONTEXT: Serialize all of step-level data as JSON. That data is not sharable to other steps.

# Domain Class

### Job

This is the highest concept in spring batch architecture. `Job` is a specification of one batch work. It describes how
batch work is configured and executed. It must contain one step at least.

### JobInstance

This is created when `Job` is executed by `JobLauncher`.

### Differences between Job and JobInstance

`Job` is the specification of one batch. Every execution the content of `Job` is the same, but parameters or other
environment differ from each execution. `JobInstance` contains all data of execution to be stored as metadata at that
every time. There is one `Job` and many `JobInstance`.

If a pair of job name and job parameters doesn't exist in the database, create a new instance of `JobInstance`, or that
already exists, return an instance of `JobInstance` using job name and job parameters stored in the database.

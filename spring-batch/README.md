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
batch work is configured and executed. It must contain one `Step` at least.

### JobInstance

This is created when `Job` is executed by `JobLauncher`.

##### Differences between Job and JobInstance

`Job` is the specification of one batch. Every execution the content of `Job` is the same, but parameters or other
environment differ from each execution. `JobInstance` contains all data of execution to be stored as metadata at that
every time. There is one `Job` and many `JobInstance`.

If a pair of job name and job parameters doesn't exist in the database, create a new instance of `JobInstance`, or that
already exists, return an instance of `JobInstance` using job name and job parameters stored in the database.

### JobParameter

One or more `JobParameter` is contained in `JobParameters`. This uniquely identifies each `JobInstance` in the database
because the spring batch doesn't allow one job to be executed with the same parameters.
`JobInstance` and `JobParameters` is one-to-one relationship.

##### Creation and Binding

- On startup: `java -jar batch-app.jar name=John`
- Code: `JobParameterBuilder`, `DefaultJobParameterConverter`
- SpEL: `@Value("#{jobParameter['name']}") String name`, `@JobScope`, `@StepScope`

##### Supported Types

These are specified in `ParameterType`.

- STRING
- DATE
- LONG
- DOUBLE

##### Binding with Program Arguments

Append program arguments like this `{key}={value}`.
(Not `--{key}={value}`. This is regarded as spring environment value)

```bash
java -jar batch-app.jar name="John Smith" birthDate=2022/07/01 age=45 weight=80.6
```

Unfortunately, these arguments are bound as string type. If you want to cast arguments to `Date`, `Long` or `Double`,
specify type after argument name like this, `{key}(type)={value}`.

```bash
java -jar batch-app.jar name(string)="John Smith" birthDate(date)=2022/07/01 age(long)=45 weight(double)=80.6
```

The specified type must consist only of lower cases, or the name of argument will be bound as string type containing
type name.

### JobExecution

This object which means try of a `JobInstance` has information of job execution. It is created on every `JobInstance`
execution.

##### Relationship between JobInstance and JobExecution

When `JobExecution.status: BatchStatus` is `COMPLETED`, the spring batch regards corresponding `JobInstance` as
completed. It means you can never execute that `Job` with the same parameters — JobInstanceAlreadyCompleteException.

When `JobExecution.status: BatchStatus` is `FAILED`, the spring batch regards the corresponding `JobInstance` as
incomplete. It means you can execute that `Job` even with the same parameters until it succeeds.

### Step

`Job` consists of one or more `Step`. `Step` is one of many steps of a corresponding `Job`. It describes how batch data
is loaded, processed and stored in detail.

##### Implementations

- TaskletStep — custom or chunk oriented
- FlowStep
- JobStep
- PartitionStep

### StepExecution

This object which means try of a `Step` has information of step execution. It is created on every `Step` execution.

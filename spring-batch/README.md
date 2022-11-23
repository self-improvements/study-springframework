# Spring Batch Auto Configuration Classes

If you annotate `@EnableBatchProcessing`, spring application will run some configuration classes automatically.

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

See more
references [here](https://docs.spring.io/spring-batch/docs/current/reference/html/schema-appendix.html#metaDataSchema).

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
is loaded, processed and stored in detail. `Step` already successfully finished is not executed again by default though
corresponding `Job` is failed.

##### Implementations

- TaskletStep — customized or chunk oriented
- FlowStep
- JobStep
- PartitionStep

### StepExecution

This object which means try of a `Step` has information of step execution. It is created on every `Step` execution. If
previous `Step` is failed, `StepExecution` of the next `Step` is not created.

##### Relationship between JobExecution and StepExecution

`JobExecution` is successful only when all of `StepExecution` are successfully finished.

### StepContribution

This object stores the changes of each chunk process (including `Tasklet`) in buffer and updates its state
with `StepExecution.apply(StepContribution)` before chunk commit. `StepExecution` creates this.

### ExecutionContext

This object has the states of `JobExecution` and `StepExecution` and can be shared in the scope. Those are serialized to
JSON and stored into the database as metadata for spring batch maintenance. This is normally used when `Job` restarts.

##### Shared scope

`JobExecution` has a `ExecutionContext` and it can be shared to other steps, but not other jobs.
`StepExecution` has a `ExecutionContext` and it can't be shared to other steps.

### JobRepository

This object stores all the metadata related to spring batch. All CRUD functions is invoked by this in the
implementations of `JobLauncher`, `Job` and `Step`. `JobBuilder` gives an implementation of `JobRepository` to
corresponding `Job`.

##### Configuration JobRepository

Annotating `EnableBatchProcessing` makes `JobRepository` registered as a bean. You can configure `JobRepository` with
implementing `BatchConfigurer` or extending `BasicBatchConfigurer`.

- JDBC: `JobRepositoryFactoryBean`
- In Memory: `MapJobRepositoryFactoryBean` — Not stored domain objects. (`ResourcelessTransactionManager`)

### JobLauncher

This accepts `Job` and `JobParameters` as arguments, performs batch processing and returns `JobExecution`.

##### Synchronous Execution

`SyncTaskExecutor` is the implementation of `TaskExecutor` — suitable for a scheduler.

```text
     ┌──────┐          ┌───────────┐                      ┌───┐          ┌────┐
     │Client│          │JobLauncher│                      │Job│          │Step│
     └──┬───┘          └─────┬─────┘                      └─┬─┘          └─┬──┘
        │       run()        │                              │              │   
        │───────────────────>│                              │              │   
        │                    │                              │              │   
        │                    │          execute()           │              │   
        │                    │─────────────────────────────>│              │   
        │                    │                              │              │   
        │                    │                              │  handle()    │   
        │                    │                              │─────────────>│   
        │                    │                              │              │   
        │                    │                              │              │   
        │                    │                              │<─────────────│   
        │                    │                              │              │   
        │                    │ExitStatus.FINISHED or FAILED │              │   
        │                    │<─────────────────────────────│              │   
        │                    │                              │              │   
        │    JobExecution    │                              │              │   
        │<───────────────────│                              │              │   
     ┌──┴───┐          ┌─────┴─────┐                      ┌─┴─┐          ┌─┴──┐
     │Client│          │JobLauncher│                      │Job│          │Step│
     └──────┘          └───────────┘                      └───┘          └────┘
```

##### Asynchronous Execution

`SimpleAsyncTaskExecutor` is the implementation of `TaskExecutor` — suitable for HTTP request.

```text
     ┌──────┐                       ┌───────────┐                      ┌───┐          ┌────┐
     │Client│                       │JobLauncher│                      │Job│          │Step│
     └──┬───┘                       └─────┬─────┘                      └─┬─┘          └─┬──┘
        │              run()              │                              │              │   
        │────────────────────────────────>│                              │              │   
        │                                 │                              │              │   
        │ JobExecution(ExitStatus.UNKNOWN)│                              │              │   
        │<────────────────────────────────│                              │              │   
        │                                 │                              │              │   
        │                                 │           execute()          │              │   
        │                                 │─────────────────────────────>│              │   
        │                                 │                              │              │   
        │                                 │                              │   handle()   │   
        │                                 │                              │─────────────>│  
        │                                 │                              │              │   
        │                                 │                              │              │   
        │                                 │                              │<─────────────│   
        │                                 │                              │              │   
        │                                 │ ExitStatus.FINISHED or FAILED│              │   
        │                                 │<─────────────────────────────│              │   
     ┌──┴───┐                       ┌─────┴─────┐                      ┌─┴─┐          ┌─┴──┐
     │Client│                       │JobLauncher│                      │Job│          │Step│
     └──────┘                       └───────────┘                      └───┘          └────┘
```

# Job

### Execution Option

`spring.batch.job.names: ${job.name:NONE}`

Single job name

```bash
java -jar batch-app.jar --job.name=simpleJob
```

Multiple job names

```bash
java -jar batch-app.jar --job.name=simpleJob,greetingJob
```

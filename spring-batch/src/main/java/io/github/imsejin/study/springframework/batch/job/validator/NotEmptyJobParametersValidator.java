package io.github.imsejin.study.springframework.batch.job.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class NotEmptyJobParametersValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if (parameters == null) {
            throw new JobParametersInvalidException("JobParameters cannot be null");
        }

        if (parameters.getParameters().isEmpty()) {
            throw new JobParametersInvalidException("JobParameters must have one or more parameters at least");
        }
    }

}

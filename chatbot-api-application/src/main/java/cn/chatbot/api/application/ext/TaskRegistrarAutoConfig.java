package cn.chatbot.api.application.ext;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

public class TaskRegistrarAutoConfig implements EnvironmentAware, SchedulingConfigurer {
    @Override
    public void setEnvironment(Environment environment) {

    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

    }
}

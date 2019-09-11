// Taken from https://gitlab.com/remal/gradle/plugins
// Copyright (c) Semyon Levin - all rights reserved 

package hjb

import name.remal.KotlinAllOpen
import name.remal.gradle_plugins.dsl.*
import name.remal.gradle_plugins.dsl.extensions.*
import name.remal.gradle_plugins.plugins.assertj.AssertJGenerate
import name.remal.gradle_plugins.plugins.java.JavaAnyPluginId
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.SourceSet.MAIN_SOURCE_SET_NAME
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.VerificationTask
import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.language.base.plugins.LifecycleBasePlugin.CHECK_TASK_NAME

@Plugin(
    id = "hjb.DisableTests",
    description = "Plugin that disables all check and test tasks. Also this plugin disables test source sets tasks.",
    tags = ["java"]
)
class DisableTestsPlugin : BaseReflectiveProjectPlugin() {

    @HighestPriorityPluginAction
    fun Project.`Display warning message`() {
        "| Tests are disabled for project '$path' |".let { msg ->
            logger.warn("-".repeat(msg.length))
            logger.warn(msg)
            logger.warn("-".repeat(msg.length))
        }
    }

    @HighestPriorityPluginAction
    fun disableTasksFromContainer(project: Project) {
        project.extensions.createWithAutoName(DisabledTasksContainer::class.java)

        project.setupTasksDependenciesAfterEvaluateOrNow(Int.MAX_VALUE) {
            val tasksToDisable = it[DisabledTasksContainer::class.java].tasks
            if (tasksToDisable.none(Task::isRequested)) {
                tasksToDisable.forEach { task ->
                    task.enabled = false
                    task.setDependsOn(emptyList<String>())
                }
            }
        }
    }

    @PluginAction
    fun TaskContainer.`Skip test tasks by type`(project: Project) {
        project.setupTasksDependenciesAfterEvaluateOrNow(Int.MAX_VALUE - 1) {
            val tasksToDisable = project[DisabledTasksContainer::class.java].tasks
            forEach task@{ task ->
                if (task is AbstractTestTask
                    || task is AssertJGenerate
                    || task is VerificationTask
                ) {
                    tasksToDisable.add(task)
                    return@task
                }

                if (task.name == CHECK_TASK_NAME) {
                    tasksToDisable.add(task)
                    return@task
                }
            }
        }
    }

    @PluginActionsGroup(order = Int.MAX_VALUE)
    @WithPlugins(JavaAnyPluginId::class)
    inner class `For 'java' plugin` {

        @PluginAction
        fun TestSourceSetContainer.`Skip test tasks by type`(project: Project, tasks: TaskContainer, sourceSets: SourceSetContainer) {
            project.setupTasksDependenciesAfterEvaluateOrNow(Int.MAX_VALUE - 1) {
                val tasksToDisable = project[DisabledTasksContainer::class.java].tasks
                val mainSourceSetTaskNames = sourceSets.findByName(MAIN_SOURCE_SET_NAME)?.sourceSetTaskNames ?: emptySet()
                forEach { sourceSet ->
                    tasks.forEach task@{ task ->
                        if (task.name in mainSourceSetTaskNames) {
                            return@task
                        }
                        if (task.name in sourceSet.sourceSetTaskNames) {
                            tasksToDisable.add(task)
                        } else if (task is AbstractCompile && task.isCompilingSourceSet(sourceSet)) {
                            tasksToDisable.add(task)
                        } else if (task is SourceTask && task.isProcessingSourceSet(sourceSet)) {
                            tasksToDisable.add(task)
                        }
                    }
                }
            }
        }

    }
}

@KotlinAllOpen
private class DisabledTasksContainer {
    val tasks = mutableSetOf<Task>()
}

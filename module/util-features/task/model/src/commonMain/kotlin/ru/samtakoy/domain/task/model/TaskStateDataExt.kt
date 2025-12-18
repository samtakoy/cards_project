package ru.samtakoy.domain.task.model

fun TaskStateData.isFinished(): Boolean {
    return this == TaskStateData.Success ||
        this == TaskStateData.Cancelled ||
        (this is TaskStateData.Error)
}
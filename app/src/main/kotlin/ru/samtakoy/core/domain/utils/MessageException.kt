package ru.samtakoy.core.domain.utils

class MessageException(val msgId: Int) : Exception(msgId.toString()) {
}
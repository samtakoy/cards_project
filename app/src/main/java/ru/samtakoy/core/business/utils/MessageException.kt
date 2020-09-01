package ru.samtakoy.core.business.utils

class MessageException(val msgId: Int) : Exception(msgId.toString()) {
}
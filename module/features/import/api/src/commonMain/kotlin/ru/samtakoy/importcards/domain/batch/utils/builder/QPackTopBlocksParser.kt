package ru.samtakoy.importcards.domain.batch.utils.builder

class QPackTopBlocksParser {
    var isFinished: Boolean = false
        private set
    private val mStringBuilders: MutableMap<String, StringBuilder>
    private var mCurStringBuilder: StringBuilder? = null

    init {
        mStringBuilders = mutableMapOf()
    }

    suspend fun processLine(line: String, lowerLine: String) {
        var line = line
        val prefix = readPrefix(lowerLine)
        if (prefix === sStopPrefix) {
            this.isFinished = true
            return
        } else if (prefix.length > 0) {
            val idx = sMyPrefixes.indexOf(prefix)

            if (idx < 0) {
                mCurStringBuilder = null
            } else {
                mCurStringBuilder = StringBuilder()
                mStringBuilders.put(prefix, mCurStringBuilder!!)
            }
            line = line.substring(prefix.length).trim { it <= ' ' }
        }

        mCurStringBuilder?.let { curStringBuilder ->
            if (curStringBuilder.isNotEmpty()) {
                curStringBuilder.append(CBuilderConst.LINE_BREAK)
            }
            curStringBuilder.append(line)
        }
    }

    private fun readPrefix(line: String): String {
        for (prefix in sMyPrefixes) {
            if (line.startsWith(prefix)) {
                return prefix
            }
        }
        if (line.startsWith(sStopPrefix)) {
            return sStopPrefix
        }
        return ""
    }

    fun get(prefix: String, defaultValue: String): String {
        return if (has(prefix)) mStringBuilders.get(prefix).toString() else defaultValue
    }

    fun has(prefix: String): Boolean {
        return mStringBuilders.containsKey(prefix)
    }

    companion object {
        private val sMyPrefixes = listOf(
            CBuilderConst.QPACK_ID_PREFIX,
            CBuilderConst.TITLE_PREFIX,
            CBuilderConst.DESC_PREFIX,
            CBuilderConst.DATE_PREFIX,
            CBuilderConst.VIEWS_PREFIX,
            CBuilderConst.TAGS_PREFIX,
            CBuilderConst.TAGS_PREFIX2
        )
        private val sStopPrefix = CBuilderConst.QUESTION_PREFIX
    }
}
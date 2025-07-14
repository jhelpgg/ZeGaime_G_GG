package fr.khelp.zegaime.utils.collections.cache

internal data class CacheElement<out V>(var lastTime: Long, val value: V)

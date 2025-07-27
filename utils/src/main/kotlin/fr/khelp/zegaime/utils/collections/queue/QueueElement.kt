package fr.khelp.zegaime.utils.collections.queue

internal class QueueElement<T>(val element : T, var next : QueueElement<T>? = null)

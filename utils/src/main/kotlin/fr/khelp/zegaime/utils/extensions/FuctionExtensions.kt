package fr.khelp.zegaime.utils.extensions

import fr.khelp.zegaime.utils.comparators.FunctionComparator

val <T : Any> ((T, T) -> Int).comparator : Comparator<T> get() = FunctionComparator(this)

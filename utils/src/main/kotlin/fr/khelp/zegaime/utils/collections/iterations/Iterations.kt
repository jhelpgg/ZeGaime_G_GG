package fr.khelp.zegaime.utils.collections.iterations

/**
 * View an iterator that shows only elements match the given criteria.
 *
 * Faster and take less memory than [Iterable.filter]
 */
fun <T : Any> Iterator<T>.select(criteria : (T) -> Boolean) : Iterator<T> =
    if (this is IteratorSelected)
    {
        IteratorSelected({ element -> this.criteria(element) && criteria(element) }, this.iterator)
    }
    else
    {
        IteratorSelected(criteria, this)
    }

/**
 * View iterator that transform all source elements with given [transformation]
 *
 * Faster and take less memory than [Iterable.map]
 */
fun <S : Any, D : Any> Iterator<S>.transform(transformation : (S) -> D) : Iterator<D> =
    IteratorTransformed(transformation, this)

inline fun <T : Any, reified D : T> Iterator<T>.selectInstance() : Iterator<D> =
    this.select { element -> element is D }.transform { element -> element as D }

/**
 * View iterable that shows only elements match the given criteria.
 *
 * Faster and take less memory than [Iterable.filter]
 */
fun <T : Any> Iterable<T>.select(criteria : (T) -> Boolean) : Iterable<T> =
    if (this is IterableSelected)
    {
        IterableSelected({ element -> this.criteria(element) && criteria(element) }, this.iterable)
    }
    else
    {
        IterableSelected(criteria, this)
    }

/**
 * View iterable that transform all source elements with given [transformation]
 *
 * Faster and take less memory than [Iterable.map]
 */
fun <S : Any, D : Any> Iterable<S>.transform(transformation : (S) -> D) : Iterable<D> =
    IterableTransformed(transformation, this)

inline fun <T : Any, reified D : T> Iterable<T>.selectInstance() : Iterable<D> =
    this.select { element -> element is D }.transform { element -> element as D }

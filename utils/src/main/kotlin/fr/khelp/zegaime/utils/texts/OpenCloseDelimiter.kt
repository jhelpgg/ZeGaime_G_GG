package fr.khelp.zegaime.utils.texts

/**
 * Delimiter with an open and close characters
 * @param open Open character
 * @param close Close character
 * @param countOpenClose Indicates if have to count open/close characters
 * @param counter Internal counter
 */
internal data class OpenCloseDelimiter(val open : Char,
                                       val close : Char,
                                       val countOpenClose : Boolean,
                                       var counter : Int = 0)

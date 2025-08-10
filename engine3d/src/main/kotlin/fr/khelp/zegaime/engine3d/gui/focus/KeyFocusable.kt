package fr.khelp.zegaime.engine3d.gui.focus

/**
 * Indicates a component that cans take the key focus
 */
interface KeyFocusable
{
    /**
     * Called when a character is received
     *
     * @param character Received character
     */
    fun receiveCharacter(character : Char)

    /**
     * Called when a special key received
     *
     * @param key Key received
     */
    fun receiveKey(key : Key)

    /**
     * Called when focus lost
     */
    fun onFocusLost()
}
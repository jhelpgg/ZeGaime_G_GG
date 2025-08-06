package fr.khelp.zegaime.engine3d.gui.message

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.resources.ERROR_IMAGE_64
import fr.khelp.zegaime.resources.IDEA_IMAGE_64
import fr.khelp.zegaime.resources.INFORMATION_IMAGE_64
import fr.khelp.zegaime.resources.MESSAGE_IMAGE_64
import fr.khelp.zegaime.resources.QUESTION_IMAGE_64
import fr.khelp.zegaime.resources.WARNING_IMAGE_64

enum class MessageType(val icon : () -> GameImage)
{
    MESSAGE({ MESSAGE_IMAGE_64 }),
    INFORMATION({ INFORMATION_IMAGE_64 }),
    IDEA({ IDEA_IMAGE_64 }),
    QUESTION({ QUESTION_IMAGE_64 }),
    WARNING({ WARNING_IMAGE_64 }),
    ERROR({ ERROR_IMAGE_64 })
}

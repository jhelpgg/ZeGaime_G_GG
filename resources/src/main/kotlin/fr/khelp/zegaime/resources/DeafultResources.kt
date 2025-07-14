package fr.khelp.zegaime.resources

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.sounds.Sound
import fr.khelp.zegaime.utils.logs.debug
import fr.khelp.zegaime.utils.logs.mark
import fr.khelp.zegaime.utils.source.ClassSource

val defaultResources : Resources by lazy { Resources(ClassSource(Resources::class.java)) }

val defaultTexts : ResourcesText by lazy { defaultResources.resourcesText("defaultTexts") }

const val YES = "yes"

const val NO = "no"

const val CANCEL = "cancel"

const val SAVE = "save"

const val SAVE_AS = "saveAs"

const val LOAD = "load"

const val OTHER = "other"

const val OPEN = "open"

const val OK = "ok"

val WARNING_IMAGE_16 : GameImage get() = defaultResources.imageThumbnail("warning.png", 16, 16)

val WARNING_IMAGE_32 : GameImage get() = defaultResources.imageThumbnail("warning.png", 32, 32)

val WARNING_IMAGE_64 : GameImage get() = defaultResources.imageThumbnail("warning.png", 64, 64)

val ERROR_IMAGE_16 : GameImage get() = defaultResources.imageThumbnail("error.png", 16, 16)

val ERROR_IMAGE_32 : GameImage get() = defaultResources.imageThumbnail("error.png", 32, 32)

val ERROR_IMAGE_64 : GameImage get() = defaultResources.imageThumbnail("error.png", 64, 64)

val IDEA_IMAGE_16 : GameImage get() = defaultResources.imageThumbnail("idea.png", 16, 16)

val IDEA_IMAGE_32 : GameImage get() = defaultResources.imageThumbnail("idea.png", 32, 32)

val IDEA_IMAGE_64 : GameImage get() = defaultResources.imageThumbnail("idea.png", 64, 64)

val INFORMATION_IMAGE_16 : GameImage get() = defaultResources.imageThumbnail("information.png", 16, 16)

val INFORMATION_IMAGE_32 : GameImage get() = defaultResources.imageThumbnail("information.png", 32, 32)

val INFORMATION_IMAGE_64 : GameImage get() = defaultResources.imageThumbnail("information.png", 64, 64)

val MESSAGE_IMAGE_16 : GameImage get() = defaultResources.imageThumbnail("message.png", 16, 16)

val MESSAGE_IMAGE_32 : GameImage get() = defaultResources.imageThumbnail("message.png", 32, 32)

val MESSAGE_IMAGE_64 : GameImage get() = defaultResources.imageThumbnail("message.png", 64, 64)

val QUESTION_IMAGE_16 : GameImage get() = defaultResources.imageThumbnail("question.png", 16, 16)

val QUESTION_IMAGE_32 : GameImage get() = defaultResources.imageThumbnail("question.png", 32, 32)

val QUESTION_IMAGE_64 : GameImage get() = defaultResources.imageThumbnail("question.png", 64, 64)

val CHORD_SOUND : Sound get() = defaultResources.sound("chord.mp3")

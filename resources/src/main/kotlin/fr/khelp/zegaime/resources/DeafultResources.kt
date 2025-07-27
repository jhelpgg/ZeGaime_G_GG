package fr.khelp.zegaime.resources

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.sounds.Sound
import fr.khelp.zegaime.utils.logs.debug
import fr.khelp.zegaime.utils.logs.mark
import fr.khelp.zegaime.utils.source.ClassSource

/**
 * The default resources.
 */
val defaultResources: Resources by lazy { Resources(ClassSource(Resources::class.java)) }

/**
 * The default texts.
 */
val defaultTexts: ResourcesText by lazy { defaultResources.resourcesText("defaultTexts") }

/** Yes key */
const val YES = "yes"

/** No key */
const val NO = "no"

/** Cancel key */
const val CANCEL = "cancel"

/** Save key */
const val SAVE = "save"

/** Save as key */
const val SAVE_AS = "saveAs"

/** Load key */
const val LOAD = "load"

/** Other key */
const val OTHER = "other"

/** Open key */
const val OPEN = "open"

/** OK key */
const val OK = "ok"

/** Warning image 16x16 */
val WARNING_IMAGE_16: GameImage get() = defaultResources.imageThumbnail("warning.png", 16, 16)

/** Warning image 32x32 */
val WARNING_IMAGE_32: GameImage get() = defaultResources.imageThumbnail("warning.png", 32, 32)

/** Warning image 64x64 */
val WARNING_IMAGE_64: GameImage get() = defaultResources.imageThumbnail("warning.png", 64, 64)

/** Error image 16x16 */
val ERROR_IMAGE_16: GameImage get() = defaultResources.imageThumbnail("error.png", 16, 16)

/** Error image 32x32 */
val ERROR_IMAGE_32: GameImage get() = defaultResources.imageThumbnail("error.png", 32, 32)

/** Error image 64x64 */
val ERROR_IMAGE_64: GameImage get() = defaultResources.imageThumbnail("error.png", 64, 64)

/** Idea image 16x16 */
val IDEA_IMAGE_16: GameImage get() = defaultResources.imageThumbnail("idea.png", 16, 16)

/** Idea image 32x32 */
val IDEA_IMAGE_32: GameImage get() = defaultResources.imageThumbnail("idea.png", 32, 32)

/** Idea image 64x64 */
val IDEA_IMAGE_64: GameImage get() = defaultResources.imageThumbnail("idea.png", 64, 64)

/** Information image 16x16 */
val INFORMATION_IMAGE_16: GameImage get() = defaultResources.imageThumbnail("information.png", 16, 16)

/** Information image 32x32 */
val INFORMATION_IMAGE_32: GameImage get() = defaultResources.imageThumbnail("information.png", 32, 32)

/** Information image 64x64 */
val INFORMATION_IMAGE_64: GameImage get() = defaultResources.imageThumbnail("information.png", 64, 64)

/** Message image 16x16 */
val MESSAGE_IMAGE_16: GameImage get() = defaultResources.imageThumbnail("message.png", 16, 16)

/** Message image 32x32 */
val MESSAGE_IMAGE_32: GameImage get() = defaultResources.imageThumbnail("message.png", 32, 32)

/** Message image 64x64 */
val MESSAGE_IMAGE_64: GameImage get() = defaultResources.imageThumbnail("message.png", 64, 64)

/** Question image 16x16 */
val QUESTION_IMAGE_16: GameImage get() = defaultResources.imageThumbnail("question.png", 16, 16)

/** Question image 32x32 */
val QUESTION_IMAGE_32: GameImage get() = defaultResources.imageThumbnail("question.png", 32, 32)

/** Question image 64x64 */
val QUESTION_IMAGE_64: GameImage get() = defaultResources.imageThumbnail("question.png", 64, 64)

/** Chord sound */
val CHORD_SOUND: Sound get() = defaultResources.sound("chord.mp3")
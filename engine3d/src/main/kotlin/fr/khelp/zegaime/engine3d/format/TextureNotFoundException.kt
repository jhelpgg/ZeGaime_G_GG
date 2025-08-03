package fr.khelp.zegaime.engine3d.format

import java.lang.Exception

class TextureNotFoundException(textureName : String) : Exception("Texture '$textureName' not found!")

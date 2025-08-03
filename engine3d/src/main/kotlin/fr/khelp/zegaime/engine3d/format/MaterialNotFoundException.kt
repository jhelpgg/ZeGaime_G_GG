package fr.khelp.zegaime.engine3d.format

import java.lang.Exception

class MaterialNotFoundException(materialName : String) : Exception("Material '$materialName' not found!")

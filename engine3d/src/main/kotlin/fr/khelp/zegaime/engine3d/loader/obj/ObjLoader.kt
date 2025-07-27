package fr.khelp.zegaime.engine3d.loader.obj

import fr.khelp.zegaime.engine3d.dsl.face
import fr.khelp.zegaime.engine3d.geometry.Mesh
import fr.khelp.zegaime.engine3d.geometry.Point2D
import fr.khelp.zegaime.engine3d.geometry.Point3D
import fr.khelp.zegaime.engine3d.geometry.Vertex
import fr.khelp.zegaime.engine3d.loader.obj.options.ObjAsIs
import fr.khelp.zegaime.engine3d.loader.obj.options.ObjColorTexture
import fr.khelp.zegaime.engine3d.loader.obj.options.ObjOption
import fr.khelp.zegaime.engine3d.loader.obj.options.ObjUseNormalMap
import fr.khelp.zegaime.engine3d.scene.Node
import fr.khelp.zegaime.engine3d.scene.Object3D
import fr.khelp.zegaime.resources.Resources
import fr.khelp.zegaime.utils.logs.exception
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.regex.Pattern
import kotlin.math.sqrt

/** Regular expression that matches to a string not empty, composed of white spaces (space, tabulation, line return, line carriage) */
private var SPACES = Pattern.compile("\\s+")

/**
 * Load an `obj` file from resources
 *
 * @param name Name to give to the main node
 * @param path Resource path to load the `obj` file
 * @param resources Resources where get the `obj` file
 * @param objOption Option to use for the `obj` file
 *
 * @return Main node that contains all objects from the `obj` file
 */
fun objLoader(name : String, path : String, resources : Resources, objOption : ObjOption = ObjAsIs) : Node =
    objLoader(name, resources.inputStream(path), objOption)

/**
 * Load an `obj` file from stream
 *
 * @param name Name to give to the main node
 * @param inputStream Stream where read the `obj` file
 * @param objOption Option to use for the `obj` file
 *
 * @return Main node that contains all objects from the `obj` file
 */
fun objLoader(name : String, inputStream : InputStream, objOption : ObjOption = ObjAsIs) : Node
{
    val objRoot = Node(name)
    val description = createObjDescription(name, inputStream, objOption)
    val generateNormal = objOption == ObjColorTexture

    for (objObject in description.objects)
    {
        val object3D = Object3D(objObject.name, createMesh(objObject, generateNormal))
        objRoot.addChild(object3D)
    }

    return objRoot
}

/**
 * Creates an `obj` file description from a stream
 *
 * @param name Name to give to the main node
 * @param inputStream Stream where read the `obj` file
 * @param objOption Option to use for the `obj` file
 *
 * @return The `obj` file description
 */
private fun createObjDescription(name : String, inputStream : InputStream, objOption : ObjOption) : ObjDescription
{
    val objDescription = ObjDescription()
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))

    try
    {
        var line = bufferedReader.readLine()?.trim()
        var objObject = ObjObject("")

        while (line != null)
        {
            when
            {
                line.startsWith("o ")      ->
                {
                    if (objObject.name.isNotEmpty())
                    {
                        if (objObject.uvs.isEmpty())
                        {
                            objObject.uvs.add(Point2D())
                        }

                        if (objObject.normals.isEmpty())
                        {
                            objObject.normals.add(Point3D())
                        }

                        objDescription.objects.add(objObject)
                    }

                    objObject = ObjObject(line.substring(2).trim())
                }

                line.startsWith("usemtl ") ->
                {
                    if (objObject.name.isNotEmpty())
                    {
                        if (objObject.uvs.isEmpty())
                        {
                            objObject.uvs.add(Point2D())
                        }

                        if (objObject.normals.isEmpty())
                        {
                            objObject.normals.add(Point3D())
                        }

                        objDescription.objects.add(objObject)
                    }

                    objObject = objObject.subObject(line.substring(7).trim())
                }

                line.startsWith("v ")      ->
                {
                    val coordinates = line.substring(2)
                        .trim()
                        .split(SPACES)
                        .map { text -> text.toFloat() }
                    objObject.points.add(Point3D(coordinates[0], coordinates[1], coordinates[2]))
                }

                line.startsWith("vt ")     ->
                {
                    val coordinates = line.substring(3)
                        .trim()
                        .split(SPACES)
                        .map { text -> text.toFloat() }
                    objObject.uvs.add(Point2D(coordinates[0], 1f - coordinates[1]))

                    if (objOption is ObjUseNormalMap)
                    {
                        objObject.normals.add(objOption.normalMap[coordinates[0], 1f - coordinates[1]])
                    }
                }

                line.startsWith("vn ")     ->
                {
                    if (objOption !is ObjUseNormalMap)
                    {
                        val coordinates = line.substring(3)
                            .trim()
                            .split(SPACES)
                            .map { text -> text.toFloat() }
                        objObject.normals.add(Point3D(coordinates[0], coordinates[1], coordinates[2]))
                    }
                }

                line.startsWith("f ")      ->
                {
                    val objFace = ObjFace()

                    for (vertex in line.substring(2)
                        .trim()
                        .split(SPACES))
                    {
                        val indexes = vertex.split('/')

                        val point = indexes[0].toInt() - 1
                        var uv = 0
                        var normal = 0

                        if (indexes.size > 1)
                        {
                            if (indexes[1].isNotEmpty())
                            {
                                uv = indexes[1].toInt() - 1
                            }

                            if (objOption is ObjUseNormalMap)
                            {
                                normal = uv
                            }
                            else if (indexes.size > 2)
                            {
                                normal = indexes[2].toInt() - 1
                            }
                        }

                        objFace.vertices.add(ObjVertex(point, uv, normal))
                    }

                    objObject.faces.add(objFace)
                }
            }

            line = bufferedReader.readLine()?.trim()
        }

        if (objObject.name.isNotEmpty())
        {
            if (objObject.uvs.isEmpty())
            {
                objObject.uvs.add(Point2D())
            }

            if (objObject.normals.isEmpty())
            {
                objObject.normals.add(Point3D())
            }

            objDescription.objects.add(objObject)
        }
    }
    catch (exception : Exception)
    {
        exception(exception, "Failed to load OBJ : $name")
    }
    finally
    {
        bufferedReader.close()
    }

    return objDescription
}

/**
 * Creates mesh from `obj` object description
 *
 * @param objObject `obj` object description
 * @param generateNormal Whether it have to generate normals
 *
 * @return Creates mesh
 */
private fun createMesh(objObject : ObjObject, generateNormal : Boolean) : Mesh
{
    val mesh = Mesh()

    for (face in objObject.faces)
    {
        var normal = Point3D()
        val size = face.vertices.size

        if (generateNormal && size > 2)
        {
            val point0 = objObject.points[face.vertices[0].point]
            val point1 = objObject.points[face.vertices[size - 1].point]
            val point2 = objObject.points[face.vertices[size - 2].point]
            val vx10 = point0.x - point1.x
            val vy10 = point0.y - point1.y
            val vz10 = point0.z - point1.z
            val vx12 = point2.x - point1.x
            val vy12 = point2.y - point1.y
            val vz12 = point2.z - point1.z
            val nx = vy10 * vz12 - vz10 * vy12
            val ny = vx10 * vz12 - vz10 * vx12
            val nz = vx10 * vy12 - vy10 * vx12
            val norme = sqrt(nx * nx + ny * ny + nz * nz)

            normal =
                if (norme > 0.01f)
                {
                    Point3D(nx / norme, ny / norme, -nz / norme)
                }
                else
                {
                    Point3D(nx, ny, nz)
                }
        }

        mesh.face {
            for (indexes in face.vertices.reversed())
            {
                if (generateNormal)
                {
                    this.add(Vertex(objObject.points[indexes.point],
                                    objObject.uvs[indexes.uv],
                                    normal))
                }
                else
                {
                    this.add(Vertex(objObject.points[indexes.point],
                                    objObject.uvs[indexes.uv],
                                    objObject.normals[indexes.normal]))
                }
            }
        }
    }

    return mesh
}

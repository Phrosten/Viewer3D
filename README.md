Viewer3D is an Android App I wrote to get into the basics of 3D Rendering.

In order to display an object you need to get a triangulated .obj file without normals
and the corresponding .mtl file.
You can do this by exporting a model in Blender as .OBJ and set the export settings accordingly.
The Python3 Script I provided then translates these files to Java source code that can be utilized
by other classes.

The algorithm I wrote is quite simple and utilizes the Android OpenGL ES 2.0 API. It is
by no means perfect and has its weaknesses. It can't display every shape properly because
the method that gets the rendering sequence (order) can't deal with overlapping shapes.

Nonetheless I want to archive this "achievement".
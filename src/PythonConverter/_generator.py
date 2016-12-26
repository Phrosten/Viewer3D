def PrintInfo():
	print(	"If the result is implemented into an actual Android app, make sure that:\n" +
			"\t- \'<uses-feature android:glEsVersion=\"0x00020000\" android:required=\"true\"/>\' is set in \'AndroidManifest.xml\'\n" +
			"\t- Target API >= 8")
	print("\n\n\n")

#
#	Vertex
#
	
def GetVertexData(objName):
	vertex = []

	with open(objName + ".obj") as f:
		for line in f:
			if len(line) > 0 and line[0] == "v":
				vertex.append(line.split()[1:4])
	return vertex
	
def PrintVertexData(file, data):
	file.write("float[] vertices = new float[]\n")
	file.write("{\n")
	
	for d in data:
		file.write("\t" + d[0] + "f, " + d[1] + "f, " + d[2] + "f,\n")
		
	file.write("};\n")

#
#	Color
#
	
def GetColorData(mtlName):
	color = []

	with open(mtlName + ".mtl") as f:
		for line in f:
			if len(line) > 0:
				if line.startswith("newmtl"):
					color.append([])
					color[len(color) - 1].append(line.replace("newmtl ", ""))
				elif line.startswith("Kd"):
					color[len(color) - 1].append(line.split()[1:4])
	return color
	
def PrintColorData(file, data):
	file.write("float[] colors = new float[]\n")
	file.write("{\n")
	
	for d in data:
		file.write("\t" + d[1][0] + "f, " + d[1][1] + "f, " + d[1][2] + "f, 1.0f,\n")
		
	file.write("};\n")
#
#	Polygon
#	

def GetPolygonData(objName, colorData):
	polygon = []
	currColor = 0
	
	with open(objName + ".obj") as f:
		for line in f:
			if len(line) > 0:
				if line[0] == "f":
					polygon.append(line.split()[1:4])
					for i in range(0, len(polygon[len(polygon) - 1])):
						polygon[len(polygon) - 1][i] = str(int(polygon[len(polygon) - 1][i]) - 1)
					polygon[len(polygon) - 1].append(str(currColor))
				if line.startswith("usemtl "):
					line = line.split()
					for i in range(0, len(colorData)):
						print(line[1] + " " + colorData[i][0])
						if line[1] in colorData[i][0]:
							currColor = i
							break
					
	return polygon
	
def PrintPolygonData(file, data):
	file.write("short[] polygons = new short[]\n")
	file.write("{\n")
	
	for d in data:
		file.write("\t" + d[0] + ", " + d[1] + ", " + d[2] + ", " + d[3] + ",\n")
		
	file.write("};\n")

#
#	Main
#
	
def Main():
	PrintInfo()
	
	f = open("output.txt", "w")
	
	print("Mesh name: ")
	meshName = input()
	
	colorData = GetColorData(meshName)
	PrintColorData(f, colorData)
	
	PrintVertexData(f, GetVertexData(meshName))
	PrintPolygonData(f, GetPolygonData(meshName, colorData))
	
	f.close()
Main()
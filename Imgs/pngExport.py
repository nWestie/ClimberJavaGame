from krita import *
import os


doc = Krita.instance().activeDocument()
wdir = os.path.dirname(doc.fileName())
bounds = doc.bounds()
# print(wdir, os.path.basename(doc.fileName()))
expDirName = os.path.basename(doc.fileName())
expDirName = os.path.splitext(expDirName)[0]
exportDir = os.path.join(wdir, expDirName)

if not os.path.isdir(exportDir):
  os.makedirs(exportDir)
else:
  for file in os.listdir(exportDir):
    # print(file)
    os.remove(os.path.join(exportDir,file))

Application.setBatchmode(True)

for node in doc.topLevelNodes():

  if node.name().lower() in ["background", "references"]:
    continue

  info = InfoObject()
  info.setProperty("alpha", True)
  info.setProperty("compression", 9)
  info.setProperty("forceSRGB", False)
  info.setProperty("indexed", False)
  info.setProperty("interlaced", False)
  info.setProperty("saveSRGBProfile", False)
  info.setProperty("transparencyFillcolor", [0,0,0])

  # print(node.name()[0:2])
  if(node.name()[0:2] == "[]"):
    path = os.path.join(exportDir, node.name()[2:] + ".png")
    node.save(path, doc.resolution(), doc.resolution(), info, bounds)
    #print(node.name(), "full size")
  else:
    path = os.path.join(exportDir, node.name() + ".png")
    node.save(path, doc.resolution(), doc.resolution(), info)

Application.setBatchmode(False)
print("Finished png export")
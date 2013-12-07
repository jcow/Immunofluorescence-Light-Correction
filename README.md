#ImmunofluorescenceLightCorrection


##What it does
In microbiology, biologists sometimes use light microscopy to take images of cells that have been fluoresced using specific dyes and antibodies.  Photobleaching/overexsposure is a common occurrence with these images.  This system was created to take in a directory of these images and render out new images where the light has been corrected.


## Created for
This project was done for my bioinformatics class.  There is a biology lab that takes quite a large of amount of photos and their manual process was taking up too much time.

##How to Run
In the dist folder there is the ImmunofluorescenceLightCorrection.jar file.  To run the system, invoke the command 
java -jar ImmunofluorescenceLightCorrection.jar from a terminal.

Once running, click the "Choose Directory" button and select the directory of images you want processed.  Once you have the directory chosen, select "Process Directory".  The system will then look at all the images in the directory and attempt to process them.  A new directory named "Corrected Images" will appear in the directory that you selected and all the images that are processed will show up there.

Note - the system currently only looks for jpg images.  All other images will simply be ignored.

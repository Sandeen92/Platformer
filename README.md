

https://github.com/caspe1000/Platformer.git

Download the latest version from main-branch. No commits or merges to main will occur before testing is done.

To run the program, simply run the main-method in MainProgram with your IDE.





If an error occurs stating that is == null, do the following(for IntelliJ):

1. Press ctrl+alt+shift+s (or enter Project Structure via the File tab in the top left corner)
2. Click on modules
3. Make sure Platformer is listed as a module
4. Under the "Sources" tab, make sure the "resources"-directory is listed below Resource Folders in the right-most pane.
5. If it is, skip to step 6. If it's not, simply highlight the resources-directory and click Mark as: Resources
6. Now swap to Dependencies (tab above)
7. Click the plus-icon to Add and then choose JARs and Directories
8. Add the resources-directory.
9. Mark the checkbox for Export on the newly added dependency.
10. Now you should be good to go! Contact the Platformer-team on Discord if it still states that is == null.# Platformer
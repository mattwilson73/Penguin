

## TopBloc Coding Challenge - Matthew Wilson

I had some issues accessing the `/server/resources` folder. Calling `getResource()` on the filename was always returning null no matter how I tried to open it. May have been an issue in my environment. 

I ended up moving the excel files to `/server/src/main/resources` as this structure is what I saw online as common structure for Maven projects when looking for a solution.

If there is a proper solution to accessing the files in the original location please let me know.

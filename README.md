***README***
- *How to run the project?*

One must use the respective terminal commands to navigate and reach the inside of the project’s directory. After being located inside of the project folder (where the file Main.java should be located) one must compile the project. To do so use the following command:

    javac Main.java

Next, one must simply run the file by using the command:

    java Main

- *What will the program do?*

The program will greet the user and ask for the first input file. This input file will be used to create and populate a new Bloom Filter with a false positive of 0.0000001. It will then show the proper Bloom Filter parameters (number of items, probability of a false positive, size, and number of hash functions) calculated. After the creation of the Bloom Filter, the program will ask for a second input file, which is used to test the Bloom Filter and will create a new file Results.csv. The program will show the user the path to the results file, in the case that the program was ran from the directory where Main.java is located as stated above, then the Results.csv file will be located next to Main.java.

- *How to pass in the input files?*

The program will prompt the user when it needs a certain input file, at that point these should be provided with the full path.

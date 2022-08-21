package edu.gatech.seclass.edtext;

import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {

    // Empty Main class for compiling Individual Project.
    // During Deliverable 1 and Deliverable 2, DO NOT ALTER THIS CLASS or implement it

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length<2){
            usage();
            return;
        }
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("f", false, "file");
        options.addOption("g", false, "all occurrences");
        options.addOption("a", false, "ascii");
        options.addOption("p", true, "prefix");
        options.addOption("d", true, "duplicate");
        options.addOption("n", true, "line numbers");

        Option r = Option.builder("r").hasArgs().build();
        r.setArgs(2);
        options.addOption(r);
        //https://stackoverflow.com/questions/17180743/apache-commons-cli-getting-list-of-values-for-an-option

        CommandLine input;

        try {
            input = parser.parse(options, args);
        } catch (Exception e) {
            usage();
            return;
        }

        if ((input.getOptions().length) == 0 ) {//no options
            usage();
            return;
        }

        List<String> argList;
        argList = input.getArgList();

        if ((argList.size()) == 0 ) {//no options
            usage();
            return;
        }

        List<String> fileLines;
        String fileString;

        String fileName = argList.get(argList.size()-1);
        //https://www.techiedelight.com/read-contents-of-file-using-files-class/
        try {
            //fileLines= Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            fileString = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        }catch (Exception e){
            usage();
            return;
        }

        if (fileString.length() == 0) {
            if (!input.hasOption("f")) {
                System.out.print("");
            }
            return;
        }

        fileLines = new ArrayList<>(Arrays.asList(fileString.split(System.getProperty("line.separator"))));

        //option order: -g, -r, -a, -p, -d, -n
        if (input.hasOption("g") && (!input.hasOption("r"))) {
            usage();
            return;
        }

        //-r
        if (input.hasOption("r")){
            String oldS= input.getOptionValues("r")[input.getOptionValues("r").length - 2];//to use the last -r option
            String newS = input.getOptionValues("r")[input.getOptionValues("r").length - 1];
            if (oldS == "") {
                usage();
                return;
            } else {
                ArrayList<String> temp = new ArrayList<String>();
                if (input.hasOption("g")){
                    for (String line : fileLines) {
                        temp.add(line.replaceAll(oldS, newS));
                    }
                }
                else{
                    for (String line : fileLines) {
                        temp.add(line.replaceFirst(oldS, newS));
                    }
                }
                fileLines=temp;
            }
        }

        //-a
        if (input.hasOption("a")){
            ArrayList<String> temps = new ArrayList<String>();
            for(String line : fileLines) {
                String temp="";
                for (int i = 0; i < line.length(); i++) {
                    int ascii = (int)line.charAt(i);
                    if ((ascii>=32) && (ascii<=126)) {
                        temp = temp.concat(String.valueOf(ascii));
                        temp = temp.concat(" ");
                    }
                    else{
                        temp = temp+line.charAt(i);
                    }
                }
                temps.add(temp);
            }
            fileLines = temps;
        }

        //-p
        if (input.hasOption("p")){
            ArrayList<String> temp = new ArrayList<String>();
            String prefix = input.getOptionValues("p")[input.getOptionValues("p").length - 1];
            for (String line: fileLines){
                temp.add(prefix + line);
            }
            fileLines = temp;
        }

        //-d
        if (input.hasOption("d")){
            List<String> temp = new ArrayList<String>();
            String numS = input.getOptionValues("d")[input.getOptionValues("d").length - 1];

            int num;
            try{
                num = Integer.valueOf(numS);
            }catch (Exception e){
                usage();
                return;
            }

            if (num<1 || num>10){
                usage();
                return;
            }

            for (String line: fileLines){
                for (int i = 0; i<=num;i++){
                    temp.add(line);
                }
            }
            fileLines = temp;
        }

        if (input.hasOption("n")){
            String numN = input.getOptionValues("n")[input.getOptionValues("n").length - 1];

            int padN;
            try{
                padN = Integer.valueOf(numN);
            }catch (Exception e){
                usage();
                return;
            }

            if (padN<1 || padN>5){
                usage();
                return;
            }

            List<String> temp = new ArrayList<String>();
            //https://www.baeldung.com/java-pad-string
            int i = 1;
            for (String line: fileLines){
                int length = (int)(Math.log10(i)+1);
                if (length>=padN){
                    temp.add(String.valueOf(i)+" "+line);
                }
                else{
                    StringBuilder sb = new StringBuilder();
                    while (sb.length() < padN-length) {
                        sb.append('0');
                    }
                    sb.append(String.valueOf(i));
                    sb.append(" ");
                    sb.append(line);
                    temp.add(sb.toString());
                }
                i++;
            }
            fileLines=temp;
        }

        //https://www.baeldung.com/java-write-to-file
        if (input.hasOption("f")){
            PrintWriter writer = new PrintWriter(fileName);
            for (String line : fileLines) {
                writer.write(line + System.getProperty("line.separator"));
            }
            writer.close();
        }
        else{
            for (String line : fileLines) {
                System.out.print(line);
                System.out.print(System.lineSeparator());
            }
        }

    }

    private static void usage() {
        System.err.println("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE");
    }
}












//Reference: https://www.youtube.com/watch?v=w0Bckb9Znfg&t=34s
//https://stackoverflow.com/questions/367706/how-do-i-parse-command-line-arguments-in-java
//https://commons.apache.org/proper/commons-cli/usage.html
//https://commons.apache.org/proper/commons-cli/apidocs/index.html
//https://www.tutorialspoint.com/commons_cli/index.htm
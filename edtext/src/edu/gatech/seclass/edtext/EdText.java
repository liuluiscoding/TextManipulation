package edu.gatech.seclass.edtext;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EdText implements EdTextInterface{

    private String filepath = null;
    private String oldString= null;
    private String newString= null;
    private boolean globalReplace=false;
    private boolean asciiConvert=false;
    private String prefix= null;
    private boolean duplicateLine=false;
    private int duplicateFactor=0;
    private boolean addLineNumber=false;
    private int width=0;
    private boolean inplaceEdit=false;

    private List<String> fileLines=new ArrayList<>();
    private String fileString = null;

    private String usage = "Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE";


    @Override
    public void reset() {
        this.filepath = null;
        this.oldString= null;
        this.newString= null;
        this.globalReplace=false;
        this.asciiConvert=false;
        this.prefix= null;
        this.duplicateLine=false;
        this.duplicateFactor=0;
        this.addLineNumber=false;
        this.width=0;
        this.inplaceEdit=false;
        fileString = null;
        fileLines=new ArrayList<>();
    }

    @Override
    public void setFilepath(String filepath) {
        this.filepath=filepath;
    }

    @Override
    public void setReplaceString(String oldString, String newString) {
        this.oldString=oldString;
        this.newString=newString;
    }

    @Override
    public void setGlobalReplace(boolean globalReplace) {
        this.globalReplace=globalReplace;
    }

    @Override
    public void setAsciiConvert(boolean asciiConvert) {
        this.asciiConvert=asciiConvert;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix=prefix;
    }

    @Override
    public void setDuplicateLines(boolean duplicateLine, int duplicateFactor) {
        this.duplicateLine=duplicateLine;
        this.duplicateFactor=duplicateFactor;
    }

    @Override
    public void setAddLineNumber(boolean addLineNumber, int width) {
        this.addLineNumber=addLineNumber;
        this.width=width;
    }

    @Override
    public void setInplaceEdit(boolean inplaceEdit) {
        this.inplaceEdit=inplaceEdit;
    }

    @Override
    public void edtext() throws EdTextException {

        try {
            //fileLines= Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            fileString = new String(Files.readAllBytes(Paths.get(filepath)), StandardCharsets.UTF_8);
        }catch (Exception e){
            throw new EdTextException(usage);
        }

        if (fileString.length() == 0) {
            if (!inplaceEdit) {
                System.out.print("");
            }
            return;
        }

        fileLines = Arrays.asList(fileString.split(System.getProperty("line.separator")));

        //option order: -g, -r, -a, -p, -d, -n
        if (globalReplace && oldString==null) {
            throw new EdTextException(usage);
        }

        //-r
        if (oldString!=null){

            if (oldString == "") {
                throw new EdTextException(usage);
            } else {
                ArrayList<String> temp = new ArrayList<String>();
                if (globalReplace){
                    for (String line : fileLines) {
                        temp.add(line.replaceAll(oldString, newString));
                    }
                }
                else{
                    for (String line : fileLines) {
                        temp.add(line.replaceFirst(oldString, newString));
                    }
                }
                fileLines=temp;
            }
        }

        //-a
        if (asciiConvert){
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
        if (prefix!=null){
            ArrayList<String> temp = new ArrayList<String>();
            for (String line: fileLines){
                temp.add(prefix + line);
            }
            fileLines = temp;
        }

        //-d
        if (duplicateLine){
            List<String> temp = new ArrayList<String>();

            int num;
            try{
                num = Integer.valueOf(duplicateFactor);
            }catch (Exception e){
                throw new EdTextException(usage);
            }

            if (num<1 || num>10){
                throw new EdTextException(usage);
            }

            for (String line: fileLines){
                for (int i = 0; i<=num;i++){
                    temp.add(line);
                }
            }
            fileLines = temp;
        }

        //-n
        if (addLineNumber){
            int padN;
            try{
                padN = Integer.valueOf(width);
            }catch (Exception e){
                throw new EdTextException(usage);
            }

            if (padN<1 || padN>5){
                throw new EdTextException(usage);
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
        if (inplaceEdit){
            try {
                PrintWriter writer = new PrintWriter(filepath);
                for (String line : fileLines) {
                    writer.write(line + System.getProperty("line.separator"));
                }
                writer.close();
            }catch (Exception e){
                throw new EdTextException(usage);
            }
        }
        else{
            for (String line : fileLines) {
                System.out.print(line);
                System.out.print(System.lineSeparator());
            }
        }

    }
}

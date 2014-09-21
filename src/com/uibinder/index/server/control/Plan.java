package com.uibinder.index.server.control;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Cesar A. Villamizar C.
 */
public class Plan {

    Career career;
    List<Semester> semesters;
    double PAPA;
    Map<Subject, SubjectValues> subMap;

    public Plan() {
        career = new Career("Unknown","Unknown");
        semesters = new ArrayList<>();
        PAPA = 0;
        subMap = new HashMap<>();

    }

    public void parseHistory(String history) {

        semesters= new ArrayList<>();
        
//////////////////////////////////////"<option value=\"(\\d+)\">\\((\\d+)\\) ([^<]+)</option>"
        Pattern careerPattern= Pattern.compile("	(\\d+) \\| (.+[\\S])");
        //group1: careerCode ; group2: carrerName
        
        Pattern semesterPattern = Pattern.compile("(\\d+)	periodo acad��mico \\| (\\d+-\\w+)");
        //group1: semesterNumber ; group2: semesterDate (year-sem).

        Pattern subjectPattern = Pattern.compile("(\\d+)-(\\d+)	(.+)	(\\d+)	(\\d+)	(\\d+)	(\\w)*	(\\d+)	(\\d+)	(\\w)*	(\\d+)");
        //                                          1      2      3       4        5      6       7        8      9       10      11
        //group1: subjectCode
        //group2: subjectGroupNumber
        //group3: subjectName
        //group4: hours
        //group5: independantHours
        //group6: totalHours
        //group7: typology
        //group8: credits
        //group9: timesSeen
        //group10: aprovationWay
        //group11: grade

//////////////////////////////////////
        
        Matcher careerMatcher = careerPattern.matcher(history);
        
        if(careerMatcher.find()){            
            career= new Career(careerMatcher.group(1),careerMatcher.group(2));            
        }
        
        Matcher semesterMatcher = semesterPattern.matcher(history);

        if (semesterMatcher.find()) {
            boolean flag;
            do {
                Semester sm = new Semester(semesterMatcher.group(2));
                
                semesters.add(sm);

                int ini = semesterMatcher.end();

                flag=semesterMatcher.find();

                String semesterContent = history.substring(ini, flag?semesterMatcher.start():history.length());                
                Matcher subjectMatcher = subjectPattern.matcher(semesterContent);
                
                while (subjectMatcher.find()) {
                    String subjectCode = subjectMatcher.group(1);
                    String groupNumber = subjectMatcher.group(2);
                    String subjectName = subjectMatcher.group(3);
                    String typology = subjectMatcher.group(7);
                    String credits = subjectMatcher.group(8);
                    String timesSeen = subjectMatcher.group(9);
                    String approvationWay = subjectMatcher.group(10);
                    String grade = subjectMatcher.group(11);
                    
                    Subject sub= new Subject(Integer.parseInt(credits),subjectCode,subjectName);                    
                    Group group= new Group(sub,Integer.parseInt(groupNumber),new Teacher("Unknown","Unknown"));
                    SubjectValues val= new SubjectValues(group,Double.parseDouble(grade),Integer.parseInt(timesSeen),typology);
                    
                    sm.getSubjects().add(sub);
                    subMap.put(sub, val);
                }
            } while (flag);

        }

    }

    public void addSubject() {

    }

    public static void main(String[] args) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("historia.txt"), "UTF8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            reader.close();
            Plan plan = new Plan();
            plan.parseHistory(sb.toString());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Plan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Plan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Plan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

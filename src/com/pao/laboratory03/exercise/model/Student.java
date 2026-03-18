package com.pao.laboratory03.exercise.model;
import com.pao.laboratory03.exercise.exception.InvalidGradeException;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private String name;
    private int age;
    Map<Subject, Double> grades;

    public Student(int age, String name) {
        if(18 > age || age > 60)
        {
            throw new InvalidGradeException("Varsta " + age + " nu e valida (18-60)!");
        }
        else{
            this.age = age;
        }

        this.name = name;
        this.grades = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Map<Subject, Double> getGrades() {
        return grades;
    }

    public void addGrade(Subject subject, double grade){
        if( grade < 1 || grade > 10){
            throw new InvalidGradeException("Nota" + grade + "nu e valida!");
        }
        else
        {
            grades.put(subject, grade);
        }
    }

    public double getAverage(){
        if(grades.isEmpty()){
            return 0;
        }
        Double avg = 0.0;
        for(Map.Entry<Subject, Double> i : grades.entrySet()){
            avg += i.getValue();
        }
        return  avg/grades.size();
    }

    @Override
    public String toString(){
        return "Student{name = " + getName() + ", age = " + getAge() + ", avg = " + getAverage() + "}";
    }
}

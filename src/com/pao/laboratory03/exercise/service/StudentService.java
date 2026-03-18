package com.pao.laboratory03.exercise.service;
import com.pao.laboratory03.exercise.exception.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService {
    private List<Student> students = new ArrayList<>();
    private static StudentService instance;

    private StudentService() {
        this.students = students;
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Studentul '" + name + "' există deja");
            }
        }
        students.add(new Student(age, name));
    }

    public Student findByName(String name){
        for(Student st : students){
            if(st.getName().equals(name)){
                return st;
            }
        }
        throw new StudentNotFoundException("Studentul " + name + " nu este in lista!");
    }

    public void addGrade(String studentName, Subject subject, double grade){
        Student st = findByName(studentName);
        st.addGrade(subject, grade);
    }

    public void printAllStudents(){
        for(Student st : students){
            System.out.println(st);
        }
    }

    public void printTopStudents(){
        List<Student> st = new ArrayList<>(students);
        st.sort((s1,s2) -> Double.compare(s1.getAverage(), s2.getAverage()));
        int i = 1;
        for(Student s : st){
            System.out.printf("%d. %s — media: %.2f%n", i, s.getName(), s.getAverage());
            i++;
        }
    }

    public Map<Subject, Double> getAveragePerSubject(){
        Map<Subject, Double> avgSubject = new HashMap<>();
        for(Subject subject: Subject.values())
        {
            double sum = 0;
            int count = 0;
            for(Student st: students){
                Double grade = st.getGrades().get(subject);
                if(grade != null)
                {
                    sum += grade;
                    count++;
                }
                if(count > 0){
                    avgSubject.put(subject, sum/count);
                }
            }
        }
        return avgSubject;
    }
}

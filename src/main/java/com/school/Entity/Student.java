package com.school.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "student")
public class Student {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public int id;


    @Column(name = "matric_number",nullable = false,unique = true)
    public String matNo;

    @Column(name = "name" , nullable = false)
    public String name;



    @Column(name = "list_of_courses")
    public String stringListOfRegisteredCourses;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="assigned_teacher")
    public Teacher assignedTeacher;

    public Student(){}
    public Student(String matnum, String name) {
        this.matNo = matnum;
        this.name = name;
        this.stringListOfRegisteredCourses="";
        this.assignedTeacher=null;
    }

    public int getStudentId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getAssignedTeacher() {
        return assignedTeacher;
    }

    public void setAssignedTeacher(Teacher assignedTeacher) {
        this.assignedTeacher = assignedTeacher;
    }

    public String getMatNo() { return matNo; }

    public void setMatNo(String matNo) { this.matNo = matNo; }

    public String getStringListOfRegisteredCourses() {
        return stringListOfRegisteredCourses;
    }

    public void setStringListOfRegisteredCourses(String stringListOfRegisteredCourses) {
        this.stringListOfRegisteredCourses = stringListOfRegisteredCourses;
    }
}

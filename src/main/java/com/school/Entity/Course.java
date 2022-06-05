package com.school.Entity;


import javax.persistence.*;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" , nullable = false)
    public int id;

    @Column(name = "name",nullable = false)
    public String name;

    @Column(name = "courseCode",nullable = false,unique = true)
    public String courseCode;

    public Course(){}
    public Course(String name, String courseCode) {
        this.name = name;
        this.courseCode = courseCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }


}

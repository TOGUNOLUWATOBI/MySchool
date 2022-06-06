package com.school.Dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.querydsl.jpa.impl.JPAQuery;
import com.school.Entity.Course;
import com.school.Entity.Student;
import com.school.Entity.QStudent;
import com.school.Entity.Teacher;
import com.school.Utils.HibernateUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class StudentDao {
    static ObjectMapper objectMapper = new ObjectMapper();
    public static boolean addStudent (Student student)
    {
        Preconditions.checkNotNull(student,"student cant be null");
        return HibernateUtil.doTransaction(session -> session.save(student));
    }

    public static List<Student> getAllStudents()
    {
        AtomicReference<List<Student>> references=new AtomicReference<>();
        HibernateUtil.doTransaction(session ->
        {
            JPAQuery<Student> query = new JPAQuery<>(session.getSessionFactory().createEntityManager());
            QStudent qStudent = QStudent.student;
            List<Student> comments = query.select(qStudent).
                    from(qStudent).
                    fetch();
            references.set(comments);
        });
        return  references.get();
    }

    public static Student getStudent(String matnum)
    {
        Preconditions.checkNotNull(matnum,"matric number cant be null");
        AtomicReference<Student> reference= new AtomicReference<>();
        HibernateUtil.doTransaction(session ->
        {
            JPAQuery<Student> query = new JPAQuery<>(session.getSessionFactory().createEntityManager());
            QStudent qStudent = QStudent.student;
            Student student = query.select(qStudent).
                    from(qStudent).
                    where(qStudent.matNo.eq(matnum)).
                    fetchOne();
            reference.set(student);
        });
        return reference.get();
    }

    public static String getAssignedTeacher(Student std) throws JsonProcessingException {
        Preconditions.checkNotNull(std,"student cant be null");
        AtomicReference<Teacher> reference=new AtomicReference<>();
        HibernateUtil.doTransaction(session ->
        {
            JPAQuery<Student> query = new JPAQuery<>(session.getSessionFactory().createEntityManager());
            QStudent qStudent = QStudent.student;
            Teacher teacher = query.select(qStudent.assignedTeacher).
                    from(qStudent).
                    where(qStudent.matNo.eq(std.getMatNo())).
                    fetchOne();
            reference.set(teacher);
        });
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reference.get());
        return json;
    }


    public static boolean assignTeacherToStudent(Student student,Teacher teacher)
    {
        Preconditions.checkNotNull(student,"Student can't be null");
        Preconditions.checkNotNull(teacher,"Teacher can't be null");

        AtomicReference<Teacher> reference = new AtomicReference<>();
        HibernateUtil.doTransaction(session ->
        {
            JPAQuery<Student> query = new JPAQuery<>(session.getSessionFactory().createEntityManager());
            QStudent qStudent = QStudent.student;
            Teacher teacher1 = query.select(qStudent.assignedTeacher).
                    from(qStudent).
                    where(qStudent.matNo.eq(student.getMatNo())).
                    fetchOne();
            reference.set(teacher1);
        });


        if (reference.get() == null) {
            student.assignedTeacher = teacher;
            return HibernateUtil.doTransaction(session -> session.update(student));
        }
        else
        {
            reference.set(null);
            Preconditions.checkNotNull(reference.get(),"Student has already been assigned a teacher");
            return false;
        }
    }

    public static boolean updateAssignedTeacher(Student student,Teacher teacher)
    {
        Preconditions.checkNotNull(student,"Student can't be null");
        Preconditions.checkNotNull(teacher,"Teacher can't be null");

        AtomicReference<Teacher> reference = new AtomicReference<>();
        HibernateUtil.doTransaction(session ->
        {
            JPAQuery<Student> query = new JPAQuery<>(session.getSessionFactory().createEntityManager());
            QStudent qStudent = QStudent.student;
            Teacher teacher1 = query.select(qStudent.assignedTeacher).
                    from(qStudent).
                    where(qStudent.matNo.eq(student.getMatNo())).
                    fetchOne();
            reference.set(teacher1);
        });

        student.assignedTeacher = teacher;
        return HibernateUtil.doTransaction(session -> session.update(student));

    }

    public static boolean registerCourse(Student student, String courseCode)  {

        Preconditions.checkNotNull(student,"Student can't be null");
        Preconditions.checkNotNull(courseCode,"Course code can't be null");

        AtomicReference<String> reference=new AtomicReference<>();
        HibernateUtil.doTransaction(session ->
        {
            JPAQuery<Student> query = new JPAQuery<>(session.getSessionFactory().createEntityManager());
            QStudent qStudent = QStudent.student;
            String jsonquery = query.select(qStudent.stringListOfRegisteredCourses).
                    from(qStudent).
                    where(qStudent.matNo.eq(student.getMatNo())).
                    fetchOne();
            reference.set(jsonquery);
        });

        if (reference.get().equals("")){reference.set(courseCode);}
        else
        {
            reference.set(reference.get() + " " + courseCode);
        }
        reference.set(reference.get().trim());
        reference.set(reference.get().replaceAll(" ",","));
        student.setStringListOfRegisteredCourses(reference.get());
        return HibernateUtil.doTransaction(session -> session.update(student));

    }

    public static List<Course> getAllRegisteredCourses(Student student)
    {
        Preconditions.checkNotNull(student,"student cant be null");

        List<Course> list =new ArrayList<>();
        AtomicReference<List<Course>> references = new AtomicReference<>();
        HibernateUtil.doTransaction(session -> {
        JPAQuery<Student> query = new JPAQuery<>(session.getSessionFactory().createEntityManager());
        QStudent qStudent = QStudent.student;
        String course = query.select(qStudent.stringListOfRegisteredCourses).
                from(qStudent).
                where(qStudent.matNo.eq(student.getMatNo())).
                fetchOne();
        String [] courses = course.split(",");
            for (String courseCode :
                    courses) {
                list.add(CourseDao.getCourse(courseCode));
            }
        references.set(list);
        });
        return references.get();
    }
}

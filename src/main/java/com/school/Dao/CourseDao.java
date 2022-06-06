package com.school.Dao;

import com.google.common.base.Preconditions;
import com.querydsl.jpa.impl.JPAQuery;
import com.school.Entity.Course;
import com.school.Entity.QCourse;
import com.school.Utils.HibernateUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CourseDao {
    public static boolean addCourse (Course course)
    {
        Preconditions.checkNotNull(course,"Course cant be null");
        return HibernateUtil.doTransaction(session -> session.save(course));
    }


    public static List<Course> getAllCourses()
    {
        AtomicReference<List<Course>> references=new AtomicReference<>();
        HibernateUtil.doTransaction(session ->
        {
            JPAQuery<Course> query = new JPAQuery<>(session.getSessionFactory().createEntityManager());
            QCourse qCourse = QCourse.course;
            List<Course> comments = query.select(qCourse).
                    from(qCourse).
                    fetch();
            references.set(comments);
        });
        return  references.get();
    }

    public static Course getCourse(String courseCode)
    {
        Preconditions.checkNotNull(courseCode,"Course code cant be null");
        AtomicReference<Course> reference=new AtomicReference<>();
        HibernateUtil.doTransaction(session ->
        {
            JPAQuery<Course> query = new JPAQuery<>(session.getSessionFactory().createEntityManager());
            QCourse qCourse = QCourse.course;
            Course course = query.select(qCourse).
                    from(qCourse).
                    where(qCourse.courseCode.eq(courseCode)).
                    fetchOne();
            reference.set(course);
        });
        return reference.get();
    }

    public static boolean isCourseExist(String courseCode)
    {
        AtomicReference<Course> reference=new AtomicReference<>();
        HibernateUtil.doTransaction(session ->
        {
            JPAQuery<Course> query = new JPAQuery<>(session.getSessionFactory().createEntityManager());
            QCourse qCourse = QCourse.course;
            Course course = query.select(qCourse).
                    from(qCourse).
                    where(qCourse.courseCode.eq(courseCode)).
                    fetchOne();
            reference.set(course);
        });
        return reference.get() != null;
    }
}

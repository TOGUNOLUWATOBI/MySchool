package com.school.Dao;

import com.google.common.base.Preconditions;
import com.querydsl.jpa.impl.JPAQuery;
import com.school.Entity.QTeacher;
import com.school.Entity.Teacher;
import com.school.Utils.HibernateUtil;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class TeacherDao {
    public static boolean addTeacher (Teacher teacher)
    {
        Preconditions.checkNotNull(teacher,"teacher cant be null");
        return HibernateUtil.doTransaction(session -> session.save(teacher));
    }


    public static List<Teacher> getAllTeachers()
    {
        AtomicReference<List<Teacher>> references=new AtomicReference<>();
        HibernateUtil.doTransaction(session ->
        {
            JPAQuery<Teacher> query = new JPAQuery<>(session.getSessionFactory().createEntityManager());
            QTeacher qTeacher = QTeacher.teacher;
            List<Teacher> teachers = query.select(qTeacher).
                    from(qTeacher).
                    fetch();
            references.set(teachers);
        });
        return  references.get();
    }

    public static Teacher getTeacher(int id)
    {
        Preconditions.checkNotNull(id,"id cant be null");
        AtomicReference<Teacher> reference=new AtomicReference<>();
        HibernateUtil.doTransaction(session ->
        {
            JPAQuery<Teacher> query = new JPAQuery<>(session.getSessionFactory().createEntityManager());
            QTeacher qTeacher = QTeacher.teacher;
            Teacher teacher = query.select(qTeacher).
                    from(qTeacher).
                    where(qTeacher.empId.eq(id)).
                    fetchOne();
            reference.set(teacher);
        });
        return reference.get();
    }

    public static Teacher getRandomTeacher()
    {
        List<Teacher> teachers = getAllTeachers();
        Random rand = new Random();
        return teachers.get(rand.nextInt(teachers.size()));

    }
}

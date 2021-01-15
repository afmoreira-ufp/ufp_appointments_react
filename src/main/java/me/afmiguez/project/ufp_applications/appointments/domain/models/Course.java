package me.afmiguez.project.ufp_applications.appointments.domain.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @EqualsAndHashCode.Include
    private String name;

    @Builder.Default
    @ManyToMany
    private List<Teacher> teachers=new ArrayList<>();

    @Builder.Default
    @ManyToMany
    private List<Student> students=new ArrayList<>();

    public void addTeacher(Teacher teacher){
        if(getTeachers()==null)setTeachers(new ArrayList<>());
        if(!this.teachers.contains(teacher)){
            this.teachers.add(teacher);
            teacher.addCourse(this);
        }
    }


    public boolean hasStudent(Student student){
        return students.contains(student);
    }

    public void addStudent(Student student) {
        if(getStudents()==null)setStudents(new ArrayList<>());
        if(!this.students.contains(student)){
            this.students.add(student);
            student.addCourse(this);
        }
    }

    public void removeTeacher(Teacher teacher){
        teachers.remove(teacher);
    }
}

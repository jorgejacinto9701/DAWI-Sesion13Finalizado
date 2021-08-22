package com.empresa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.empresa.entity.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {

	
	@Query("Select a from Alumno a where nombre like :fil")
	public abstract List<Alumno> listaAlumnoPorNombreLike(@Param("fil") String filtro);

	public abstract List<Alumno> findByDni(String dni);
	public abstract List<Alumno> findByDniAndIdAlumnoNot(String dni, int idAlumno);
}

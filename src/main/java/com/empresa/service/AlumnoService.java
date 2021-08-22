package com.empresa.service;

import java.util.List;
import java.util.Optional;

import com.empresa.entity.Alumno;

public interface AlumnoService {

	public abstract List<Alumno> listaAlumno();
	public abstract Alumno insertaActualizaAlumno(Alumno obj);
	public abstract Optional<Alumno> obtienePorId(int idAlumno);
	public abstract void eliminaAlumno(int id);
	public abstract List<Alumno> listaAlumnoPorNombreLike(String filtro);
	public abstract List<Alumno> listaPorDni(String dni);
	public abstract List<Alumno> listaPorDni(String dni, int idAlumno);
	
}

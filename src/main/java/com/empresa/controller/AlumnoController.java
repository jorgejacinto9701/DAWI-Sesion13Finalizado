package com.empresa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.empresa.entity.Alumno;
import com.empresa.service.AlumnoService;
import com.empresa.util.Constantes;

@Controller
public class AlumnoController {

	@Autowired
	private AlumnoService service;

	@RequestMapping("/")
	public String verInicio() {
		return "crudAlumno";
	}

	@ResponseBody
	@RequestMapping("/consultaCrudAlumno")
	public List<Alumno> lista(String filtro) {
		return service.listaAlumnoPorNombreLike(filtro.trim() + "%");
	}


	
	@ResponseBody
	@RequestMapping("/eliminaCrudAlumno")
	public Map<String, Object> elimina(int id) {
		Map<String, Object> salida = new HashMap<>();
		try {
			Optional<Alumno> optAlumno = service.obtienePorId(id);
			if (optAlumno.isPresent()) {
				service.eliminaAlumno(id);
				salida.put("mensaje", Constantes.MENSAJE_ELI_EXITOSO);
			} else {
				salida.put("mensaje", Constantes.MENSAJE_ELI_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			salida.put("mensaje", Constantes.MENSAJE_ELI_ERROR);
		} finally {
			List<Alumno> lista = service.listaAlumno();
			salida.put("lista", lista);
		}
		return salida;
	}

	
	
}

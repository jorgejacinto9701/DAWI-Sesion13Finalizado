package com.empresa.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.empresa.entity.Alumno;
import com.empresa.service.AlumnoService;
import com.empresa.util.Constantes;
import com.empresa.util.FunctionUtil;

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
	@RequestMapping("/buscaPorDni")
	public String validaDni(String dni) {
		List<Alumno> lstAlumnos = service.listaPorDni(dni);
		if (CollectionUtils.isEmpty(lstAlumnos)) {
			return "{\"valid\" : true }";
		} else {
			return "{\"valid\" : false }";
		}
	}

	@ResponseBody
	@RequestMapping("/buscaPorCorreo")
	public String validaCorreo(String correo) {
		List<Alumno> lstAlumnos = service.listaPorCorreo(correo);
		if (CollectionUtils.isEmpty(lstAlumnos)) {
			return "{\"valid\" : true }";
		} else {
			return "{\"valid\" : false }";
		}
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

	@ResponseBody
	@PostMapping(value = "/registraCrudAlumno", consumes = "multipart/form-data")
	public Map<String, Object> registra(@RequestParam("nombre") String nombre, 
										@RequestParam("dni") String dni,
										@RequestParam("correo") String correo, 
										@RequestParam("fechaNacimiento") String fechaNacimiento,
										@RequestParam("foto") MultipartFile foto) {
		Map<String, Object> salida = new HashMap<>();
		try {

			Alumno obj = new Alumno();
			obj.setNombre(nombre);
			obj.setCorreo(correo);
			obj.setDni(dni);
			obj.setFechaNacimiento(FunctionUtil.toFechaDate(fechaNacimiento));
			obj.setFoto(foto.getBytes());

			List<Alumno> lstAlumno = service.listaPorDni(obj.getDni());
			if (CollectionUtils.isEmpty(lstAlumno)) {
				Alumno objSalida = service.insertaActualizaAlumno(obj);
				if (objSalida == null) {
					salida.put("mensaje", Constantes.MENSAJE_REG_ERROR);
				} else {
					salida.put("mensaje", Constantes.MENSAJE_REG_EXITOSO);
				}
			} else {
				salida.put("mensaje", Constantes.MENSAJE_REG_DNI_YA_EXISTE + obj.getDni());
			}
		} catch (Exception e) {
			e.printStackTrace();
			salida.put("mensaje", Constantes.MENSAJE_REG_ERROR);
		} finally {
			List<Alumno> lista = service.listaAlumno();
			salida.put("lista", lista);
		}
		return salida;
	}

	
	@RequestMapping(value = "/verImagen/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> getImage(@PathVariable("id") int imageId) throws IOException {

		Optional<Alumno> optAlumno = service.obtienePorId(imageId);

		byte[] imageContent = optAlumno.get().getFoto();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);

		return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
	}

	
	
	@ResponseBody
	@PostMapping(value = "/actualizaCrudAlumno", consumes = "multipart/form-data")
	public Map<String, Object> actualiza(
										@RequestParam("id") String id,
										@RequestParam("nombre") String nombre, 
										@RequestParam("dni") String dni,
										@RequestParam("correo") String correo, 
										@RequestParam("fechaNacimiento") String fechaNacimiento,
										@RequestParam("foto") MultipartFile foto,
										@RequestParam("flat") String flat) {
	
		Map<String, Object> salida = new HashMap<>();
		try {
			
			Alumno obj = new Alumno();
			obj.setIdAlumno(Integer.parseInt(id));
			obj.setNombre(nombre);
			obj.setCorreo(correo);
			obj.setDni(dni);
			obj.setFechaNacimiento(FunctionUtil.toFechaDate(fechaNacimiento));
			
			if (flat.equals("update_ok")) {
				obj.setFoto(foto.getBytes());	
			}else {
				Alumno objAlumno =  service.obtienePorId(Integer.parseInt(id)).get();
				obj.setFoto(objAlumno.getFoto());
			}
			
			List<Alumno> lstAlumno = service.listaPorDni(obj.getDni(), obj.getIdAlumno());
			if (CollectionUtils.isEmpty(lstAlumno)) {
				Alumno objSalida = service.insertaActualizaAlumno(obj);
				if (objSalida == null) {
					salida.put("mensaje", Constantes.MENSAJE_ACT_ERROR);
				} else {
					salida.put("mensaje", Constantes.MENSAJE_ACT_EXITOSO);
				}
			} else {
				salida.put("mensaje", Constantes.MENSAJE_REG_DNI_YA_EXISTE + obj.getDni());
			}
		} catch (Exception e) {
			e.printStackTrace();
			salida.put("mensaje", Constantes.MENSAJE_ACT_ERROR);
		} finally {
			List<Alumno> lista = service.listaAlumno();
			salida.put("lista", lista);
		}
		return salida;
	}

}

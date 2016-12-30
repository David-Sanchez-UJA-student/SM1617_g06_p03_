package com.example.dimanxe.practica3;

import java.util.LinkedList;

public interface Client {
	/**
	 * Metodo para autenticarse
	 * @param user Nick de usuario
	 * @param pass contrase�a del usuario
	 * @return true o false en funcion de si la autenticacion es correcta o no
	 */
	public boolean autentica(String user, String pass);
	/**
	 * Metodo para que los usuarios eliminen su cuenta
	 * @param user Nick de usuario
	 * @param pass Contrase�a del usuario.
	 * @return true o false en funcion de si se ha eliminado el usuario.
	 */
	public boolean eliminaUsuario(String user, String pass);
	/**
	 * Metodo para el registro de usuarios
	 * @param user Nick de usuario
	 * @param name Nombre de usuario
	 * @param surname Apellidos del usuario
	 * @param DNI DNI del usuario
	 * @param pass contrase�a del usuario
	 * @return true o false en funcion de si se ha podido crear el usuario.
	 */
	public boolean registra(String user, String name, String surname, String DNI, String pass);
	/**
	 * Metodo para buscar billetes
	 * @param origen Estacion y ciudad de origen
	 * @param destino Estacion y ciudad de destino
	 * @param fecha Fecha en la que se busca el billete
	 * @return Lista de Array de String con los datos de los billetes para la fecha seleccionada.
	 */
	public LinkedList<String[]> buscarbilletes(String origen, String destino, String fecha);
	/**
	 * Metodo para la compra del billete
	 * @param IDbillete Es un ID que se generara cuando se presenten los billetes al usuario para su seleccion.
	 * @param NumPlazas numero de plazas que va a comprar
	 * @param user Nick del usuario
	 * @return true o false en funcion de si se ha realizado la compra
	 */
	public boolean comprabillete(int IDbillete, int NumPlazas, String user);
	/**
	 * Metodo para obtener el �ltimo billete que ha adquirido el usuario.
	 * @param user Nick del usuario.
	 * @return Array de String con los datos del billete
	 */
	public String[] ultimoBilleteComprado(String user);
	/**
	 * Metodo para obtener el historial de billetes comprados por un usuario.
	 * @param user Nick del usuario
	 * @return Lista de objetos Billete con los datos de los billetes comprados.
	 */
	public LinkedList<String[]> historialBilletes(String user);
}

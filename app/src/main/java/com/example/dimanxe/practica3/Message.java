package com.example.dimanxe.practica3;

public class Message implements Protocolo{
	
	private int estado=0;
	private String header="";
	private String data="";
	private byte[] ByteData=null;
	
	public Message(){
		
	}
	
	/**
	 * Constructor con String de la clase Message.
	 * @param mEstado Estado en el que se encuentra cliente/servidor
	 * @param mHeader Comando de entrada o de respuesta
	 * @param mData Datos asociados al comando o respuesta.
	 */
	public Message(int mEstado, String mHeader,String mData){
		this.estado=mEstado;
		this.header=mHeader;
		this.data=mData;
	}
	/**
	 * Constructor con Array de byte de la clase Message.
	 * @param mBytedata Mensaje en bytes.
	 */
	public Message(byte[] mBytedata){
		this.ByteData=mBytedata;
	}
	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public byte[] getByteData() {
		return ByteData;
	}

	public void setByteData(byte[] byteData) {
		ByteData = byteData;
	}

	/**
	 * Metodo para convertir de String a Array de bytes.
	 * @return El mensaje en un Array de bytes
	 */
	public byte[] toByteArray(){
		String msg=null;
		msg=this.estado+SP+this.header+SP+this.data+CRLF;
		return msg.getBytes();
	}
	/**
	 * Metodo para convertir el mensaje de Array de bytes a String
	 * @return El mensaje en un String.
	 */
	public String toString(){
		return this.ByteData.toString();
	}
	public String toMessage(){
		return this.estado+SP+this.header+SP+this.data+CRLF;
	}
}

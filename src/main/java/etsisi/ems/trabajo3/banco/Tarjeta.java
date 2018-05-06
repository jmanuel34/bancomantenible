package etsisi.ems.trabajo3.banco;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Tarjeta {
    protected Cuenta mCuentaAsociada;
    protected int mMarcaInt;//mastercard, maestro, visa ...
    protected int mCCV;
    protected int nombreEntidad;
    protected ArrayList<Movimiento> mMovimientos;
    protected String mNumero, mTitular;
    protected LocalDate mFechaDeCaducidad;
    protected String mNombreEntidad;
    protected int mTipo; //oro platino clÃ¡sica
    
    public Tarjeta(Cuenta casociada,String numero, String titular, LocalDate fechacaducidad, int marcaint,	String nombreentidad, int ccv) {
    	mCuentaAsociada = casociada;
    	mNumero = numero;
		mTitular = titular;
		mFechaDeCaducidad = fechacaducidad;
		mMarcaInt = marcaint;
		mMovimientos = new ArrayList<Movimiento>();
		mNombreEntidad = nombreentidad;
		mCCV = ccv;
    	
    }


    public Cuenta asociarTarjeta() {
   	 return this.mCuentaAsociada;
    }

    public int getMarcaInternacional() {
   	 return mMarcaInt;
    }

    public void setMarcaInternacional(int marcaInternacional) {
   	 this.mMarcaInt = marcaInternacional;
    }

    public int getCcv() {
   	 return mCCV;
    }

    public void setCcv(int ccv) {
   	 this.mCCV = ccv;
    }

    public int getNombreEntidad() {
   	 return nombreEntidad;
    }

    public void setNombreEntidad(int nombreEntidad) {
   	 this.nombreEntidad = nombreEntidad;
    }
    
	public void setCuenta(Cuenta c) {
		mCuentaAsociada = c;
	}

    public void retirar(double cantidad) throws IOException {
		this.mCuentaAsociada.retirar("Retirada en cajero automático", cantidad);
	}

	public void ingresar(double cantidad) throws IOException {
		this.mCuentaAsociada.ingresar("Ingreso en cajero automático", cantidad);
	}

	public void pagoEnEstablecimiento(String datos, double cantidad) throws IOException {
		this.mCuentaAsociada.retirar("Compra en :" + datos, cantidad);
	}

	public double getSaldo() {
		return mCuentaAsociada.getSaldo();
	}

}

package etsisi.ems.trabajo3.banco;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class Cuenta {
	protected String mNumero;
	protected String mTitular;
	protected ArrayList<Movimiento> mMovimientos;

	public Cuenta(String numero, String titular) {
		mNumero = numero;
		mTitular = titular;
		mMovimientos = new ArrayList<Movimiento>();
	}

	public void ingresar(double cantidad) throws IOException {
		if (cantidad <= 0) {
			throw new IOException("No se puede ingresar una cantidad negativa");
		}
		Movimiento mov = new Movimiento();
		mov.añadirMovimiento("Ingreso en efectivo", cantidad);
		this.mMovimientos.add(mov);
	}

	public void retirar(double cantidad) throws IOException {
		if (cantidad <= 0) {
			throw new IOException("No se puede retirar una cantidad negativa");
		}
		if (getSaldo() < cantidad) {
			throw new IOException("Saldo insuficiente");
		}
		Movimiento mov = new Movimiento();
		mov.añadirMovimiento("Retirada de efectivo", cantidad);
		this.mMovimientos.add(mov);

	}

	public void ingresar(String concepto, double cantidad) throws IOException {
		if (cantidad <= 0) {
			throw new IOException("No se puede ingresar una cantidad negativa");
		}
		Movimiento mov = new Movimiento();
		mov.añadirMovimiento(concepto, cantidad);
		this.mMovimientos.add(mov);
	}

	public void retirar(String concepto, double cantidad) throws IOException {
		if (cantidad <= 0) {
			throw new IOException("No se puede retirar una cantidad negativa");
		}
		if (getSaldo() < cantidad) {
			throw new IOException("Saldo insuficiente");
		}
		Movimiento mov = new Movimiento();
		mov.añadirMovimiento(concepto, -cantidad);
		this.mMovimientos.add(mov);
	}

	public double getSaldo() {
		double saldo = 0.0;
		for (int i = 0; i < this.mMovimientos.size(); i++) {
			Movimiento mov = (Movimiento) mMovimientos.get(i);
			saldo += mov.getImporte();
		}
		return saldo;
	}

	public void addMovimiento(Movimiento m) {
		mMovimientos.add(m);
	}

	public ArrayList<Movimiento> buscarMovimiento(LocalDate fecha) {
		ArrayList<Movimiento> movFecha = new ArrayList<Movimiento>();
		for (int i = 0; i < mMovimientos.size(); i++) {
			if (mMovimientos.get(i).getFecha() == fecha)
				movFecha.add(mMovimientos.get(i));
		}
		return movFecha;
	}

}
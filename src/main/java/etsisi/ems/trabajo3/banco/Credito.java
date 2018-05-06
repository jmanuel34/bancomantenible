package etsisi.ems.trabajo3.banco;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.sun.javafx.collections.MappingChange.Map;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

	public class Credito extends Tarjeta{
	protected double mCredito;
	protected ArrayList<Movimiento> mMovimientos;
	public int mTipo; //oro platino clásica
	protected HashMap<Integer, Double> mapComision;
	protected HashMap<Integer, Integer> mapTipo;

	public Credito(Cuenta casociada , String numero, String titular, LocalDate fechacaducidad, double credito, int marcaint,String nombreentidad, int ccv) {
		super(casociada,numero,titular,fechacaducidad,marcaint,nombreentidad,ccv);
		Credito(numero,titular,fechacaducidad,marcaint,nombreentidad,ccv);
		mCredito = credito;
	}

	public Credito(Cuenta casociada,String numero, String titular, LocalDate fechacaducidad, int tipo, int marcaint, String nombreentidad, int ccv) {
		super(casociada,numero,titular,fechacaducidad,marcaint,nombreentidad,ccv);
		Credito(numero,titular,fechacaducidad,marcaint,nombreentidad,ccv);
		mTipo = tipo;
		mCredito = mapTipo.getOrDefault(tipo,600);
	}
	
	public void Credito(String numero, String titular, LocalDate fechacaducidad, int marcaint,String nombreentidad, int ccv) {
	
		mNumero = numero;
		mTitular = titular;
		mFechaDeCaducidad = fechacaducidad;
		mMovimientos = new ArrayList<Movimiento>();
		mNombreEntidad = nombreentidad;
		mCCV = ccv;
		inicializarMap(); 
	}
	
	public void inicializarMap() {
		
		mapTipo = new HashMap<Integer, Integer>();
		mapTipo.put(1,1000);
		mapTipo.put(2,800);
		mapTipo.put(3,600);
		mapComision = new HashMap<Integer, Double>();
		mapComision.put(1, 0.05);
		mapComision.put(2, 0.05);
		mapComision.put(3, 0.03);
		mapComision.put(4, 0.02);
	}

	public void setCuenta(Cuenta cuenta) {
		mCuentaAsociada = cuenta;
	}

	public void retirar(double cantidad) throws IOException {	
		double comisiontarifa;
		double comision;
		
		comisiontarifa = mapComision.getOrDefault(mMarcaInt,0.05);
				
		comision = añadirComision(comisiontarifa,cantidad);
		Movimiento mov = new Movimiento();
		mov.añadirMovimiento("Retirada en cuenta asociada (cajero automático)",-(cantidad+comision));
		mMovimientos.add(mov);	
	}

	//traspaso tarjeta a cuenta
	public void ingresar(double cantidad) throws IOException {
		double comision;
		// Movimiento m=new Movimiento();
		// m.setConcepto("Ingreso en cuenta asociada (cajero automático)");
		// m.setImporte(x);
		// mMovimientos.addElement(m);
		
		comision = añadirComision(0.05,cantidad);
		Movimiento mov = new Movimiento();
		mov.añadirMovimiento("Traspaso desde tarjeta a cuenta",cantidad);
		mMovimientos.add(mov);
		
		mCuentaAsociada.ingresar("Traspaso desde tarjeta a cuenta", cantidad);
		mCuentaAsociada.retirar("Comision Traspaso desde tarjeta a cuenta", comision);
	}

	public void pagoEnEstablecimiento(String datos, double cantidad) throws IOException {
		Movimiento mov = new Movimiento();
		mov.añadirMovimiento("Compra a crédito en: " + datos,cantidad);
		mMovimientos.add(mov);
	}

	public double getSaldo() {
		double saldo = 0.0;
		for (int i = 0; i < this.mMovimientos.size(); i++) {
			Movimiento m = (Movimiento) mMovimientos.get(i);
			saldo += m.getImporte();
		}
		return saldo;
	}

	public double getCreditoDisponible() {
		return mCredito - getSaldo();
	}

	public void liquidar(int mes, int anyo) throws IOException {
		
		double saldo = 0.0;
		for (int i = 0; i < this.mMovimientos.size(); i++) {
			Movimiento mov = (Movimiento) mMovimientos.get(i);
			if (mov.getFecha().getMonthValue() == mes && mov.getFecha().getYear() == anyo && !mov.isLiquidado())
			{
				saldo += mov.getImporte();
				mov.setLiquidado(true);
			}
		}
		
		if (saldo != 0) {
			Movimiento liq = new Movimiento();
			liq.añadirMovimiento("Liquidación de operaciones tarj. crédito, " + (mes) + " de " + (anyo),-saldo);
			mCuentaAsociada.addMovimiento(liq);			
		}
	}
	
	//TODO
	public void liquidarPlazos (int mes, int anyo) throws IOException {
		double saldo = 0.0;
		for (int i = 0; i < this.mMovimientos.size(); i++) {
			Movimiento mov = (Movimiento) mMovimientos.get(i);
			if (mov.getFecha().getMonthValue() == mes && mov.getFecha().getYear() == anyo && !mov.isLiquidado())
			{
				saldo += mov.getImporte();	
			}
		}
		saldo = saldo*1.12;
		Movimiento liq1 = new Movimiento();
		liq1.setConcepto ("Liquidar a Plazos");
		liq1.setFecha(LocalDate.now());
		liq1.setImporte(saldo/3);
		mCuentaAsociada.addMovimiento(liq1);
		

		Movimiento liq2 = new Movimiento();
		liq2.setConcepto ("Liquidar a Plazos");
		liq2.setFecha(LocalDate.now().plusDays(30)   );
		liq2.setImporte(saldo/3);
		mCuentaAsociada.addMovimiento(liq2);
		
		Movimiento liq3 = new Movimiento();
		liq3.setConcepto ("Liquidar a Plazos");
		liq3.setFecha(LocalDate.now().plusDays(60));
		liq3.setImporte(saldo/3);
		mCuentaAsociada.addMovimiento(liq3);
		
	}
	
	
	public double añadirComision (double comisiontarifa, double cantidad) throws IOException {
		
		double comision;
		comision = (cantidad * comisiontarifa < 3.0 ? 3 : cantidad * comisiontarifa); 
		if (cantidad > getCreditoDisponible())
		{
			throw new IOException("Crédito insuficiente");
		}
		return comision;
	}
}
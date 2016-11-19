package com.codpaa.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codpaa.provider.DbEstructure;
import com.codpaa.provider.DbEstructure.Usuario;
import com.codpaa.provider.DbEstructure.VisitaTienda;
import com.codpaa.provider.DbEstructure.Mensaje;
import com.codpaa.provider.DbEstructure.Tienda;
import com.codpaa.provider.DbEstructure.ProductByFormato;
import com.codpaa.provider.DbEstructure.ProductoByTienda;
import com.codpaa.provider.DbEstructure.TiendaProductoCatalogo;
import com.codpaa.provider.DbEstructure.PhotoProducto;
import com.codpaa.provider.DbEstructure.Materiales;
import com.codpaa.provider.DbEstructure.EncuestaFoto;


import java.io.File;
import java.util.Locale;


public class BDopenHelper extends SQLiteOpenHelper {

    private static final String name= "codpaa";
    private static SQLiteDatabase.CursorFactory cursorfactory = null;

    // v1.2.5 = 26
    private static final int version = 26;
    private static SQLiteDatabase baseDatosLocal = null;

    //fields of DB
    private static String usuarios;
    private static String tiendasVisitadas;
    private static String coordenadas;
    private static String encargadoTienda;
    private static String frentesCharola;
    private static String exhibiciones;
    private static String inventarioProducto;
    private static String productoPrecio;
    private static String productos;
    private static String surtido;
    private static String tipoExhibicion;
    private static String ruta;
    private static String tiendas;
    private static String marca;
    private static String coordenadasEnviar;
    private static String comentarioTienda;
    private static String rastreo;
    private static String inteligenciaMercado;
    private static String updateInfor;
    private static String cajasMayoreo;
    private static String photo;
    private static String preguntas;
    private static String respuesta;
    private static String respuestaTipo;
    private static String mensaje;
    private static String ventaPromedio;
    private static String direcciones;
    private static String productoByFormato;
    private static String productoByTienda;
    private static String tiendaProductoCatalogo;
    private static String photoProducto;
    private static String materiales;
    private static String encuestaFoto;


    public BDopenHelper(Context miContext) {
        super(miContext, name, cursorfactory, version);

        fields();

    }

    private void fields(){
        usuarios = "create table if not exists " + Usuario.TABLE_NAME + "(" +
                Usuario.ID_USER + " int primary key, " +
                Usuario.NOMBRE +" varchar(100)," +
                Usuario.USER + " varchar(15), " +
                Usuario.PASS + " varchar(15)," +
                Usuario.TIPO_PROMOTOR + " int(2)," +
                Usuario.STATUS + " int(2))";

        tiendasVisitadas = "Create table if not exists " +
                "tiendasVisitadas(idTienda int,nombre, idPromotor int,  fecha date, " +
                "ingreso char(6), salida char(7))";
        coordenadas = "create table if not exists " + VisitaTienda.TABLE + "(" +
                VisitaTienda.ID_TIENDA + " int, " +
                VisitaTienda.ID_PROMOTOR + " int, " +
                VisitaTienda.FECHA + " char(20)," +
                VisitaTienda.HORA + " char(10), " +
                VisitaTienda.LATITUD + " double," +
                VisitaTienda.LONGITUD + " double, " +
                VisitaTienda.PRECISION + " int, " +
                VisitaTienda.TIPO + " char(1), " +
                VisitaTienda.ESTATUS + " int, " +
                VisitaTienda.SEMANA + " int(3))";
        encargadoTienda = "create table if not exists " +
                "encargadotienda(idTienda int, idPromotor int, nombre char (50), " +
                "puesto char(20), fecha char(15))";
        frentesCharola = "create table if not exists " +
                "frentesCharola(idTienda int, idPromotor int,fecha char(10),idMarca int, " +
                "idProducto int, cha1 int,cha2 int, cha3 int, cha4 int,cha5 int, cha6 int,status int," +
                "unifila int(2), fila1 int(2), fila2 int(2), fila3 int(2)," +
                "fila4 int(2), fila5 int(2), fila6 int(2), fila7 int(2), fila8 int(2), fila9 int(2), fila10 int(2)," +
                "fila11 int(2), fila12 int(2), fila13 int(2), fila14 int(2))";
        exhibiciones = "create table if not exists " +
                "exhibiciones(idTienda int, idPromotor int, idExhibicion int, fecha char(15), " +
                "idProducto int, cantidad decimal (10,2), status int)";
        inventarioProducto = "create table if not exists " +
                "invProducto(idTienda int, idPromotor int ,fecha char(25), idProducto int, cantidad int " +
                ",cantidadFisico int, cantidadSistema int,status int,tipo varchar(10),fecha_caducidad varchar(15)," +
                "lote varchar(20), estatus int, estatus_producto int)";
        productoPrecio = "Create table if not exists " +
                "prodPrecio (idTienda int, idPromotor int, codBarpieza char(14), " +
                "fecha date, precio  decimal(18,2))";
        productos = "Create table if not exists " +
                "producto(idProducto int primary key, nombre varchar(50), presentacion varchar(10)," +
                "idMarca int, cb varchar(45), img varchar(250))";
        surtido = "create table if not exists " +
                "surtido(idTienda int, idPromotor int,surtido char(2), fecha char(25), " +
                "idProducto int, cajas int, unifila int, caja1 int, caja2 int, caja3 int," +
                "caja4 int, caja5 int, caja6 int, caja7 int, caja8 int, caja9 int, caja10 int," +
                "caja11 int, caja12 int, caja13 int, caja14 int)";
        tipoExhibicion = "create table if not exists " +
                "tipoexhibicion(idExhibicion int primary key, nombre char(30))";
        ruta = "create table if not exists " +
                "visitaTienda(idTienda int primary key, lunes int, martes int, miercoles int, " +
                "jueves int, viernes int, sabado int, domingo int, idCelular int, rol varchar(250))";
        tiendas = "create table if not exists " + Tienda.TABLE_NAME + "(" +
                Tienda.ID_TIENDA+" int primary key, " +
                Tienda.GRUPO + " varchar(60), " +
                Tienda.SUCURSAL+ " varchar(60), " +
                Tienda.LATITUD + " varchar(25), " +
                Tienda.LONGITUD + " varchar(25),  " +
                Tienda.ID_TIPO +" int, " +
                Tienda.ID_FORMATO + " int)";
        marca ="create table if not exists " +
                "marca(idMarca int primary key, nombre char(20), img varchar(250))";
        coordenadasEnviar = "create table if not exists " +
                "coordenadasEnviar(idTienda int, idCelular int, fecha varchar(15), " +
                "ingreso varchar(8), salida varchar(8))";
        comentarioTienda = "create table if not exists " +
                "comentarioTienda(idComentario integer primary key autoincrement,idTienda int," +
                "idCelular int, fecha varchar(25),comentario text,status int)";
        rastreo = "create table if not exists " +
                "rastreo(idCelular int, fecha varchar(10), hora varchar(10), latitud double, " +
                "longitud double, altitud double, numero_telefono varchar(14))";
        inteligenciaMercado = "create table if not exists " +
                "inteligencia(idCelular int,idTienda int,idProducto int,precioNormal varchar(8)," +
                "precioOferta varchar(8),fecha varchar(15),ofertaCruz varchar(5),productoExtra varchar(5)," +
                "productoEmpla varchar(5), cambioImagen varchar(5), status int,iniofer varchar(10)," +
                "finofer varchar(10),preciocaja varchar(8),cambioprecio varchar(5))";
        updateInfor = "create table if not exists " +
                "upInfo(nombre varchar(15) primary key, fecha varchar(10))";
        cajasMayoreo = "create table if not exists " +
                "cajasMayoreo(idCelular int,idMarca int,fecha varchar(10),cajas int, status int)";
        photo = "create table if not exists " +
                "photo(idPhoto integer primary key autoincrement NOT NULL, idTienda int NOT NULL, " +
                "idCelular int NOT NULL, idMarca int NOT NULL, idExhibicion int NOT NULL, " +
                "fecha varchar(10) NOT NULL, dia int NOT NULL, mes int NOT NULL, anio int NOT NULL, " +
                "imagen varchar(250) NOT NULL, status int(2) NOT NULL, evento int(2), fecha_captura char(20) NOT NULL)";

        photoProducto = "create table if not exists " +
                PhotoProducto.TABLE_NAME + "(" + PhotoProducto.ID_PHOTO + " int ," +
                PhotoProducto.ID_PRODUCTO + " int, " +
                "primary key("+PhotoProducto.ID_PHOTO+","+PhotoProducto.ID_PRODUCTO+"))";


        preguntas = "create table if not exists " +
                    "preguntas(id_pregunta integer NOT NULL, " +
                "descripcion varchar(250) NOT NULL, id_tipo int NOT NULL, " +
                "id_encuesta integer NOT NULL, nombre_encuesta varchar(50) NOT NULL," +
                " id_marca int NOT NULL, PRIMARY KEY(id_pregunta, id_encuesta))";

        encuestaFoto = "create table if not exists " +
                EncuestaFoto.TABLE_NAME + " (" +
                EncuestaFoto.ID_ENCUESTA + " int, " +
                EncuestaFoto.PHOTO_PATH + " varchar(250), " +
                EncuestaFoto.ID_PROMOTOR + " int, " +
                EncuestaFoto.STATUS + " int)";

        respuesta = "create table if not exists " +
                "respuestas(id_pregunta int NOT NULL, respuesta varchar, id_encuesta, id_promotor)";


        respuestaTipo = "create table if not exists "+
                "respuestaTipo(id_tipo int NOT NULL, descripcion varchar(15) NOT NULL)";

        mensaje = "create table if not exists " +
                Mensaje.TABLE_NAME + "("+
                Mensaje.ID_MENSAJE + " integer primary key autoincrement NOT NULL, " +
                Mensaje.MENSAJE + " varchar(50) NOT NULL, " +
                Mensaje.ASUNTO + " varchar(50) NOT NULL ," +
                Mensaje.CONTENT + " varchar(250) NOT NULL, " +
                Mensaje.FECHA + " varchar(15) NOT NULL, " +
                Mensaje.ESTATUS + " integer default 0, " +
                Mensaje.ENVIADO + " integer default 0," +
                Mensaje.ID_SERVIDOR + " int, " +
                Mensaje.ID_PROMOTOR + " int," +
                Mensaje.FECHA_LECTURA + " varchar(15))";

        ventaPromedio = "create table if not exists " +
                "ventaPromedio(idMarca int NOT NULL, tipo varchar(10) NOT NULL, cantidad float NOT NULL," +
                "fecha_inicio varchar(15),fecha_fin varchar(15), idTienda int NOT NULL, " +
                "idPromotor int NOT NULL, estatus integer default 1)";

        direcciones = "create table if not exists " +
                "direcciones(idTienda int NOT NULL, idPromotor int NOT NULL, direccion varchar(200) NOT NULL," +
                " estatus integer default 1)";

        productoByFormato = "create table if not exists " +
                ProductByFormato.TABLE_NAME + "(" +
                ProductByFormato.ID_PRODUCTO + " int," +
                ProductByFormato.ID_FORMATO + " int)";

        productoByTienda = "create table if not exists " +
                ProductoByTienda.TABLE_NAME + "(" +
                ProductoByTienda.ID_PRODUCTO + " int, " +
                ProductoByTienda.ID_TIENDA + " int)";

        tiendaProductoCatalogo = "create table if not exists " +
                TiendaProductoCatalogo.TABLE_NAME + "(" +
                TiendaProductoCatalogo.ID_PRODUCTO + " int," +
                TiendaProductoCatalogo.ID_TIENDA + " int," +
                TiendaProductoCatalogo.FECHA + " varchar(20), " +
                TiendaProductoCatalogo.ID_PROMOTOR + " int," +
                TiendaProductoCatalogo.ESTATUS + " integer default 1, " +
                "primary key("+
                TiendaProductoCatalogo.ID_TIENDA + "," +
                TiendaProductoCatalogo.FECHA + "," +
                TiendaProductoCatalogo.ID_PRODUCTO +") )";

        materiales = "create table if not exists " +
                Materiales.TABLE_NAME + "(" +
                Materiales.ID_MATERIAL + " int, " +
                Materiales.MATERIAL + " varchar(50), " +
                Materiales.UNIDAD + " varchar(25)," +
                Materiales.SOLICITUD_MAXIMA + " int, " +
                Materiales.ESTATUS + " int)";





    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(usuarios);
        db.execSQL(tiendasVisitadas);
        db.execSQL(coordenadas);
        db.execSQL(ruta);
        db.execSQL(encargadoTienda);
        db.execSQL(frentesCharola);
        db.execSQL(exhibiciones);
        db.execSQL(inventarioProducto);
        db.execSQL(productoPrecio);
        db.execSQL(productos);
        db.execSQL(surtido);
        db.execSQL(tipoExhibicion);
        db.execSQL(tiendas);
        db.execSQL(marca);
        db.execSQL(coordenadasEnviar);
        db.execSQL(comentarioTienda);
        db.execSQL(rastreo);
        db.execSQL(inteligenciaMercado);
        db.execSQL(updateInfor);
        db.execSQL(cajasMayoreo);
        db.execSQL(photo);
        db.execSQL(preguntas);
        db.execSQL(respuesta);
        db.execSQL(respuestaTipo);
        db.execSQL(encuestaFoto);
        db.execSQL(mensaje);
        db.execSQL(ventaPromedio);
        db.execSQL(direcciones);
        db.execSQL(productoByFormato);
        db.execSQL(productoByTienda);
        db.execSQL(tiendaProductoCatalogo);
        db.execSQL(photoProducto);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {



        if (newVersion == 24 && oldVersion == 22){
            db.execSQL("alter table frentesCharola add column unifila int(2)");
            db.execSQL("alter table frentesCharola add column fila1 int(2)");
            db.execSQL("alter table frentesCharola add column fila2 int(2)");
            db.execSQL("alter table frentesCharola add column fila3 int(2)");
            db.execSQL("alter table frentesCharola add column fila4 int(2)");
            db.execSQL("alter table frentesCharola add column fila5 int(2)");
            db.execSQL("alter table frentesCharola add column fila6 int(2)");
            db.execSQL("alter table frentesCharola add column fila7 int(2)");
            db.execSQL("alter table frentesCharola add column fila8 int(2)");
            db.execSQL("alter table frentesCharola add column fila9 int(2)");
            db.execSQL("alter table frentesCharola add column fila10 int(2)");
            db.execSQL("alter table frentesCharola add column fila11 int(2)");
            db.execSQL("alter table frentesCharola add column fila12 int(2)");
            db.execSQL("alter table frentesCharola add column fila13 int(2)");
            db.execSQL("alter table frentesCharola add column fila14 int(2)");

            db.execSQL(direcciones);

            db.execSQL("alter table " + Mensaje.TABLE_NAME + " add column " + Mensaje.ID_SERVIDOR + " int");
            db.execSQL("alter table " + Usuario.TABLE_NAME + " add column " + Usuario.TIPO_PROMOTOR + " int(2)");

            db.execSQL("alter table " + Tienda.TABLE_NAME + " add column " + Tienda.ID_TIPO + " int");
        }

        if (oldVersion == 23 && newVersion == 24){
            db.execSQL("alter table " + Mensaje.TABLE_NAME + " add column " + Mensaje.ID_SERVIDOR + " int");
            db.execSQL("alter table " + Mensaje.TABLE_NAME + " add column " + Mensaje.ID_PROMOTOR + " int");
            db.execSQL("alter table " + Mensaje.TABLE_NAME + " add column " + Mensaje.FECHA_LECTURA + " varchar(15)");
            db.execSQL("alter table " + Usuario.TABLE_NAME + " add column " + Usuario.TIPO_PROMOTOR + " int(2)");

            db.execSQL("drop table if exists " + Tienda.TABLE_NAME);
            db.execSQL(tiendas);

            db.execSQL("drop table if exists " + Usuario.TABLE_NAME);
            db.execSQL(usuarios);

            db.execSQL("alter table " + DbEstructure.Photo.TABLE_NAME + " add column " + DbEstructure.Photo.FECHA_CAPTURA + " char(20)");

            db.execSQL(productoByFormato);
            db.execSQL(productoByTienda);
            db.execSQL(tiendaProductoCatalogo);

            db.execSQL("alter table invProducto add column estatus_producto int");


        }

        if (oldVersion == 24 && newVersion == 25){

            Log.d("OnUpgrade", "new == v25 && oldv== 24");


            db.execSQL(photoProducto);
        }

        if (newVersion == 26){
            db.execSQL("drop table if exists respuestas");
            db.execSQL(respuesta);
            db.execSQL(encuestaFoto);
        }



    }

    public void insertar(String table,ContentValues values){
        baseDatosLocal = getWritableDatabase();
        if (baseDatosLocal != null){
            baseDatosLocal.insert(table, null, values);
            baseDatosLocal.close();
        }

    }


    @Override
    public void close() {
        super.close();
        if(baseDatosLocal != null)
            baseDatosLocal.close();



    }


    public void insertarImagen(int idTien, int idCel, int idMarca, int idExhi, String fecha, int dia, int mes, int anio, String imagen, int status, int evento, String fechaCaptura){
        baseDatosLocal = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("idTienda", idTien);
        valores.put("idCelular", idCel);
        valores.put("idMarca", idMarca);
        valores.put("idExhibicion", idExhi);
        valores.put("fecha", fecha);
        valores.put("dia", dia);
        valores.put("mes", mes);
        valores.put("anio", anio);
        valores.put("imagen", imagen);
        valores.put("status", status);
        valores.put("evento", evento);
        valores.put("fecha_captura", fechaCaptura);
        if(baseDatosLocal != null) {
            baseDatosLocal.insert("photo", null, valores);
            baseDatosLocal.close();
        }
    }


    public int getIdPromotor(){
        baseDatosLocal = getReadableDatabase();
        Cursor cursor = baseDatosLocal.rawQuery("select idCelular from usuarios", null);
        int id = 0;
        if (cursor.getCount() == 1 ){
            cursor.moveToFirst();

            id = cursor.getInt(0);

        }
        cursor.close();
        return id;
    }

    public long insertarImagenId(int idTien, int idCel, int idMarca, int idExhi, String fecha, int dia,
                                 int mes, int anio, String imagen, int status, int evento, String fechaCaptura){
        baseDatosLocal = getWritableDatabase();
        ContentValues valores = new ContentValues();
        long id = 0;
        valores.put("idTienda", idTien);
        valores.put("idCelular", idCel);
        valores.put("idMarca", idMarca);
        valores.put("idExhibicion", idExhi);
        valores.put("fecha", fecha);
        valores.put("dia", dia);
        valores.put("mes", mes);
        valores.put("anio", anio);
        valores.put("imagen", imagen);
        valores.put("status", status);
        valores.put("evento", evento);
        valores.put("fecha_captura", fechaCaptura);

        if(baseDatosLocal != null) {
            id = baseDatosLocal.insert("photo", null, valores);
            baseDatosLocal.close();
        }

        return id;
    }




    public void insertarUsuarios(int idcelular, String nombre, String user, String pass ) throws SQLiteException {

        baseDatosLocal = getWritableDatabase();

        if(baseDatosLocal != null)

            baseDatosLocal.execSQL("insert or replace into usuarios(idCelular,nombre,user,pass) values("+idcelular+",'"+nombre+"','"+user+"','"+pass+"') ");

        if(baseDatosLocal != null)baseDatosLocal.close();

    }

    public void borrarInven(String fecha, int sta){
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("delete from invProducto where fecha!='"+fecha+"' and status="+sta);
        if(baseDatosLocal != null)baseDatosLocal.close();

    }

    public void borrarExhi(String fecha, int sta){
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("delete from exhibiciones where fecha!='"+fecha+"' and status="+sta);
        if(baseDatosLocal != null)baseDatosLocal.close();

    }

    public void borrarInteli(String fecha, int sta){
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("delete from inteligencia where fecha!='"+fecha+"' and status="+sta);
        if(baseDatosLocal != null)baseDatosLocal.close();

    }

    public void borrarFrentes(String fecha, int sta){
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("delete from frentesCharola where fecha!='"+fecha+"' and status="+sta);
        if(baseDatosLocal != null)baseDatosLocal.close();

    }

    public void borrarCajasMa(String fecha, int sta){
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("delete from cajasMayoreo where fecha!='"+fecha+"' and status="+sta);
        if(baseDatosLocal != null)baseDatosLocal.close();

    }

    public void borrarVisitas(String fecha, int sta){
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("delete from coordenadas where fecha!='"+fecha+"' and status="+sta);
        if(baseDatosLocal != null)baseDatosLocal.close();
    }

    public void borrarFotos(String fecha){
        SQLiteDatabase baseFoto = getWritableDatabase();
        if (baseFoto != null){
            //Cursor eliminarFoto = baseFoto.query("photo",new String[]{"imagen"},)
            Cursor eliminarFoto = baseFoto.rawQuery("select p.imagen, p.fecha, p.status from photo as p where p.fecha!='"+fecha+"';", null);
            for (eliminarFoto.moveToFirst();!eliminarFoto.isAfterLast();eliminarFoto.moveToNext()){

                try {
                    File archivo = new File(eliminarFoto.getString(0));


                    if (archivo.exists()){
                        if (eliminarFoto.getInt(2) == 2){
                            if (archivo.delete()){
                                baseFoto.execSQL("delete from photo where imagen='"+eliminarFoto.getString(0)+"';");

                                Log.d("Delete file", "Archivo borrado: "+eliminarFoto.getString(0));
                            }else {
                                Log.d("Delete file","Archivo no se pudo borrar");
                            }
                        }
                    }else {
                        baseFoto.execSQL("delete from photo where imagen='"+eliminarFoto.getString(0)+"';");
                    }


                }catch (Exception e){
                    e.printStackTrace();

                }



            }

            eliminarFoto.close();
        }
        if (baseFoto != null) baseFoto.close();

    }


    public void insertarRutaVisitas(int idTienda, int lu, int ma, int mi, int ju, int vi, int sa, int dom, int idCel, String rol) throws SQLiteException{

        baseDatosLocal = getWritableDatabase();


        baseDatosLocal.execSQL("insert or replace into visitaTienda(idTienda, lunes, martes, miercoles, jueves, viernes, sabado, domingo, idCelular, rol) values("+idTienda+","+lu+","+ma+","+mi+","+ju+","+vi+","+sa+","+dom+","+idCel+",'"+rol+"')");

        baseDatosLocal.close();
    }

    public void insertarClientes(int idTienda, String grupo,String sucur, String lon, String la, int idFormato, int idTipoTienda) throws SQLiteException{
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert or replace into clientes(idTienda,grupo,sucursal,latitud,longitud, idFormato, idTipo) " +
                    "values("+idTienda+",'"+grupo+"','"+sucur+"','"+lon+"','"+la+"', "+ idFormato +","+idTipoTienda+")");
        if(baseDatosLocal != null)baseDatosLocal.close();
    }

    public void insertarEncargadoTienda(int idTienda, int idPromotor, String nombre,String puesto,String fecha) throws SQLiteException{

        baseDatosLocal = getWritableDatabase();

        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert or replace into encargadotienda(idTienda,idPromotor,nombre,puesto,fecha) values("+idTienda+","+idPromotor+",'"+nombre+"','"+puesto+"','"+fecha+"')");
        if(baseDatosLocal != null)baseDatosLocal.close();

    }

    public void insertarMarca(int idMarc, String nombre, String img)throws SQLiteException {
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert or replace into marca(idMarca,nombre,img) values("+idMarc+",'"+nombre+"','"+img+"')");
        if(baseDatosLocal != null)baseDatosLocal.close();
    }

    public void insertarProducto(int idProd, String nombre, String presentacion, int idMarc, String cb) throws SQLiteException{
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert or replace into producto(idProducto,nombre,presentacion,idMarca,cb) values("+idProd+",'"+nombre+"','"+presentacion+"',"+idMarc+",'"+cb+"')");
        if(baseDatosLocal != null)baseDatosLocal.close();
    }

    public void insertarFrentes(int idTien, int idPromo, String fecha, int idMarca, int idProdu, int ch1,
                                int ch2,int ch3,int ch4,int ch5,int ch6, int status, int unifila, int f1,
                                int f2, int f3, int f4, int f5, int f6, int f7, int f8, int f9, int f10,
                                int f11, int f12, int f13, int f14) throws SQLiteException {
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert or replace into frentesCharola(idTienda,idPromotor,fecha," +
                    "idMarca,idProducto,cha1,cha2,cha3,cha4,cha5,cha6,status,unifila,fila1,fila2,fila3,fila4," +
                    "fila5,fila6,fila7,fila8,fila9,fila10,fila11,fila12,fila13,fila14) " +
                    "values("+idTien+","+idPromo+",'"+fecha+"',"+idMarca+","+idProdu+","+ch1+","+ch2+"," +
                    ""+ch3+","+ch4+","+ch5+","+ch6+","+status+","+unifila+","+f1+","+f2+","+f3+","+f4+","+f5+"," +
                    ""+f6+","+f7+","+f8+","+f9+","+f10+","+f11+","+f12+","+f13+","+f14+")");
        if(baseDatosLocal != null)baseDatosLocal.close();
    }

    public void insertarRastreo(int idCel,String fecha, String hora, double latitud, double longitud, double altitud,String numeroTelefono) throws SQLiteException{
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert into rastreo(idCelular,fecha,hora,latitud,longitud,altitud,numero_telefono) values("+idCel+",'"+fecha+"','"+hora+"',"+latitud+","+longitud+","+altitud+","+numeroTelefono+")");
        if(baseDatosLocal != null)baseDatosLocal.close();

    }

    public void vaciarTabla(String tabla) throws SQLiteException {
        baseDatosLocal = getWritableDatabase();

        baseDatosLocal.execSQL("delete from "+tabla);
        if(baseDatosLocal != null) baseDatosLocal.close();


    }

    public void deleteTable(String table){
        baseDatosLocal = getWritableDatabase();
        baseDatosLocal.delete(table, null, null);
        if (baseDatosLocal != null)
            baseDatosLocal.close();
    }

    public void insertarLocalizacion(int idTien,int idPro, String fech,String hora, double lat, double lon, int prec, String tip, int status,int semana) throws SQLiteException{
        baseDatosLocal = getWritableDatabase();

        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert into coordenadas(idTienda,idPromotor,fecha,hora,latitud,longitud,precision,tipo,status,semana) values("+idTien+","+idPro+",'"+fech+"','"+hora+"',"+lat+","+lon+","+prec+",'"+tip+"',"+status+","+semana+")");

        if(baseDatosLocal != null)baseDatosLocal.close();
    }

    public void insertarExhibiciones(int idTien,int idPromo,int idExh, String fecha, int idProd, float cantidad, int sta) throws SQLiteException{
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert or replace into exhibiciones(idTienda,idPromotor,idExhibicion,fecha,idProducto,cantidad,status) values ("+idTien+","+idPromo+","+idExh+",'"+fecha+"',"+idProd+","+cantidad+","+sta+")");
        if(baseDatosLocal != null)baseDatosLocal.close();
    }

    public void insertarTipoExhibicion(int idExh, String nombre)throws SQLiteException {
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert or replace into tipoexhibicion(idExhibicion,nombre) values ("+idExh+",'"+nombre+"')");
        if(baseDatosLocal != null)baseDatosLocal.close();
    }

    public void insertarSurtido(int idTien,int idPromo,String surtido,String fecha, int idProd, int cajas,
                                int unifila, int caja1, int caja2, int caja3, int caja4,
                                int caja5, int caja6, int caja7, int caja8, int caja9, int caja10,
                                int caja11, int caja12, int caja13, int caja14)throws SQLiteException {
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert or replace into surtido(idTienda," +
                    "idPromotor,surtido,fecha,idProducto,cajas,unifila,caja1,caja2,caja3,caja4,caja5," +
                    "caja6,caja7,caja8,caja9,caja10,caja11,caja12,caja13,caja14) values " +
                    "("+idTien+","+idPromo+",'"+surtido+"','"+fecha+"',"+idProd+","+cajas+","+unifila+"," +
                    caja1+","+caja2+","+caja3+","+caja4+","+caja5+","+caja6+","+caja7+","+caja8+","+caja9+"," +
                    ""+caja10+","+caja11+","+caja12+","+caja13+","+caja14+")");
        if(baseDatosLocal != null)baseDatosLocal.close();
    }

    public void insertarInventario(int idTien, int idPromo ,String fecha, int Producto, int CantidadFisico,
                                   int CantidadSistema,int sta,String tipo, String fechaCaducidad,
                                   String lote, int estatus, int estatusProducto) throws SQLiteException {
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert or replace into invProducto (idTienda, idPromotor" +
                    " ,fecha, idProducto, cantidadFisico,cantidadSistema,status," +
                    "tipo, fecha_caducidad, lote, estatus, estatus_producto) values" +
                    " ("+idTien+","+idPromo+
                    ",'"+fecha+"',"+Producto+","+CantidadFisico+","+CantidadSistema+","+sta+",'"+tipo+"','"+fechaCaducidad+"','"+lote+"', "+estatus+"," +
                    estatusProducto +")");
        if(baseDatosLocal != null) baseDatosLocal.close();

    }


    public void insertarComentarios(int idTienda, int idCel, String fecha,String comentario) throws SQLiteException{
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert into comentarioTienda(idTienda,idCelular, fecha,comentario) values("+idTienda+","+idCel+",'"+fecha+"','"+comentario+"')");
        if(baseDatosLocal != null) baseDatosLocal.close();

    }

    public void insertarInteligencia(int idCel,int idTien,int idProd, String precioNormal, String precioOfer, String fecha,String ofertaCruz,String proExtr,String producEmpl,String cambioIm, int sta,String iniOfer,String finOfer,String preciCaja,String cambioPrecio) throws SQLiteException{
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert into inteligencia(idCelular,idTienda,idProducto,precioNormal,precioOferta,fecha,ofertaCruz,productoExtra,productoEmpla,cambioImagen,status,iniofer,finofer,preciocaja,cambioprecio) values("+idCel+","+idTien+","+idProd+",'"+precioNormal+"','"+precioOfer+"','"+fecha+"','"+ofertaCruz+"','"+proExtr+"','"+producEmpl+"','"+cambioIm+"',"+sta+",'"+iniOfer+"','"+finOfer+"','"+preciCaja+"','"+cambioPrecio+"')");
        if(baseDatosLocal != null) baseDatosLocal.close();
    }




    public Cursor fotos() throws SQLiteException{
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select p.idPhoto, p.idTienda, p.idCelular, p.idMarca, p.idExhibicion," +
                " p.fecha, p.dia, p.mes, p.anio, p.imagen, p.evento, p.fecha_captura, group_concat(pp.idProducto) as productos " +
                " from photo as p " +
                " left join photo_producto as pp on p.idPhoto=pp.idFoto where p.status=1 group by p.idPhoto;",null);

    }

    public Cursor datosFoto(int idFoto) throws SQLiteException{
        baseDatosLocal = getReadableDatabase();

        return baseDatosLocal.rawQuery("select p.idTienda, p.idCelular, p.idMarca, p.idExhibicion," +
                "p.fecha, p.dia, p.mes, p.anio, p.evento, p.fecha_captura, group_concat(pp.idProducto) as productos, p.imagen " +
                "from photo as p left join photo_producto as pp on p.idPhoto=pp.idFoto where idPhoto="+idFoto+" group by p.idPhoto;", null);


    }


    public Cursor datosUser(String user) throws SQLiteException{

        baseDatosLocal = getReadableDatabase();

        return baseDatosLocal.rawQuery("select * from usuarios where user='"+user+"';", null);

    }

    public Cursor datosVenta() throws SQLiteException {
        baseDatosLocal = getReadableDatabase();

        return baseDatosLocal.rawQuery("select * from ventaPromedio where estatus=1", null);
    }

    public Cursor datosPhoto() throws SQLiteException{
        baseDatosLocal = getReadableDatabase();

        return baseDatosLocal.rawQuery("select p.idPhoto,(c.grupo||' '||c.sucursal) as tienda," +
                "m.nombre, p.fecha, p.imagen, p.status " +
                "from photo as p inner join clientes as c on p.idTienda=c.idTienda " +
                "inner join marca as m on p.idMarca=m.idMarca", null);


    }

    public Cursor datosVisitas() throws SQLiteException{
        baseDatosLocal = getReadableDatabase();

        return baseDatosLocal.rawQuery("select idTienda,idPromotor,fecha,hora,latitud,longitud,tipo from coordenadas where status=1", null);


    }

    public Cursor datosFrentes() throws SQLiteException{
        baseDatosLocal = getReadableDatabase();

        return baseDatosLocal.rawQuery("select idTienda,idPromotor,fecha,idMarca,idProducto,cha1,cha2," +
                "cha3,cha4,cha5,cha6,unifila,fila1,fila2,fila3,fila4,fila5,fila6,fila7,fila8,fila9," +
                "fila10,fila11,fila12,fila13,fila14 from frentesCharola where status=1", null);

    }

    public Cursor datosCajasMay() throws SQLiteException{
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select idCelular,idMarca,fecha,cajas from cajasMayoreo where status=1", null);

    }



    public Cursor tienda(int idT) throws SQLiteException{

        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select grupo, sucursal from clientes where idTienda="+idT+";", null);

    }

    public Cursor nombrePromotor(int idP) throws SQLiteException{

        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select nombre from usuarios where idCelular="+idP+";", null);

    }

    public Cursor encargadoTienda() throws SQLiteException{
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select idTienda, idPromotor , nombre, puesto, fecha from encargadotienda;",null);

    }

    public Cursor contadorEncargados(int idTienda, String fecha) throws SQLiteException{
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select nombre from encargadotienda where idTienda="+idTienda+" and fecha='"+fecha+"' ;", null);


    }

    public Cursor contadorFrentes(int idTien, String fecha) throws SQLiteException{
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select idTienda from frentesCharola where idTienda="+idTien+" and fecha='"+fecha+"';", null);


    }

    public Cursor productos(int idMar) throws SQLiteException{
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select idProducto as _id, nombre,presentacion, cb, idMarca from producto where idMarca="+idMar+" order by nombre asc;", null);

    }


    public Cursor getProductCatalogo() throws SQLiteException {
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery(String.format(Locale.getDefault(),
                "select group_concat(%s, ',') as productos, %s, %s, %s from %s where %s=1 group by idTienda, fecha",
                TiendaProductoCatalogo.ID_PRODUCTO,TiendaProductoCatalogo.ID_PROMOTOR,
                TiendaProductoCatalogo.ID_TIENDA, TiendaProductoCatalogo.FECHA,
                TiendaProductoCatalogo.TABLE_NAME, TiendaProductoCatalogo.ESTATUS),null);
    }

    public Cursor getProductosByTienda(int idMarca, int idTienda){
        baseDatosLocal = getReadableDatabase();

        return baseDatosLocal.rawQuery("select distinct p.idProducto as _id, p.nombre, p.presentacion, p.cb, p.idMarca " +
                " from (select p.idProducto, p.nombre, p.presentacion, p.cb, p.idMarca from productoformato as pf " +
                " left join producto as p on p.idProducto=pf.idProducto " +
                " left join clientes as c on c.idFormato=pf.idFormato " +
                " where p.idMarca=" + idMarca + " and c.idTienda=" + idTienda +
                " union all " +
                " select p.idProducto, p.nombre, p.presentacion, p.cb, p.idMarca  from productotienda as pt " +
                " left  join producto as p on pt.idProducto=p.idProducto " +
                " where p.idMarca="+ idMarca +" and pt.idTienda="+ idTienda +") as p order by p.nombre asc", null);
    }

    public Cursor Surtido() throws SQLiteException {
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select idTienda, idPromotor,surtido,fecha,idProducto,cajas," +
                "unifila,caja1,caja2,caja3,caja4,caja5,caja6,caja7,caja8,caja9,caja10,caja11,caja12,caja13,caja14 " +
                " from surtido",null);

    }

    public Cursor addresses() throws SQLiteException {
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select idTienda, idPromotor, direccion from direcciones where estatus=1", null);
    }

    public Cursor SurtidoCantidad(int idTien, String fecha) throws SQLiteException{
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select idTienda, idPromotor,surtido,fecha,idProducto,cajas from " +
                "surtido where idTienda="+idTien+" and fecha='"+fecha+"';",null);


    }
    public Cursor contarInventario(int idTienda, String fecha)throws SQLiteException {
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select idTienda from invProducto where" +
                " idTienda="+idTienda+" and fecha='"+fecha+"';",null);

    }

    public Cursor mensajes() throws SQLiteException{
        baseDatosLocal = getReadableDatabase();

        return baseDatosLocal.rawQuery("select mensaje, asunto, content, fecha, estatus, enviado, " +
                " id_mensaje, id_servidor, enviado from mensaje order by "+ Mensaje.ESTATUS +" asc, "+ Mensaje.FECHA+ " desc", null);
    }

    public Cursor getMensajes(int idMensaje) throws SQLiteException{
        baseDatosLocal = getReadableDatabase();

        return baseDatosLocal.rawQuery("select ", null);
    }

    public int contarExhibiciones(int idTienda, String fecha) throws  SQLiteException{
        baseDatosLocal = getReadableDatabase();


        Cursor cursor =  baseDatosLocal.rawQuery("select idTienda from exhibiciones where" +
                " idTienda=" + idTienda + " and fecha='" + fecha + "';", null);

        int cantidad = cursor.getCount();
        cursor.close();

        return cantidad;

    }

    public int contarFotos(int idTienda){
        baseDatosLocal = getReadableDatabase();
        Cursor cursor = baseDatosLocal.rawQuery("select idPhoto from photo where idTienda="+idTienda,null);
        int cantidad = cursor.getCount();

        cursor.close();

        return cantidad;

    }

    public Cursor Inventario() throws SQLiteException{
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select idTienda,idPromotor,fecha,idProducto,cantidadFisico," +
                "cantidadSistema,cantidad,tipo, fecha_caducidad, lote, estatus, estatus_producto from invProducto where status=1",null);

    }

    public Cursor Exhibiciones() throws SQLiteException{

        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select idTienda, idPromotor, idExhibicion, fecha, idProducto," +
                " cantidad from exhibiciones where status=1;",null);

    }

    public Cursor VisitaTienda(int idTien, String fecha, String tipo)throws SQLiteException {
        baseDatosLocal = getReadableDatabase();

        return baseDatosLocal.rawQuery("select * from coordenadas where idTienda="+idTien+" and" +
                " fecha='"+fecha+"' and tipo='"+tipo+"';", null);

    }

    public Cursor visitaPendiente(int semana) throws SQLiteException{
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select idTienda,idPromotor,fecha,hora,latitud,longitud,tipo from coordenadas where (semana="+(semana-1)+" or semana="+semana+");", null);


    }


    public Cursor ComentariosTienda() throws SQLiteException{
        baseDatosLocal = getReadableDatabase();

        return baseDatosLocal.rawQuery("select idTienda,idCelular,fecha,comentario from comentarioTienda", null);

    }

    public Cursor datosInteligenciaMercado() throws SQLiteException{
        baseDatosLocal = getReadableDatabase();

        return baseDatosLocal.rawQuery("select idCelular,idTienda,idProducto,precioNormal,precioOferta,fecha,ofertaCruz,productoExtra,productoEmpla,cambioImagen,iniofer,finofer,preciocaja,cambioprecio from inteligencia where status=1", null);

    }



    public int idUser(){
        baseDatosLocal = getReadableDatabase();
        int id;
        Cursor cursor = baseDatosLocal.rawQuery("select idCelular from usuarios", null);

        cursor.moveToFirst();
        id = cursor.getInt(0);
        cursor.close();
        return id;
    }

    public Cursor datosRastreo() throws SQLiteException{
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select idCelular, fecha, hora, latitud, " +
                "longitud, altitud, numero_telefono from rastreo", null);

    }

    public int countMessege() throws  SQLiteException{
        baseDatosLocal = getReadableDatabase();
        int count;

        Cursor cursor = baseDatosLocal.rawQuery("select id_mensaje from mensaje where estatus<1", null);

        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int tipoTienda(int idTienda) throws SQLiteException{


        baseDatosLocal = getReadableDatabase();
        int tipo = 0;

        Cursor c = baseDatosLocal.rawQuery("select idTipo from clientes where idTienda="+idTienda,null);

        if (c.getCount() > 0){
            c.moveToFirst();

            tipo = c.getInt(0);

        }

        c.close();
        return tipo;
    }
}

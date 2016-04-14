package com.codpaa.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;


public class BDopenHelper extends SQLiteOpenHelper {

    private static final String name= "codpaa";
    private static SQLiteDatabase.CursorFactory cursorfactory = null;
    private static final int version = 21;
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
    private static String clientes;
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


    public BDopenHelper(Context miContext) {
        super(miContext, name, cursorfactory, version);

        fields();


    }

    private void fields(){
        usuarios = "create table if not exists " +
                "usuarios(idCelular int primary key, nombre varchar(100), " +
                "user varchar(15), pass varchar(15) )";
        tiendasVisitadas = "Create table if not exists " +
                "tiendasVisitadas(idTienda int,nombre, idPromotor int,  fecha date, " +
                "ingreso char (6), salida char(7))";
        coordenadas = "create table if not exists " +
                "coordenadas(idTienda int, idPromotor int, fecha char(20),hora char(10), " +
                "latitud double, longitud double, precision int, " +
                "tipo char(1), status int, semana int(3))";
        encargadoTienda = "create table if not exists " +
                "encargadotienda(idTienda int, idPromotor int, nombre char (50), " +
                "puesto char(20), fecha char(15))";
        frentesCharola = "create table if not exists " +
                "frentesCharola(idTienda int, idPromotor int,fecha char(10),idMarca int, " +
                "idProducto int, cha1 int,cha2 int, cha3 int, cha4 int,cha5 int, cha6 int,status int)";
        exhibiciones = "create table if not exists " +
                "exhibiciones(idTienda int, idPromotor int, idExhibicion int, fecha char(15), " +
                "idProducto int, cantidad decimal (10,2), status int)";
        inventarioProducto = "create table if not exists " +
                "invProducto(idTienda int, idPromotor int ,fecha char(25), idProducto int, cantidad int " +
                ",cantidadFisico int, cantidadSistema int,status int,tipo varchar(10),fecha_caducidad varchar(15)," +
                "lote varchar(20), estatus int)";
        productoPrecio = "Create table if not exists " +
                "prodPrecio (idTienda int, idPromotor int, codBarpieza char(14), " +
                "fecha date, precio  decimal(18,2))";
        productos = "Create table if not exists " +
                "producto(idProducto int primary key, nombre varchar(50), presentacion varchar(10)," +
                "idMarca int, cb varchar(45), img varchar(250))";
        surtido = "create table if not exists " +
                "surtido(idTienda int, idPromotor int,surtido char(2), fecha char(25), " +
                "idProducto int, cajas int)";
        tipoExhibicion = "create table if not exists " +
                "tipoexhibicion(idExhibicion int primary key, nombre char(30))";
        ruta = "create table if not exists " +
                "visitaTienda(idTienda int primary key, lunes int, martes int, miercoles int, " +
                "jueves int, viernes int, sabado int, domingo int, idCelular int, rol varchar(250))";
        clientes = "create table if not exists " +
                "clientes(idTienda int primary key, grupo varchar(60), sucursal varchar(60), " +
                "latitud varchar(25), longitud varchar(25))";
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
                "imagen varchar(250) NOT NULL, status int(2) NOT NULL, evento int(2))";
        preguntas = "create table if not exists " +
                    "preguntas(id_pregunta integer NOT NULL, " +
                "descripcion varchar(250) NOT NULL, id_tipo int NOT NULL, " +
                "id_encuesta integer NOT NULL, nombre_encuesta varchar(50) NOT NULL," +
                " id_marca int NOT NULL, PRIMARY KEY(id_pregunta, id_encuesta))";
        respuesta = "create table if not exists " +
                "respuestas(id_pregunta int NOT NULL, respuesta varchar, id_encuesta)";
        respuestaTipo = "create table if not exists "+
                "respuestaTipo(id_tipo int NOT NULL, descripcion varchar(15) NOT NULL)";

        mensaje = "create table if not exists " +
                "mensaje(id_mensaje integer primary key autoincrement NOT NULL, " +
                "mensaje varchar(50) NOT NULL, asunto varchar(50) NOT NULL ," +
                "content varchar(250) NOT NULL, fecha varchar(15) NOT NULL, " +
                "estatus integer default 0, enviado integer default 0)";



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
        db.execSQL(surtido);
        db.execSQL(tipoExhibicion);
        db.execSQL(clientes);
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
        db.execSQL(mensaje);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {




        if (oldVersion == 17 && newVersion == 18){
            db.execSQL("Alter table photo add column evento int(2)");
        }


        if (newVersion == 19){
            db.execSQL("Alter table rastreo add column numero_telefono varchar(14)");
            db.execSQL("Drop table visitaTienda");
            db.execSQL(ruta);

        }

        if (newVersion == 20 && oldVersion==19){
            db.execSQL(preguntas);
            db.execSQL(respuesta);
            db.execSQL(respuestaTipo);
            db.execSQL("Alter table invProducto add column estatus int");
        }

        if (newVersion == 21 && oldVersion == 20){
            db.execSQL(mensaje);

            db.execSQL("Drop table if exists visitaTienda");
            db.execSQL(ruta);


        }

        if (newVersion == 21 && oldVersion == 19){
            db.execSQL(preguntas);
            db.execSQL(respuesta);
            db.execSQL(respuestaTipo);
            db.execSQL("Alter table invProducto add column estatus int");

            db.execSQL(mensaje);

            db.execSQL("Drop table if exists visitaTienda");
            db.execSQL(ruta);
        }



    }

    public void insertar(String table,ContentValues values){
        baseDatosLocal = getWritableDatabase();
        if (baseDatosLocal != null){
            baseDatosLocal.insert(table, null, values);
            baseDatosLocal.close();
        }

    }

    public void remplazar(String table, ContentValues values){
        baseDatosLocal = getWritableDatabase();
        if (baseDatosLocal != null){
            baseDatosLocal.replace(table, null, values);
            baseDatosLocal.close();
        }

    }

    @Override
    public void close() {
        super.close();
        if(baseDatosLocal != null)
            baseDatosLocal.close();



    }


    public void insertarImagen(int idTien, int idCel, int idMarca, int idExhi, String fecha, int dia, int mes, int anio, String imagen, int status, int evento){
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

        if(baseDatosLocal != null) {
            baseDatosLocal.insert("photo", null, valores);
            baseDatosLocal.close();
        }
    }

    public long insertarImagenId(int idTien, int idCel, int idMarca, int idExhi, String fecha, int dia, int mes, int anio, String imagen, int status, int evento){
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

        if(baseDatosLocal != null) {
            id = baseDatosLocal.insert("photo", null, valores);
            baseDatosLocal.close();
        }

        return id;
    }

    public void insertarCajasMay(int idCel, int idMar, String fecha,int Cajas, int status){
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert into cajasMayoreo(idCelular,idMarca,fecha,cajas,status) values("+idCel+","+idMar+",'"+fecha+"',"+Cajas+","+status+")");
        if(baseDatosLocal != null) baseDatosLocal.close();
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

    public void insertarClientes(int idTienda, String grupo,String sucur, String lon, String la) throws SQLiteException{
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert or replace into clientes(idTienda,grupo,sucursal,latitud,longitud) values("+idTienda+",'"+grupo+"','"+sucur+"','"+lon+"','"+la+"')");
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

    public void insertarFrentes(int idTien, int idPromo, String fecha, int idMarca, int idProdu, int ch1,int ch2,int ch3,int ch4,int ch5,int ch6, int status) throws SQLiteException {
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert or replace into frentesCharola(idTienda,idPromotor,fecha,idMarca,idProducto,cha1,cha2,cha3,cha4,cha5,cha6,status) values("+idTien+","+idPromo+",'"+fecha+"',"+idMarca+","+idProdu+","+ch1+","+ch2+","+ch3+","+ch4+","+ch5+","+ch6+","+status+")");
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

    public void insertarSurtido(int idTien,int idPromo,String surtido,String fecha, int idProd, int cajas)throws SQLiteException {
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert or replace into surtido(idTienda,idPromotor,surtido,fecha,idProducto,cajas) values ("+idTien+","+idPromo+",'"+surtido+"','"+fecha+"',"+idProd+","+cajas+")");
        if(baseDatosLocal != null)baseDatosLocal.close();
    }

    public void insertarInventario(int idTien, int idPromo ,String fecha, int Producto, int CantidadFisico,
                                   int CantidadSistema,int sta,String tipo, String fechaCaducidad,
                                   String lote, int estatus) throws SQLiteException {
        baseDatosLocal = getWritableDatabase();
        if(baseDatosLocal != null)
            baseDatosLocal.execSQL("insert or replace into invProducto (idTienda, idPromotor" +
                    " ,fecha, idProducto, cantidadFisico,cantidadSistema,status,tipo, fecha_caducidad, lote, estatus) values ("+idTien+","+idPromo+
                    ",'"+fecha+"',"+Producto+","+CantidadFisico+","+CantidadSistema+","+sta+",'"+tipo+"','"+fechaCaducidad+"','"+lote+"', "+estatus+")");
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
        return baseDatosLocal.rawQuery("select idPhoto, idTienda, idCelular, idMarca, idExhibicion," +
                " fecha, dia, mes, anio, imagen, evento from photo where status=1;",null);

    }

    public Cursor datosFoto(int idFoto) throws SQLiteException{
        baseDatosLocal = getReadableDatabase();

        return baseDatosLocal.rawQuery("select idTienda, idCelular, idMarca, idExhibicion," +
                " fecha, dia, mes, anio, evento from photo where idPhoto="+idFoto+";", null);


    }


    public Cursor datosUser(String user) throws SQLiteException{

        baseDatosLocal = getReadableDatabase();

        return baseDatosLocal.rawQuery("select * from usuarios where user='"+user+"';", null);

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

        return baseDatosLocal.rawQuery("select idTienda,idPromotor,fecha,idMarca,idProducto,cha1,cha2,cha3,cha4,cha5,cha6 from frentesCharola where status=1", null);

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

    public Cursor Surtido() throws SQLiteException {
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select idTienda, idPromotor,surtido,fecha,idProducto,cajas from surtido",null);

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
                " id_mensaje from mensaje order by id_mensaje desc, estatus", null);
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
                "cantidadSistema,cantidad,tipo, fecha_caducidad, lote, estatus from invProducto where status=1",null);

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

    public Cursor infoActualizada(String nombre, String fecha) throws SQLiteException{
        baseDatosLocal = getReadableDatabase();
        return baseDatosLocal.rawQuery("select nombre, fecha from upInfo where nombre='"+nombre+"' and fecha='"+fecha+"'",null);

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

        return count;
    }
}

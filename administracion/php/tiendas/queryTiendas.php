<?


ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');


    $manager = DataBase::getInstance();

    $idUsuario = $_SESSION['idUser'];


    if (isset($_REQUEST['buscarTienda'])) {
        $_SESSION['buscarTienda'] = $_REQUEST['buscarTienda'];
    } else if ($_REQUEST['buscarTienda'] == "*") {
        $_SESSION['buscarTienda'] = "*";
    }
    $filtro = "";
    if ($_SESSION['buscarTienda'] != "*" and isset($_SESSION['buscarTienda'])) {

        $filtro = " and (m.idTienda='" . $_SESSION['buscarTienda'] . "' OR tf.grupo like '%" . $_SESSION['buscarTienda'] . "%' OR sucursal like '%"
            . $_SESSION['buscarTienda'] . "%' OR mun.nombre like '%" . $_SESSION['buscarTienda'] . "%')";
    }


    if (isset($_REQUEST['idMarca'])) {

        $filtro .= " and tm.id_marca in " . $manager->array_to_tuple($_REQUEST['idMarca']);

    }

    if (isset($_REQUEST['formatos'])) {

        if (count($_REQUEST['formatos']) > 0)
            $filtro .= ' and tf.idFormato in ' . $manager->array_to_tuple($_REQUEST['formatos']);

    }


    if (isset($_REQUEST['tipotienda'])) {

        if (count($_REQUEST['tipotienda']) > 0)
            $filtro .= ' and m.idTipoTienda in ' . $manager->array_to_tuple($_REQUEST['tipotienda']);

    }

    if (isset($_REQUEST['estructura']) && !empty($_REQUEST['estructura'])) {

        $filtro .= " and te.id_estructura = " . $_REQUEST['estructura'];


    }


    if (isset($_REQUEST['idPromotor']) && !empty($_REQUEST['idPromotor'])){

        $idPromo = $_REQUEST['idPromotor'];

        $filtro .= " and rp.idPromotor = $idPromo";

    }
    
    if(isset($_REQUEST['tiendaCanal']) && !empty($_REQUEST['tiendaCanal'])){
        
        if($_REQUEST['tiendaCanal'] == "TRADICIONAL"){
            $filtro .= " and m.canal = 'TRADICIONAL'";
        }
        else if($_REQUEST['tiendaCanal'] == "MODERNO"){
           $filtro .= " and m.canal = 'MODERNO'"; 
        }
        else if($_REQUEST['tiendaCanal'] == "TRADICIONAL CLOROX"){
           $filtro .= " and m.canal = 'TRADICIONAL CLOROX'"; 
        }
        else if($_REQUEST['tiendaCanal'] == "OLEOFINOS EXCLUSIVO"){
           $filtro .= " and m.canal = 'OLEOFINOS EXCLUSIVO'"; 
        }
        else if($_REQUEST['tiendaCanal'] == "EXCLUSIVO MOST"){
           $filtro .= " and m.canal = 'EXCLUSIVO MOST'"; 
        }
        else if($_REQUEST['tiendaCanal'] == "EXCLUSIVO IMASA"){
           $filtro .= " and m.canal = 'EXCLUSIVO IMASA'"; 
        }

    }
    
    if(isset($_REQUEST['estatus']) && !empty($_REQUEST['estatus'])){
        
        if($_REQUEST['estatus'] == "2"){
            $filtro .= " and m.status = 1";
        }
        else if($_REQUEST['estatus'] == "3"){
           $filtro .= " and m.status = 0"; 
        }

    }
    

    $sql = '';

    if ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '5' || $_SESSION['id_perfil'] == '2' || $_SESSION['id_perfil'] == '3' || $_SESSION['id_perfil'] == '13') {

        $sql = "SELECT
                IF(GROUP_CONCAT(DISTINCT rpr.idRuta ORDER BY rpr.idPromotor SEPARATOR ' / ') IS NULL, 'VACANTE', CONCAT(GROUP_CONCAT(DISTINCT rpr.idRuta ORDER BY rpr.idPromotor SEPARATOR ' / '))) RUTA,
                IF(GROUP_CONCAT(DISTINCT rp.idPromotor ORDER BY rp.idPromotor SEPARATOR ' / ') IS NULL, 'VACANTE', CONCAT(GROUP_CONCAT(DISTINCT rp.idPromotor  ORDER BY rp.idPromotor SEPARATOR ' / '))) CARTERA,
                if(GROUP_CONCAT(distinct rp.nombre order by rp.idPromotor separator ' / ') is null, 'VACANTE',  GROUP_CONCAT(distinct rp.nombre order by rp.idPromotor separator ' / ')) PROMOTOR,
                m.idTienda AS 'ID TIENDA',
                numeroEconomico AS 'NO. ECO.', UPPER(sucursal) AS 'SUCURSAL', 
                UPPER(estados.nombre) AS 'ESTADO', UPPER(mun.nombre) AS 'MUNICIPIO' , UPPER(m.colonia) AS 'COLONIA', UPPER(m.direccion) as 'DIRECCION', if(m.status = 1, 'ACTIVA', 'INACTIVA') as 'ESTATUS' , m.canal AS 'CANAL', UPPER(tf.grupo) AS 'GRUPO', UPPER(tf.cadena) AS 'CADENA' ,
                UPPER(mar.marcas) AS 'MARCAS', UPPER(niel.descripcion) AS 'NIELSEN',
                
                 concat(IF(sum(rp.visitas)=0, ' Sin Servicio ' , ''), group_concat(distinct rp.nombre,' ', rp.incapacidad)) AS 'ESTADO SERVICIO',
       
                group_concat( distinct rp.promo,if(rp.lunes = 1, '|Lunes', ''), if(rp.martes=1, '|Martes', ''), if(rp.miercoles = 1, '|Miercoles', ''), if(rp.jueves = 1, '|Jueves', ''), if(rp.viernes=1, '|Viernes', ''), if(rp.sabado=1, '|Sabado', ''), if(rp.domingo = 1, '|Domingo', '') separator ' / ') RUTAS
                , rp.lunes AS 'L', rp.martes AS 'M', rp.miercoles AS 'MM', rp.jueves AS 'J', rp.viernes AS 'V', rp.sabado AS 'S', rp.domingo AS 'D' 
       
                FROM maestroTiendas m
                LEFT JOIN estados ON (m.idEstado=estados.id)
                LEFT JOIN tiendas_formatos tf ON (tf.idFormato=m.idFormato)
                
                LEFT JOIN municipios mun ON (mun.id=m.id_municipio)
                    
                LEFT JOIN (
		            SELECT p.idPromotor promo,rp.idPromotor, rp.idTienda, p.nombre, concat(' Inc:', p.start_date,' a ',p.end_date) incapacidad,
		            (rp.lunes + rp.martes + rp.miercoles + rp.jueves + rp.viernes + rp.sabado ) visitas,rp.lunes,rp.martes,rp.miercoles,rp.jueves,rp.viernes,rp.sabado,rp.domingo 
		            FROM rutasPromotores rp 
		            RIGHT JOIN (
                        SELECT p.idCelular idPromotor, i.start_date, i.end_date, p.nombre FROM Promotores p
                        LEFT JOIN incapacidad i ON (i.id_promotor=p.idCelular AND ( CURDATE() BETWEEN i.start_date AND i.end_date) )
                        WHERE p.status='a'
			
		            )AS p ON (p.idPromotor=rp.idPromotor)
			
	            ) AS rp ON (rp.idTienda=m.idTienda)
                    
                
                    
                LEFT JOIN tienda_marca tm ON (tm.id_tienda = m.idTienda) 

				LEFT JOIN (

					SELECT tm.id_tienda, GROUP_CONCAT(mar.nombre SEPARATOR ' / ') marcas FROM tienda_marca tm 
					LEFT JOIN Marca mar ON (mar.idMarca = tm.id_marca)
					GROUP BY tm.id_tienda
					
				) AS mar ON (mar.id_tienda = m.idTienda)


                left join tienda_estructura te on m.idTienda = te.id_tienda
                
                LEFT JOIN rutasPromotores rpr ON rpr.idTienda = m.idTienda

                LEFT JOIN estados_nielsen niel ON (niel.id_nielsen = estados.id_nielsen)
                
                
                WHERE m.idFormato!='0' $filtro   
                
                
                GROUP BY m.idTienda 
					
                
                ORDER BY m.idTienda ";


    } else if ($_SESSION['id_perfil'] == '6') {

        $sql = "SELECT m.tamanio_tienda, m.cajas_registradoras,m.idTienda, tf.grupo,tf.cadena, numeroEconomico, sucursal, 
        UPPER(estados.nombre) AS estado, mun.nombre AS municipio , m.direccion, m.colonia, m.idFormato
		FROM maestroTiendas m
		INNER JOIN estados ON (m.idEstado=estados.id)
		INNER JOIN photoCatalogo pho ON (pho.id_tienda=m.idTienda)
		LEFT JOIN tiendas_formatos tf ON (tf.idFormato=m.idFormato)
		
		LEFT JOIN municipios mun ON (mun.id=m.id_municipio)
		
		WHERE m.idFormato!='0' AND 
		pho.id_marca IN (SELECT idMarca FROM ClientesMarcas WHERE idCliente=(SELECT idCliente FROM usuarios WHERE idUsuario='" . $idUsuario . "'))
		" . $filtro . " 
		GROUP BY m.idTienda
		ORDER BY m.idTienda ASC";
    }

    $rs = $manager->ejecutarConsulta($sql);
    $result = array();
    while ($row = mysqli_fetch_object($rs)) {
        array_push($result, $row);
    }


    echo json_encode(array("result" => $result), JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_NUMERIC_CHECK);
} else {
    echo 'no has iniciado sesion';

    http_response_code(422);

    header('refresh:2,../index.php');
}



<?php


/**
 * Created by PhpStorm.
 * User: Gustavo
 * Date: 1/08/14
 * Time: 13:59
 */
ob_start();
session_start();


if (isset($_SESSION['idUser']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');

    $idUsuario = $_SESSION['idUser'];

    $idTiendas = $_REQUEST['nombreTienda'];

    $idMarca = $_REQUEST['nombreMarca'];

    $idNiel = $_REQUEST['idNielsen'];
    
    $idRegSumma = $_REQUEST['regionSumma'];

    $fechaInicio = date('Y-m-d', strtotime($_REQUEST['fechaInicio']));

    $fechaFin = date('Y-m-d', strtotime($_REQUEST['fechaFin']));


    $idTipoProm = $_REQUEST['idTipoProm'];

    $idPromotor = $_REQUEST['idPromotor'];

    //$idProducto=$_GET['idProducto'];

    $idSupervisor = $_REQUEST['idSupervisor'];

    $Estado = $_REQUEST['estado'];


    $Formato = $_REQUEST['Formato'];

    $evento = $_REQUEST['Evento'];

    $order = $_REQUEST['order'];
    
    //$asesor = $_REQUEST['asesor'];
    
    $orderFormato = $_REQUEST['orderFormato'];

    $idProductos = $_REQUEST['productos'];

    $economico = $_REQUEST['economico'];
    
    $asesorSumma = $_REQUEST['asesorSumma'];
    
    $categorias = $_REQUEST['categoria'];
    
    $canales = $_REQUEST['canal'];

    $filtro = "";


    $manager = DataBase::getInstance();




    if (!empty($idMarca)){

        $nMarca = count($idMarca);
        $k = 0;
        if ($nMarca > 0) {

            $filtro .= " and pc.id_marca in (";
            foreach ($idMarca as $marca) {

                if (is_numeric($marca)) {

                    $filtro .= $marca;

                    if ($k != $nMarca - 1) {

                        $filtro .= ",";

                    }


                }


                $k++;
            }
            $filtro .= ")";
        }

    }



    if (isset($_REQUEST['nombreTienda']) && !empty($idTiendas) && isset($_REQUEST['nombreTienda'])) {


        $n_tiend = count($idTiendas);
        $k = 0;


        if ($n_tiend > 0) {

            $filtro .= " and pc.id_tienda in (";

            foreach ($idTiendas as $tiendas) {


                if (is_numeric($tiendas)) {

                    $filtro .= $tiendas;

                    if ($k != $n_tiend - 1) {

                        $filtro .= ",";

                    }

                }
                $k++;
            }

            $filtro .= ")";
        }
    }


    if (isset($_REQUEST['exhibicion']) && !empty($_REQUEST['exhibicion'])) {


        $filtro .= " and pc.id_exhibicion=" . $_REQUEST['exhibicion'];

    }

    if ($evento == 1) {
        $filtro .= " and pc.evento='1' ";
    }

    if (isset($_REQUEST['idTipoProm']) && !empty($_REQUEST['idTipoProm'])) {

        $filtro .= " and pro.idtipoPromotor=" . $_REQUEST['idTipoProm'];

    }


    if (isset($_REQUEST['razon']) && !empty($_REQUEST['razon'])){

        $filtro .= " and m.razonsocial=" . $_REQUEST['razon'];


    }

    if (isset($_REQUEST['idPromotor']) && !empty($_REQUEST['idPromotor'])) {

        $filtro .= " and pc.id_promotor=" . $idPromotor;

    }

    /*  if(isset($_GET['idProducto']) && !empty($_GET['idProducto'])){

         $sql.=" and pp.idProducto=".$idProducto;

      }*/
    if (isset($_REQUEST['idSupervisor']) && !empty($_REQUEST['idSupervisor'])) {

        $filtro .= " and pro.Supervisor=" . $idSupervisor;

    }

    if (isset($_REQUEST['estado']) && !empty($_REQUEST['estado'])) {


        //filtro de estado
        $h = 0;
        $nEstados = count($Estado);

        if ($nEstados > 0) {

            $filtro .= " and m.idEstado in (";

            foreach ($Estado as $item) {

                if (is_numeric($item)) {

                    $filtro .= $item;

                    if ($h != $nEstados - 1) {

                        $filtro .= ",";
                    }

                }

                $h++;

            }

            $filtro .= " ) ";

        }


    }

    if (isset($_REQUEST['Formato']) && !empty($_REQUEST['Formato'])) {

        $filtro .= " and m.idFormato=" . $Formato . " ";

    }

    if ($_SESSION['id_perfil'] == '8') {
        $filtro .= " and pro.Supervisor=(select idSupervisor from usuarios where idUsuario='" . $idUsuario . "')";
    }

    if ($_SESSION['id_perfil'] == '9') {
        $gerente_q = "SELECT idGerente FROM usuarios WHERE idUsuario='" . $idUsuario . "'";
        $gerente_r = $manager->ejecutarConsulta($gerente_q);
        $gerente_d = mysqli_fetch_array($gerente_r);

        $filtro .= " and pro.Supervisor IN (select idSupervisores from Supervisores
	                where idGerente='" . $gerente_d['idGerente'] . "')";
    }


    if (isset($_REQUEST['idNielsen']) && !empty($_REQUEST['idNielsen'])) {

        $filtro .= " and es.id_nielsen =  " . $_REQUEST['idNielsen'] . " ";

    }
    
    if (isset($_REQUEST['regionSumma']) && !empty($_REQUEST['regionSumma'])) {

        $filtro .= " and es.id_summa =  " . $_REQUEST['regionSumma'] . " ";

    }


    if (isset($_REQUEST['productos']) && !empty($_REQUEST['productos'])) {

        $filtro .= " and pp.idProducto in (";


        $j = 0;


        $nProductos = count($idProductos);

        if ($nProductos > 0) {

            foreach ($idProductos as $producto) {

                if (is_numeric($producto)) {

                    $filtro .= $producto;

                    if ($j != $nProductos - 1) {

                        $filtro .= ",";


                    }


                }

                $j++;

            }


        }


        $filtro .= ")";


    }
    
    //CATEGORIA
    if (isset($_REQUEST['categoria']) && !empty($_REQUEST['categoria'])) {
    
        $filtro .= " and cat.idCategoria in (";
        
        $j = 0;

        $nCategorias = count($categorias);

        if ($nCategorias > 0) {

            foreach ($categorias as $categoria) {

                if (is_numeric($categoria)) {

                    $filtro .= $categoria;

                    if ($j != $nCategorias - 1) {
                        $filtro .= ",";
                    }
                }

                $j++;
            }
        }
        $filtro .= ")";
    }
    
    //CONSULTA DE CANAL
     if(isset($_REQUEST['canal']) && !empty($_REQUEST['canal'])){
        $filtro .= " and m.canal in (  ";
        $k = 0;
        $gCanal = count($canales);
        if ($gCanal > 0){
            foreach ($canales as $canal){
                if (is_string($canal)){
                    $strcanal = "'$canal'";
                    $filtro .= $strcanal;
                    if ($k != $gCanal -1){
                        $filtro .= ",";
                    }
                }
                $k++;
            }
        }
        $filtro .=")";
     }

    /*
     *
     *
     * Diconsa profile
     *
     */

    if ($_SESSION['id_perfil'] == 14){



        $filtro .= "  and m.idFormato =  39  ";


    }

    if (!empty($economico)) {


        $filtro .= " and (numeroEconomico=" . $economico . " ) ";

    }


    if($order == 1){

        if ($orderFormato == 1) {
            
            $order = " order by pc.id_tienda, tf.idFormato, pc.fecha_captura asc ";
        }
        else {
            
            $order = " order by pc.id_tienda, pc.fecha_captura asc ";
        }
        

    }else{

        if ($orderFormato == 1) {
            
            $order = " order by tf.idFormato, pc.idphotoCatalogo ";
        }
        else {
            
            $order = " order by pc.idphotoCatalogo ";
        }
    }
    
    $queryAsesor = "";
    $queryJoin1 = "";
    $queryJoin2 = "";
    
    if($asesorSumma == 1){
        $queryAsesor = "v.nombre as asesor,";
        $queryJoin1 = " LEFT JOIN vendedor_tienda as vt on vt.id_tienda=m.idTienda ";
        $queryJoin2 = " LEFT JOIN vendedores as v on v.id_vendedor=vt.id_vendedor ";
    }
    else{
        $queryAsesor = " ";
        $queryJoin1 = " ";
        $queryJoin2 = " ";
    }

    $filtro .= "  GROUP BY pc.idphotoCatalogo  ". $order;

    $host = "plataformavanguardia.net/";


    //concat(m.grupo,' ',m.sucursal,' (',m.idTienda,')') AS nombreTienda
    
    
    $sql = "SELECT pc.idphotoCatalogo,concat(tf.grupo,' ',m.sucursal,' (',m.idTienda,')') AS nombreTienda, m.idTienda ,e.nombre,m.numeroEconomico economico, es.id_nielsen nielsen ,
	            pro.nombre AS nombrePromo,pro.idCelular AS idProm,concat(pc.fecha,' ',TIME(pc.fecha_captura)) AS fecha, pc.imagen, tp.descripcion AS nombreTipo,
	            concat(sup.nombreSupervisor,' ',apellidoSupervisor) AS nombreSuper, m.idTipoTienda as tipoTienda," .$queryAsesor.
	            "es.nombre as estado, pc.id_marca , CONCAT('https://','$host',pc.imagen)  hv, tf.cadena, CONCAT(tf.grupo, ' - ', tf.cadena) AS grupo, pc.comentario  comentarios, ct.comentarios comentariosGenerales, m.canal

                FROM photoCatalogo AS pc
                

                RIGHT JOIN maestroTiendas AS m ON pc.id_tienda=m.idTienda"
                
                .$queryJoin1.
                
                $queryJoin2.
                
                "LEFT JOIN tipoExhibicion AS e ON pc.id_exhibicion=e.idExhibicion

                RIGHT JOIN Promotores AS pro ON pc.id_promotor=pro.idCelular
				
				LEFT JOIN Supervisores AS sup ON sup.idSupervisores=pro.Supervisor

                LEFT JOIN tipoPromotor AS tp ON pro.idtipoPromotor=tp.idtipoPromotor
                
                LEFT JOIN photo_producto as pp on pp.idPhotoCatalogo=pc.idphotoCatalogo
                
                LEFT JOIN photo_categoria as cat on cat.idPhotoCatalogo=pc.idphotoCatalogo
                
                LEFT JOIN estados as es on es.id = m.idEstado
				
                left join tiendas_formatos as tf on (m.idFormato = tf.idFormato) 
                    
                LEFT JOIN (
			
			        SELECT ct.idTienda, ct.fecha_captura, ct.idMarca, GROUP_CONCAT(DISTINCT ct.comentario SEPARATOR '/') comentarios FROM comentarioTienda AS ct 
			
			        WHERE ct.fecha_captura between '$fechaInicio' and '$fechaFin'  
			        GROUP BY ct.idTienda, ct.fecha_captura, ct.idMarca 
			
                ) AS ct ON (ct.idTienda = pc.id_tienda AND ct.fecha_captura = DATE(pc.fecha_captura) AND ct.idMarca = pc.id_marca)


				WHERE DATE(pc.fecha_captura) BETWEEN '" . $fechaInicio . "' and '" . $fechaFin . "' " . $filtro ;



    $rs = $manager->ejecutarConsulta($sql);



    $json = array();

    while ($fila = mysqli_fetch_array($rs)) {

        if (file_exists("../../../../" . $fila['imagen'])) {

            //$exif = exif_read_data('../../'.$fila['imagen'], 'IFD0');

            try {
                $image = new Imagick('https://www.plataformavanguardia.net/' . $fila['imagen']);
                $orientation = $image->getImageOrientation();
            } catch (Exception $e) {
                $orientation = 0;
            }
            $nombrePromo = htmlentities(preg_replace('/\s+/', ' ', $fila['nombrePromo']));

            $nombreSuper = htmlentities(preg_replace('/\s+/', ' ', $fila['nombreSuper']));

            $fila['nombreTienda'] = preg_replace('/\s+/', ' ', $fila['nombreTienda']);


            array_push($json, array('idphotoCatalogo' => $fila['idphotoCatalogo'],
                'nombreTienda' => $fila['nombreTienda'],
                'nombre' => $fila['nombre'],
                'nombrePromo' => $nombrePromo,
                'idProm' => $fila['idProm'],
                'nombreSuper' => $nombreSuper,
                'fecha' => $fila['fecha'],
                'imagen' => $fila['imagen'],
                'orientationImage' => $orientation,
                'nombreTipo' => $fila['nombreTipo'],
                'tipoTienda' => $fila['tipoTienda'],
                'asesor' => $fila['asesor'],
                'estado' => $fila['estado'],
                'idMarca' => $fila['id_marca'],
                'economico' => $fila['economico'],
                'nielsen'=>$fila['nielsen'],
                'idTienda'=>$fila['idTienda'],
                'hv'=>$fila['hv'],
                'cadena'=>$fila['cadena'],
                'grupo' => $fila['grupo'],
                'comentarios'=> $fila['comentarios'],
                'comentariosGenerales'=> $fila['comentariosGenerales'],
                'canal'=>$fila['canal']));

        }


    }


    if (count($json) > 0) {

        echo json_encode(array('total' => count($json), 'rows' => $json), JSON_NUMERIC_CHECK | JSON_PRETTY_PRINT );
    } else {
        echo json_encode(array('total' => 0, 'rows' => $json), JSON_NUMERIC_CHECK | JSON_PRETTY_PRINT );
    }


} else {

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}




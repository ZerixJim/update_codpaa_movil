<?php
/**
 * Created by PhpStorm.
 * User: Christian-Lara
 * Date: 20/08/2018
 * Time: 05:04 PM
 */

ob_start();

session_start();





if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');


///*********************************Recibo los datos de los filtros *******************************//


    $id_formato = $_REQUEST['idFormato'];

    $id_estado = $_REQUEST['idEstado'];

    $desde = $_REQUEST['Desde'];

    $hasta = $_REQUEST['Hasta'];

    $id_marca = $_REQUEST['idMarca'];

    $id_prod = $_REQUEST['idProd'];

    $idUsuario = $_SESSION['idUser'];

    $manager = DataBase::getInstance();

    $filtro = "";

    $n_for = count($id_formato);
    $k = 0;
    if ($n_for > 0) {
        foreach ($id_formato as $formatos) {
            if ($k == 0) {
                $filtro .= " and (t.idFormato='" . $formatos . "'";
            } else {
                $filtro .= " or t.idFormato='" . $formatos . "'";
            }
            $k++;
        }
        $filtro .= ")";
    }

    if ($id_estado != "" && $id_estado != "all") {
        $filtro .= " and t.idEstado";
    }

    //*******************Si es Perfil Gerente
    if ($_SESSION['id_perfil'] == '9') {
        $gerente_q = "SELECT idGerente FROM usuarios WHERE idUsuario='" . $idUsuario . "'";
        $gerente_r = $manager->ejecutarConsulta($gerente_q);
        $gerente_d = mysqli_fetch_array($gerente_r);

        $filtro .= " and p.Supervisor IN (select idSupervisores from Supervisores
		 where idGerente='" . $gerente_d['idGerente'] . "')";
    }



    if (!empty($id_marca)){

        $n_marc = count($id_marca);


        $filtro .= " and fre.idMarca in (";
        $k = 0;

        foreach ($id_marca as $marcas) {

            if (is_numeric($marcas)){

                $filtro.= $marcas;

                if ($k != $n_marc - 1){

                    $filtro .= ",";
                }
            }

            $k++;
        }
        $filtro .= ")";
    }


    if (!empty($id_prod)){
        $n_prod = count($id_prod);
        $k = 0;

        $filtro .= " and fre.idProducto in (";

        foreach ($id_prod as $product) {
            if (is_numeric($product)){

                $filtro.= $product;

                if ($k != $n_prod - 1){

                    $filtro .= ",";

                }

            }


        }
        $filtro .= ")";

    }


    $dateIn = date("Y-m-d", strtotime($desde));
    $dateOut = date("Y-m-d", strtotime($hasta));

    $sql_prods = "SELECT t.idTienda, fre.idMarca,t.grupo, t.sucursal, t.numeroEconomico, f.grupo AS formato,f.cadena AS cadenaf,e.nombre AS estadoMex, 
                (fre.cha1+fre.cha2+fre.cha3+fre.cha4+ fre.cha5+ fre.cha6) frentes , fre.idProducto, CONCAT(pro.nombre,' ', pro.presentacion) producto ,
                DATE(fre.fecha_captura) fecha_captura


                FROM maestroTiendas t 
                LEFT JOIN frentesCharola fre ON (t.idTienda=fre.idTienda) 


                LEFT JOIN tiendas_formatos f ON (t.idFormato=f.idFormato) 
                LEFT JOIN estados e ON (e.id=t.idEstado) 
                LEFT JOIN Promotores p ON (fre.idCelular=p.idCelular) 

                LEFT JOIN Producto pro ON (pro.idProducto=fre.idProducto)


                WHERE (fre.fecha_captura BETWEEN '".$dateIn."' AND '". $dateOut ."') 
                
                
                ". $filtro ."  

                GROUP BY t.idTienda,fre.idMarca, fre.idProducto";

    $rs_prods = $manager->ejecutarConsulta($sql_prods);

    $json = array();



    while ($dato_prods = mysqli_fetch_object($rs_prods)) {


        array_push($json, $dato_prods);


    }////********Final While Productos

    echo json_encode($json, JSON_NUMERIC_CHECK | JSON_PRETTY_PRINT);


} else {

    echo 'no has iniciado sesion';

    http_response_code(422);

    header('refresh:2,../index.php');

}
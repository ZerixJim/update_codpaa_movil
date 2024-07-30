<?

ob_start();

session_start();





if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');


    $idMarca = $_GET['idMarca'];
    $idExhibicion = $_GET['idExhi'];
    $Estado = $_GET['Estado'];
    $Formato = $_GET['Formato'];

    $idPromotor = $_REQUEST['idPromotor'];


    $productos = $_GET['idProductos'];


    $fechaInicio = date('Y-m-d', strtotime($_REQUEST['Desde']));
    $fechaFin = date('Y-m-d', strtotime($_REQUEST['Hasta']));



    $idTipo = $_GET['idTipo'];
    $id_region = $_GET['idRegion'];
    
    $id_summa = $_GET['idRegSumma'];


    $manager = DataBase::getInstance();

    $sql = "SELECT m.idTienda,concat(tf.grupo,' ', tf.cadena ,' ', m.sucursal,
            ' fotos(',count(distinct pc.idphotoCatalogo),')') AS nombre

            FROM maestroTiendas AS m

                INNER JOIN photoCatalogo AS pc ON (m.idTienda=pc.id_tienda)
                
                LEFT JOIN tiendas_formatos tf USING (idFormato)
			
			    LEFT JOIN Promotores p ON (p.idCelular=pc.id_promotor)
			
			    LEFT JOIN photo_producto as pp on pp.idPhotoCatalogo=pc.idphotoCatalogo 
			
			    LEFT JOIN estados as es on es.id = m.idEstado

            WHERE  ";



    $n_marc = count($idMarca);

    //filtro marcas

    if ($n_marc > 0) {

        $sql.= " pc.id_marca in (";
        $k = 0;

        foreach ($idMarca as $marcas) {


            if (is_numeric($marcas)){
                $sql.= $marcas;

                if ($k != $n_marc - 1){

                    $sql .= ",";

                }

            }

            $k++;
        }
        $sql .= ")";
    }


    //echo $sql;

    //filtro de region

    if ($id_region != "" && !empty($id_region)) {


        $sql .= " and es.id_nielsen = " . $id_region . " ";
    }
    
    //PARA REGIÓN SUMMA
    
    if($id_summa != "" && !empty($id_summa)){
        
        $sql .= " and es.id_summa = " . $id_summa . " ";
    }

    //filtro productos
    $nProductos = count($productos);
    $j = 0;

    if ($nProductos > 0){

        $sql .= " and pp.idProducto in (";

        foreach ($productos as $producto){

            if (is_numeric($producto)){


                $sql .= $producto;

                if ($j != $nProductos - 1){

                    $sql .= ",";
                }

            }

            $j++;

        }

        $sql .= ")";


    }

    //filtro de estado
    $h = 0;
    $nEstados = count($Estado);
    if ($Estado != "") {

        if ($nEstados > 0){

            $sql .= " and m.idEstado in (";

            foreach ($Estado as $item){

                if (is_numeric($item)){

                    $sql.= $item;

                    if ($h != $nEstados - 1){

                        $sql.= ",";
                    }

                }

                $h++;

            }

            $sql.= ") ";

        }

    }



    // filtro de fechas

    if (isset($_REQUEST['Desde']) && !empty($_REQUEST['Desde']) && isset($_REQUEST['Hasta']) && !empty($_REQUEST['Hasta'])) {

        //$sql.=" and (STR_TO_DATE(pc.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$desde."','%d-%m-%Y') and STR_TO_DATE('".$hasta."','%d-%m-%Y') ) ";
        $sql .= " and DATE(pc.fecha_captura) BETWEEN '" . $fechaInicio . "' and '" . $fechaFin . "'";
    }

    //filtro de exhibicion
    if ($idExhibicion != "") {

        $sql .= " and pc.id_exhibicion=" . $idExhibicion . " ";

    }



    if ($idPromotor != ""){

        $sql .= " and p.idCelular =" . $idPromotor ;

    }


    //filtro de formato
    if ($Formato != "") {
        $sql .= " and m.idFormato='" . $Formato . "'";
    }

    //filtro tipo

    if ($idTipo != "") {
        $sql .= " and p.idtipoPromotor='" . $idTipo . "'";
    }


    /*
     *
     *
     * Diconsa Profile
     *
     */

    if ($_SESSION['id_perfil'] == 14){



        $sql .= " and m.idFormato = 39 ";

    }


    $sql .= " group by pc.id_tienda";



    $rs = $manager->ejecutarConsulta($sql);

    $result = array();

    while ($row = mysqli_fetch_object($rs)) {

        array_push($result, $row);

    }


    echo json_encode($result, JSON_PRETTY_PRINT);


} else {

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}














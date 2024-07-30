<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');


    $tipo = $_SESSION['tipo'];
    $idUsuario = $_SESSION['idUser'];


    $filtro = "";




    if (!empty($_REQUEST['fechaIni']) && isset($_REQUEST['fechaIni'])
        && !empty($_REQUEST['fechaFin']) && isset($_REQUEST['fechaFin'])) {

        $dateIn = date("Y-m-d", strtotime($_REQUEST['fechaIni'] ));
        $dateOut = date("Y-m-d", strtotime($_REQUEST['fechaFin'] ));



        $filtro .= " and (STR_TO_DATE(ct.fecha,'%d-%m-%Y') 
                BETWEEN '" . $dateIn ."' 
                and '" . $dateOut . "' )";
    }

    if (!empty($_REQUEST['idMarca']) && isset($_REQUEST['idMarca'])){

        $nMarcas = count($_REQUEST['idMarca']);

        $marcas = $_REQUEST['idMarca'];

        $filtro.= " and ct.idMarca in (";

        $k = 0;
        foreach ($marcas as $marca){

            if (is_numeric($marca)){

                $filtro.= $marca;

                if ($k != $nMarcas - 1){

                    $filtro .= ",";
                }


            }

            $k++;

        }

        $filtro.= ")";


    }


    if ($_REQUEST['buscarComnt'] != "*" and isset($_REQUEST['buscarComnt'])) {

        $filtro .= "  and (p.nombre like '%" . $_REQUEST['buscarComnt'] . "%' OR ct.idTienda='" .
            $_REQUEST['buscarComnt'] . "' OR ct.idCelular='" . $_REQUEST['buscarComnt'] . "') ";
    }


    if (!empty($_REQUEST['tipoTienda']) && isset($_REQUEST['tipoTienda'])){


        $filtro .= " and mt.idTipoTienda = " . $_REQUEST['tipoTienda'];


    }

    $manager = DataBase::getInstance();

    if ($_SESSION['id_perfil'] == '1') {
        $sql = "SELECT ct.idcomentarioTienda, ct.idTienda, ct.idCelular as idPromotor, ct.fecha, ct.comentario, ct.fecha_captura, ct.idMarca, p.nombre AS Promotor,concat(mt.grupo,' ',mt.sucursal) AS Tienda, m.nombre marca, mt.numeroEconomico as economico  FROM comentarioTienda ct 
		LEFT JOIN Promotores p ON (ct.idCelular=p.idCelular)
		LEFT JOIN maestroTiendas mt ON (mt.idTienda=ct.idTienda) 
		LEFT JOIN Marca m on m.idMarca = ct.idMarca 
		WHERE comentario!='' 
		" . $filtro . "
		ORDER BY ct.idCelular";

    } else if ($_SESSION['id_perfil'] == '2' || $_SESSION['id_perfil'] == '5') {
        $sql = "SELECT ct.idcomentarioTienda, ct.idTienda, ct.idCelular as idPromotor, ct.fecha, ct.comentario, ct.fecha_captura, ct.idMarca,ct.idCelular as idPromotor, p.nombre AS Promotor,concat(mt.grupo,' ',mt.sucursal) AS Tienda, m.nombre marca, mt.numeroEconomico as economico  FROM comentarioTienda ct 
		LEFT JOIN Promotores p ON (ct.idCelular=p.idCelular)
		LEFT JOIN maestroTiendas mt ON (mt.idTienda=ct.idTienda) 
		LEFT JOIN Marca m on m.idMarca = ct.idMarca 
		INNER JOIN  marcaAsignadaPromotor M ON (M.idPromotor=p.idCelular)
		WHERE comentario!='' AND M.idMarca IN (SELECT idMarca FROM usuariosMarcaAsignada WHERE idUsuario='" . $idUsuario . "') 
		" . $filtro . "
		GROUP BY ct.idcomentarioTienda
		ORDER BY ct.idCelular";

    } else if ($_SESSION['id_perfil'] == '3' || $_SESSION['id_perfil'] == '8') {
        $sql = "SELECT ct.idcomentarioTienda, ct.idTienda, ct.idCelular as idPromotor, ct.fecha, ct.comentario, ct.fecha_captura, ct.idMarca,ct.idCelular as idPromotor, p.nombre AS Promotor,concat(mt.grupo,' ',mt.sucursal) AS Tienda, m.nombre marca, mt.numeroEconomico as economico  FROM comentarioTienda ct 
		LEFT JOIN Promotores p ON (ct.idCelular=p.idCelular)
		LEFT JOIN maestroTiendas mt ON (mt.idTienda=ct.idTienda) 
		LEFT JOIN Supervisores S ON (p.Supervisor=S.idSupervisores) 
		LEFT JOIN Marca m on m.idMarca = ct.idMarca 
		WHERE comentario!='' AND p.Supervisor = 
		          (SELECT idSupervisor FROM usuarios WHERE idUsuario='" . $idUsuario . "') 
		" . $filtro . "
		GROUP BY ct.idcomentarioTienda
		ORDER BY ct.idCelular";
    } else if ($_SESSION['id_perfil'] == '6') {


        $sql = "SELECT ct.idcomentarioTienda, ct.idTienda, ct.idCelular as idPromotor, ct.fecha, ct.comentario, ct.fecha_captura, ct.idMarca,ct.idCelular as idPromotor,p.nombre AS Promotor,concat(mt.grupo,' ',mt.sucursal) AS Tienda, mt.numeroEconomico as economico  FROM comentarioTienda ct 
		LEFT JOIN Promotores p ON (ct.idCelular=p.idCelular)
		LEFT JOIN maestroTiendas mt ON (mt.idTienda=ct.idTienda) 
		INNER JOIN  marcaAsignadaPromotor M ON (M.idPromotor=p.idCelular)
		WHERE comentario!='' AND
		M.idMarca IN (SELECT idMarca FROM ClientesMarcas WHERE idCliente = (SELECT idCliente FROM usuarios WHERE idUsuario='" . $idUsuario . "'))
		" . $filtro . "
		GROUP BY ct.idcomentarioTienda
		ORDER BY ct.idCelular";
    } else if($_SESSION['id_perfil'] == '13'){

        $sql = "SELECT ct.idcomentarioTienda, ct.idTienda, ct.idCelular as idPromotor, ct.fecha, ct.comentario, ct.fecha_captura, 
        ct.idMarca,ct.idCelular as idPromotor, p.nombre AS Promotor,concat(mt.grupo,' ',mt.sucursal) AS Tienda, m.nombre marca, mt.numeroEconomico as economico  FROM comentarioTienda ct 
		LEFT JOIN Promotores p ON (ct.idCelular=p.idCelular)
		LEFT JOIN maestroTiendas mt ON (mt.idTienda=ct.idTienda) 
		LEFT JOIN Supervisores S ON (p.Supervisor=S.idSupervisores) 
		LEFT JOIN Marca m on m.idMarca = ct.idMarca 
		WHERE comentario!='' AND p.idtipoPromotor = 1 
		" . $filtro ;


    }


    $rs = $manager->ejecutarConsulta($sql);
    $result = array();
    while ($row = mysqli_fetch_object($rs)) {

        array_push($result, $row);
    }

    echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_UNESCAPED_UNICODE);
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}


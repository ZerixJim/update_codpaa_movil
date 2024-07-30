<?

ob_start();

session_start();


if (isset($_SESSION['usuario']) && isset($_SESSION['permiso']) && isset($_POST['marcas'])) {

    include_once('../../connexion/DataBase.php');

    $marcas = $_POST['marcas'];

    $filtro = "";
    $k = 0;

    $len = count($_POST['marcas']);

    foreach ($marcas as $marca) {


        if (is_numeric($marca)) {

            $filtro .= $marca;

            if ($k != $len - 1) {

                $filtro .= ",";
            }

        }

        $k++;

    }


    if ($_SESSION['id_perfil'] == 14) {


        $sql = "SELECT p.idCelular, p.nombre 
	            FROM Promotores AS p
	
	            LEFT JOIN rutasPromotores rp ON (rp.`idPromotor` = p.`idCelular`)
	
                LEFT JOIN maestroTiendas mt ON (mt.`idTienda` = rp.`idTienda`)
                
                WHERE mt.`idFormato` = 39 AND p.`status` = 'a' ";


    } else {

        $sql = "select p.idCelular, p.nombre from Promotores as p
	    right join marcaAsignadaPromotor as mA on mA.idPromotor=p.idCelular
        left join usuariosMarcaAsignada as uM on uM.idMarca=mA.idMarca
        where uM.idUsuario='" . $_SESSION['idUser'] . "' and mA.idMarca in (" . $filtro . ") and p.status = 'a'
        group by p.idCelular";

    }


    $bd = DataBase::getInstance();
    $rs = $bd->ejecutarConsulta($sql);

    $result = array();

    while ($row = mysqli_fetch_array($rs)) {

        array_push($result, array('id' => $row['idCelular'], 'nombre' => $row['nombre']));

    }


    echo json_encode($result, JSON_PRETTY_PRINT);


} else {

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}













